package com.example.demo.alone;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

public class main {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		Singleton1 instanc1 = Singleton1.getInstanc();
		Singleton1 instanc2 = Singleton1.getInstanc();
		System.out.println(instanc1 == instanc2);
		

		Singleton2 instanc3 = Singleton2.getInstanc();
		Singleton2 instanc4 = Singleton2.getInstanc();
		System.out.println(instanc3 == instanc4);
		

		Singleton3 instanc5 = Singleton3.getInstance();
		Singleton3 instanc6 = Singleton3.getInstance();
		System.out.println(instanc5 == instanc6);
		

		Singleton instanc7 = Singleton.getInstance();
		Singleton instanc8 = Singleton.getInstance();
		System.out.println(instanc7 == instanc8);
		
		
		// 反射破坏单例
		Singleton instance = Singleton.getInstance();
		Constructor<Singleton> declaredConstructor = Singleton.class.getDeclaredConstructor();
		declaredConstructor.setAccessible(true);
		Singleton newInstance = declaredConstructor.newInstance();
		System.out.println(newInstance == instance);
		
		// 序列化破坏单例
		Singleton2 instanc = Singleton2.getInstanc();
		ObjectOutputStream ops = new ObjectOutputStream(new FileOutputStream("aaa"));
		ops.writeObject(instanc);
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("aaa"));
		Singleton2 readObject = (Singleton2) ois.readObject();
		
		System.out.println(instanc == readObject);
	}

}
