package com.minivision.authplat2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 授权信息配置
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
@ToString
@Component
@ConfigurationProperties(prefix="auth")
public class AuthConfig {
	
	//生成license算法库路径
	private String libpath;
	
	//设备信息文件临时存储路径
	private String filepath;
	
	//license文件存储路径
    private String licpath;
	
	//不需要进行登录认证的访问路径
	private String skipUrls;

}
