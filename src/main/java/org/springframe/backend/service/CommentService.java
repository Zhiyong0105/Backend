package org.springframe.backend.service;

import org.springframe.backend.domain.dto.UserCommentDTO;
import org.springframe.backend.domain.vo.ArticleCommentVO;
import org.springframe.backend.domain.vo.PageVo;
import org.springframe.backend.utils.ResponseResult;

import java.util.List;

public interface CommentService {

    PageVo<List<ArticleCommentVO>> getComment(Integer type, Integer typeId,Integer pageNum, Integer pageSize);

    ResponseResult<String> userComment(UserCommentDTO userCommentDTO);

    ResponseResult<Void> deleteComment(Integer Id);
}
