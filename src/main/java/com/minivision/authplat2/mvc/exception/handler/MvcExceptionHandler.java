package com.minivision.authplat2.mvc.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.minivision.authplat2.enumeration.Status;
import com.minivision.authplat2.rest.result.RestResult;

/**
 * MVC请求全局异常处理器
 * @author hughzhao
 * @2017年5月22日
 */
@RestControllerAdvice
public class MvcExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MvcExceptionHandler.class);

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public Object handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
		LOGGER.error("系统异常", ex);
		String url = request.getRequestURL().toString();
		//开放给外部调用的Rest接口
		if (url.contains("/api")) {
			return new RestResult<>(Status.FAIL.getCode(), Status.FAIL.getDescription());
		}
		if (ex != null && StringUtils.isNotBlank(ex.getMessage())) {
			return ex.getMessage();
		}
		return "failed";
	}
	
}
