package com.example.demo.warpper;

public class Test {
	
	public static void main(String[] args) {
		//由于SMS目标对象和BUSS装饰器遵循同一接口， 因此你可用装饰来对对象进行无限次的封装。
		//结果对象将获得所有封装器叠加而来的行为。
		// 下面的代码类似 对文件操作时包装类的叠加
		Notifer message = new WXMessage(new QQMessage( new MessageBuss(new SMSMessage())));
		message.sendMsg("同一份数据");
	}

}
