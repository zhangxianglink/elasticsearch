package com.example.demo.warpper;

public class QQMessage extends MessageBuss{

	public QQMessage(Notifer notifer) {
		super(notifer);
	}

	@Override
	public void sendMsg(String args) {
		super.sendMsg(args + " {QQ}");
		System.out.println("QQ发送");
	}

}
