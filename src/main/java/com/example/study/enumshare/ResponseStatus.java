package com.example.study.enumshare;

public enum ResponseStatus {

    SUCCESS("200",  "操作成功"),
    BAD_REQUEST("400",  "参数校验不通过"),
    UNAUTHORIZED("401",  "没有授权"),
    BAD_PARAMETER("402",  "参数格式异常"),
    FAILED("999",  "通用权限"),
    NO_RESOURCES("404",  "没有资源"),
    PARAMETERS_EMPTY("405",  "权限为空"),
    NO_PERMISSION("403",  "没有权限"),
    INTERNAL_ERROR("500",  "内部异常"),
    CHECK_FAIL("602",  "内部校验失败"),
    NO_EXISTS("601",  "查询不存在"),
    EXISTED("600",  "查询已存在");

    private String code;
    private String message;

    ResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
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
}
