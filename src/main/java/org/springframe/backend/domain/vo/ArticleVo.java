package org.springframe.backend.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(name = "ArticleVo",description = "Article")
public class ArticleVo {
    private Long id;
    private String articleTitle;
    private String articleSummary;
    private Date createTime;
    private Long CommentCount;

}
