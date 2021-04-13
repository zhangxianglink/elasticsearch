package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.result.ResultResponseInterceptor;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ResultResponseInterceptor()).addPathPatterns("/**");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor
		//https://blog.csdn.net/qq_26472621/article/details/102684266?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_title-1&spm=1001.2101.3001.4242
		// add方法可以指定顺序，有多个自定义的WebMvcConfigurerAdapter时，可以改变相互之间的顺序
		// 但是都在springmvc内置的converter前面
		//org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
//		converters.add(0, new MappingJackson2HttpMessageConverter());  
		ArrayList<HttpMessageConverter<?>> objects = new ArrayList<>();
		for (HttpMessageConverter<?> converter : converters) {
			if (converter.getClass().isAssignableFrom(StringHttpMessageConverter.class)) {
				objects.add(converter);
			}
		}
		converters.removeAll(objects);

	}
}
