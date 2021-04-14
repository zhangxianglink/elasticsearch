package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTemplateTest {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	public void getkey() {
		 redisTemplate.opsForValue().set("hello","world");
		String object = (String)  redisTemplate.opsForValue().get("name");
		System.err.println(object);
		  Object foo = redisTemplate.opsForValue().get("hello");
	        System.out.println(foo);
	}

}
