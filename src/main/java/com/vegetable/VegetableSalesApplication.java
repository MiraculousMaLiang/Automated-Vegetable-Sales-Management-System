package com.vegetable;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 自动化蔬菜销售管理系统 - 启动类
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@SpringBootApplication
@MapperScan("com.vegetable.mapper")
public class VegetableSalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(VegetableSalesApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("蔬菜销售管理系统启动成功!");
        System.out.println("接口文档地址: http://localhost:8080/api/doc.html");
        System.out.println("========================================\n");
    }
}
