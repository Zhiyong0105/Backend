package org.springframe.backend.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LoginDTO {

    private String username;

    private Integer state;

    private Date loginTimeStart;

    private Date loginTimeEnd;


}
