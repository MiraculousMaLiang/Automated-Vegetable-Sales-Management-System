package com.vegetable.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token配置类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册Sa-Token拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token拦截器,打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 指定一条match规则
            SaRouter
                    .match("/**")    // 拦截所有路由
                    .notMatch(        // 排除不需要登录的路径
                            "/user/login",           // 登录接口
                            "/user/register",        // 注册接口
                            "/vegetable/list",       // 商品列表(游客可见)
                            "/vegetable/{id}",       // 商品详情(游客可见)
                            "/doc.html",             // Knife4j文档
                            "/swagger-resources/**", // Swagger资源
                            "/v3/api-docs/**",       // OpenAPI文档
                            "/webjars/**",           // webjars
                            "/favicon.ico",          // 图标
                            "/error"                 // 错误页面
                    )
                    .check(r -> StpUtil.checkLogin());  // 校验登录状态
        })).addPathPatterns("/**");
    }
}
