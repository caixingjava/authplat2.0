package com.minivision.authplat2.rest.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 授权管理接口入参
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AuthParam extends PageParam {

	private static final long serialVersionUID = -2361006193243313428L;

	/*@ApiParam(required = true)
	@NotNull*/
    private Long companyId;
	
	private Integer authPlat;
    
    private Integer authFunc;
	
	private String startTime;
    private String endTime;
	
}
