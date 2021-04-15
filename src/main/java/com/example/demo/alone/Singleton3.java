package com.example.demo.alone;

/**
 * 双写dcl 
 * 
 * @author Administrator
 *
 */
public class Singleton3 {
	
	private Singleton3() {}
	
//	volatile的语义会禁止指令重排，而本质上就是加上了内存屏障。
	private volatile static Singleton3 instance;
	
	public static   Singleton3 getInstance() {
		if (instance == null) {
			synchronized (Singleton3.class) {
				if (instance == null) {
					instance = new Singleton3();	
				}
			}
		}
		return instance;
	}

}
