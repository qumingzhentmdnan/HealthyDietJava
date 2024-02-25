package com.fjut.healthydietapp.vo;

import lombok.Data;

@Data
public class RegisterVo {
    private String phone;
    private String password;
    private String nickname;
    private String headPicture;
    private String code;
}