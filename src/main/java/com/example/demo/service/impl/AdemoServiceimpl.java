package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.service.DemoService;

@Service
public class AdemoServiceimpl extends DemoService {

	public String key() {
		return "A";
	}

	public String hello() {
		return "A hello .....";
	}
	
	@Override
	public String other() {
		return "AdemoServiceimpl";
	}

}
