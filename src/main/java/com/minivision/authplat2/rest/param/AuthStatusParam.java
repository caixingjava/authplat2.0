package com.minivision.authplat2.rest.param;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 授权状态接口入参
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
@ToString
public class AuthStatusParam extends RestParam {
	
	private static final long serialVersionUID = 2672842521838036489L;

	@NotBlank(message = "设备信息文件编码不能为空")
	@ApiModelProperty(value = "设备信息文件唯一编码", required = true)
	private String authNum;
	
	@NotBlank(message = "授权代码不能为空")
    @ApiModelProperty(value = "授权代码", required = true)
    private String authCode;
	
	@ApiModelProperty(value = "授权状态：1-成功，2-失败")
	private int authStatus;
	
}
