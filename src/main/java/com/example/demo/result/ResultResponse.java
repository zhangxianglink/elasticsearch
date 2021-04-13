package com.example.demo.result;

import java.io.Serializable;

public class ResultResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String msg;
	private Object data;
	
	public ResultResponse(ResultCode resultCode, Object data) {
		this.code = resultCode.getCode();
		this.msg = resultCode.getMsg();
		this.data = data;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	

}
