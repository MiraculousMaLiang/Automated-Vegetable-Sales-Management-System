package com.vegetable.config;

import cn.dev33.satoken.stp.StpInterface;
import com.vegetable.entity.User;
import com.vegetable.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token 权限认证接口实现
 * 用于提供用户的角色和权限信息
 *
 * @author vegetable-system
 * @since 2025-11-14
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserService userService;

    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId   账号id
     * @param loginType 登录类型
     * @return 权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本项目暂不使用细粒度权限控制，返回空列表
        // 如需权限控制，可根据用户角色返回相应权限
        List<String> permissions = new ArrayList<>();

        // 示例：可以根据角色添加权限
        // User user = userService.getById(Integer.parseInt(loginId.toString()));
        // if ("admin".equals(user.getRole())) {
        //     permissions.add("user.add");
        //     permissions.add("user.delete");
        // }

        return permissions;
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 登录类型
     * @return 角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roles = new ArrayList<>();

        try {
            // 根据用户ID查询用户信息，获取角色
            User user = userService.getById(Integer.parseInt(loginId.toString()));
            if (user != null && user.getRole() != null) {
                roles.add(user.getRole());
            }
        } catch (Exception e) {
            // 如果查询失败，返回空角色列表
            e.printStackTrace();
        }

        return roles;
    }
}
