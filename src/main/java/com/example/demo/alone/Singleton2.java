package com.example.demo.alone;

import java.io.Serializable;

/**
 * 减少内存，多线程情况下开销大
 * @author Administrator
 *
 */
public class Singleton2 implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3929313448399440138L;

	private Singleton2() {}
	
	private static Singleton2 instance;
	
	public static synchronized  Singleton2 getInstanc() {
		if (instance == null) {
			instance = new Singleton2();	
		}
		return instance;
	}

	// 防止序列化,干扰单例
//	private Object readResolve() {
//		return instance;
//	}
}
