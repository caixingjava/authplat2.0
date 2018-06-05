package com.minivision.authplat2.enumeration;

import lombok.Getter;

/**
 * 操作人员账户角色
 * @author hughzhao
 * @2017年5月22日
 */
@Getter
public enum Role {
	
	ROLE_ROOT(1, "超级管理员"),
	ROLE_ADMIN(2, "管理员"),
	ROLE_USER(3, "用户");
	
	private int index;
	private String description;
	
	private Role(int index, String description) {
		this.index = index;
		this.description = description;
	}
	
	public static String getNameByIndex(int index) {
		for(Role role : Role.values()) {
			if (role.getIndex() == index) {
				return role.name();
			}
		}
		
		return "UNKNOWN";
	}
	
	@Override
	public String toString() {
		return this.index + "_" + this.description;
	}
	
}
