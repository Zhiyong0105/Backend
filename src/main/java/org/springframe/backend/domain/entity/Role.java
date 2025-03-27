package org.springframe.backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Table(name = "sys_role_tb")
public class Role {
    @Id
    private Long id;

    private String roleName;

    private String roleCode;

    private String description;

    private Integer status;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

}
