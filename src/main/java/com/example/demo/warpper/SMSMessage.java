package com.example.demo.warpper;

public class SMSMessage implements Notifer{

	@Override
	public void sendMsg(String args) {
		System.out.println("第一步邮件。" + args);
	}

}
