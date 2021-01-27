package com.nerdhead.restful.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Jackson ObjectMapper 역직렬화 시 Collection, Map 안의 POJO가 LinkedHashMap가 되는 문제 해결을 위한 Helper Class<br>
 * Support : Collection, Map, POJO, 기본 타입<br>
 * 
 * @version 1.0.0 ( 2021.01.25 ~ 2021.01.26)
 * @author NERDHEAD ( JS.Yanagi )
 * @see <a href="https://gist.github.com/NERDHEAD/67c5368f569223f5d04ee547886562b2">gist.github document</a>
 * @see #wrapData(data)
 * @see #unwrapData(data)
 * 
 */
public class JacksonHelper {
	private final ObjectMapper mapper;
	
	private final String D="D";		//KEY for Data String 
	private final String T="T";		//KEY for Data Class Name 
	
	//Constructor
	public JacksonHelper() {
		mapper=new ObjectMapper();
	}
	/**
	 * data를 직렬화 하여 JSONObject {D : 직렬화 된 data, T : data의 Class 타입} 으로 만들어 줌<br>
	 * data의 타입이 Collection, Map 일 경우 재귀 호출 되어 내부의 객체들이 같은 형식으로 직렬화 됨<br> 
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
			// Process 1-2 : foreach문으로 재귀 호출
			for(Object obj : (Collection<?>)data) {
				// Process 1-3 : JSONArray에 wrap된 data를 누적
				list.add(wrapData(obj));
			}
			// Process 1-4 : data의 Class 타입으로 list를 wrap 하기
			return _wrapData(list, data.getClass());
		}
		//Process 2 : Map Check
		if (data instanceof Map) {
			JSONObject map=new JSONObject();
			// Process 2-2 : foreach문으로 재귀 호출
			for(Object key : ((Map)data).keySet()) {
				// Process 2-3 : JSONObject에 wrap된 key, value를 누적
				Object value =((Map)data).get(key);
				map.put(wrapData(key), wrapData(value));
			}
			// Process 2-4 : data의 Class 타입으로 map을 wrap 하기
			return _wrapData(map, data.getClass());
		}
		//Process 3 : default wrapping
		return _wrapData(data, data.getClass());
	}
	
	//TODO
	@SuppressWarnings("unchecked")
	public Object unwrapData(JSONObject data) 
			throws JsonParseException, JsonMappingException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String dataStr;
		String className;
		Class<?> clazz;
		
		dataStr		= (String) data.get(D);					//직렬화 된 데이터
		className	= (String) data.get(T);					//데이터의 Class Type
		clazz		= Class.forName(className);
		
		// Process 1-1 : Collection Check
		if(Collection.class.isAssignableFrom(clazz)) {
			JSONArray list_temp=mapper.readValue(dataStr, JSONArray.class);		//Jackson 이슈로 인해 내부의 값은 LinkedHashMap으로 처리 된 상태
			// Process 1-2 : data의 class 타입으로 객체 생성
			Collection<Object> innerData=(Collection<Object>)clazz.getDeclaredConstructor().newInstance();
			// Process 1-3 : 담긴 Map을 foreach 문으로 추출
			for (Object object : list_temp) {
				JSONObject map=new JSONObject((Map<String, String>)object);
				// Process 1-4 : 1-2에서 생성된 객체에 역직렬화 후 추가
				innerData.add(unwrapData(map));
			}
			// Process 1-5 : Collection 관련 처리 끝
			return innerData;
		}
		
		//Process 2 : Map Check
		if(Map.class.isAssignableFrom(clazz)) {
			JSONObject map_temp=mapper.readValue(dataStr, JSONObject.class);	//Jackson 이슈로 인해 내부의 값은 LinkedHashMap으로 처리 된 상태
			// Process 2-2 : data의 class 타입으로 객체 생성
			Map<Object, Object> innerData=(Map<Object, Object>)clazz.getDeclaredConstructor().newInstance();
			// Process 2-3 : 담긴 Map의 K,V를 foreach 문으로 추출
			for (Object object : map_temp.keySet()) {
				JSONObject key		=mapper.readValue((String)object, JSONObject.class);	
				JSONObject value	=new JSONObject((Map<String, String>)map_temp.get(object));
				// Process 2-4 : 2-2에서 생성된 객체에 K,V 각각 역직렬화 후 추가
				innerData.put(unwrapData(key), unwrapData(value));
			}
			// Process 1-5 : Map 관련 처리 끝, Return
			return innerData;
		}
		
		//Process 3 : Do default unwrapping
		return _unwrapData(dataStr, className);
	}
	
	
	
	
	
	
	
	/**
	 * Collection과 Map을 제외한 경우 data를 직렬화 해주는 method<br>
	 * @param data
	 * @param clazz
	 * @return
	 * @throws JsonProcessingException
	 */
	private JSONObject _wrapData(Object data, Class<?> clazz) throws JsonProcessingException {
		Map<String,String> map = new HashMap<>();
		String dataStr;
		String className;
		
		dataStr		= mapper.writeValueAsString(data);			//데이터 직렬화
		className	= clazz.getName();							//데이터의 Class Type
		//	CanonicalName은 Class.forName()에 사용 할 수 없음.
		//	className	= data.getClass().getCanonicalName();
		
		map.put(D, dataStr);
		map.put(T, className);
		
		return new JSONObject(map);
	}
	
	
	/**
	 * Collection과 Map을 제외한 경우 dataStr을 className의 타입으로 역직렬화 해주는 method<br>
	 * @param dataStr
	 * @param className
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws NoSuchMethodException 
	 */
	private Object _unwrapData(String dataStr, String className) 
			throws JsonParseException, JsonMappingException, ClassNotFoundException, IOException, NoSuchMethodException{
		return mapper.readValue(dataStr, Class.forName(className));
	}
}
