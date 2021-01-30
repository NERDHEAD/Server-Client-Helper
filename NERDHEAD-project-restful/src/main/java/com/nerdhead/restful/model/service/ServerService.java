package com.nerdhead.restful.model.service;


import org.springframework.stereotype.Service;

@Service
public class ServerService {
	
	public String test_String(String data) {
		return data+"-test-test-test-test-test-test-";
	}
	
	public String test001(String...strings) {
		StringBuffer sb=new StringBuffer();
		
		for (String string : strings) {
			sb.append(string).append("   ");
		}
		
		return sb.toString();
	}
}
