package org.springframe.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframe.backend.domain.dto.UserCommentDTO;
import org.springframe.backend.domain.vo.ArticleCommentVO;
import org.springframe.backend.domain.vo.PageVo;
import org.springframe.backend.repository.CommentRepository;
import org.springframe.backend.service.CommentService;
import org.springframe.backend.utils.ControllerUtils;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;


    @GetMapping("/getComment")
    public ResponseResult<PageVo<List<ArticleCommentVO>>> comment(
            @Valid @NotNull Integer type,
            @Valid @NotNull Integer typeId,
            @Valid @NotNull Integer pageNum,
            @Valid @NotNull Integer pageSize
    ){
        return ControllerUtils.messageHandler(()->commentService.getComment(type, typeId, pageNum, pageSize));
    }

    @PostMapping("/auth/add")
    public ResponseResult<String> addComment(@Valid @RequestBody UserCommentDTO commentDTO){
        return commentService.userComment(commentDTO);
    }

    @GetMapping("/auth/check")
    public void check(){
        System.out.println("check");
    }

    @DeleteMapping("/auth/delete")
    public ResponseResult<Void> deleteComment(List<Long> ids){

        try{
            commentRepository.deleteAllById(ids);
            return ResponseResult.Success();
        }catch (Exception e){
            return ResponseResult.Fail();
        }
    }


}
