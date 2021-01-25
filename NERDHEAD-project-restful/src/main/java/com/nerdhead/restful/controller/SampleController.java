package com.nerdhead.restful.controller;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nerdhead.restful.dto.TestDto;
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
	
	@SuppressWarnings({ "finally", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "test/wrapData.do",produces="application/json;charset=UTF-8")
	public String test_wrapData(){
		StringBuffer sb=new StringBuffer();
		
		String[][] strArray = {{"str"},{"array"}};
		int[][][][] intArray = {{{{0}}}};
		boolean isc = false;
		
		String str1 = "끼루룩";
		String str2 = "우히히";
		String str3 = "메뚜기";
		
		TestDto[] dto1 = {
				new TestDto("1", "류주성", "wntjd337@gmail.com", "010-2827-3059"),
				new TestDto("2", "류주현", "wntjd312@gmail.com", "010-2827-3058"),
				new TestDto("3", "류아랑", "wntjd777@gmail.com", "010-2827-3057")
		};
		
		List<Object> list=new LinkedList<Object>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		list.add(strArray);
		list.add(intArray);
		list.add(isc);
		list.add(str1);
		list.add(str2);
		list.add(str3);
		list.add(dto1);
		
		map.put(list, dto1);
		
		sb.append("\n====Before====\n")
			.append(map.toString());
		
		try {
			JSONObject dataStr = jHelper.wrapData(map);
			sb.append("\n====Data String====\n")
				.append(dataStr.toString());
			
			Map<Object, Object> resultMap = (Map<Object, Object>) jHelper.unwrapData(dataStr);
			
			sb.append("\n====After====\n")
				.append(resultMap.toString())
				.append("\n");
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			System.out.println(sb);
		}
		return sb.toString();
		
	}
	
	@SuppressWarnings({ "finally", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "test/wrapData2.do",produces="application/json;charset=UTF-8")
	public String test_wrapData2(){
		StringBuffer sb=new StringBuffer();
		
		String str1 = "끼루룩";
		String str2 = "우히히";
		
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		map.put(str1, str2);
		
		sb.append("\n====Before====\n")
			.append(map.toString());
		
		try {
			JSONObject dataStr = jHelper.wrapData(map);
			sb.append("\n====Data String====\n")
				.append(dataStr.toString());
			
			Map<Object, Object> resultMap = (Map<Object, Object>) jHelper.unwrapData(dataStr);
			
			sb.append("\n====After====\n")
				.append(resultMap.toString())
				.append("\n");
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			System.out.println(sb);
		}
		
		return sb.toString();
		
	}
	
}

















