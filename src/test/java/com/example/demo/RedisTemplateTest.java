package com.example.demo;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.demo.model.User;
import com.example.demo.model.sys.TokenInfo;

@SpringBootTest
public class RedisTemplateTest {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	public void getkey() {
		 redisTemplate.opsForValue().set("hello","world");
		 Object foo = redisTemplate.opsForValue().get("hello");
	     System.out.println(foo);
	     
		  Object object = redisTemplate.opsForValue().get("820b8435-b4c9-4441-bc4d-80672da209bd");
		System.err.println(object.toString());
		 
	}
	
	@Test
	public void getUser() {
		User user = new User("23", "test", 0, "007");
		redisTemplate.opsForValue().set("user1", user);
		
		User user2 = (User) redisTemplate.opsForValue().get("user1");
		System.out.println(user2.toString());
	}

}
