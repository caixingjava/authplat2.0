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
 * 充值信息实体类
 * @author hughzhao
 * @2017年5月22日
 */
@Entity
@Setter
@Getter
@ToString
public class Payment extends IdEntity {

	private static final long serialVersionUID = 8441691382371981252L;

	//所属公司
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;
	
	//授权有效期
	private int authDays;
	
	//授权平台
	private int authPlat;
	
	//授权功能
	private int authFunc;
	
	//授权码
	private String authCode;
	
	//授权总费用
	private double authFee;
	
	//授权次数
	private int authAmount;
	
	//授权单价
	private double price;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
}
