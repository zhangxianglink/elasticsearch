package com.example.demo.result;

public enum ResultCode {
	
	SUCCESS(200,"success"),
	ERROR(500,"Service Error"),
	PASS_SEARCH_ERROR(600,"自定义异常"),
	UNKNOW_ERROR(1000,"未知错误"),
    PARAMETER_ERROR(2000,"参数错误"),
    TOKEN_EXPIRE(3000,"认证过期"),
    REQUEST_TIMEOUT(4000,"请求超时"),
    SIGN_ERROR(5000,"签名错误"),
    REPEAT_SUBMIT(60000,"请不要频繁操作"),
	    ;
	
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
