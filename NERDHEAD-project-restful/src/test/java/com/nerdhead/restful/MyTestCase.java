package com.nerdhead.restful;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.json.simple.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdhead.restful.model.service.Server_SampleService;
import com.nerdhead.restful.util.InvokeHelper;

public class MyTestCase {
	
	@Autowired Server_SampleService serverService = new Server_SampleService();
	
	@Test
	@Ignore
	public void test() throws Exception {
		String str=null;
		
		InvokeHelper iHelper = new InvokeHelper();
		String result = (String)iHelper.clazz(serverService)
			.method("test_String")
			.parameter(str)
			.run();
	}
	
	@Test
	@Ignore
	public void test2() {
		for (Method m : Server_SampleService.class.getDeclaredMethods()) {
			System.out.print(m.getName());
			System.out.print("("+m.getParameterCount()+")");
			System.out.println(": ");
			for (Parameter p : m.getParameters()) {
				System.out.print("\t"+p.getName());
				System.out.println(": "+p.getType().getName());
			}
		}
	}
	
	
	@Test
	public void test3() {
		
		
		
	}
	
	/*
	 * @Test public void testTest_String() { Method m =
	 * ServerService.class.getDeclaredMethod(name, parameterTypes); }
	 * 
	 * @Test public void testTest000() { Method m =
	 * ServerService.class.getDeclaredMethod(name, parameterTypes); }
	 * 
	 * @Test public void testTest001() { Method m =
	 * ServerService.class.getDeclaredMethod(name, parameterTypes); }
	 * 
	 * @Test public void testTest002() { Method m =
	 * ServerService.class.getDeclaredMethod(name, parameterTypes); }
	 */

}
