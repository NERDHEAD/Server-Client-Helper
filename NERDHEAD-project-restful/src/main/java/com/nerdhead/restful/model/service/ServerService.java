package com.nerdhead.restful.model.service;

import org.springframework.stereotype.Service;

@Service
public class ServerService {

	public String test_String(String data) {
		return data + "-test-test-test-test-test-test-";
	}

	public String test000(String... strings) {
		StringBuffer sb = new StringBuffer();

		for (String string : strings) {
			sb.append(string).append("   ");
		}

		return sb.toString();
	}

	public String test001(String[] strings, String s) {
		StringBuffer sb = new StringBuffer();

		for (String string : strings) {
			sb.append(string).append("   ");
		}

		return sb.toString() + s + s + s + s;
	}

	public String test002(String str1, String str2, String str3) {

		return str1 + str2 + str3;
	}

	public int test003(int... i) {
		int result=0;
		for (int j : i) {
			result+=j;
		}

		return result;
	}
	
	public int test004(int[] i, int i2) {
		int result=0;
		for (int j : i) {
			result+=j;
		}
		result+=i2;
		return result;
	}
}
