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
	
	/*
	 * @Override public void postHandle(HttpServletRequest request,
	 * HttpServletResponse response, Object handler, ModelAndView modelAndView)
	 * throws Exception { //在业务处理器处理请求执行完成后，生成视图之前执行。
	 * HandlerInterceptor.super.postHandle(request, response, handler,
	 * modelAndView); }
	 * 
	 * @Override public void afterCompletion(HttpServletRequest request,
	 * HttpServletResponse response, Object handler, Exception ex) throws Exception
	 * { // 在DispatcherServlet完全处理完请求后被调用，可用于清理资源等
	 * HandlerInterceptor.super.afterCompletion(request, response, handler, ex); }
	 */
}
