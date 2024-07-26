-- 创建库
create database if not exists openapi_platform;

-- 切换库
use openapi_platform;

-- 用户表
drop table if exists openapi_platform.`user`;
create table if not exists openapi_platform.`user`
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    email        varchar(256)                           not null comment '邮箱',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    accessKey    varchar(512)                              null comment 'accessKey',
    secretKey    varchar(512)                              null comment 'secretKey',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 接口信息表
drop table if exists  openapi_platform.`interface_info`;
create table if not exists openapi_platform.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestParam` text null comment  '请求参数描述,key-value数组JSON对象，key是字段名称，value是字段的JSON类型',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息';


-- 接口测试数据

INSERT INTO openapi_platform.`interface_info` (`name`, `description`, `url`, `requestParam`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `createTime`, `updateTime`, `isDelete`)
VALUES
    ('GetUserInfo', '获取用户信息接口', 'https://api.example.com/getUserInfo', '{"userId":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'GET', 1001, '2024-07-26 12:00:00', '2024-07-26 12:00:00', 0),
    ('CreateOrder', '创建订单接口', 'https://api.example.com/createOrder', '{"productId":"integer", "quantity":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'POST', 1002, '2024-07-26 12:01:00', '2024-07-26 12:01:00', 0),
    ('UpdateUserProfile', '更新用户资料接口', 'https://api.example.com/updateUserProfile', '{"userId":"integer", "profileData":"json"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'PUT', 1003, '2024-07-26 12:02:00', '2024-07-26 12:02:00', 0),
    ('DeleteOrder', '删除订单接口', 'https://api.example.com/deleteOrder', '{"orderId":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 0, 'DELETE', 1004, '2024-07-26 12:03:00', '2024-07-26 12:03:00', 0),
    ('ListProducts', '获取产品列表接口', 'https://api.example.com/listProducts', '{}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'GET', 1005, '2024-07-26 12:04:00', '2024-07-26 12:04:00', 0),
    ('SubmitFeedback', '提交反馈接口', 'https://api.example.com/submitFeedback', '{"userId":"integer", "feedback":"string"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'POST', 1006, '2024-07-26 12:05:00', '2024-07-26 12:05:00', 0),
    ('GetOrderStatus', '获取订单状态接口', 'https://api.example.com/getOrderStatus', '{"orderId":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'GET', 1007, '2024-07-26 12:06:00', '2024-07-26 12:06:00', 0),
    ('UpdateInventory', '更新库存接口', 'https://api.example.com/updateInventory', '{"productId":"integer", "quantity":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 0, 'PUT', 1008, '2024-07-26 12:07:00', '2024-07-26 12:07:00', 0),
    ('CancelOrder', '取消订单接口', 'https://api.example.com/cancelOrder', '{"orderId":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'POST', 1009, '2024-07-26 12:08:00', '2024-07-26 12:08:00', 0),
    ('UserLogin', '用户登录接口', 'https://api.example.com/userLogin', '{"username":"string", "password":"string"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'POST', 1010, '2024-07-26 12:09:00', '2024-07-26 12:09:00', 0),
    ('UserLogout', '用户登出接口', 'https://api.example.com/userLogout', '{"userId":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'POST', 1011, '2024-07-26 12:10:00', '2024-07-26 12:10:00', 0),
    ('GetProductDetails', '获取产品详情接口', 'https://api.example.com/getProductDetails', '{"productId":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'GET', 1012, '2024-07-26 12:11:00', '2024-07-26 12:11:00', 0),
    ('PlaceOrder', '下订单接口', 'https://api.example.com/placeOrder', '{"userId":"integer", "productList":"json"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'POST', 1013, '2024-07-26 12:12:00', '2024-07-26 12:12:00', 0),
    ('UpdateShippingAddress', '更新收货地址接口', 'https://api.example.com/updateShippingAddress', '{"userId":"integer", "address":"json"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'PUT', 1014, '2024-07-26 12:13:00', '2024-07-26 12:13:00', 0),
    ('GetUserOrders', '获取用户订单接口', 'https://api.example.com/getUserOrders', '{"userId":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'GET', 1015, '2024-07-26 12:14:00', '2024-07-26 12:14:00', 0),
    ('UploadProfilePicture', '上传用户头像接口', 'https://api.example.com/uploadProfilePicture', '{"userId":"integer", "image":"binary"}', '{"Content-Type":"multipart/form-data"}', '{"Content-Type":"application/json"}', 1, 'POST', 1016, '2024-07-26 12:15:00', '2024-07-26 12:15:00', 0),
    ('GetCategories', '获取分类列表接口', 'https://api.example.com/getCategories', '{}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'GET', 1017, '2024-07-26 12:16:00', '2024-07-26 12:16:00', 0),
    ('ApplyCoupon', '应用优惠券接口', 'https://api.example.com/applyCoupon', '{"userId":"integer", "couponCode":"string"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'POST', 1018, '2024-07-26 12:17:00', '2024-07-26 12:17:00', 0),
    ('GetOrderHistory', '获取订单历史接口', 'https://api.example.com/getOrderHistory', '{"userId":"integer"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'GET', 1019, '2024-07-26 12:18:00', '2024-07-26 12:18:00', 0),
    ('RegisterUser', '用户注册接口', 'https://api.example.com/registerUser', '{"username":"string", "password":"string", "email":"string"}', '{"Content-Type":"application/json"}', '{"Content-Type":"application/json"}', 1, 'POST', 1020, '2024-07-26 12:19:00', '2024-07-26 12:19:00', 0);

