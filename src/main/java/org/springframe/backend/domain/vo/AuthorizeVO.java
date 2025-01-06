package org.springframe.backend.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {

    private String token;
    private String username;
    private String email;
    private Date expireTime;
}
