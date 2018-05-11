CREATE DATABASE sophdb;

# 用户表 可以达到多个平台用户整合,以手机号、邮箱、身份证、护照进行合并,数据可以共享,不同平台加入尽可能保留其他平台用户状态数据
CREATE TABLE IF NOT EXISTS `s_user` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `nick`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实名字',
  `id_number`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号' ,
  `head`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像url',
  `mobile`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号' ,
  `email`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱' ,
  `gender`  tinyint DEFAULT '0' COMMENT '性别:0未知;1男;2女;3人妖;' ,
  `grade`  int DEFAULT '0' COMMENT '等级' ,
  `rank`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '等级' ,
  `role`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色' ,
  `join_from`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加入来源账号,s_account.platform对应' ,
  `source`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源,记录推荐来源' ,
  `status`  tinyint DEFAULT '0' COMMENT '状态:0正常;-1禁用;' ,
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,

  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNI_IDX_ID_NUM` (`id_number`) USING BTREE ,
  INDEX `IDX_MOBILE` (`mobile`) USING BTREE,
  INDEX `IDX_EMAINL` (`email`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1;

# 用户扩展表,存储一些扩展的数据,便于扩展 (成长值,积分等等)
CREATE TABLE IF NOT EXISTS `s_user_ext` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `uid`  bigint NOT NULL COMMENT 'user id' ,
  `data_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据类型' ,
  `data_value`  varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据值' ,
  `cmmt`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注释',
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  INDEX `UNI_IDX_QUERY` (`uid`,`data_type`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1
;

# 账号表; 用户能绑定多个账号,所有的账号,都具备登录的能力
CREATE TABLE IF NOT EXISTS `s_account` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `platform`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台名|大陆手机号(China mobile)|邮箱账号',
  `open_id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '又名Account:开放平台id|+86-15673886363|soulshangm@gmail.com' ,
  `uuid`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开放平台唯一id' ,
  `nick`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `head`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像url',
  `gender`  tinyint DEFAULT '0' COMMENT '性别:0未知;1男;2女;3人妖;' ,
  `mobile`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号' ,
  `email`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱' ,
  `pswd` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码掩码' ,
  `pswd_salt` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码加盐' ,
  `uid`  bigint NULL DEFAULT NULL COMMENT 'user id,解除绑定后需要清空,且其他信息也需要被抹除' ,
  `info`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方其他信息保留',
  `source`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源,记录推荐来源' ,
  `pre_bk`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备份字段,用于解绑上一个信息备份' ,
  `auth` int DEFAULT '0' COMMENT '认证级别: 1三方授权; 11短信授权; 21语音授权; 51绑定银行卡; 81客服人工调查; 91视频头像+身份证+人工' ,
  `auth_at` bigint DEFAULT '0' COMMENT '认证时间点,主要是可以用于将来过滤认证太久远的账号' ,
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNI_IDX_ID` (`platform`,`open_id`) USING BTREE ,
  INDEX `IDX_USER_ID` (`uid`) USING BTREE,
  INDEX `IDX_MOBILE` (`mobile`) USING BTREE,
  INDEX `IDX_EMAINL` (`email`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1;


# 账号扩展表,存储一些扩展的数据,便于扩展
CREATE TABLE IF NOT EXISTS `s_account_ext` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `account_id`  bigint NOT NULL COMMENT 'account id' ,
  `data_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据类型' ,
  `data_value`  varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据值' ,
  `cmmt`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注释',
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  INDEX `UNI_IDX_QUERY` (`account_id`,`data_type`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1
;

# 用于验证码校验数据表
CREATE TABLE IF NOT EXISTS `s_captcha` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `session` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会话、或者token',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证码类型' ,
  `code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `cmmt`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status`  tinyint DEFAULT '0' COMMENT '状态:1 已验证，0 未验证，-1 验证失败' ,
  `aging`  tinyint DEFAULT '60' COMMENT '时效，单位秒' ,
  `account`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理账号信息，有多个时;号隔开',
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  INDEX `IDX_QUERY` (`session`,`type`,`code`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1
;