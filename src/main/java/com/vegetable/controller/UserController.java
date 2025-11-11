package com.vegetable.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.vegetable.common.Result;
import com.vegetable.entity.User;
import com.vegetable.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Tag(name = "用户管理", description = "用户注册、登录、信息管理等接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam @NotBlank(message = "手机号不能为空") String phone,
                                              @RequestParam @NotBlank(message = "密码不能为空") String password) {
        String token = userService.login(phone, password);
        User user = userService.getCurrentUser();

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", user);

        return Result.success(data);
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<User> register(@RequestParam @NotBlank(message = "用户名不能为空") String username,
                                  @RequestParam @NotBlank(message = "手机号不能为空") String phone,
                                  @RequestParam @NotBlank(message = "密码不能为空") String password) {
        User user = userService.register(username, phone, password);
        // 清空密码
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        return Result.success(user);
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestParam @NotBlank(message = "旧密码不能为空") String oldPassword,
                                        @RequestParam @NotBlank(message = "新密码不能为空") String newPassword) {
        userService.changePassword(oldPassword, newPassword);
        return Result.success();
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息")
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody User user) {
        User currentUser = userService.getCurrentUser();
        user.setUserId(currentUser.getUserId());
        // 不允许修改敏感字段
        user.setPhone(null);
        user.setPassword(null);
        user.setRole(null);
        user.setStatus(null);

        userService.updateById(user);
        return Result.success();
    }
}
