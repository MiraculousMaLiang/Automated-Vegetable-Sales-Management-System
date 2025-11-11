-- ========================================
-- 自动化蔬菜销售管理系统数据库
-- 数据库版本: MySQL 8.0
-- 创建时间: 2025-11-11
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `vegetable_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `vegetable_db`;

-- ========================================
-- 1. 用户表
-- ========================================
CREATE TABLE `t_user` (
  `user_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户编号',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `phone` VARCHAR(20) UNIQUE NOT NULL COMMENT '手机号',
  `password` VARCHAR(64) NOT NULL COMMENT '密码(SHA256加密)',
  `avatar_url` VARCHAR(255) DEFAULT '/images/default_avatar.jpg' COMMENT '头像URL',
  `role` VARCHAR(20) NOT NULL DEFAULT 'customer' COMMENT '角色(admin/merchant/customer)',
  `status` TINYINT DEFAULT 1 COMMENT '账号状态(0禁用/1正常/2冻结)',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `id_card` VARCHAR(18) COMMENT '身份证号(加密存储)',
  `business_license` VARCHAR(255) COMMENT '营业执照(商家)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  INDEX idx_phone(phone),
  INDEX idx_role(role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ========================================
-- 2. 收货地址表
-- ========================================
CREATE TABLE `t_address` (
  `address_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '地址编号',
  `user_id` INT NOT NULL COMMENT '用户编号',
  `contact_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `province` VARCHAR(50) NOT NULL COMMENT '省份',
  `city` VARCHAR(50) NOT NULL COMMENT '城市',
  `district` VARCHAR(50) NOT NULL COMMENT '区县',
  `detail_address` VARCHAR(255) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT DEFAULT 0 COMMENT '是否默认(0否/1是)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE,
  INDEX idx_user_id(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址表';

-- ========================================
-- 3. 蔬菜信息表
-- ========================================
CREATE TABLE `t_vegetable` (
  `veg_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '蔬菜编号',
  `veg_name` VARCHAR(50) NOT NULL COMMENT '蔬菜名称',
  `category` VARCHAR(30) NOT NULL COMMENT '分类(叶菜类/茄果类/瓜类/根茎类)',
  `price` DECIMAL(8,2) NOT NULL COMMENT '单价(元/斤)',
  `stock` INT DEFAULT 0 COMMENT '库存数量(斤)',
  `stock_threshold` INT DEFAULT 20 COMMENT '库存预警阈值(斤)',
  `sales_count` INT DEFAULT 0 COMMENT '累计销量(斤)',
  `origin` VARCHAR(50) COMMENT '产地',
  `image_urls` TEXT COMMENT '商品图片(JSON数组,最多9张)',
  `description` TEXT COMMENT '商品描述(富文本)',
  `status` TINYINT DEFAULT 1 COMMENT '上架状态(0下架/1上架)',
  `merchant_id` INT NOT NULL COMMENT '商家编号',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (merchant_id) REFERENCES t_user(user_id) ON DELETE CASCADE,
  INDEX idx_category(category),
  INDEX idx_status(status),
  INDEX idx_merchant(merchant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='蔬菜信息表';

-- ========================================
-- 4. 购物车表
-- ========================================
CREATE TABLE `t_cart` (
  `cart_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车编号',
  `user_id` INT NOT NULL COMMENT '用户编号',
  `veg_id` INT NOT NULL COMMENT '蔬菜编号',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量(斤)',
  `is_selected` TINYINT DEFAULT 1 COMMENT '是否选中(0否/1是)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE,
  FOREIGN KEY (veg_id) REFERENCES t_vegetable(veg_id) ON DELETE CASCADE,
  UNIQUE KEY uk_user_veg(user_id, veg_id),
  INDEX idx_user_id(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- ========================================
-- 5. 订单表
-- ========================================
CREATE TABLE `t_order` (
  `order_id` VARCHAR(32) PRIMARY KEY COMMENT '订单编号(时间戳+随机数)',
  `user_id` INT NOT NULL COMMENT '下单用户',
  `address_id` INT NOT NULL COMMENT '收货地址编号',
  `total_price` DECIMAL(10,2) NOT NULL COMMENT '商品总价',
  `freight` DECIMAL(6,2) DEFAULT 0.00 COMMENT '运费',
  `coupon_discount` DECIMAL(6,2) DEFAULT 0.00 COMMENT '优惠券抵扣',
  `actual_payment` DECIMAL(10,2) NOT NULL COMMENT '实际支付金额',
  `order_status` VARCHAR(20) NOT NULL DEFAULT 'WAIT_PAY' COMMENT '订单状态',
  `pay_method` VARCHAR(20) COMMENT '支付方式(WECHAT/ALIPAY)',
  `pay_time` DATETIME COMMENT '支付时间',
  `pay_transaction_id` VARCHAR(64) COMMENT '支付流水号',
  `ship_time` DATETIME COMMENT '发货时间',
  `tracking_number` VARCHAR(50) COMMENT '物流单号',
  `logistics_company` VARCHAR(50) COMMENT '物流公司',
  `receive_time` DATETIME COMMENT '确认收货时间',
  `cancel_time` DATETIME COMMENT '取消时间',
  `cancel_reason` VARCHAR(255) COMMENT '取消原因',
  `remark` VARCHAR(255) COMMENT '订单备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES t_user(user_id),
  FOREIGN KEY (address_id) REFERENCES t_address(address_id),
  INDEX idx_user_id(user_id),
  INDEX idx_status(order_status),
  INDEX idx_create_time(create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- ========================================
-- 6. 订单详情表
-- ========================================
CREATE TABLE `t_order_detail` (
  `detail_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细编号',
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单编号',
  `veg_id` INT NOT NULL COMMENT '商品编号',
  `veg_name` VARCHAR(50) NOT NULL COMMENT '商品名称(快照)',
  `veg_image` VARCHAR(255) COMMENT '商品图片(快照)',
  `quantity` INT NOT NULL COMMENT '购买数量(斤)',
  `price` DECIMAL(8,2) NOT NULL COMMENT '单价(快照)',
  `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES t_order(order_id) ON DELETE CASCADE,
  INDEX idx_order_id(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单详情表';

-- ========================================
-- 7. 退款/售后表
-- ========================================
CREATE TABLE `t_refund` (
  `refund_id` VARCHAR(32) PRIMARY KEY COMMENT '售后单号',
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单编号',
  `user_id` INT NOT NULL COMMENT '用户编号',
  `refund_type` VARCHAR(20) NOT NULL COMMENT '类型(REFUND_ONLY/RETURN_REFUND)',
  `refund_amount` DECIMAL(10,2) NOT NULL COMMENT '退款金额',
  `refund_reason` VARCHAR(255) NOT NULL COMMENT '退款原因',
  `refund_images` TEXT COMMENT '凭证图片(JSON数组)',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态(PENDING/APPROVED/REJECTED/COMPLETED)',
  `merchant_reply` VARCHAR(500) COMMENT '商家回复',
  `reject_reason` VARCHAR(255) COMMENT '拒绝原因',
  `refund_time` DATETIME COMMENT '退款完成时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES t_order(order_id),
  FOREIGN KEY (user_id) REFERENCES t_user(user_id),
  INDEX idx_order_id(order_id),
  INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款售后表';

-- ========================================
-- 8. 库存日志表
-- ========================================
CREATE TABLE `t_inventory_log` (
  `log_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志编号',
  `veg_id` INT NOT NULL COMMENT '蔬菜编号',
  `change_type` VARCHAR(20) NOT NULL COMMENT '变动类型(IN/OUT/SALE/RETURN/LOSS)',
  `quantity_change` INT NOT NULL COMMENT '数量变动(正数为增加,负数为减少)',
  `stock_before` INT NOT NULL COMMENT '变动前库存',
  `stock_after` INT NOT NULL COMMENT '变动后库存',
  `related_order_id` VARCHAR(32) COMMENT '关联订单号',
  `operator_id` INT COMMENT '操作人编号',
  `remark` VARCHAR(255) COMMENT '备注',
  `change_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '变动时间',
  FOREIGN KEY (veg_id) REFERENCES t_vegetable(veg_id),
  INDEX idx_veg_id(veg_id),
  INDEX idx_change_time(change_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存日志表';

-- ========================================
-- 9. 评价表
-- ========================================
CREATE TABLE `t_review` (
  `review_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '评价编号',
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单编号',
  `user_id` INT NOT NULL COMMENT '用户编号',
  `veg_id` INT NOT NULL COMMENT '蔬菜编号',
  `rating` TINYINT NOT NULL COMMENT '星级评分(1-5)',
  `comment` TEXT COMMENT '评价内容',
  `review_images` TEXT COMMENT '评价图片(JSON数组)',
  `is_anonymous` TINYINT DEFAULT 0 COMMENT '是否匿名(0否/1是)',
  `status` TINYINT DEFAULT 1 COMMENT '状态(0审核中/1已发布/2已屏蔽)',
  `merchant_reply` TEXT COMMENT '商家回复',
  `reply_time` DATETIME COMMENT '回复时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  FOREIGN KEY (order_id) REFERENCES t_order(order_id),
  FOREIGN KEY (user_id) REFERENCES t_user(user_id),
  FOREIGN KEY (veg_id) REFERENCES t_vegetable(veg_id),
  INDEX idx_veg_id(veg_id),
  INDEX idx_rating(rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价表';

-- ========================================
-- 10. 优惠券表
-- ========================================
CREATE TABLE `t_coupon` (
  `coupon_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '优惠券编号',
  `coupon_name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
  `coupon_type` VARCHAR(20) NOT NULL COMMENT '类型(FULL_REDUCTION/DISCOUNT/NEW_USER)',
  `discount_amount` DECIMAL(6,2) COMMENT '满减金额',
  `discount_rate` DECIMAL(3,2) COMMENT '折扣率(如0.8表示8折)',
  `min_purchase` DECIMAL(8,2) DEFAULT 0 COMMENT '最低消费金额',
  `total_quantity` INT NOT NULL COMMENT '发放总量',
  `remaining_quantity` INT NOT NULL COMMENT '剩余数量',
  `start_time` DATETIME NOT NULL COMMENT '有效期开始',
  `end_time` DATETIME NOT NULL COMMENT '有效期结束',
  `status` TINYINT DEFAULT 1 COMMENT '状态(0下架/1上架)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券表';

-- ========================================
-- 11. 用户优惠券表
-- ========================================
CREATE TABLE `t_user_coupon` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT NOT NULL COMMENT '用户编号',
  `coupon_id` INT NOT NULL COMMENT '优惠券编号',
  `status` VARCHAR(20) DEFAULT 'UNUSED' COMMENT '状态(UNUSED/USED/EXPIRED)',
  `used_order_id` VARCHAR(32) COMMENT '使用订单号',
  `receive_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `used_time` DATETIME COMMENT '使用时间',
  FOREIGN KEY (user_id) REFERENCES t_user(user_id),
  FOREIGN KEY (coupon_id) REFERENCES t_coupon(coupon_id),
  INDEX idx_user_id(user_id),
  INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户优惠券表';

-- ========================================
-- 12. 操作日志表
-- ========================================
CREATE TABLE `t_operation_log` (
  `log_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志编号',
  `operator_id` INT NOT NULL COMMENT '操作人编号',
  `operator_name` VARCHAR(50) COMMENT '操作人姓名',
  `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型(LOGIN/LOGOUT/ADD/UPDATE/DELETE)',
  `operation_module` VARCHAR(50) COMMENT '操作模块(USER/VEGETABLE/ORDER等)',
  `operation_content` TEXT COMMENT '操作内容',
  `ip_address` VARCHAR(50) COMMENT 'IP地址',
  `user_agent` VARCHAR(255) COMMENT '浏览器信息',
  `execute_time` INT COMMENT '执行时长(ms)',
  `status` TINYINT DEFAULT 1 COMMENT '状态(0失败/1成功)',
  `error_message` TEXT COMMENT '错误信息',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  INDEX idx_operator(operator_id),
  INDEX idx_create_time(create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ========================================
-- 13. 登录日志表
-- ========================================
CREATE TABLE `t_login_log` (
  `log_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志编号',
  `user_id` INT COMMENT '用户编号',
  `login_phone` VARCHAR(20) NOT NULL COMMENT '登录手机号',
  `login_type` VARCHAR(20) NOT NULL COMMENT '登录方式(PASSWORD/SMS_CODE)',
  `ip_address` VARCHAR(50) COMMENT 'IP地址',
  `login_location` VARCHAR(100) COMMENT '登录地点(根据IP解析)',
  `device_type` VARCHAR(50) COMMENT '设备类型(HarmonyOS/Android/iOS)',
  `browser` VARCHAR(100) COMMENT '浏览器',
  `status` TINYINT NOT NULL COMMENT '登录状态(0失败/1成功)',
  `fail_reason` VARCHAR(255) COMMENT '失败原因',
  `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  INDEX idx_user_id(user_id),
  INDEX idx_login_time(login_time),
  INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- ========================================
-- 14. 消息通知表
-- ========================================
CREATE TABLE `t_notification` (
  `notify_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知编号',
  `user_id` INT NOT NULL COMMENT '接收用户编号',
  `notify_type` VARCHAR(30) NOT NULL COMMENT '通知类型(ORDER/SYSTEM/PROMOTION/INVENTORY)',
  `title` VARCHAR(100) NOT NULL COMMENT '通知标题',
  `content` TEXT NOT NULL COMMENT '通知内容',
  `related_id` VARCHAR(50) COMMENT '关联业务ID(如订单号)',
  `is_read` TINYINT DEFAULT 0 COMMENT '是否已读(0未读/1已读)',
  `push_status` TINYINT DEFAULT 0 COMMENT '推送状态(0未推送/1已推送)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `read_time` DATETIME COMMENT '阅读时间',
  FOREIGN KEY (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE,
  INDEX idx_user_id(user_id),
  INDEX idx_is_read(is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息通知表';
