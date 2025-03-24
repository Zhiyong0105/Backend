package org.springframe.backend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframe.backend.constants.RedisConst;
import org.springframe.backend.constants.SQLConst;
import org.springframe.backend.domain.dto.ArticleDTO;
import org.springframe.backend.domain.entity.Article;
import org.springframe.backend.domain.vo.ArticleDetailVo;
import org.springframe.backend.domain.vo.ArticleVo;
import org.springframe.backend.domain.vo.PageVo;
import org.springframe.backend.enums.CountTypeEnum;
import org.springframe.backend.repository.ArticleRepository;
import org.springframe.backend.service.IArticleService;
import org.springframe.backend.utils.RedisCache;
import org.springframe.backend.utils.ResponseResult;
import org.springframe.backend.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    RedisCache redisCache;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PageVo<List<ArticleVo>> listAllArticle(Integer pageNum, Integer pageSize) {
        long start = System.currentTimeMillis();
        boolean hasKey = redisCache.isHasKey(RedisConst.ARTICLE_COMMENT_COUNT);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Article> page = articleRepository.findAllByStatusOrderByCreateTimeDesc(SQLConst.PUBLIC_ARTICLE,pageable);
        List<Article> list = page.getContent();
        List<ArticleVo> articleVos = list.stream()
                .map(article -> article.asViewObject(ArticleVo.class))
                .toList();
        articleVos = articleVos.stream().peek(articleVo -> {
            setArticleCount(articleVo,RedisConst.ARTICLE_COMMENT_COUNT, CountTypeEnum.COMMENT);
        }).toList();
        log.info("Search Time: {}ms", System.currentTimeMillis() - start);
        PageVo<List<ArticleVo>> listPageVo = new PageVo<>(articleVos,page.getTotalElements());
        return listPageVo;
    }

    @Override
    public PageVo<List<ArticleVo>> listHotArticle() {
        long start = System.currentTimeMillis();
        List<String> topIds = redisCache.getCacheZsetRevRange(RedisConst.ARTICLE_VISIT_COUNT,0,4);

        if (topIds == null || topIds.isEmpty()) {
            return new PageVo<>(Collections.emptyList(),0L);
        }
        List<ArticleVo> articleVos = topIds.stream()
                .map(id -> redisCache.getCacheObject(RedisConst.ARTICLE_SAVE + id))
                .filter(Objects::nonNull)
                .map(jsonStr ->{
                    try {
                        Article article = objectMapper.readValue((String)jsonStr, Article.class);
                        ArticleVo articleVo = new ArticleVo();
                        BeanUtils.copyProperties(article,articleVo);
                        articleVo.setVisitCount(getVisitCount(article.getId()).longValue());
                        return articleVo;
                    }catch (JsonProcessingException e){
                        log.error(e.getMessage(),e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        log.info("Search HotArticle Time: {}ms", System.currentTimeMillis() - start);
        return new PageVo<>(articleVos,(long) articleVos.size());
    }

    private void setArticleCount(ArticleVo articleVo,String key,CountTypeEnum countTypeEnum){
        String articleId = articleVo.getId().toString();
        Object countObj = redisCache.getCacheMap(key).get(articleId);
        long count = 0L;
        if(countObj != null){
            count = Long.parseLong(String.valueOf(countObj));
        }else{
            redisCache.setCacheMap(key, Map.of(articleId, 0));
        }
        if(countTypeEnum.equals(CountTypeEnum.COMMENT)){
            articleVo.setCommentCount(count);
        }
        Long visitCount = getVisitCount(articleVo.getId());
        articleVo.setVisitCount(visitCount);
    }

    @Override
    public ArticleDetailVo getArticleDetail(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article, articleDetailVo);
       return articleDetailVo;
    }

    @Override
    public void addVisitCount(Long id) {
        String redisKey = RedisConst.ARTICLE_VISIT_COUNT;
        redisCache.incrementZsetScore(redisKey,id.toString(),1L);
    }

    @Override
    public Long getVisitCount(Long id) {
        String redisKey = RedisConst.ARTICLE_VISIT_COUNT;
        Long articleCount = redisCache.getCacheScore(redisKey,id.toString());
        if(articleCount == null ){
            articleCount = 0L;
        }
        return articleCount;
    }

    @Override
    public ResponseResult<Void> saveDraft(ArticleDTO articleDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO,article);
        articleRepository.save(article);
        return ResponseResult.Success();
    }


    @Override
    public ResponseResult<Void> publishArticle(ArticleDTO articleDTO) {

        Article article = new Article();

        BeanUtils.copyProperties(articleDTO, article);
        try{
            articleRepository.save(article);
            String articleKey = RedisConst.ARTICLE_SAVE + article.getId();
            String articleJson = new ObjectMapper().writeValueAsString(article);

            redisCache.setCacheObject(articleKey,articleJson);
            redisCache.setCacheZsetObject(RedisConst.ARTICLE_VISIT_COUNT,article.getId().toString(), 0L);
            return ResponseResult.Success();
        }catch (Exception e){

            return ResponseResult.Fail();
        }

    }



    @Override
    public ResponseResult<Void> deleteArticle(List<Long> ids) {
        try{
            articleRepository.deleteAllById(ids);
            return ResponseResult.Success();
        }catch (Exception e){
            return ResponseResult.Fail();
        }

    }

    @Override
    @Transactional
    public ResponseResult<Void> updateArticle(ArticleDTO articleDTO) {
        Article existArticle = articleRepository.findById(articleDTO.getId()).orElse(null);
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        article.setCreateTime(existArticle.getCreateTime());
        try{
            articleRepository.save(article);
            return ResponseResult.Success();
        }catch (Exception e){

            return ResponseResult.Fail();
        }


    }


}
