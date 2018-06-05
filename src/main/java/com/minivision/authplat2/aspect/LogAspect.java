package com.minivision.authplat2.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.minivision.authplat2.annotation.Log;
import com.minivision.authplat2.constants.CommonConstants;
import com.minivision.authplat2.domain.OpLog;
import com.minivision.authplat2.domain.Operator;
import com.minivision.authplat2.enumeration.Status;
import com.minivision.authplat2.rest.result.RestResult;
import com.minivision.authplat2.service.OpLogService;
import com.minivision.authplat2.service.OperatorService;
import com.minivision.authplat2.util.ParamUtils;

/**
 * 用于记录操作日志和方法调用时间的切面
 * @author hughzhao
 * @2017年5月22日
 */
@Aspect
@Component
public class LogAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Autowired
	private OpLogService opLogService;

	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private GaugeService gaugeService;

	/**
	 * 记录操作日志
	 * @param joinPoint
	 * @param log
	 * @param retVal
	 * @throws Throwable
	 */
	@AfterReturning(pointcut = "@annotation(com.minivision.authplat2.annotation.Log) && @annotation(log)", returning="retVal")
	public void doLog(JoinPoint joinPoint, Log log, Object retVal) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		String url = request.getRequestURL().toString();
		/*Map<String, String[]> paramMap = request.getParameterMap();
		if (!log.ignoreArgs() && !CollectionUtils.isEmpty(paramMap)) {
			StringBuilder params = new StringBuilder();
			for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
				String paramName = entry.getKey();
				String[] paramValues = entry.getValue();
				if (paramName.contains("password")) {
					params.append(paramName).append("=").append("******").append(",");
				} else {
					params.append(paramName).append("=").append(Arrays.toString(paramValues)).append(",");
				}
			}
			params.deleteCharAt(params.length() - 1);
			logger.info("{}---->{}", url, params.toString());
		}*/
		String paramString = ParamUtils.getParamString(request);
		if (!log.ignoreArgs()) {
		  logger.info("{}---->{}", url, paramString);
		}
		String ip = request.getRemoteAddr();
		/*Object[] methodArgs = joinPoint.getArgs();
		List<Object> argList = new ArrayList<>();
		if (url.contains("/api")) {
			for (Object arg : methodArgs) {
				if (! (arg instanceof Errors) && ! (arg instanceof ServletRequest)) {
					argList.add(arg);
				}
			}
			methodArgs = argList.toArray();
		}
		String args = Arrays.toString(methodArgs);*/
		// save in DB
		OpLog opLog = new OpLog();
		opLog.setIp(ip);
		opLog.setOperation(log.operation() + "：" + request.getMethod() + " " + url);
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (user instanceof UserDetails) {
			username = ((UserDetails) user).getUsername();
		} else {
			username = (String) user;
		}
		
		try {
			Operator operator = operatorService.findByUsername(username);
			opLog.setOperator(operator);
			opLog.setOpTime(new Date());
			opLog.setRequestId(retVal instanceof RestResult<?> ? ((RestResult<?>) retVal).getRequestId() : "");
			opLog.setRequest(log.ignoreArgs() ? "" : paramString);
			opLog.setResponse(retVal instanceof RestResult<?> ? JSON.toJSONString(retVal) : (retVal instanceof String ? (String) retVal : ""));
			String requestTxt = opLog.getRequest();
			String responseTxt = opLog.getResponse();
			if (requestTxt.length() > CommonConstants.OPLOG_DATA_LIMIT) {
				opLog.setRequest(requestTxt.substring(0, CommonConstants.OPLOG_DATA_LIMIT));
			}
			if (responseTxt.length() > CommonConstants.OPLOG_DATA_LIMIT) {
				opLog.setResponse(responseTxt.substring(0, CommonConstants.OPLOG_DATA_LIMIT));
			}
			opLogService.create(opLog);
		} catch (Throwable e) {
			logger.error("写操作日志发生异常", e);
		}
	}

	/**
	 * 记录方法调用时间
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("within(@com.minivision.authplat2.annotation.TimeUsed *)")
	public Object timeuse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed();
		} catch (Throwable e) {
			logger.error("Rest接口调用异常", e);
			result = new RestResult<>(Status.FAIL.getCode(), Status.FAIL.getDescription());
		}
		
		if(result instanceof RestResult<?>){
			RestResult<?> res = (RestResult<?>) result;
			int consume = (int) (System.currentTimeMillis() - start);
			res.setTimeUsed(consume);
			gaugeService.submit(proceedingJoinPoint.getSignature().toShortString(), consume);
			String name = proceedingJoinPoint.getSignature().getName();
			Object[] args = proceedingJoinPoint.getArgs();
			List<Object> argList = new ArrayList<>();
			for (Object arg : args) {
				if (! (arg instanceof Errors) && ! (arg instanceof ServletRequest)) {
					argList.add(arg);
				}
			}
			args = argList.toArray();
			logger.info("method: {}", name);
			logger.info("args: {}", Arrays.toString(args));
			logger.info("result: {}", res);
		}
		
		return result;
	}

}
