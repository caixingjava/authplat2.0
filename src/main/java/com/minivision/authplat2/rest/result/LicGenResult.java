package com.minivision.authplat2.rest.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 生成license文件接口返回结果
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
@ToString
public class LicGenResult {

	private String name;
	private String md5;
	private int errCode;
	
}
