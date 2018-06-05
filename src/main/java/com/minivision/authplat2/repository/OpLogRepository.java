package com.minivision.authplat2.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.minivision.authplat2.domain.OpLog;

/**
 * 操作日志管理Dao
 * @author hughzhao
 * @2017年5月22日
 */
public interface OpLogRepository extends JpaRepository<OpLog, Long> {

	List<OpLog> findAll();
	
	List<OpLog> findByOperatorIdOrderByOpTimeDesc(Long operatorId);
	
	Page<OpLog> findByOperatorIdOrderByOpTimeDesc(Long operatorId, Pageable pageable);
	
	Page<OpLog> findByOperatorIdAndOpTimeBetweenOrderByOpTimeDesc(Long operatorId, Date startTime, Date endTime, Pageable pageable);
	
}
