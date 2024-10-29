package org.springframe.backend.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleCommentVO {
    private Integer id;
    private Integer commentType;
    private Integer typeId;
    private Long parentId;
    private Long replyId;
    private String CommentContent;

    private Integer commentUserId;
    private Long replyUserId;
    private Date createTime;

    private Long childCommentCount;
    private Long parentCommentCount;

    private List<ArticleCommentVO> childComments;
}
