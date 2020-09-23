package com.example.study.config;

import com.example.study.enumshare.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseMessage<String> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.error("上传文件太大:"+ e.getMessage(), e);
        BusinessException businessException = new BusinessException(ResponseStatus.BAD_PARAMETER.getCode(), "上传文件太大！");
        return ResponseMessage.failResponse(businessException);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseMessage<String> HttpMessageNotReadableException(Exception e, HttpServletRequest request, ServletResponse response) {
        log.error("参数格式异常:"+ e.getMessage(), e);
        BusinessException businessException = new BusinessException(ResponseStatus.BAD_PARAMETER.getCode(), "参数格式不正确！");
        return ResponseMessage.failResponse(businessException);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseMessage<String> ExceptionHandler(Exception e, HttpServletRequest request, ServletResponse response) {
        log.error("内部异常:"+ e.getMessage(), e);
        BusinessException businessException = new BusinessException(ResponseStatus.INTERNAL_ERROR.getCode(), "内部异常！");
        return ResponseMessage.failResponse(businessException);
    }

}
