CREATE DATABASE IF NOT EXISTS `authplat2` DEFAULT CHARACTER SET utf8;
USE `authplat2`;

CREATE TABLE IF NOT EXISTS `company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `full_name` varchar(255) NOT NULL COMMENT '客户公司全称，唯一',
  `identifier` varchar(255) NOT NULL COMMENT '客户简称',
  `ip` varchar(1000) DEFAULT NULL COMMENT '客户标识',
  `short_name` varchar(255) NOT NULL COMMENT '客户IP，用作IP白名单控制',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9yd0wx6lvs4076ua6rg8nqk07` (`full_name`),
  UNIQUE KEY `identifier` (`identifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息表';

CREATE TABLE IF NOT EXISTS `auth_file_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `auth_code` varchar(50) NOT NULL COMMENT '授权license文件存储路径',
  `auth_days` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '授权license文件MD5值',
  `auth_file_path` varchar(255) DEFAULT NULL COMMENT '设备信息文件唯一编码',
  `auth_func` tinyint(4) NOT NULL COMMENT '授权状态：1-成功，2-失败',
  `auth_num` varchar(255) NOT NULL COMMENT '授权天数',
  `auth_plat` tinyint(4) NOT NULL COMMENT '授权平台：1-Windows，2-Android',
  `auth_status` tinyint(4) DEFAULT NULL COMMENT '授权功能：1-1:1，2-1:N，3-活体，4-1:1和1:N，5-1:1和活体，6-1:N和活体，7-1:1、1:N和活体',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权代码',
  `md5` varchar(255) DEFAULT NULL COMMENT '记录创建时间',
  `success_time` datetime DEFAULT NULL COMMENT '授权成功时间',
  `company_id` bigint(20) NOT NULL COMMENT '关联company表，授权客户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `auth_code_auth_num` (`auth_code`,`auth_num`),
  KEY `FK3mei1xvnem6nwnpmklkhmhxb6` (`company_id`),
  CONSTRAINT `FK3mei1xvnem6nwnpmklkhmhxb6` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权文件信息表';

CREATE TABLE IF NOT EXISTS `operator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `password` varchar(255) NOT NULL COMMENT '密码（密文）',
  `role` tinyint(4) NOT NULL COMMENT '角色：1-超级管理员，2-管理员，3-用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  `username` varchar(255) NOT NULL COMMENT '用户名，唯一',
  `company_id` bigint(20) DEFAULT NULL COMMENT '用户角色需关联客户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b6s10egswjwti1ymxj4iut2cf` (`username`),
  KEY `FKo6ur8xhyxppe8i1tncaf3ag27` (`company_id`),
  CONSTRAINT `FKo6ur8xhyxppe8i1tncaf3ag27` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理平台用户表';

CREATE TABLE IF NOT EXISTS `op_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `ip` varchar(255) DEFAULT NULL COMMENT '操作人员所在机器IP',
  `op_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operation` varchar(255) NOT NULL COMMENT '操作名称',
  `request` varchar(1000) DEFAULT NULL COMMENT '请求参数',
  `request_id` varchar(255) DEFAULT NULL COMMENT '请求ID',
  `response` varchar(1000) DEFAULT NULL COMMENT '响应数据',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人员ID，关联operator表',
  PRIMARY KEY (`id`),
  KEY `FKbhycubda8ajsalcl4pbymxgvj` (`operator_id`),
  CONSTRAINT `FKbhycubda8ajsalcl4pbymxgvj` FOREIGN KEY (`operator_id`) REFERENCES `operator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志表';

CREATE TABLE IF NOT EXISTS `payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `auth_amount` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '充值授权条数',
  `auth_code` varchar(50) NOT NULL COMMENT '授权代码',
  `auth_days` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '授权天数',
  `auth_fee` double unsigned NOT NULL DEFAULT '0' COMMENT '充值金额',
  `auth_func` tinyint(4) NOT NULL COMMENT '授权功能：1-1:1，2-1:N，3-活体，4-1:1和1:N，5-1:1和活体，6-1:N和活体，7-1:1、1:N和活体',
  `auth_plat` tinyint(4) NOT NULL COMMENT '授权平台：1-Windows，2-Android',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `price` double unsigned NOT NULL DEFAULT '0' COMMENT '充值每条单价',
  `company_id` bigint(20) NOT NULL COMMENT '客户ID，关联company表',
  PRIMARY KEY (`id`),
  KEY `FKahr1jwmhpvh514p2j0vdg60cw` (`company_id`),
  CONSTRAINT `FKahr1jwmhpvh514p2j0vdg60cw` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='充值记录表';

CREATE TABLE IF NOT EXISTS `payment_sum` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `auth_code` varchar(50) NOT NULL COMMENT '授权代码',
  `auth_days` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '授权天数',
  `auth_func` tinyint(4) NOT NULL COMMENT '授权功能：1-1:1，2-1:N，3-活体，4-1:1和1:N，5-1:1和活体，6-1:N和活体，7-1:1、1:N和活体',
  `auth_plat` tinyint(4) NOT NULL COMMENT '授权平台：1-Windows，2-Android',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `remain_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '剩余授权条数',
  `total_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '授权总条数',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  `company_id` bigint(20) NOT NULL COMMENT '客户ID，关联company表',
  PRIMARY KEY (`id`),
  UNIQUE KEY `auth_code_company_id` (`auth_code`,`company_id`),
  KEY `FK7jnx8wqg4a1jeut9hsdm2fskw` (`company_id`),
  CONSTRAINT `FK7jnx8wqg4a1jeut9hsdm2fskw` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权条数表';
