package com.minivision.authplat2.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minivision.authplat2.domain.OpLog;
import com.minivision.authplat2.repository.OpLogRepository;
import com.minivision.authplat2.rest.param.OpLogParam;
import com.minivision.authplat2.util.ChunkRequest;

@Service
@Transactional(rollbackFor={Exception.class})
public class OpLogServiceImpl implements OpLogService {
	
	@Autowired
	private OpLogRepository opLogRepo;

	@Override
	public List<OpLog> findAll() {
		return opLogRepo.findAll();
	}

	@Override
	public OpLog update(OpLog opLog) {
		return opLogRepo.save(opLog);
	}

	@Override
	public OpLog create(OpLog opLog) {
		return opLogRepo.save(opLog);
	}

	@Override
	public void delete(Long id) {
		opLogRepo.delete(id);
	}

	@Override
	public Page<OpLog> findOpLogs(Pageable pageable) {
		return opLogRepo.findAll(pageable);
	}

	@Override
	public OpLog findById(long id) {
		return opLogRepo.findOne(id);
	}

	@Override
	public List<OpLog> findByOperatorId(Long operatorId) {
		return opLogRepo.findByOperatorIdOrderByOpTimeDesc(operatorId);
	}

	@Override
	public Page<OpLog> findByOperatorId(OpLogParam param) {
		return opLogRepo.findByOperatorIdOrderByOpTimeDesc(param.getOperatorId(), new ChunkRequest(param.getOffset(), param.getLimit()));
	}

	@Override
	public Page<OpLog> findByOperatorIdAndOpTimeBetween(OpLogParam param) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse(param.getStartTime());
		Date endDate = sdf.parse(param.getEndTime());
		return opLogRepo.findByOperatorIdAndOpTimeBetweenOrderByOpTimeDesc(param.getOperatorId(), startDate, endDate, new ChunkRequest(param.getOffset(), param.getLimit()));
	}

}
