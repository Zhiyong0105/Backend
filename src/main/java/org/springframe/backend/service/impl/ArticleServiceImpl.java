package org.springframe.backend.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    RedisCache redisCache;

    @Autowired
    ArticleRepository articleRepository;

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
            if(hasKey){
                setArticleCount(articleVo,RedisConst.ARTICLE_COMMENT_COUNT, CountTypeEnum.COMMENT);
            }
        }).toList();
        log.info("Search Time: {}ms", System.currentTimeMillis() - start);
        PageVo<List<ArticleVo>> listPageVo = new PageVo<>(articleVos,page.getTotalElements());
        return listPageVo;
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
    }

    @Override
    public ArticleDetailVo getArticleDetail(Integer id) {
       return null;

    }

    @Override
    public void addVisitCount(Integer id) {
        if(redisCache.isHasKey(RedisConst.ARTICLE_VISIT_COUNT + id)){
            redisCache.increment(RedisConst.ARTICLE_VISIT_COUNT + id,1L);
        }else{
            redisCache.setCacheObject(RedisConst.ARTICLE_VISIT_COUNT + id,0);
        }

    }


    @Override
    public ResponseResult<Void> publishArticle(ArticleDTO articleDTO) {
        Article article = articleDTO.asViewObject(Article.class,v -> v.setUserId(SecurityUtils.getUserId()));
        try{
            articleRepository.save(article);
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
}
