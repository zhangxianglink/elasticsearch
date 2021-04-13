package com.example.demo.result;

public enum ResultCode {
	
	SUCCESS(200,"success"),
	ERROR(500,"Service Error"),
	PASS_SEARCH_ERROR(600,"自定义异常");
	
	ResultCode(Integer code ,String msg) {
		this.code = code;
		this.msg = msg;
	}

	private Integer code;
	public Integer getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}

	private String msg;
	
	
	
}
