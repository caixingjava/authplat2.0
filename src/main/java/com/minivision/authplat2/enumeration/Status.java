package com.minivision.authplat2.enumeration;

import lombok.Getter;

/**
 * 授权和接口调用状态
 * @author hughzhao
 * @2017年5月22日
 */
@Getter
public enum Status {

	SUCCESS(1, "成功"),
	FAIL(2, "失败");
	
	private int code;
	private String description;
	
	private Status(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static String getNameByCode(int code) {
		for(Status status : Status.values()) {
			if (status.getCode() == code) {
				return status.name();
			}
		}
		
		return "UNKNOWN";
	}
	
	@Override
	public String toString() {
		return this.code + "_" + this.description;
	}
	
}
