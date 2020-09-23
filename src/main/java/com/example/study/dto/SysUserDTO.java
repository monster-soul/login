package com.example.study.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserDTO {

    private String userName;
    private String password;
    private String phone;
    private String remake;

}
