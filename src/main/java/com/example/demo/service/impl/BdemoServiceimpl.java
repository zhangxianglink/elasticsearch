package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.service.DemoService;

@Service
public class BdemoServiceimpl extends DemoService {

	public String key() {
		return "B";
	}

	public String hello() {
		return "B hello .....";
	}
	
	@Override
	public String other() {
		return "BdemoServiceimpl";
	}

}
