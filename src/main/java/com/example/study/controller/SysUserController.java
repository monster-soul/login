package com.example.study.controller;


import com.example.study.config.ResponseMessage;
import com.example.study.dto.SysUserDTO;
import com.example.study.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;


    @PostMapping(value = "/addSysUser")
    public ResponseMessage<Void> addSysUser(@RequestBody SysUserDTO dto) {
        sysUserService.addSysUser(dto);
        return ResponseMessage.successResponse();
    }
}
