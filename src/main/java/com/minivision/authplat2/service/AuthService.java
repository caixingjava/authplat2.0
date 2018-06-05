package com.minivision.authplat2.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minivision.authplat2.domain.AuthFileInfo;
import com.minivision.authplat2.rest.param.AuthParam;

/**
 * 授权Service
 * @author hughzhao
 * @2017年5月22日
 */
public interface AuthService {

	List<AuthFileInfo> findAll();
	
	Page<AuthFileInfo> findAll(AuthParam param);

	AuthFileInfo update(AuthFileInfo info);

	AuthFileInfo create(AuthFileInfo info);

	Page<AuthFileInfo> findAuthFileInfos(Pageable pageable);

	AuthFileInfo findById(long id);
	
	List<AuthFileInfo> findByCompanyId(Long companyId);
	
	Page<AuthFileInfo> findByCompanyId(AuthParam param);
	
	List<AuthFileInfo> findByAuthNum(String authNum);
	
	AuthFileInfo findByAuthNumAndAuthCode(String authNum, String authCode);
	
	void delete(Long id);
	
}
