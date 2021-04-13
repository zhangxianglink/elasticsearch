package com.example.demo.result;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class ResultResponseInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制、权限校验等处理；
		if (handler instanceof HandlerMethod) {
			final HandlerMethod handlerMethod = (HandlerMethod) handler;
			final Class<?> clazz = handlerMethod.getBeanType();
			final Method method = handlerMethod.getMethod();
			if (clazz.isAnnotationPresent(ResultAnnotation.class)) {
				request.setAttribute("BASE_RESULT", clazz.getAnnotation(ResultAnnotation.class));
			}else if(method.isAnnotationPresent(ResultAnnotation.class)) {
				request.setAttribute("BASE_RESULT", method.getAnnotation(ResultAnnotation.class));
			}
		}
		return true;
	}
	
}
