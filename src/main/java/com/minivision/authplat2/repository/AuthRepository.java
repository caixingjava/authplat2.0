package com.minivision.authplat2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.minivision.authplat2.domain.AuthFileInfo;

/**
 * 授权管理Dao
 * @author hughzhao
 * @2017年5月22日
 */
public interface AuthRepository extends JpaRepository<AuthFileInfo, Long>, JpaSpecificationExecutor<AuthFileInfo> {

	List<AuthFileInfo> findAll();
	
	List<AuthFileInfo> findByCompanyIdOrderByCreateTimeDesc(Long companyId);
	
	Page<AuthFileInfo> findByCompanyIdOrderByCreateTimeDesc(Long companyId, Pageable page);
	
	List<AuthFileInfo> findByAuthNum(String authNum);
	
	AuthFileInfo findByAuthNumAndAuthCode(String authNum, String authCode);
	
}
