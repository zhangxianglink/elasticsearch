package com.example.demo.warpper;

public class MessageBuss implements Notifer{
	
	private Notifer notifer;
	
	public  MessageBuss(Notifer notifer) {
		this.notifer = notifer;
	}

	@Override
	public void sendMsg(String args) {
		notifer.sendMsg(args);
	}

}
