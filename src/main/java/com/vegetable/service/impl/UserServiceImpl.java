package com.vegetable.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vegetable.common.exception.BusinessException;
import com.vegetable.common.util.PasswordUtil;
import com.vegetable.entity.User;
import com.vegetable.mapper.UserMapper;
import com.vegetable.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Override
    public String login(String phone, String password) {
        // 1. 查询用户
        User user = getUserByPhone(phone);
        if (user == null) {
            throw new BusinessException(400, "手机号或密码错误");
        }

        // 2. 验证密码（支持BCrypt和SHA-256）
        if (!PasswordUtil.matches(password, user.getPassword())) {
            throw new BusinessException(400, "手机号或密码错误");
        }

        // 3. 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(400, "账号已被禁用");
        }
        if (user.getStatus() == 2) {
            throw new BusinessException(400, "账号已被冻结");
        }

        // 4. 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);

        // 5. 使用Sa-Token登录,传入用户ID
        // 角色信息会通过StpInterfaceImpl自动获取，无需手动设置
        StpUtil.login(user.getUserId());

        // 6. 返回Token
        return StpUtil.getTokenValue();
    }

    @Override
    public User register(String username, String phone, String password) {
        // 1. 检查手机号是否已注册
        User existUser = getUserByPhone(phone);
        if (existUser != null) {
            throw new BusinessException(400, "手机号已被注册");
        }

        // 2. 检查密码强度
        if (!PasswordUtil.isStrongPassword(password)) {
            throw new BusinessException(400, "密码长度至少为6位");
        }

        // 3. 密码加密（使用SHA-256以兼容现有测试数据）
        // 新用户建议使用: String encryptedPassword = PasswordUtil.encode(password);
        String encryptedPassword = PasswordUtil.encodeWithSHA256(password);

        // 4. 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(encryptedPassword);
        user.setRole("customer"); // 默认角色为普通用户
        user.setStatus(1); // 默认状态为正常
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 5. 保存用户
        save(user);

        return user;
    }

    @Override
    public User getUserByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getCurrentUser() {
        // 获取当前登录用户ID
        int userId = StpUtil.getLoginIdAsInt();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(401, "用户不存在或已被删除");
        }
        // 清空密码字段
        user.setPassword(null);
        return user;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        // 1. 获取当前用户
        User user = getCurrentUser();
        user = getById(user.getUserId()); // 重新查询以获取密码字段

        // 2. 验证旧密码
        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(400, "旧密码错误");
        }

        // 3. 检查新密码强度
        if (!PasswordUtil.isStrongPassword(newPassword)) {
            throw new BusinessException(400, "新密码长度至少为6位");
        }

        // 4. 更新密码（使用SHA-256以兼容现有数据格式）
        String encryptedNewPassword = PasswordUtil.encodeWithSHA256(newPassword);
        user.setPassword(encryptedNewPassword);
        user.setUpdateTime(LocalDateTime.now());

        return updateById(user);
    }
}
