package org.springframe.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseEnum {
    LOGIN_SUCCESS(200,"Login success"),
    SUCCESS(200,"success"),
    FAIL(300,"Fail to get data"),
    USER_EX(301,"User  exist"),
    ERROR(302,"Error Request"),
    TOKEN_VERIFY_ERROR(401,"Failed token authentication");
    private Integer code;
    private String msg;
    public static  ResponseEnum getResponseEnum(Integer code){
        for(ResponseEnum responseEnum:ResponseEnum.values()){
            if(responseEnum.code.equals(code)){
                return responseEnum;
            }
        }
        return ResponseEnum.ERROR;
    }

}
