package org.springframe.backend.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    private Integer userId;
    private String username;
//    private String password;
}
