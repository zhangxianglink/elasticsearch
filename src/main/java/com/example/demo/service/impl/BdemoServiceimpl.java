package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.service.DemoService;

@Service
public class BdemoServiceimpl implements DemoService {

	@Override
	public String key() {
		return "B";
	}

	@Override
	public String hello() {
		return "B hello .....";
	}

}
