package com.nerdhead.restful.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nerdhead.restful.util.JacksonHelper;



@Controller
public class SampleController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final JacksonHelper jHelper = new JacksonHelper();
	private final String R = "redirect:";
	
	@RequestMapping(value = "index.do")
	public String index(Integer i) {
		if(i==null) 
			return "index";

		
		
		switch (i) {
		case 1:
			return R+"/test/wrapData.do";
		default:
			return "index";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "test/wrapData.do")
	public Map<String, String> test_wrapData(){
		List<Object> list=new ArrayList<Object>();
		list.add("String 문자열");
		list.add(1234);
		list.add(1234.1234);
		
		try {
			JSONObject map = jHelper.wrapData(list);
			System.out.println(map.toString());
			return map;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
