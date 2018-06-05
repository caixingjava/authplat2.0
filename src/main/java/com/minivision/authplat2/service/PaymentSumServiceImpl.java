package com.minivision.authplat2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minivision.authplat2.domain.PaymentSum;
import com.minivision.authplat2.repository.PaymentSumRepository;
import com.minivision.authplat2.rest.param.PaymentParam;
import com.minivision.authplat2.util.ChunkRequest;

@Service
@Transactional(rollbackFor={Exception.class})
public class PaymentSumServiceImpl implements PaymentSumService {
  
  @Autowired
  private PaymentSumRepository paymentSumRepo;

  @Override
  public PaymentSum create(PaymentSum sum) {
    return paymentSumRepo.save(sum);
  }

  @Override
  public PaymentSum findByCompanyIdAndAuthCode(Long companyId, String authCode) {
    return paymentSumRepo.findByCompanyIdAndAuthCode(companyId, authCode);
  }

  @Override
  public Page<PaymentSum> findByCompanyId(PaymentParam param) {
    return paymentSumRepo.findByCompanyId(param.getCompanyId(), new ChunkRequest(param.getOffset(), param.getLimit()));
  }

}
