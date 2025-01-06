package org.springframe.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
    private Long userId;
    private Long categoryId;
    private String articleCover;
    private String articleTitle;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String articleContent;
    private String articleType;
    private Integer isTop;
    private Integer status;
    private Long visitCount;
    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;
    private Integer isDelete;
    private String articleSummary;
}
