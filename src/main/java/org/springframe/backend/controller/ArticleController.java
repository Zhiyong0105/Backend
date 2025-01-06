package org.springframe.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframe.backend.domain.dto.ArticleDTO;
import org.springframe.backend.domain.vo.ArticleDetailVo;
import org.springframe.backend.domain.vo.ArticleVo;
import org.springframe.backend.domain.vo.PageVo;
import org.springframe.backend.service.IArticleService;
import org.springframe.backend.service.impl.ArticleServiceImpl;
import org.springframe.backend.utils.ControllerUtils;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")

public class ArticleController {
    @Autowired
    private ArticleServiceImpl articleService;

    @GetMapping("/list")
    public ResponseResult<PageVo<List<ArticleVo>>> list(
             @NotNull Integer pageNum,
             @NotNull Integer pageSize
    ){
        return ControllerUtils.messageHandler((()-> articleService.listAllArticle(pageNum,pageSize)));
    }

    @GetMapping("/visit/{id}")
    public ResponseResult<Void> visit(@PathVariable("id") Long id){
        articleService.addVisitCount(id);
        return ControllerUtils.messageHandler(()->null);
    }

    @GetMapping("/{id}")
    public ResponseResult<ArticleDetailVo> getArticle(@PathVariable("id") Long id){

        return ControllerUtils.messageHandler((()-> articleService.getArticleDetail(id)));
    }
    @PostMapping("/publish")
    public ResponseResult<Void> publish(@Valid @RequestBody ArticleDTO articleDTO){
        return articleService.publishArticle(articleDTO);
    }

    @PostMapping("/update")
    public ResponseResult<Void> update(@Valid @RequestBody ArticleDTO articleDTO){
        return articleService.updateArticle(articleDTO);
    }

    @DeleteMapping("/auth/back/delete")
    public ResponseResult<Void> deleteArticle(@RequestBody List<Long> ids){
        return articleService.deleteArticle(ids);
    }

}
