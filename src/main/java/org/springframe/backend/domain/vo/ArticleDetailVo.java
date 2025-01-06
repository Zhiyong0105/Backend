package org.springframe.backend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleDetailVo {
    private Long id;
    private String categoryName;
    private Long categoryId;
    private String articleCover;
    private String articleTitle;
    private String articleContent;
    private Integer articleType;
    private Integer isTop;
    private Long visitCount;
    private Long commentCount;
    private String articleSummary;
    private Date createTime;
    private Date updateTime;
}
