package com.minivision.authplat2.rest.param;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取license文件接口入参
 * @author hughzhao
 * @2017年5月22日
 */
@Setter
@Getter
public class AuthFileParam extends RestParam {

	private static final long serialVersionUID = -8651309859072244151L;

	@NotNull(message = "设备信息文件不能为空")
	@ApiModelProperty(value = "设备信息文件", required = true)
	private MultipartFile authfile;
	
	/*@NotBlank(message = "设备信息文件编码不能为空")
	@ApiModelProperty(value = "设备信息文件唯一编码", required = true)
	private String authNum;*/ 
	
	@NotBlank(message = "授权代码不能为空")
	@ApiModelProperty(value = "授权代码", required = true)
	private String authCode;
	
	@ApiModelProperty(value = "设备信息文件MD5值", required = true)
	@NotBlank(message = "设备信息文件MD5值不能为空")
	private String md5;
	
	@ApiModelProperty(value = "客戶公司全称", required = true)
	@NotBlank(message = "客戶公司全称不能为空")
	private String companyName;
	
	@ApiModelProperty(value = "客戶公司标识", required = true)
	@NotBlank(message = "客戶公司标识不能为空")
	private String identifier;

	/*@Override
	public String toString() {
		return "AuthFileParam [authfile=" + (authfile == null ? "null" : authfile.getOriginalFilename()) + ", authNum=" + authNum
				+ ", authCode=" + authCode + ", md5=" + md5 + ", companyName=" + companyName + ", identifier=" + identifier + "]";
	}*/
	
	@Override
    public String toString() {
        return "AuthFileParam [authfile=" + (authfile == null ? "null" : authfile.getOriginalFilename())
                + ", authCode=" + authCode + ", md5=" + md5 + ", companyName=" + companyName + ", identifier=" + identifier + "]";
    }
	
}
