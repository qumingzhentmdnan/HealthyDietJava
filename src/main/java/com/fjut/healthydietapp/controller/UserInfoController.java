package com.fjut.healthydietapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fjut.healthydietapp.entity.UserInfo;
import com.fjut.healthydietapp.service.UserInfoService;
import com.fjut.healthydietapp.util.JwtUtil;
import com.fjut.healthydietapp.util.MD5Util;
import com.fjut.healthydietapp.util.MessageUtil;
import com.fjut.healthydietapp.util.Result;
import com.fjut.healthydietapp.vo.LoginVo;
import com.fjut.healthydietapp.vo.PasswordVo;
import com.fjut.healthydietapp.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Console;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    //判断是否登录
    @GetMapping("/isLogin")
    public Result isLogin(){
        System.out.println("已登录");
        return Result.ok().message("已登录");
    }

    //登录
    @PostMapping("/login")
    public Result Login(@RequestBody LoginVo userInfo){
        boolean res = userInfoService.verifyAccount(userInfo.getPhone(), userInfo.getPassword());
        System.out.println(userInfo);
        if(!res){
            return Result.error().message("用户名或密码错误");
        }else{
            String JwtToken = JwtUtil.createToken(userInfo.getPhone());
            return Result.ok().data("token", JwtToken).message("登录成功");
        }
    }

    //注册
    @PostMapping("/register")
    public Result Register(@RequestBody RegisterVo registerVo){
        //对前端传来的数据进行校验
        if(userInfoService.getUserInfoByPhone(registerVo.getPhone()) != null)
            return Result.error().message("该手机号已被注册");
        if(!registerVo.getPassword().matches("^[a-zA-Z0-9]{6,16}$"))
            return Result.error().message("密码不符合要求，请输入6-16位数字或字母");

        //校验验证码
        String key =registerVo.getPhone() + ":code";
        String value = (String) redisTemplate.opsForValue().get(key);

        if(!registerVo.getCode().equals(value)){
            return Result.error().message("验证码错误");
        }

        //封装到UserInfo中，将用户信息存入数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(registerVo.getPhone());
        userInfo.setPassword(MD5Util.encrypt(registerVo.getPassword()));
        userInfo.setNickname(registerVo.getNickname());
        userInfo.setHeadPicture(registerVo.getHeadPicture());
        userInfoService.save(userInfo);

        //注册成功，返回token
        String JwtToken = JwtUtil.createToken(userInfo.getPhone());
        return Result.ok().data("token",JwtToken).message("注册成功");
    }

    //获取当前用户信息
    @GetMapping("/info")
    public Result getUserInfo(@RequestHeader String token){
        String phone = JwtUtil.getUsername(token);
        UserInfo userInfo = userInfoService.getUserInfoByPhone(phone);

        //隐藏部分信息
        userInfo.setPhone(userInfo.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        userInfo.setPassword("********");
        return Result.ok().data("userInfo",userInfo);
    }

    //修改昵称，@RequestBody可以封装pojo对象，但是无法封装单个值。
    @PutMapping("/updateNickname")
    public Result updateNickname(@RequestHeader String token,String nickname){
        //信息验证
        if(nickname.length()>30)
            return Result.error().message("昵称过长");

        //执行修改
        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<UserInfo>().
                eq("phone", JwtUtil.getUsername(token)).
                set("nickname", nickname);
        userInfoService.update(wrapper);

        //返回结果
        return Result.ok().message("修改成功");
    }

    //通过旧密码修改密码
    @PutMapping("/updatePasswordByOldPassword")
    public Result updatePasswordByOldPassword(@RequestHeader String token,@RequestBody PasswordVo passwordVo){
        //信息验证
        if(!passwordVo.getPassword().matches("^[a-zA-Z0-9]{6,16}$"))
            return Result.error().message("密码不符合要求，请输入6-16位数字或字母");
        UserInfo userInfo = userInfoService.getUserInfoByPhone(JwtUtil.getUsername(token));
        if(!userInfo.getPassword().equals(MD5Util.encrypt(passwordVo.getOldPassword())))
            return Result.error().message("原密码错误");

        //执行修改
        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<UserInfo>().
                eq("phone", JwtUtil.getUsername(token)).
                set("password", MD5Util.encrypt(passwordVo.getPassword()));
        userInfoService.update(wrapper);

        //返回结果
        return Result.ok().message("修改成功");
    }

    //通过验证码修改密码
    @PutMapping("/updatePasswordByCode")
    public Result updatePasswordByCode(@RequestBody PasswordVo passwordVo){
        //信息验证
        if(!passwordVo.getPassword().matches("^[a-zA-Z0-9]{6,16}$"))
            return Result.error().message("密码不符合要求，请输入6-16位数字或字母");
        String key = passwordVo.getPhone() + ":code";
        String value = (String) redisTemplate.opsForValue().get(key);
        if(!passwordVo.getCode().equals(value))
            return Result.error().message("验证码错误");

        //执行修改
        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<UserInfo>().
                eq("phone", passwordVo.getPhone()).
                set("password", MD5Util.encrypt(passwordVo.getPassword()));
        userInfoService.update(wrapper);

        //返回结果
        return Result.ok().message("修改成功");
    }

    //获取验证码
    @GetMapping("/getCode")
    public Result getCode(String phone){
        //判断手机号格式
        if(!phone.matches("(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}")){
            return Result.error().message("手机号格式错误");
        }
        return new MessageUtil().getCode(phone);
    }


    //获取一个token,用于测试
    @GetMapping("/token")
    public Result getToken(){
        return Result.ok().data("token",JwtUtil.createToken("18250956975"));
    }


}
