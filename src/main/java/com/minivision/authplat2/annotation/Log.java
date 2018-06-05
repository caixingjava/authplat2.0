package com.minivision.authplat2.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用在需要记录操作日志的方法上
 * @author hughzhao
 * @2017年5月22日
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Log {

	//操作名称
	String operation() default "";
	
	//是否记录参数
	boolean ignoreArgs() default false;
	
}
