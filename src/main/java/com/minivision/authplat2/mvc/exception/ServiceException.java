package com.minivision.authplat2.mvc.exception;

/**
 * 服务层异常
 * @author hughzhao
 * @2017年5月22日
 */
public class ServiceException extends Exception{

	private static final long serialVersionUID = -5786981134077802501L;

	private String msg;

	private Throwable throwable;

	public ServiceException(String msg, Throwable throwable) {
		this.msg = msg;
		this.throwable = throwable;
	}

	public ServiceException(String msg) {
		this.msg = msg;
	}

	public ServiceException(Throwable throwable) {
		this.msg = throwable.getMessage();
		this.throwable = throwable;
	}

	public String getMessage() {
		return msg;
	}


	public void setMessage(String msg) {
		this.msg = msg;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

}
