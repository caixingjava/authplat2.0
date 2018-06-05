package com.minivision.authplat2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minivision.authplat2.domain.Payment;
import com.minivision.authplat2.domain.PaymentSum;
import com.minivision.authplat2.repository.PaymentRepository;
import com.minivision.authplat2.repository.PaymentSumRepository;
import com.minivision.authplat2.rest.param.PaymentDeleteParam;
import com.minivision.authplat2.rest.param.PaymentParam;
import com.minivision.authplat2.util.ChunkRequest;

@Service
@Transactional(rollbackFor={Exception.class})
public class PaymentServiceImpl implements PaymentService {

  @Autowired
  private PaymentRepository paymentRepo;

  @Autowired
  private PaymentSumRepository paymentSumRepo;

  @Override
  public List<Payment> findAll() {
    return paymentRepo.findAll();
  }

  @Override
  public Payment update(Payment payment) {
    return paymentRepo.save(payment);
  }

  //涉及事务处理
  @Override
  public Payment create(Payment payment) {
    Payment newPayment = paymentRepo.save(payment);
    PaymentSum paymentSum = paymentSumRepo.findByCompanyIdAndAuthCode(payment.getCompany().getId(), payment.getAuthCode());
    if (paymentSum == null) {
      PaymentSum sum = new PaymentSum();
      sum.setCompany(payment.getCompany());
      sum.setAuthDays(payment.getAuthDays());
      sum.setAuthPlat(payment.getAuthPlat());
      sum.setAuthFunc(payment.getAuthFunc());
      sum.setAuthCode(payment.getAuthCode());
      sum.setTotalCount(payment.getAuthAmount());
      sum.setRemainCount(payment.getAuthAmount());
      sum.setCreateTime(payment.getCreateTime());
      sum.setUpdateTime(payment.getCreateTime());
      paymentSumRepo.save(sum);
    } else {
      paymentSumRepo.update(newPayment.getAuthAmount(), newPayment.getCompany().getId(), newPayment.getAuthCode());
    }
    return newPayment;
  }

  //涉及事务处理
  @Override
  public void delete(PaymentDeleteParam param) {
    paymentRepo.delete(param.getId());
    paymentRepo.flush();
    paymentSumRepo.update(0 - param.getAuthAmount(), param.getCompanyId(), param.getAuthCode());
    paymentSumRepo.flush();
  }

  @Override
  public Page<Payment> findPayments(Pageable pageable) {
    return paymentRepo.findAll(pageable);
  }

  @Override
  public Payment findById(long id) {
    return paymentRepo.findOne(id);
  }

  @Override
  public List<Payment> findByCompanyId(Long companyId) {
    return paymentRepo.findByCompanyIdOrderByCreateTimeDesc(companyId);
  }

  @Override
  public Page<Payment> findPaymentsByCompanyId(PaymentParam param) {
    return paymentRepo.findByCompanyIdOrderByCreateTimeDesc(param.getCompanyId(), new ChunkRequest(param.getOffset(), param.getLimit()));
  }

}
