package com.nerdhead.restful.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Jackson ObjectMapper 역직렬화 시 Collection, Map 안의 POJO가 LinkedHashMap가 되는 문제 해결을 위한 Helper Class<br>
 * Support : Collection, Map, POJO, 기본 타입<br>
 * 
 * @version 1.0.0 ( 2021.01.25 ~ )
 * @author NERDHEAD ( JS.Yanagi )
 * @see <a href="https://gist.github.com/NERDHEAD/67c5368f569223f5d04ee547886562b2">gist.github document</a>
 */
public class JacksonHelper {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	private final ObjectMapper mapper;
	//Constructor
	public JacksonHelper() {
		mapper=new ObjectMapper();
	}
	/**
	 * 전달 받은 data의 type이 Colletion이나 Map이면 wrapData를 재귀 호출<br>
	 * 그 이외의 경우에는 {D : 직렬화 된 데이터, T : 데이터의 원래 클래스 타입<br> 
	 * @param <T>
	 * @param data
	 * @return
	 * @throws JsonProcessingException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject wrapData(Object data) throws JsonProcessingException{
		// Process 1-1 : Collection Check
		if (data instanceof Collection<?>) {
			JSONArray list=new JSONArray();
			// Process 1-2 : call recursive method with foreach		// foreach문으로 재귀 호출
			for(Object obj : (Collection<?>)data) {
				// Process 1-3 : add wrapData to JSONArray			// JSONArray에 wrap된 data를 누적
				list.add(wrapData(obj));
			}
			// Process 1-4 : wrapping list with data's Class type	// data의 Class 타입으로 list를 wrap 하기
			return _wrapData(list, data.getClass());
		}
		//Process 2 : Map Check
		if (data instanceof Map) {
			JSONObject map=new JSONObject();
			// Process 2-2 : call recursive method with foreach		// foreach문으로 재귀 호출
			for(Object key : ((Map)data).keySet()) {
				// Process 2-3 : add wrapData(key), wrapData(value) to JSONObject	
				Object value =((Map)data).get(key);					// JSONObject에 wrap된 key, value를 누적
				map.put(wrapData(key), wrapData(value));
			}
			// Process 2-4 : wrapping list with data's Class type	// data의 Class 타입으로 map을 wrap 하기
			return _wrapData(map, data.getClass());
		}
		//Process 3 : Do default wrapping
		return _wrapData(data, data.getClass());
	}
	
	//TODO
	public Object unwrapData(JSONObject data) {
		
		
		return _unwrapData(data);
	}
	
	
	
	
	
	
	
	
	private JSONObject _wrapData(Object data, Class<?> clazz) throws JsonProcessingException {
		Map<String,String> map = new HashMap<>();
		String dataStr;
		String className;
		
		dataStr		= mapper.writeValueAsString(data);	//데이터 직렬화
		className	= clazz.getName();		//데이터의 Class Type
		//	CanonicalName은 Class.forName()에 사용 할 수 없음.
		//	className	= data.getClass().getCanonicalName();
		
		map.put("D", dataStr);
		map.put("T", className);
		
		return new JSONObject(map);
	}
	
	
	//TODO
	private Object _unwrapData(JSONObject data) {
		//TODO return null;
		return null;
	}
}
