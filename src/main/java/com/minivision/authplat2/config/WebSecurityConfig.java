package com.minivision.authplat2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.minivision.authplat2.security.DBUserDetailService;

/**
 * Spring Security配置
 * @author hughzhao
 * @2017年5月22日
 */
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DBUserDetailService userDetailService;
	
	@Autowired
	private AuthConfig authConfig;
	
	/**
	 * 使用数据库存储账号信息
	 */
	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailService;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	    authenticationProvider.setUserDetailsService(userDetailsService());
	    authenticationProvider.setPasswordEncoder(passwordEncoder());
	    return authenticationProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.rememberMe().key("authplat")
		.and().authorizeRequests()
		//静态资源过滤
		.antMatchers(authConfig.getSkipUrls().split(",")).permitAll()
		.antMatchers("/user").fullyAuthenticated()
		//API接口文档是否需要单独的权限
		//.antMatchers("/swagger-ui.html").hasRole("ADMIN")
		.and().authorizeRequests()
		.anyRequest().hasRole("USER")
		.and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
		.and().logout().permitAll();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService());
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String pass = encoder.encode("cx");
		System.out.println(pass);
	}

}
