package com.nerdhead.restful.model.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nerdhead.restful.util.HttpClientHelper;
@Service
public class Client_SampleService {
	@Autowired
	private HttpClientHelper helper;
	
	
	public String test_String(String data) {
		try {
			String[] testData= {"a", "b", "c", "d", "e"};
			String result; 
			System.out.println("가변인자 테스트 : test001");
			result=(String) helper.method("test000").data(testData).request();
			result=(String) helper.method("test001").data(testData, "asdf").request();
			result=(String) helper.method("test002").data("a", "b", "c").request();
			int result2=(int) helper.method("test003").data(1,2,3).request();
			int result3=(int) helper.method("test004").data(new int[]{1,2},3).request();
			
			
			
			return (String) helper.method("test_String").data(data).request();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | IOException
				| ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
