package com.fjut.healthydietapp.service;

import com.fjut.healthydietapp.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
public interface UserInfoService extends IService<UserInfo> {
    boolean verifyAccount(String phone, String password);
    UserInfo getUserInfoByPhone(String phone);
}
