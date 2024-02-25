package com.fjut.healthydietapp.config;

import com.fjut.healthydietapp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//全局拦截器，判断是否登录
@Component
public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if(!request.getMethod().equals("OPTIONS")){
                String token = request.getHeader("Token");
                System.out.println("token = " + token);
                if(token == null||JwtUtil.getUsername(token) == null){
                    throw new CustomizedException(20001,"请先登录");
                }
            }
        }catch (Exception e){
            throw new CustomizedException(20001,"请先登录");
        }
        return true;
    }


}