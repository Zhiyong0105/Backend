package org.springframe.backend.domain.dto;

import lombok.Data;
import org.springframe.backend.domain.BaseData;

import java.io.Serializable;

@Data
public class ArticleDTO implements BaseData {

    private Long id;
    private String articleTitle;
    private String articleContent;
    private Integer status;
}
