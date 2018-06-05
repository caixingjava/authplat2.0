package com.minivision.authplat2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minivision.authplat2.domain.Operator;
import com.minivision.authplat2.repository.OperatorRepository;

@Service
@Transactional(rollbackFor={Exception.class})
public class OperatorServiceImpl implements OperatorService {
	
	@Autowired
	private OperatorRepository operatorRepo;

	@Override
	public List<Operator> findAll() {
		return operatorRepo.findAll();
	}

	@Override
	public Operator update(Operator operator) {
		return operatorRepo.save(operator);
	}

	@Override
	public Operator create(Operator operator) {
		return operatorRepo.save(operator);
	}

	@Override
	public void delete(Long id) {
		operatorRepo.delete(id);
	}

	@Override
	public Page<Operator> findOperators(Pageable pageable) {
		return operatorRepo.findAll(pageable);
	}

	@Override
	public Operator findById(long id) {
		return operatorRepo.findOne(id);
	}

	@Override
	public Operator findByUsername(String username) {
		return operatorRepo.findByUsername(username);
	}

}
