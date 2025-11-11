# 自动化蔬菜销售管理系统

基于鸿蒙HarmonyOS的自动化蔬菜销售管理系统后端服务

## 📌 项目简介

本项目是一个完整的蔬菜销售管理系统后端服务，提供用户管理、商品管理、购物车、订单处理、库存管理、售后服务等功能模块。

## 🛠️ 技术栈

- **框架**: Spring Boot 3.1.5
- **安全认证**: Sa-Token 1.37.0 (替代传统JWT)
- **数据库**: MySQL 8.0
- **ORM框架**: MyBatis Plus 3.5.4.1
- **缓存**: Redis 7.0
- **工具库**: Hutool 5.8.23
- **API文档**: Knife4j 4.3.0
- **构建工具**: Maven

## 📁 项目结构

```
vegetable-sales-system/
├── sql/                          # 数据库脚本
│   ├── schema.sql               # 建表脚本
│   └── data.sql                 # 测试数据
├── src/main/java/com/vegetable/
│   ├── VegetableSalesApplication.java  # 启动类
│   ├── common/                  # 通用模块
│   │   ├── Result.java         # 统一响应结果
│   │   └── exception/          # 异常处理
│   ├── config/                  # 配置类
│   │   ├── SaTokenConfig.java  # Sa-Token配置
│   │   ├── RedisConfig.java    # Redis配置
│   │   └── Knife4jConfig.java  # API文档配置
│   ├── entity/                  # 实体类(14张表)
│   │   ├── User.java           # 用户
│   │   ├── Address.java        # 收货地址
│   │   ├── Vegetable.java      # 蔬菜商品
│   │   ├── Cart.java           # 购物车
│   │   ├── Order.java          # 订单
│   │   ├── OrderDetail.java    # 订单详情
│   │   ├── Refund.java         # 退款售后
│   │   ├── InventoryLog.java   # 库存日志
│   │   ├── Review.java         # 评价
│   │   ├── Coupon.java         # 优惠券
│   │   ├── UserCoupon.java     # 用户优惠券
│   │   ├── Notification.java   # 消息通知
│   │   ├── OperationLog.java   # 操作日志
│   │   └── LoginLog.java       # 登录日志
│   ├── mapper/                  # 数据访问层
│   ├── service/                 # 服务层
│   │   ├── UserService.java
│   │   ├── VegetableService.java
│   │   ├── CartService.java
│   │   └── impl/               # 服务实现类
│   └── controller/              # 控制器层
│       ├── UserController.java
│       ├── VegetableController.java
│       └── CartController.java
└── src/main/resources/
    ├── application.yml          # 配置文件
    └── mapper/                  # MyBatis XML映射文件
```

## 🚀 快速开始

### 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 7.0+
- Maven 3.6+

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行建表脚本
source sql/schema.sql

# 导入测试数据
source sql/data.sql
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库和Redis连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vegetable_db
    username: root
    password: your_password

  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
```

### 4. 启动项目

```bash
# 使用Maven启动
mvn spring-boot:run

# 或打包后启动
mvn clean package
java -jar target/vegetable-sales-system-1.0.0.jar
```

### 5. 访问接口文档

启动成功后访问: http://localhost:8080/api/doc.html

## 📚 核心功能模块

### 1. 用户管理模块 ✅

- ✅ 用户注册与登录
- ✅ 多角色权限管理(管理员/商家/消费者)
- ✅ 用户信息管理
- ✅ 收货地址管理
- ✅ 密码修改

### 2. 商品管理模块 ✅

- ✅ 商品信息增删改查
- ✅ 分页查询、分类筛选
- ✅ 上下架管理
- ✅ 库存管理(扣减/增加)
- ✅ 库存预警检查
- ✅ 库存预警商品列表

### 3. 购物车模块 ✅

- ✅ 添加/删除商品
- ✅ 修改数量
- ✅ 全选/清空
- ✅ 商品选中状态管理

### 4. 订单管理模块 ✅

- ✅ 订单创建(含库存校验)
- ✅ 订单支付(模拟支付)
- ✅ 订单状态跟踪(待支付/已支付/已发货/已完成/已取消)
- ✅ 商家发货(物流单号)
- ✅ 确认收货
- ✅ 订单取消
- ✅ 我的订单列表
- ✅ 商家订单管理
- ✅ 订单详情查询
- ✅ 支付超时自动取消

### 5. 库存管理模块 ✅

- ✅ 库存自动扣减(订单支付后)
- ✅ 库存自动恢复(退货)
- ✅ 库存预警检查
- ✅ 库存日志记录(入库/出库/销售/退货/损耗)
- ✅ 库存预警通知推送

### 6. 售后管理模块 ✅

- ✅ 退款申请(仅退款/退货退款)
- ✅ 售后审核流程(商家审核)
- ✅ 同意退款(自动退款到账)
- ✅ 拒绝退款
- ✅ 我的退款列表
- ✅ 商家待处理退款列表
- ✅ 退款详情查询

### 7. 消息通知模块 ✅

- ✅ 订单消息推送(支付成功/发货/完成)
- ✅ 库存预警通知(商家端)
- ✅ 系统消息通知
- ✅ 营销活动推送
- ✅ 未读消息列表
- ✅ 标记已读/全部已读

### 8. 数据统计模块 ✅

- ✅ 销售额统计(按时间段)
- ✅ 热销商品Top10
- ✅ 销售额Top10商品
- ✅ 用户统计(总数/角色分布)
- ✅ 订单统计(各状态订单数)
- ✅ 每日销售趋势图

## 🔐 权限说明

系统使用Sa-Token进行权限认证，支持三种角色：

- **admin**: 管理员，拥有所有权限
- **merchant**: 商家，可管理自己的商品
- **customer**: 普通用户，可浏览商品、下单购买

## 📡 API接口示例

### 1. 用户登录

```http
POST /api/user/login
Content-Type: application/x-www-form-urlencoded

phone=13812345678&password=123456
```

**响应:**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "userId": 1001,
      "username": "张三",
      "phone": "138****5678",
      "role": "customer"
    }
  },
  "timestamp": 1699999999999
}
```

### 2. 获取商品列表

```http
GET /api/vegetable/list?page=1&size=10&category=叶菜类
```

### 3. 添加到购物车

```http
POST /api/cart?vegId=2001&quantity=3
Authorization: Bearer {token}
```

### 4. 创建订单

```http
POST /api/order/create
Authorization: Bearer {token}
Content-Type: application/json

{
  "addressId": 1,
  "remark": "送到门口即可",
  "items": [
    {
      "vegId": 2001,
      "quantity": 3
    },
    {
      "vegId": 2002,
      "quantity": 2
    }
  ]
}
```

### 5. 支付订单

```http
POST /api/order/pay/2025111009453012345678?payMethod=WECHAT
Authorization: Bearer {token}
```

### 6. 申请退款

```http
POST /api/refund/apply
Authorization: Bearer {token}

orderId=2025111009453012345678&refundType=REFUND_ONLY&refundReason=商品质量问题
```

### 7. 获取销售统计

```http
GET /api/statistics/sales?startDate=2025-11-01&endDate=2025-11-11
Authorization: Bearer {token}
```

## 🔧 配置说明

### Sa-Token配置

```yaml
sa-token:
  token-name: Authorization      # Token名称
  timeout: 7200                  # Token有效期(秒)
  activity-timeout: 1800         # 临时有效期(秒)
  is-concurrent: true            # 是否允许并发登录
  is-read-header: true          # 从Header读取Token
```

### Redis配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
      timeout: 10000ms
```

## 📝 开发规范

### 代码规范

- 使用Lombok简化代码
- 统一使用Result包装响应结果
- 异常统一由GlobalExceptionHandler处理
- 使用@Validated进行参数校验

### 数据库规范

- 表名使用`t_`前缀
- 主键统一使用`id`结尾
- 时间字段统一使用LocalDateTime
- 必须有create_time和update_time字段

### 接口规范

- 遵循RESTful设计原则
- 使用Swagger/Knife4j注解标注接口
- 统一返回Result对象

## 🐛 常见问题

### 1. 数据库连接失败

检查MySQL是否启动，用户名密码是否正确。

### 2. Redis连接失败

确保Redis服务已启动，端口6379未被占用。

### 3. 启动报错找不到类

执行 `mvn clean install` 重新构建项目。

## 📄 测试账号

系统已内置测试账号：

| 角色 | 手机号 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | 13000000000 | 123456 | 系统管理员 |
| 商家 | 13987654321 | 123456 | 测试商家 |
| 用户 | 13812345678 | 123456 | 测试用户 |

注意：密码为SHA256加密后的值，原始密码均为空字符串的SHA256值，实际使用时请修改。

## 📞 联系方式

- 项目地址: https://github.com/yourname/vegetable-sales-system
- 问题反馈: https://github.com/yourname/vegetable-sales-system/issues
- 邮箱: support@vegetable-system.com

## 📜 开源协议

本项目基于 [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) 开源协议。

## 🙏 致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Sa-Token](https://sa-token.cc/)
- [MyBatis Plus](https://baomidou.com/)
- [Hutool](https://hutool.cn/)
- [Knife4j](https://doc.xiaominfo.com/)

---

**版本**: v1.0.0
**最后更新**: 2025-11-11
