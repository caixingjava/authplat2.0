CREATE DATABASE IF NOT EXISTS `authplat2` DEFAULT CHARACTER SET utf8;
USE `authplat2`;

CREATE TABLE IF NOT EXISTS `company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '��������ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '��¼����ʱ��',
  `full_name` varchar(255) NOT NULL COMMENT '�ͻ���˾ȫ�ƣ�Ψһ',
  `identifier` varchar(255) NOT NULL COMMENT '�ͻ����',
  `ip` varchar(1000) DEFAULT NULL COMMENT '�ͻ���ʶ',
  `short_name` varchar(255) NOT NULL COMMENT '�ͻ�IP������IP����������',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '��¼����ʱ��',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9yd0wx6lvs4076ua6rg8nqk07` (`full_name`),
  UNIQUE KEY `identifier` (`identifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='�ͻ���Ϣ��';

CREATE TABLE IF NOT EXISTS `auth_file_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '��������ID',
  `auth_code` varchar(50) NOT NULL COMMENT '��Ȩlicense�ļ��洢·��',
  `auth_days` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '��Ȩlicense�ļ�MD5ֵ',
  `auth_file_path` varchar(255) DEFAULT NULL COMMENT '�豸��Ϣ�ļ�Ψһ����',
  `auth_func` tinyint(4) NOT NULL COMMENT '��Ȩ״̬��1-�ɹ���2-ʧ��',
  `auth_num` varchar(255) NOT NULL COMMENT '��Ȩ����',
  `auth_plat` tinyint(4) NOT NULL COMMENT '��Ȩƽ̨��1-Windows��2-Android',
  `auth_status` tinyint(4) DEFAULT NULL COMMENT '��Ȩ���ܣ�1-1:1��2-1:N��3-���壬4-1:1��1:N��5-1:1�ͻ��壬6-1:N�ͻ��壬7-1:1��1:N�ͻ���',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '��Ȩ����',
  `md5` varchar(255) DEFAULT NULL COMMENT '��¼����ʱ��',
  `success_time` datetime DEFAULT NULL COMMENT '��Ȩ�ɹ�ʱ��',
  `company_id` bigint(20) NOT NULL COMMENT '����company����Ȩ�ͻ�ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `auth_code_auth_num` (`auth_code`,`auth_num`),
  KEY `FK3mei1xvnem6nwnpmklkhmhxb6` (`company_id`),
  CONSTRAINT `FK3mei1xvnem6nwnpmklkhmhxb6` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='��Ȩ�ļ���Ϣ��';

CREATE TABLE IF NOT EXISTS `operator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '��������ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '��¼����ʱ��',
  `password` varchar(255) NOT NULL COMMENT '���루���ģ�',
  `role` tinyint(4) NOT NULL COMMENT '��ɫ��1-��������Ա��2-����Ա��3-�û�',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '��¼����ʱ��',
  `username` varchar(255) NOT NULL COMMENT '�û�����Ψһ',
  `company_id` bigint(20) DEFAULT NULL COMMENT '�û���ɫ������ͻ�',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b6s10egswjwti1ymxj4iut2cf` (`username`),
  KEY `FKo6ur8xhyxppe8i1tncaf3ag27` (`company_id`),
  CONSTRAINT `FKo6ur8xhyxppe8i1tncaf3ag27` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='����ƽ̨�û���';

CREATE TABLE IF NOT EXISTS `op_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '��������ID',
  `ip` varchar(255) DEFAULT NULL COMMENT '������Ա���ڻ���IP',
  `op_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `operation` varchar(255) NOT NULL COMMENT '��������',
  `request` varchar(1000) DEFAULT NULL COMMENT '�������',
  `request_id` varchar(255) DEFAULT NULL COMMENT '����ID',
  `response` varchar(1000) DEFAULT NULL COMMENT '��Ӧ����',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '������ԱID������operator��',
  PRIMARY KEY (`id`),
  KEY `FKbhycubda8ajsalcl4pbymxgvj` (`operator_id`),
  CONSTRAINT `FKbhycubda8ajsalcl4pbymxgvj` FOREIGN KEY (`operator_id`) REFERENCES `operator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='������־��';

CREATE TABLE IF NOT EXISTS `payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '��������ID',
  `auth_amount` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '��ֵ��Ȩ����',
  `auth_code` varchar(50) NOT NULL COMMENT '��Ȩ����',
  `auth_days` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '��Ȩ����',
  `auth_fee` double unsigned NOT NULL DEFAULT '0' COMMENT '��ֵ���',
  `auth_func` tinyint(4) NOT NULL COMMENT '��Ȩ���ܣ�1-1:1��2-1:N��3-���壬4-1:1��1:N��5-1:1�ͻ��壬6-1:N�ͻ��壬7-1:1��1:N�ͻ���',
  `auth_plat` tinyint(4) NOT NULL COMMENT '��Ȩƽ̨��1-Windows��2-Android',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '��¼����ʱ��',
  `price` double unsigned NOT NULL DEFAULT '0' COMMENT '��ֵÿ������',
  `company_id` bigint(20) NOT NULL COMMENT '�ͻ�ID������company��',
  PRIMARY KEY (`id`),
  KEY `FKahr1jwmhpvh514p2j0vdg60cw` (`company_id`),
  CONSTRAINT `FKahr1jwmhpvh514p2j0vdg60cw` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='��ֵ��¼��';

CREATE TABLE IF NOT EXISTS `payment_sum` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '��������ID',
  `auth_code` varchar(50) NOT NULL COMMENT '��Ȩ����',
  `auth_days` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '��Ȩ����',
  `auth_func` tinyint(4) NOT NULL COMMENT '��Ȩ���ܣ�1-1:1��2-1:N��3-���壬4-1:1��1:N��5-1:1�ͻ��壬6-1:N�ͻ��壬7-1:1��1:N�ͻ���',
  `auth_plat` tinyint(4) NOT NULL COMMENT '��Ȩƽ̨��1-Windows��2-Android',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '��¼����ʱ��',
  `remain_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'ʣ����Ȩ����',
  `total_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '��Ȩ������',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '��¼����ʱ��',
  `company_id` bigint(20) NOT NULL COMMENT '�ͻ�ID������company��',
  PRIMARY KEY (`id`),
  UNIQUE KEY `auth_code_company_id` (`auth_code`,`company_id`),
  KEY `FK7jnx8wqg4a1jeut9hsdm2fskw` (`company_id`),
  CONSTRAINT `FK7jnx8wqg4a1jeut9hsdm2fskw` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='��Ȩ������';
