package com.fjut.healthydietapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fjut.healthydietapp.entity.UserInfo;
import com.fjut.healthydietapp.mapper.UserInfoMapper;
import com.fjut.healthydietapp.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fjut.healthydietapp.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public boolean verifyAccount(String phone, String password) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        QueryWrapper<UserInfo> query = userInfoQueryWrapper.eq("phone", phone).eq("password", MD5Util.encrypt(password));
        UserInfo userInfo = userInfoMapper.selectOne(query);
        return userInfo != null;
    }

    @Override
    public UserInfo getUserInfoByPhone(String phone) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        QueryWrapper<UserInfo> query = userInfoQueryWrapper.eq("phone", phone);
        return  userInfoMapper.selectOne(query);
    }
}
