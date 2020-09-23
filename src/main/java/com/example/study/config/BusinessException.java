package com.example.study.config;



public class BusinessException extends Exception {
    private String code;
    private String message;


    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message, String code, String message1) {
        super(message);
        this.code = code;
        this.message = message1;
    }

    public BusinessException(String message, Throwable cause, String code, String message1) {
        super(message, cause);
        this.code = code;
        this.message = message1;
    }

    public BusinessException(Throwable cause, String code, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, String message1) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.message = message1;
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
