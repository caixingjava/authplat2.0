package com.minivision.authplat2.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用于需要记录方法调用时间的类或方法上
 * @author hughzhao
 * @2017年5月22日
 */
@Documented
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface TimeUsed {

}
