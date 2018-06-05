package com.minivision.authplat2.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import com.minivision.authplat2.domain.Operator;

/**
 * 操作人员管理Service
 * @author hughzhao
 * @2017年5月22日
 */
public interface OperatorService {

	List<Operator> findAll();

	//@Secured({ "ROLE_ROOT" })
	Operator update(Operator operator);

	@Secured({ "ROLE_ROOT" })
	Operator create(Operator operator);

	@Secured({ "ROLE_ROOT" })
	void delete(Long id);

	Page<Operator> findOperators(Pageable pageable);

	Operator findById(long id);
	
	Operator findByUsername(String username);
	
}
