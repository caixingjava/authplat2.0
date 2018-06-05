package com.minivision.authplat2.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import com.minivision.authplat2.domain.Company;

/**
 * 客户管理Service
 * @author hughzhao
 * @2017年5月22日
 */
public interface CompanyService {

	List<Company> findAll();

	@Secured({ "ROLE_ADMIN", "ROLE_ROOT" })
	Company update(Company company);

	@Secured({ "ROLE_ADMIN", "ROLE_ROOT" })
	Company create(Company company);

	@Secured({ "ROLE_ADMIN", "ROLE_ROOT" })
	void delete(Long id);

	Page<Company> findCompanies(Pageable pageable);

	Company findById(long id);
	
	Company findByFullName(String fullName);

}
