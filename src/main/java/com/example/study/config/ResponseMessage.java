package com.example.study.config;

import com.example.study.enumshare.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage<T> {


    private String code;
    private String message;
    T data;

    public ResponseMessage(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public ResponseMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public static ResponseMessage successResponse() {
        return new ResponseMessage(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage());
    }

    public static ResponseMessage successResponse(Object data) {
        return new ResponseMessage(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data);
    }

    public static ResponseMessage failResponse(BusinessException e) {
        return new ResponseMessage(e.getCode(), e.getMessage());
    }

    public static ResponseMessage failResponse(Exception e) {
        return new ResponseMessage(ResponseStatus.FAILED.getCode(), e.getMessage());
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
