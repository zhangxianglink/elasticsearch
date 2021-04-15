package com.example.demo.alone;

/**
 * 安全可靠，但是浪費内存
 * @author Administrator
 *
 */
public class Singleton1 {
	
	private Singleton1() {}
	
	private static Singleton1 instance = new Singleton1();
	
	public static Singleton1 getInstanc() {
		return Singleton1.instance;
	}

}
