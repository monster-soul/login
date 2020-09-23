package com.example.study.service.impl;

import com.example.study.config.BusinessException;
import com.example.study.config.RedisKeys;
import com.example.study.dao.SysUserMapper;
import com.example.study.dto.LoginDTO;
import com.example.study.enumshare.ResponseStatus;
import com.example.study.jwt.JwtTokenUtil;
import com.example.study.jwt.JwtUser;
import com.example.study.model.SysUser;
import com.example.study.service.AuthLoginService;
import com.example.study.utils.Md5Util;
import com.example.study.vo.LogInVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;


@Service(value = "AuthLoginService")
public class AuthLoginServiceImpl implements AuthLoginService {


    @Autowired
    private SysUserMapper sysUserDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public LogInVO login(LoginDTO dto) throws BusinessException {
        if (StringUtils.isEmpty(dto.getUsername()) || StringUtils.isEmpty(dto.getPassword())) {
            throw new BusinessException(ResponseStatus.BAD_REQUEST.getCode(), "用户名或密码不能为空");
        }
        SysUser user = sysUserDao.selectPasswordByName(dto.getUsername());
        if (user == null) {
            throw new BusinessException(ResponseStatus.NO_EXISTS.getCode(), "用户不存在");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResponseStatus.BAD_REQUEST.getCode(), "密码输入不正确");
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwtTokenUtil.generateToken(jwtUser);
        String id = jwtTokenUtil.getUserIdFromToken(token);


        redisTemplate.opsForValue().set(RedisKeys.KEY_PREFIX_TOKEN + id, token, 60, TimeUnit.MINUTES);
        LogInVO result = new LogInVO();
        result.setToken(token);
        result.setStatus(ResponseStatus.SUCCESS.getCode());
        return result;
    }
}
