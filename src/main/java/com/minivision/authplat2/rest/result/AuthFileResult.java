package com.minivision.authplat2.rest.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 获取license文件接口响应
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
public class AuthFileResult {
	
	private String licFileBase64Str;
	
	private String md5;
	
	private String authNum;
	
	private int remainCount;

	@Override
	public String toString() {
		return "AuthFileResult [md5=" + md5 + ", authNum=" + authNum + ", remainCount=" + remainCount + "]";
	}
	
}
