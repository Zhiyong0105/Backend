package org.springframe.backend.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {

    private String token;

    private Date expireTime;
}
