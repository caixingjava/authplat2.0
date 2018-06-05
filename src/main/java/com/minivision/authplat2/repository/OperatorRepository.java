package com.minivision.authplat2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minivision.authplat2.domain.Operator;

/**
 * 操作人员管理Dao
 * @author hughzhao
 * @2017年5月22日
 */
public interface OperatorRepository extends JpaRepository<Operator, Long> {

	List<Operator> findAll();
	
	Operator findByUsername(String username);
	
}
