package com.example.study.service;

import com.example.study.config.BusinessException;
import com.example.study.dto.LoginDTO;
import com.example.study.vo.LogInVO;

public interface AuthLoginService {
    LogInVO login(LoginDTO dto) throws BusinessException;
}
