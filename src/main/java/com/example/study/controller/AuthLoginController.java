package com.example.study.controller;


import com.example.study.config.BusinessException;
import com.example.study.config.ResponseMessage;
import com.example.study.service.AuthLoginService;
import com.example.study.vo.LogInVO;
import com.example.study.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthLoginController {


    @Autowired
    private AuthLoginService loginService;

    @PostMapping(value = "/login")
    public ResponseMessage<LogInVO> login(@RequestBody LoginDTO dto) {

        try {
            LogInVO result = loginService.login(dto);
            return ResponseMessage.successResponse(result);
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
            return ResponseMessage.failResponse(e);
        }
    }
}
