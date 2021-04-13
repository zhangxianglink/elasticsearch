package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.service.DemoService;

@Service
public class CdemoServiceimpl implements DemoService {

	@Override
	public String key() {
		return "C";
	}

	@Override
	public String hello() {
		return "C hello .....";
	}

}
