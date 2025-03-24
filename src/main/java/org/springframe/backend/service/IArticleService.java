package org.springframe.backend.service;

import org.springframe.backend.domain.dto.ArticleDTO;
import org.springframe.backend.domain.vo.ArticleDetailVo;
import org.springframe.backend.domain.vo.ArticleVo;
import org.springframe.backend.domain.vo.PageVo;
import org.springframe.backend.utils.ResponseResult;

import java.util.List;

public interface IArticleService {

    PageVo<List<ArticleVo>> listAllArticle(Integer pageNum, Integer pageSize);

    PageVo<List<ArticleVo>> listHotArticle();

    ArticleDetailVo getArticleDetail(Long id);

    void addVisitCount(Long id);

    Long getVisitCount(Long id);


    ResponseResult<Void> saveDraft(ArticleDTO articleDTO);

    ResponseResult<Void> publishArticle(ArticleDTO articleDTO);

    ResponseResult<Void> deleteArticle(List<Long> ids);

    ResponseResult<Void> updateArticle(ArticleDTO articleDTO);



}
