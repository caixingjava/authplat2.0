package com.minivision.authplat2.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 客户信息实体类
 * @author hughzhao
 * @2017年5月22日
 */
@Entity
@Setter
@Getter
@ToString(callSuper = true)
public class Company extends IdEntity {

	private static final long serialVersionUID = -249144096379408285L;

	//公司简称
	private String shortName;
	//公司全称
	@Column(unique = true)
	private String fullName;
	//公司唯一标识
	@Column(unique = true)
	private String identifier;
	
	//请求IP
	@Column(length = 1000)
	private String ip;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
}
