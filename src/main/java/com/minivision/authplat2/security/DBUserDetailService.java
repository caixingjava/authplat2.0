package com.minivision.authplat2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.minivision.authplat2.domain.Operator;
import com.minivision.authplat2.repository.OperatorRepository;

/**
 * 自定义账户信息处理Service，使用数据库存储
 * @author hughzhao
 * @2017年5月22日
 */
@Service
public class DBUserDetailService implements UserDetailsService {
	
	@Autowired
	private OperatorRepository operatorRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Operator operator = operatorRepo.findByUsername(username);
		if (operator == null) {
			throw new UsernameNotFoundException("用户" + username + "不存在！");
		}
		return operator;
	}

}
