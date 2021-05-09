package com.example.demo.warpper;

public class WXMessage extends MessageBuss{

	public WXMessage(Notifer notifer) {
		super(notifer);
	}

	@Override
	public void sendMsg(String args) {
		super.sendMsg(args + " [WX]");
		System.out.println("WX 发送");
	}

}
