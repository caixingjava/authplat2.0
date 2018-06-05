package com.minivision.authplat2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.minivision.authplat2.domain.PaymentSum;

public interface PaymentSumRepository extends JpaRepository<PaymentSum, Long> {

  @Modifying(clearAutomatically = true)
  @Query("update PaymentSum p set p.totalCount = p.totalCount + ?1, p.remainCount = p.remainCount + ?1 where p.company.id = ?2 and p.authCode = ?3")
  void update(Integer amount, Long companyId, String authCode);
  
  @Modifying(clearAutomatically = true)
  @Query("update PaymentSum p set p.remainCount = p.remainCount - 1 where p.company.id = ?1 and p.authCode = ?2")
  void updateRemainCount(Long companyId, String authCode);
  
  PaymentSum findByCompanyIdAndAuthCode(Long companyId, String authCode);
  
  Page<PaymentSum> findByCompanyId(Long companyId, Pageable pageable);
  
}
