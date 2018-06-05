package com.minivision.authplat2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minivision.authplat2.domain.Company;
import com.minivision.authplat2.repository.CompanyRepository;

@Service
@Transactional(rollbackFor={Exception.class})
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	@Override
	public Company update(Company company) {
		return companyRepository.save(company);
	}

	@Override
	public Company create(Company company) {
		return companyRepository.save(company);
	}

	@Override
	public void delete(Long id) {
		companyRepository.delete(id);
	}

	@Override
	public Page<Company> findCompanies(Pageable pageable) {
		return companyRepository.findAll(pageable);
	}

	@Override
	public Company findById(long id) {
		return companyRepository.findOne(id);
	}

	@Override
	public Company findByFullName(String fullName) {
		return companyRepository.findByFullName(fullName);
	}

}
