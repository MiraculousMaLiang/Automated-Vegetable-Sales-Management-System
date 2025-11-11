package com.vegetable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vegetable.entity.User;

/**
 * 用户服务接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return token
     */
    String login(String phone, String password);

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param phone    手机号
     * @param password 密码
     * @return 用户信息
     */
    User register(String username, String phone, String password);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User getUserByPhone(String phone);

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    User getCurrentUser();

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(String oldPassword, String newPassword);
}
