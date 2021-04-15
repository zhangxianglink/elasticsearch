package com.example.demo.config;

import com.example.demo.model.sys.interceptor.TokenInterceptor;
import com.example.demo.result.ResultResponseInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

	private static final String[] excludePathPatterns  = {"/api/token/api_token","/api/token/sign"};
	
	@Autowired
	private TokenInterceptor tokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResultResponseInterceptor()).addPathPatterns("/demo/**");
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/api/**")
        .excludePathPatterns(excludePathPatterns);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
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
