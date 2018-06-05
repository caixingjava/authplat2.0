package com.minivision.authplat2.service;

import org.springframework.data.domain.Page;

import com.minivision.authplat2.domain.PaymentSum;
import com.minivision.authplat2.rest.param.PaymentParam;

public interface PaymentSumService {

  PaymentSum create(PaymentSum sum);
  
  PaymentSum findByCompanyIdAndAuthCode(Long companyId, String authCode);
  
  Page<PaymentSum> findByCompanyId(PaymentParam param);
  
}
