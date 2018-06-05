package com.minivision.authplat2.rest.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 充值管理接口入参
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
@ToString(callSuper = true)
public class PaymentParam extends PageParam {

	private static final long serialVersionUID = -3091450352579387905L;

	@ApiParam(required = true)
	@NotNull
    private Long companyId;
	
}
