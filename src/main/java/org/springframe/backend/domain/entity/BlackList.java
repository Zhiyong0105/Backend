package org.springframe.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframe.backend.domain.ip.BlackListIpInfo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_black_list")
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String reason;

    private Date bannedTime;

    private Date expiresTime;

    private Integer type;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

    private Integer isDeleted;

    private BlackListIpInfo ipInfo;


}
