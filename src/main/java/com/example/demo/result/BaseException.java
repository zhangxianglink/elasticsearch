package com.example.demo.result;

public class BaseException extends RuntimeException{

	private ResultCode resultCode;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3566943863347135905L;


	public BaseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BaseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BaseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


}
