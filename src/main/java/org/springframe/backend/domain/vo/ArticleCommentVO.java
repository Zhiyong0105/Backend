package org.springframe.backend.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Accessors(chain = true)
public class ArticleCommentVO {
    private Long id;
    private Integer commentType;
    private Integer typeId;
    private Long parentId;
    private Long replyId;
    private String commentContent;

    private Integer commentUserId;
    private Long replyUserId;
    private Date createTime;

    private Long childCommentCount;
    private Long parentCommentCount;

    private List<ArticleCommentVO> childComments ;
}
