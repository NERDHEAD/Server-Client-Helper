package com.nerdhead.restful.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public  class HttpServerHelper<T> {
	private final HttpServiceHelper<T> service;


	public HttpServerHelper(T service, String KEY) {
		this.service=new HttpServiceHelper<T>(service, KEY);
	}
	
	public HttpServiceHelper<T> method(String methodStr) {
		return service.method(methodStr);
	}
	
	
	public class HttpServiceHelper<T>{
		private final JacksonHelper jHelper;
		private final InvokeHelper iHelper;
		
		private final String KEY;
		private final T service;
		private String methodStr;
		
		private HttpServiceHelper(T service, String KEY) {
			this.KEY=KEY;
			this.service=service;
			this.jHelper=new JacksonHelper();
			this.iHelper=new InvokeHelper();
		}

		private HttpServiceHelper<T> method(String methodStr) {
			this.methodStr=methodStr;
			return this;
		}
		
		/*TODO
		 * 1. 받은 데이터를 체크
		 * 2-1. KEY 가 틀리면 Cert =false 후 return
		 * 2-2. KEY 가 맞으면 data를 unwrap
		 * TODO : data 말고 param type 도 client에서 받아야함
		 */
		public JSONObject data(Map<String, Object> dataMap) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
			Map<String,Object> result = new HashMap<>();
			
			// Process 1 : KEY contain check
			if(!dataMap.containsKey(HelperVO.KEY)) {
				result.put(HelperVO.ERROR_MSG, this.getClass().getName()+".generateResult : No KEY Found.");
			}
			// Process 2 : KEY match check
			boolean isSameKey=dataMap.get(HelperVO.KEY).equals(KEY);
			
			//TODO Process 3 : DATA contain Check -> DATA : null이랑 다름, DATA 라는 key가 담겨있는지 체크
			
			// Process 4 : KEY 가 일치 하면 result에 데이터 입력
			if(isSameKey) {
				// DOTO : iHelper에서 예외 throw 할경우 여기서 catch하여  ERROR_MSG 처리 후 return
				// DOTO : 데이터를 정제해서 parameter에 던져야 함
				Object[] parameter = unwrapData(dataMap);
				
				Object resultData=iHelper.clazz(service)
						.method(methodStr)
						.parameter(parameter)
						.run();
				if(resultData==null) {
					//TODO : 결과가 null일때
				}else if(resultData.equals(Void.TYPE)) {
					//TODO : 결과가 Void.TYPE일때
				}
				System.out.println("@@@@@@  resultData : " + resultData.toString());//TODO Delete
				result.put(HelperVO.DATA, wrapData(resultData));
			}
			// Process 5 : Certification -> KEY가 일치하면 true, 일치하지 않으면 false가 들어감
			result.put(HelperVO.Certification, isSameKey);
			
			return new JSONObject(result);
		}
		
		private JSONObject wrapData(Object data) throws JsonProcessingException {
			return jHelper.wrapData(data);
		}
		//TODO : jHepler.unwrapData 예외 발생 throw
		@SuppressWarnings("unchecked")
		private Object[] unwrapData(Map<String, Object> data) throws JsonParseException, JsonMappingException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
			//TODO Casting Exception 처리 -> 억지로 형태에 맞춰서 통신을 시도 할 수도 있음
			ArrayList<Map<String, Object>> dataList=
					(ArrayList<Map<String, Object>>) data.get(HelperVO.DATA);
			
			// Process 1 : Null Check
			if(dataList==null) return null;
			// Process 2 : Make Object[] with dataList.size()
			Object[] unwrapedData = new Object[dataList.size()];
			for (int i = 0; i < unwrapedData.length; i++) {
				unwrapedData[i]=jHelper.unwrapData((Map<String, Object>) dataList.get(i));
			}
			
			
			return unwrapedData;
		}
		
	}
}
