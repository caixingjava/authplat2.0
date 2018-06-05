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
 * 授权使用统计信息实体类
 * @author hughzhao
 * @2018年3月2日
 */
@Entity
@Setter
@Getter
@ToString
public class PaymentSum extends IdEntity {
  
  private static final long serialVersionUID = -4287219471958575527L;

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
  
  //授权总次数
  private int totalCount;
  
  //授权剩余次数
  private int remainCount;
  
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
  
}
