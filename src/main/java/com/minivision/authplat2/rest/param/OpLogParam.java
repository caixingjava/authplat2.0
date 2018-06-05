package com.minivision.authplat2.rest.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 操作日志查询接口入参
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
@ToString(callSuper = true)
public class OpLogParam extends PageParam {

	private static final long serialVersionUID = -2556551150864228151L;

	@ApiParam(required = true)
	@NotNull
    private Long operatorId;
	
	private String startTime;
	private String endTime;
	
}
