package com.example.demo.result;

public class BaseException extends RuntimeException{

	private ResultCode resultCode;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3566943863347135905L;


	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}


}
