package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.service.DemoService;

@Service
public class AdemoServiceimpl implements DemoService {

	@Override
	public String key() {
		return "A";
	}

	@Override
	public String hello() {
		return "A hello .....";
	}

}
