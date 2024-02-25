package com.fjut.healthydietapp.vo;

import lombok.Data;

@Data
public class PasswordVo {
    private String phone;
    private String code;
    private String oldPassword;
    private String password;
}