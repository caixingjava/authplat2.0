package com.minivision.authplat2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.minivision.authplat2.domain.Payment;

/**
 * 充值管理Dao
 * @author hughzhao
 * @2017年5月22日
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	List<Payment> findAll();
	
	List<Payment> findByCompanyIdOrderByCreateTimeDesc(Long companyId);
	
	Page<Payment> findByCompanyIdOrderByCreateTimeDesc(Long companyId, Pageable pageable);
	
}
