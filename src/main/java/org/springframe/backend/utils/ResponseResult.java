package org.springframe.backend.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframe.backend.enums.ResponseEnum;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseResult <T>{
    private int code;
    private String msg;
    private T data;
    private String token;



    public ResponseResult(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static<T> ResponseResult<T> Success(T data,String token){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(HttpStatus.OK.value());
        responseResult.setData(data);
        responseResult.setMsg("success");
        responseResult.setToken(token);
        return responseResult;
    }
    public static<T> ResponseResult<T> SuccessRegister(T data){
        ResponseResult<T> responseResult = new ResponseResult<T>();
        responseResult.setCode(ResponseEnum.SUCCESS.getCode());
        responseResult.setData(data);
        responseResult.setMsg(ResponseEnum.SUCCESS.getMsg());
        return responseResult;
    }
    public static<T> ResponseResult<T> error(String msg,Integer code){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMsg(msg);
        return responseResult;


    }
    public static<T> ResponseResult<T> error(ResponseEnum responseEnum){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(responseEnum.getCode());
        responseResult.setMsg(responseEnum.getMsg());
        return responseResult;
    }
    public static<T> ResponseResult<T> LoginError(int code,T data,String msg){
        return new ResponseResult<T>(code,msg);
    }






}
