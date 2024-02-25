package com.fjut.healthydietapp.config;


import com.fjut.healthydietapp.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleException {
    //全局异常处理
    @ExceptionHandler(Exception.class)
    public Result GlobalHandle(Exception exception){
        System.out.println(exception.getMessage());
        return Result.error().message("您的网络存在异常，请稍后再试");
    }

    //自定义异常处理
    @ExceptionHandler(CustomizedException.class)
    public Result HandleCustomizedException(CustomizedException exception){
        System.out.println(exception.getMessage());
        return Result.error().code(exception.getCode()).message(exception.getMessage());
    }
}

