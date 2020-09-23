package com.example.study.service.impl;

import com.example.study.dao.SysUserMapper;
import com.example.study.dto.SysUserDTO;
import com.example.study.model.SysUser;
import com.example.study.service.SysUserService;
import com.example.study.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void addSysUser(SysUserDTO dto) {
        SysUser user = BeanHelper.copyProperties(dto, SysUser.class);
        user.setDateCreated(LocalDateTime.now());
        String password = passwordEncoder.encode(dto.getPassword());
        user.setPassword(password);
        sysUserDao.insertSelective(user);
    }
}
