package com.minivision.authplat2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minivision.authplat2.domain.Company;

/**
 * 客户管理Dao
 * @author hughzhao
 * @2017年5月22日
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {
	
	List<Company> findAll();
	
	Company findByFullName(String fullName);
	
}
