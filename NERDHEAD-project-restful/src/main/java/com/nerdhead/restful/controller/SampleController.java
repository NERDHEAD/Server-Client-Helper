package com.nerdhead.restful.controller;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nerdhead.restful.dto.TestDto;
import com.nerdhead.restful.model.service.Client_SampleService;
import com.nerdhead.restful.model.service.Server_SampleService;
import com.nerdhead.restful.util.HttpClientHelper;
import com.nerdhead.restful.util.JacksonHelper;
import com.nerdhead.restful.util.HttpClientHelper.HttpServiceHelper;
import com.nerdhead.restful.util.HttpServerHelper;




@Controller
public class SampleController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Client_SampleService cService;
	@Autowired
	HttpServerHelper<?> sHelper;
	
	
	@RequestMapping(value = "index.do")
	public String index() {
		return "index";
	}
	

	//Client Request Sample
	@ResponseBody
	@RequestMapping(value = "testClient/test_String.do",produces="application/json;charset=UTF-8")
	public String test_String(){
		String result = cService.test_String("테스트!!");
		System.out.println("@@@@@@@@@@ 결과  :  " + result);
		return result;
	}
	
	//Server response Sample
	@ResponseBody
	@RequestMapping(value = "test/{method}")
	public JSONObject test_Server(
			@PathVariable("method") String method,
			@RequestBody Map<String, Object> map){
		System.out.println("method : "+method);
		if(map==null) {
			System.out.println("일반 요청입니다.(map = null)");
		}
		
		
		try {
			return sHelper.method(method).data(map);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}

















