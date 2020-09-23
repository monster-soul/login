package com.example.study.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {

    @Id
    private Integer id;

    private String userName;

    private String password;

    private LocalDateTime dateCreated;

    private String phone;

    private String remake;

}
