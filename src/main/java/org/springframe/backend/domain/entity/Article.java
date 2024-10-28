package org.springframe.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframe.backend.domain.BaseData;

import java.io.Serializable;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "t_article")
@Entity
public class Article implements BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer userId;
    private Long categoryId;
    private String articleCover;
    private String articleTitle;
    private String articleContent;
    private String articleType;
    private Integer isTop;
    private Integer status;
    private Long visitCount;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
}
