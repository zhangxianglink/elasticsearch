package com.example.demo.result;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class ResultResponseHandler implements ResponseBodyAdvice<Object>{
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	//判断是否要执行beforeBodyWrite方法，true为执行，false不执行
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		ServletRequestAttributes sRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = sRequestAttributes.getRequest();
		ResultAnnotation attribute = (ResultAnnotation) request.getAttribute("BASE_RESULT");
		return attribute != null ? true : false;
	}

	@Override
	//对response处理的执行方法
	@ResponseBody
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		/* 方式一，序列化为字符串
		 * Object result = body; try { result = objectMapper.writeValueAsString(new
		 * ResultResponse(ResultCode.SUCCESS, body)); } catch (JsonProcessingException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		 return new ResultResponse(ResultCode.SUCCESS, body);
	}
	
	    @ExceptionHandler(value = BaseException.class)
	    @ResponseBody
	    public ResultResponse handleBaseException(BaseException e) {
	        return new ResultResponse(ResultCode.ERROR, e.getMessage());
	    }

}
