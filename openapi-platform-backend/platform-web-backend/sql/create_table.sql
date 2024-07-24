-- 创建库
create database if not exists openapi_platform;

-- 切换库
use openapi_platform;

-- 用户表
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
create table if not exists openapi_platform.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息';

-- 插入测试数据
INSERT INTO openapi_platform.interface_info (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `createTime`, `updateTime`, `isDelete`) VALUES
                                                                                                                                                                                        ('接口1', '这是接口1的描述', 'https://api.example.com/interface1', '{"Content-Type": "application/json"}', '{"Content-Type": "application/json"}', 1, 'POST', 1, '2024-07-23 10:00:00', '2024-07-23 10:00:00', 0),
                                                                                                                                                                                        ('接口2', '这是接口2的描述', 'https://api.example.com/interface2', '{"Authorization": "Bearer token"}', '{"Content-Type": "application/json"}', 0, 'GET', 2, '2024-07-23 11:00:00', '2024-07-23 11:00:00', 0),
                                                                                                                                                                                        ('接口3', '这是接口3的描述', 'https://api.example.com/interface3', '{"Content-Type": "application/xml"}', '{"Content-Type": "application/xml"}', 1, 'POST', 1, '2024-07-23 12:00:00', '2024-07-23 12:00:00', 0),
                                                                                                                                                                                        ('接口4', '这是接口4的描述', 'https://api.example.com/interface4', '{"Authorization": "Basic credentials"}', '{"Content-Type": "application/json"}', 1, 'GET', 3, '2024-07-23 13:00:00', '2024-07-23 13:00:00', 0),
                                                                                                                                                                                        ('接口5', '这是接口5的描述', 'https://api.example.com/interface5', NULL, NULL, 1, 'POST', 2, '2024-07-23 14:00:00', '2024-07-23 14:00:00', 0),
                                                                                                                                                                                        ('接口6', '这是接口6的描述', 'https://api.example.com/interface6', '{"Content-Type": "application/json"}', '{"Content-Type": "application/json"}', 0, 'POST', 3, '2024-07-23 15:00:00', '2024-07-23 15:00:00', 0),
                                                                                                                                                                                        ('接口7', '这是接口7的描述', 'https://api.example.com/interface7', '{"Authorization": "Bearer token"}', '{"Content-Type": "application/json"}', 1, 'GET', 1, '2024-07-23 16:00:00', '2024-07-23 16:00:00', 0),
                                                                                                                                                                                        ('接口8', '这是接口8的描述', 'https://api.example.com/interface8', '{"Content-Type": "application/xml"}', '{"Content-Type": "application/xml"}', 1, 'POST', 2, '2024-07-23 17:00:00', '2024-07-23 17:00:00', 0),
                                                                                                                                                                                        ('接口9', '这是接口9的描述', 'https://api.example.com/interface9', '{"Authorization": "Basic credentials"}', '{"Content-Type": "application/json"}', 0, 'GET', 3, '2024-07-23 18:00:00', '2024-07-23 18:00:00', 0),
                                                                                                                                                                                        ('接口10', '这是接口10的描述', 'https://api.example.com/interface10', NULL, NULL, 1, 'POST', 1, '2024-07-23 19:00:00', '2024-07-23 19:00:00', 0),
                                                                                                                                                                                        ('接口11', '这是接口11的描述', 'https://api.example.com/interface11', '{"Content-Type": "application/json"}', '{"Content-Type": "application/json"}', 1, 'POST', 2, '2024-07-23 20:00:00', '2024-07-23 20:00:00', 0),
                                                                                                                                                                                        ('接口12', '这是接口12的描述', 'https://api.example.com/interface12', '{"Authorization": "Bearer token"}', '{"Content-Type": "application/json"}', 0, 'GET', 3, '2024-07-23 21:00:00', '2024-07-23 21:00:00', 0),
                                                                                                                                                                                        ('接口13', '这是接口13的描述', 'https://api.example.com/interface13', '{"Content-Type": "application/xml"}', '{"Content-Type": "application/xml"}', 1, 'POST', 1, '2024-07-23 22:00:00', '2024-07-23 22:00:00', 0),
                                                                                                                                                                                        ('接口14', '这是接口14的描述', 'https://api.example.com/interface14', '{"Authorization": "Basic credentials"}', '{"Content-Type": "application/json"}', 1, 'GET', 2, '2024-07-23 23:00:00', '2024-07-23 23:00:00', 0),
                                                                                                                                                                                        ('接口15', '这是接口15的描述', 'https://api.example.com/interface15', '{"Content-Type": "application/json"}', '{"Content-Type": "application/json"}', 1, 'POST', 3, '2024-07-24 00:00:00', '2024-07-24 00:00:00', 0);

-- 更多的测试数据可以根据需要插入

