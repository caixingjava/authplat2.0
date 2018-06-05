package com.minivision.authplat2.constants;

/**
 * 公共常量类
 * @author hughzhao
 * @2017年5月24日
 */
public class CommonConstants {

	public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	//操作日志字段长度上限
	public static final int OPLOG_DATA_LIMIT = 1000;
	
	//剩余授权条数警告阈值
	public static final int AUTH_AMOUNT_WARN_LIMIT = 10;
	
	//正式版永久授权天数为100年
	public static final int AUTH_DAYS_FOREVER = 36500;
	
	//是否测试用
	public static final String ENV_TEST = "test";
	
	//动态IP
	public static final String DYNAMIC_IP = "0.0.0.0";
	
}
