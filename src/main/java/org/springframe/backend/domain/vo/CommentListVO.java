package org.springframe.backend.domain.vo;

import java.util.Date;

public class CommentListVO {
    private Integer id;
    private Integer type;
    private Integer typeId;
    private Long parentId;
    private Long replyId;
    private String CommentContent;
    private String commentUserName;
    private Date createTime;
    private Date updateTime;
}
