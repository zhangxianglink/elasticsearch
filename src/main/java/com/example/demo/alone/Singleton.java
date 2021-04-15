package com.example.demo.alone;

/**
 * 静态内部类,这个通过JVM来保证创建单例对象的线程安全和唯一性
 * @author Administrator
 *
 */
public class Singleton {
	
	private Singleton() {}
	
	private static class SingletonHolder {
		private final static Singleton Instance = new Singleton();
	}
	
	public static Singleton getInstance() {
		return SingletonHolder.Instance;
	}

}
