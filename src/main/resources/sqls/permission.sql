CREATE DATABASE sophdb;

# 权限设置表
CREATE TABLE IF NOT EXISTS `s_permission` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `domain` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限分类或者权限作用域',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限定义键值(字母数字加下划线)' ,
  `name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称' ,
  `cmmt`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNI_IDX_KEY` (`domain`,`key`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1
;

# 权限角色设置
CREATE TABLE IF NOT EXISTS `s_role` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `domain` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限分类或者权限作用域',
  `name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称' ,
  `cmmt`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNI_IDX_KEY` (`domain`,`key`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1
;

# 权限角色包含的权限
CREATE TABLE IF NOT EXISTS `s_role_permissions` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `role_id`  bigint NOT NULL COMMENT 'Role Id' ,
  `permission_id`  bigint NOT NULL COMMENT 'Permission Id' ,
  `permission_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '【冗余】权限定义键值(字母数字加下划线)' ,
  `permission_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '【冗余】权限名称，冗余存储' ,
  `domain` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '【冗余】权限分类或者权限作用域',
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNI_IDX_KEY` (`role_id`,`permission_id`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1
;

# 用户所拥有的单一权限
CREATE TABLE IF NOT EXISTS `s_account_permission` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `account_id`  bigint NOT NULL COMMENT 'Account Id' ,
  `permission_id`  bigint NOT NULL COMMENT 'Permission Id' ,
  `permission_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '【冗余】权限定义键值(字母数字加下划线)' ,
  `permission_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '【冗余】权限名称，冗余存储' ,
  `domain` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '【冗余】权限分类或者权限作用域',
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNI_IDX_KEY` (`account_id`,`permission_id`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1
;

# 用户所拥有的角色
CREATE TABLE IF NOT EXISTS `s_account_role` (
  `id`  bigint NOT NULL AUTO_INCREMENT ,
  `account_id`  bigint NOT NULL COMMENT 'Account Id' ,
  `role_id`  bigint NOT NULL COMMENT 'Role Id' ,
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  `modified_at`  bigint DEFAULT '0' COMMENT '修改时间' ,
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNI_IDX_KEY` (`account_id`,`role_id`) USING BTREE
)
  ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
  AUTO_INCREMENT=1
;