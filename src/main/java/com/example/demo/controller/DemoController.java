package com.example.demo.controller;

import com.example.demo.annotation.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.DemoConfig;
import com.example.demo.result.ResultAnnotation;

@RestController
@RequestMapping("/demo")
public class DemoController {
	
	@Autowired
	private DemoConfig DemoConfig;

	@UserLog
	@GetMapping("/branch/{key}")
	@ResultAnnotation
	public String branch(@PathVariable("key") String key ) {
		return DemoConfig.getService(key).other();
	}

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@UserLog
	@GetMapping("/redis/{key}")
	@ResultAnnotation
	public Object test(@PathVariable("key") String key ) {
		int max=100,min=1;
		int ran2 = (int) (Math.random()*(max-min)+min);
		redisTemplate.opsForValue().set(key,"nice "+ran2);
		return redisTemplate.opsForValue().get(key);
	}


}
