package com.minivision.authplat2.domain;

import java.util.Date;

import javax.persistence.Column;
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
 * 操作日志信息实体类
 * @author hughzhao
 * @2017年5月22日
 */
@Entity
@Setter
@Getter
@ToString
public class OpLog extends IdEntity {

	private static final long serialVersionUID = 3322209454335712723L;

	//操作账号
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_id")
	private Operator operator;
	
	//请求IP
	private String ip;
	
	//操作内容
	private String operation;
	
	//请求ID
	private String requestId;
	
	//请求参数
	@Column(length = 1000)
	private String request;
	
	//响应数据
	@Column(length = 1000)
	private String response;
	
	//操作时间
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date opTime;
	
}
