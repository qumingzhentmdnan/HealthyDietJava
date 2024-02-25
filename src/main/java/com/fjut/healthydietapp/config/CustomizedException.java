package com.fjut.healthydietapp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义异常类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomizedException extends RuntimeException{
    private Integer code;//状态码;
    private String message;//相应消息
}