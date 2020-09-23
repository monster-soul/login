package com.example.study.jwt;

import com.example.study.dao.SysUserMapper;

import com.example.study.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserDao;


    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名查询 用户信息
        SysUser user = sysUserDao.selectPasswordByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("'%s'.这个用户不存在", username));
        }
        JwtUser jwtUser = new JwtUser(user,  null);
        return jwtUser;

    }
}
