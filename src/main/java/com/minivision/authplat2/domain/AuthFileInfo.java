package com.minivision.authplat2.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 授权信息实体类
 * @author hughzhao
 * @2017年5月22日
 */
@Entity
@Setter
@Getter
@ToString
public class AuthFileInfo extends IdEntity {

	private static final long serialVersionUID = -7474073095161302668L;

	//所属公司
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;
	
	//授权编号
	private String authNum;
	
	//授权有效期
	private int authDays;
	
	//授权平台
	private int authPlat;
	
	//授权功能
	private int authFunc;
	
	//授权码
	private String authCode;
	
	//授权文件存储路径
	private String authFilePath;
	
	//授权文件MD5
	private String md5;
	
	//授权状态
	private int authStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	//授权成功时间
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date successTime;
	
}
