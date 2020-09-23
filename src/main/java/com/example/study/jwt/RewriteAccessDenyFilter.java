package com.example.study.jwt;

import com.alibaba.fastjson.JSON;
import com.example.study.config.BusinessException;
import com.example.study.config.ResponseMessage;
import com.example.study.enumshare.ResponseStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**

 */
@Component
public class RewriteAccessDenyFilter implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        BusinessException exception = new BusinessException(ResponseStatus.NO_PERMISSION.getCode(), "抱歉，您没有访问该接口的权限");
        ResponseMessage retResult = ResponseMessage.failResponse(exception);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(retResult));
    }


}
