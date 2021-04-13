package com.example.demo.config;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.example.demo.service.DemoService;

@Component
public class DemoConfig {
	
	private static HashMap<String, DemoService> serviceMap = new HashMap<>();
	
	@Autowired
	private List<DemoService> demoService;
	
	@PostConstruct
	public void init() {
		demoService.forEach(e -> serviceMap.put(e.key(), e));
	}
	
	public DemoService getService(String key) {
		DemoService service = serviceMap.get(key);
		Assert.notNull(service, "查询服务不存在");
		return service;
	}

}
