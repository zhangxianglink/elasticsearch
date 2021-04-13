package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping("/branch/{key}")
	@ResultAnnotation
	public String branch(@PathVariable("key") String key ) {
		return DemoConfig.getService(key).hello();
	}

}
