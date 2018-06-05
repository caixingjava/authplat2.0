package com.minivision.authplat2.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minivision.authplat2.domain.OpLog;
import com.minivision.authplat2.rest.param.OpLogParam;

/**
 * 操作日志管理Service
 * @author hughzhao
 * @2017年5月22日
 */
public interface OpLogService {

	List<OpLog> findAll();

	OpLog update(OpLog opLog);

	OpLog create(OpLog opLog);

	void delete(Long id);

	Page<OpLog> findOpLogs(Pageable pageable);

	OpLog findById(long id);
	
	List<OpLog> findByOperatorId(Long operatorId);
	
	Page<OpLog> findByOperatorId(OpLogParam param);
	
	Page<OpLog> findByOperatorIdAndOpTimeBetween(OpLogParam param) throws ParseException;
	
}
