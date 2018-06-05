package com.minivision.authplat2.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

/**
 * API调用监控
 * @author hughzhao
 * @2018年3月2日
 */
@Aspect
@Component
public class ServiceMonitor {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceMonitor.class);

	@Autowired
	private CounterService counterService;
	
	@Before("within(@com.minivision.authplat2.annotation.TimeUsed *)")
	public void countServiceInvoke(JoinPoint joinPoint) {
		logger.info("API invoke:" + joinPoint.getSignature().toLongString());
		counterService.increment(joinPoint.getSignature().toShortString());
	}

}
