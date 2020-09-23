package com.example.study.dao;

import com.example.study.model.SysUser;
import org.springframework.data.repository.query.Param;


public interface SysUserMapper {
    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectPasswordByName(@Param("userName") String userName);
}
