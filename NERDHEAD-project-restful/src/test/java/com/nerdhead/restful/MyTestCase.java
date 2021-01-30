package com.nerdhead.restful;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nerdhead.restful.model.service.ServerService;
import com.nerdhead.restful.util.InvokeHelper;

public class MyTestCase {
	
	@Autowired ServerService serverService = new ServerService();
	
	@Test
	public void test() throws Exception {
		String str=null;
		
		InvokeHelper iHelper = new InvokeHelper();
		String result = (String)iHelper.clazz(serverService)
			.method("test_String")
			.parameter(str)
			.run();
	}

}
