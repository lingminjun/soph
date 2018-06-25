CREATE DATABASE springbootdb;

DROP TABLE IF EXISTS  `city`;
CREATE TABLE `city` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '城市编号',
  `province_id` int(10) unsigned  NOT NULL COMMENT '省份编号',
  `city_name` varchar(25) DEFAULT NULL COMMENT '城市名称',
  `description` varchar(25) DEFAULT NULL COMMENT '描述',
  `is_delete`  tinyint DEFAULT '0' COMMENT '0: enabled, 1: deleted' ,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNI_IDX_ID` (`province_id`,`city_name`) USING BTREE ,
  INDEX `IDX_USER_ID` (`province_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

# insert into city(province_id,city_name,description,is_delete) value(1,'温岭市','BYSocket 的家在温岭。',0);
# insert into city(province_id,city_name,description,is_delete) value(2,'株洲市','我就是随便测试下',0);
# insert into city(province_id,city_name,description,is_delete) value(2,'长沙市','没啥要说的',0);

