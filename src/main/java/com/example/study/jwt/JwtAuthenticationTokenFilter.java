package com.example.study.jwt;

import com.alibaba.fastjson.JSON;
import com.example.study.config.BusinessException;
import com.example.study.config.GlobalConfig;
import com.example.study.config.RedisKeys;
import com.example.study.config.ResponseMessage;
import com.example.study.enumshare.ResponseStatus;
import com.example.study.utils.Md5Util;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private GlobalConfig globalConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 如果已经通过认证
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }
        // 通过header 用户获取token 信息
        String authHeader = request.getHeader(jwtTokenUtil.getHeader());
        String url = request.getRequestURI();
        log.info("url:{}, access_token:{}", url, authHeader);
        try {
            if (!checkUrl(url)) {
                //token 不等于空
                jwtTokenUtil.validateToken(authHeader);//验证Token是否过期
                String id = jwtTokenUtil.getUserIdFromToken(authHeader);//从token获取Id
                String redis = redisTemplate.opsForValue().get(RedisKeys.KEY_PREFIX + id);//根据Id从redis中获取用户信息
                if (StringUtils.isEmpty(redis)) {
                    unauthorizedResponse(response, "您的登录信息已过期，请重新登录");
                    return;
                }
                JwtUser jwtUser = JSON.parseObject(redis, JwtUser.class);//根据获取用户信息
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
                //认证方式 设置用户认证方式
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            //认证通过执行方法
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {

            String jwt = Md5Util.stringMD5(authHeader);
            String userId = redisTemplate.opsForValue().get(RedisKeys.KEY_PREFIX_TOKEN + jwt);
            if (StringUtils.isEmpty(userId)) {
                unauthorizedResponse(response, "您的登录信息已过期，请重新登录");
                return;
            }

            //获取用户信息
            String id = jwtTokenUtil.getUserIdFromToken(authHeader);//从token获取Id
            String redis = redisTemplate.opsForValue().get(RedisKeys.KEY_PREFIX + id);//根据Id从redis中获取用户信息

            //判断redis过期时间 如果快过期
            Long expire = redisTemplate.opsForValue().getOperations().getExpire(RedisKeys.KEY_PREFIX_TOKEN + jwt);
            if (expire < 3600l) {
                redisTemplate.expire(RedisKeys.KEY_PREFIX_TOKEN + jwt, 100, TimeUnit.SECONDS);//更新用户Id过期时间
                redisTemplate.expire(RedisKeys.KEY_PREFIX + id, 100, TimeUnit.SECONDS); //更新用户信息 过期时间
            }
            JwtUser jwtUser = JSON.parseObject(redis, JwtUser.class);//根据获取用户信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
            //认证方式 设置用户认证方式
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
    }

    private void jwtAuthResponse(HttpServletResponse response, String message) throws IOException {
        ResponseMessage retResult = ResponseMessage.failResponse(new BusinessException(ResponseStatus.NO_PERMISSION.getCode(), message));
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON.toJSONString(retResult));
    }


    private Boolean checkUrl(String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        List<String> excludeUrl = Arrays.asList(globalConfig.getExcludeUrl());
        return excludeUrl.stream().anyMatch(i -> matcher.match(i, url));
    }

    private void unauthorizedResponse(HttpServletResponse response, String messsage) throws IOException {
        ResponseMessage retResult = ResponseMessage.failResponse(new BusinessException(ResponseStatus.UNAUTHORIZED.getCode(), messsage));
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON.toJSONString(retResult));
    }
}
