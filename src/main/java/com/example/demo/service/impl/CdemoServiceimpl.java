package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.service.DemoService;

@Service
public class CdemoServiceimpl extends DemoService {

	public String key() {
		return "C";
	}

	public String hello() {
		return "C hello .....";
	}

	@Override
	public String other() {
		return super.other();
	}
}
