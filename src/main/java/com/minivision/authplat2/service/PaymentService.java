package com.minivision.authplat2.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import com.minivision.authplat2.domain.Payment;
import com.minivision.authplat2.rest.param.PaymentDeleteParam;
import com.minivision.authplat2.rest.param.PaymentParam;

/**
 * 充值管理Service
 * @author hughzhao
 * @2017年5月22日
 */
public interface PaymentService {

	List<Payment> findAll();
	
	List<Payment> findByCompanyId(Long companyId);

	@Secured({ "ROLE_ADMIN", "ROLE_ROOT" })
	Payment update(Payment payment);

	@Secured({ "ROLE_ADMIN", "ROLE_ROOT" })
	Payment create(Payment payment);

	@Secured({ "ROLE_ADMIN", "ROLE_ROOT" })
	void delete(PaymentDeleteParam param);

	Page<Payment> findPayments(Pageable pageable);
	
	Page<Payment> findPaymentsByCompanyId(PaymentParam param);

	Payment findById(long id);
	
}
