create database if not exists openapi_platform;
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


-- 用户接口信息表
drop table if exists openapi_platform.`user_interface_info`;
create table if not exists openapi_platform.`user_interface_info`
(
    id           bigint auto_increment comment 'id' primary key,
    userId       bigint not null comment  '用户id',
    interfaceId      bigint not null comment '接口id' ,
    totalNum int default 0 not null comment '用户调用该接口的总的调用次数',
    leftNum int default 0 not null comment '用户调用该接口的剩余调用次数',
    status int default 0 not null comment '用户调用该接口的状态，0:正常，1:禁止',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户接口信息表' collate = utf8mb4_unicode_ci;

-- 插入测试数据
INSERT INTO openapi_platform.user_interface_info (userId, interfaceId, totalNum, leftNum, status, createTime, updateTime, isDelete)
VALUES
    (1, 101, 100, 80, 0, '2024-08-01 10:00:00', '2024-08-01 10:00:00', 0),
    (1, 102, 50, 50, 0, '2024-08-02 11:30:00', '2024-08-02 11:30:00', 0),
    (2, 101, 200, 150, 0, '2024-08-03 14:00:00', '2024-08-03 14:00:00', 0),
    (2, 103, 75, 75, 1, '2024-08-04 16:45:00', '2024-08-04 16:45:00', 0),
    (3, 104, 300, 250, 0, '2024-08-05 09:15:00', '2024-08-05 09:15:00', 1),
    (3, 105, 20, 10, 0, '2024-08-06 12:00:00', '2024-08-06 12:00:00', 0),
    (4, 106, 500, 450, 0, '2024-08-07 18:30:00', '2024-08-07 18:30:00', 0),
    (4, 107, 10, 5, 1, '2024-08-08 20:00:00', '2024-08-08 20:00:00', 0);



-- 调用信息表
drop table if exists openapi_platform.`invoke_info`;
create table if not exists openapi_platform.`invoke_info`
(
    id           bigint auto_increment comment 'id' primary key,
    userId       bigint not null comment  '调用的用户',
    interfaceId      bigint not null comment '调用的接口' ,
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '调用信息表' collate = utf8mb4_unicode_ci;



/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/******************************************/
/*   表名称 = config_info_gray             */
/******************************************/
CREATE TABLE config_info_gray (
                                  id bigint NOT NULL AUTO_INCREMENT,
                                  data_id varchar(255) NOT NULL,
                                  group_id varchar(128) NOT NULL,
                                  tenant_id varchar(128) default '',
                                  gray_name varchar(128) NOT NULL,
                                  gray_rule TEXT,
                                  app_name varchar(128),
                                  src_ip varchar(128),
                                  src_user varchar(128) default '',
                                  content TEXT,
                                  md5 varchar(32) DEFAULT NULL,
                                  encrypted_data_key varchar(32) DEFAULT NULL,
                                  gmt_create timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
                                  gmt_modified timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
                                  constraint configinfogray_id_key PRIMARY KEY (id),
                                  constraint uk_configinfogray_datagrouptenantgrayname UNIQUE (data_id,group_id,tenant_id,gray_name));
CREATE INDEX config_info_gray_dataid_gmt_modified ON config_info_gray(data_id,gmt_modified);
CREATE INDEX config_info_gray_gmt_modified ON config_info_gray(gmt_modified);
/******************************************/
/*   表名称 = config_info                  */
/******************************************/
create database if not exists nacos;
use nacos;
CREATE TABLE `config_info` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                               `group_id` varchar(128) DEFAULT NULL COMMENT 'group_id',
                               `content` longtext NOT NULL COMMENT 'content',
                               `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                               `src_user` text COMMENT 'source user',
                               `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
                               `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                               `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
                               `c_desc` varchar(256) DEFAULT NULL COMMENT 'configuration description',
                               `c_use` varchar(64) DEFAULT NULL COMMENT 'configuration usage',
                               `effect` varchar(64) DEFAULT NULL COMMENT '配置生效的描述',
                               `type` varchar(64) DEFAULT NULL COMMENT '配置的类型',
                               `c_schema` text COMMENT '配置的模式',
                               `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   表名称 = config_info_aggr             */
/******************************************/
CREATE TABLE `config_info_aggr` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                    `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                    `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
                                    `content` longtext NOT NULL COMMENT '内容',
                                    `gmt_modified` datetime NOT NULL COMMENT '修改时间',
                                    `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                                    `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   表名称 = config_info_beta             */
/******************************************/
CREATE TABLE `config_info_beta` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                    `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                    `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                                    `content` longtext NOT NULL COMMENT 'content',
                                    `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
                                    `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                                    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `src_user` text COMMENT 'source user',
                                    `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
                                    `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
                                    `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   表名称 = config_info_tag              */
/******************************************/
CREATE TABLE `config_info_tag` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                   `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                   `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                   `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
                                   `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
                                   `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                                   `content` longtext NOT NULL COMMENT 'content',
                                   `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   `src_user` text COMMENT 'source user',
                                   `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   表名称 = config_tags_relation         */
/******************************************/
CREATE TABLE `config_tags_relation` (
                                        `id` bigint(20) NOT NULL COMMENT 'id',
                                        `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
                                        `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
                                        `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                        `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                        `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
                                        `nid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
                                        PRIMARY KEY (`nid`),
                                        UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
                                        KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   表名称 = group_capacity               */
/******************************************/
CREATE TABLE `group_capacity` (
                                  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
                                  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
                                  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
                                  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
                                  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
                                  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
                                  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
                                  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   表名称 = his_config_info              */
/******************************************/
CREATE TABLE `his_config_info` (
                                   `id` bigint(20) unsigned NOT NULL COMMENT 'id',
                                   `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
                                   `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                   `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                   `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                                   `content` longtext NOT NULL COMMENT 'content',
                                   `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   `src_user` text COMMENT 'source user',
                                   `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
                                   `op_type` char(10) DEFAULT NULL COMMENT 'operation type',
                                   `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
                                   `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
                                   PRIMARY KEY (`nid`),
                                   KEY `idx_gmt_create` (`gmt_create`),
                                   KEY `idx_gmt_modified` (`gmt_modified`),
                                   KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   表名称 = tenant_capacity              */
/******************************************/
CREATE TABLE `tenant_capacity` (
                                   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
                                   `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
                                   `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
                                   `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
                                   `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
                                   `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
                                   `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `kp` varchar(128) NOT NULL COMMENT 'kp',
                               `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
                               `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
                               `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
                               `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
                               `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
                               `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
                               KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE `users` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `username` varchar(50) NOT NULL COMMENT 'username',
                         `password` varchar(500) NOT NULL COMMENT 'password',
                         `enabled` boolean NOT NULL COMMENT 'enabled',
                         PRIMARY KEY (`id`)
);

CREATE TABLE `roles` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `username` varchar(50) NOT NULL COMMENT 'username',
                         `role` varchar(50) NOT NULL COMMENT 'role',
                         PRIMARY KEY (`id`),
                         UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `role` varchar(50) NOT NULL COMMENT 'role',
                               `resource` varchar(128) NOT NULL COMMENT 'resource',
                               `action` varchar(8) NOT NULL COMMENT 'action',
                               PRIMARY KEY (`id`),
                               UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

