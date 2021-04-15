package com.example.demo.alone;

public enum Singleton4 {

	INSTANCE;
	
	private Singleton4() {}
	
	public void doSomething() {
		System.out.println("-----------------");
	}

}
