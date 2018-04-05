CREATE DATABASE sophdb;

# 记录 信号集（抽象成数据，神经存储数据是有序信号集，描述为故事）
DROP TABLE IF EXISTS  `s_data`;
CREATE TABLE `s_data` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `hcode` INT UNSIGNED NOT NULL COMMENT '内容 hash code，这里取java hash code',
  `md5` varchar(32) DEFAULT NULL COMMENT '内容 md5',
  `len` INT UNSIGNED NOT NULL COMMENT '内容长度',
  `bites` varchar(4028) DEFAULT NULL COMMENT '内容 数据集(暂时先用varchar)',
  `ts` INT UNSIGNED NOT NULL COMMENT '提及次数，每次被告知，或者每次被匹配上',
  `create_at`  bigint DEFAULT '0' COMMENT '创建时间' ,
  PRIMARY KEY (`id`),
  KEY `CODE_IDX` (`hcode`,`md5`,`len`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

# 记录 信号集 组合关联
DROP TABLE IF EXISTS  `s_data_detail`;
CREATE TABLE `s_data_detail` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `did` BIGINT UNSIGNED NOT NULL COMMENT 'data id',
  `subid` BIGINT UNSIGNED NOT NULL COMMENT 'sub data id',
  `len` INT UNSIGNED NOT NULL COMMENT 'data 长度',
  `slen` INT UNSIGNED NOT NULL COMMENT 'sub data 长度',
  `bites` varchar(4028) DEFAULT NULL COMMENT '冗余存储sub data的内容',
  `ts` INT UNSIGNED NOT NULL COMMENT '匹配次数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `MAP_IDX` (`did`,`subid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

# 记录 抽象对象（对一些数据进行了抽象归纳）
DROP TABLE IF EXISTS  `s_group`;
CREATE TABLE `s_group` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `did` BIGINT UNSIGNED NOT NULL COMMENT '抽象初始来源',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

# 抽象对象，最后被某数据(故事)描述出来了，这个模仿智慧行为，基本很难定义
DROP TABLE IF EXISTS  `s_data_group`;
CREATE TABLE `s_data_group` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gid` BIGINT UNSIGNED NOT NULL COMMENT '抽象类型id',
  `did` BIGINT UNSIGNED NOT NULL COMMENT '数据id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `DATA_GROUP_IDX` (`gid`,`did`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

# 抽象对象，最后被某数据(故事)描述出来了，这个模仿智慧行为，基本很难定义
DROP TABLE IF EXISTS  `s_group_as`;
CREATE TABLE `s_group_as` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gid` BIGINT UNSIGNED NOT NULL COMMENT '抽象类型id',
  `asid` BIGINT UNSIGNED NOT NULL COMMENT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AS_IDX` (`gid`,`asid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;