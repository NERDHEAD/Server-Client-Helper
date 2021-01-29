package com.nerdhead.restful.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;

/**
 * 
 * 
 * @author nerdhead
 * * @version 1.0.0 ( 2021.01.26 ~ )
 * @author NERDHEAD ( JS.Yanagi )
 * @see #method(method)
 */
public class HttpClientHelper {
	private final HttpClientInfo info = new HttpClientInfo();
	
	
	public HttpClientHelper() {}
	public HttpClientHelper(String KEY, String URL) {
		info.KEY = KEY;
		info.URL = URL;
	}
	
	public HttpClientHelper setKEY(String KEY) {
		info.KEY = KEY;
		return this;
	}
	public HttpClientHelper setURL(String URL) {
		info.URL = URL;
		return this;
	}
	/**
	 * Option Default : false<br>
	 * 이 옵션은 생성자에 URL, KEY를 바로 입력했을 때 가능합니다. 
	 * 
	 * @param isc
	 */
	public void setCheckOnloadOption() {
		info.validCheck();
	}
	
	/**
	 * Option Default : false
	 * @param isc
	 * @return
	 */
	public void setMethodListLogOption() {
		info.METHOD_LIST_LOG_OPTION = true;
	}
	
	public HttpServiceHelper method(String method) {
		if(method==null) {
			throw new NullPointerException("HttpClientHelper.method(String method) cannot be NULL!");
		}
		info.method=method;
		
		return new HttpServiceHelper(info.forService());
	}
	
	
	public class HttpServiceHelper{
		private final HttpClientInfo info;
		
		private HttpServiceHelper(HttpClientInfo info) {
			this.info=info;
		}
		
		public HttpRequestHelper data(Object...data) {
			info.data=data;
			return new HttpRequestHelper(info.forRequest());
		}
	}
	
	//TOD : 주석
	public class HttpRequestHelper{
		private final JacksonHelper 		jHelper;
		private final HttpClientInfo 		info;
		private final CloseableHttpClient 	httpClient;
		private final HttpPost 				request;
		
		private HttpRequestHelper(HttpClientInfo info) {
			this.info=			info;
			this.request=		new HttpPost(info.URL+info.method);
			this.jHelper=		new JacksonHelper();
			this.httpClient = 	HttpClientBuilder.create().build();
		}
		public Object request() 
				throws JsonProcessingException, ClassNotFoundException, 
				InstantiationException, IllegalAccessException, 
				IllegalArgumentException, InvocationTargetException, 
				NoSuchMethodException, SecurityException, IOException, ParseException {
			//TODO : 데이터 생성 및 요청/응답 처리
			JSONObject 				requestData;
			StringEntity 			params;
			CloseableHttpResponse 	response;
			int 					responseStatus;
			String 					responseData_str=null;
			JSONObject 				responseData;
			Object					resultData;
			
			requestData=generateData();
			params = new StringEntity(requestData.toString(), "UTF-8");
			request.addHeader("content-type", "application/json; charset=UTF-8");
			request.setEntity(params);
			
			try {
				response 			= httpClient.execute(request);
				// TODO responseStatus 예외처리
				responseStatus 		= response.getStatusLine().getStatusCode();
				responseData_str 	= EntityUtils.toString(response.getEntity());
				responseData		= (JSONObject)(new JSONParser().parse(responseData_str));
				resultData			= generateResult(responseData);
				
				response.close();
				
				return resultData;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				throw new ClientProtocolException(this.getClass().getName()+".request : url("+request.getURI()+") does not exist!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
				throw new ParseException(e.getPosition(), this.getClass().getName()+".request : this isn't data from "+HttpServerHelper.class.getName()+"!!!\n"
						+ "Response data : "+responseData_str);
			}
			
			return null;
			
		}
		
		
		/**
		 * key, data를 포장해서 전달 해 주는 method
		 * @return
		 * @throws JsonProcessingException
		 */
		private JSONObject generateData() throws JsonProcessingException {
			HashMap<String, Object> requestData = new HashMap<String, Object>();
			
			requestData.put(HelperVO.KEY, info.KEY);
			requestData.put(HelperVO.DATA, wrapData(info.data));
			
			return new JSONObject(requestData);
		}
		
		/**
		 * Certification의 값이 true이면 값을 return 해 주는 method
		 * @param responseData
		 * @return
		 * @throws IOException
		 * @throws ClassNotFoundException
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 * @throws InvocationTargetException
		 * @throws NoSuchMethodException
		 * @throws SecurityException
		 */
		@SuppressWarnings("unchecked")
		private Object generateResult(JSONObject responseData) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
			// Process 1-1 : Certification contain check
			if(!responseData.containsKey(HelperVO.Certification)) {
				// throw with massage : "No Certification Found massage"
				throw new IOException(this.getClass().getName()+".generateResult : No Certification Found. (Unknown Error)");
			}
			// Process 2 : ERROR_MSG contain check
			if(responseData.containsKey(HelperVO.ERROR_MSG)) {
				// throw with ERROR_MSG
				throw new IOException(this.getClass().getName()+".generateResult : Error occurred!!!\n"
						+ "Error massage : "+responseData.get(HelperVO.ERROR_MSG));
			}
			// Process 3 : Certification match check
			if(!(boolean)responseData.get(HelperVO.Certification)) {
				// throw with massage : "Certification has return false, password mismatch"  
				throw new IOException(this.getClass().getName()+".generateResult : Certification has return false, password mismatch!!!");
			// Process 4 : Data contain check
			}
			if(!responseData.containsKey(HelperVO.DATA)){
				// throw with massage : "No Data Found."
				throw new IOException(this.getClass().getName()+".generateResult : No Data Found.");
			}
			
			JSONObject result = (JSONObject)responseData.get(HelperVO.DATA);
			
			return jHelper.unwrapData(result);
		}
		
		/**
		 * 전달 받은 data가 없으면 null을 리턴<br>
		 * 이외에는 입력받은 parameter의 갯수 만큼 JSONArray에 누적해서 리턴
		 * 
		 * @param data
		 * @return
		 * @throws JsonProcessingException 
		 */
		@SuppressWarnings("unchecked")
		private JSONArray wrapData(Object[] dataArray) throws JsonProcessingException {
			// Process 1 : Null Check
			if(dataArray.length == 0) return null;
			// Process 2 : add Data with foreach
			JSONArray result=new JSONArray();
			for(Object data : dataArray) {
				result.add(jHelper.wrapData(data));
			}
			return result;
		}
	}
	
	/**
	 * Helper 에서 사용할 정보들을 담고있는 Class<br>
	 * 
	 * @author nerdhead
	 *
	 */
	public class HttpClientInfo{
		boolean METHOD_LIST_LOG_OPTION  = false;
		List<String> methodList;

		String KEY;
		String URL;
		String method;
		Object[] data;
		
		
		private HttpClientInfo() {}
		private HttpClientInfo(String KEY, String URL, String method) {
			this.KEY=	KEY;
			this.URL=	URL;
			this.method=method;
			
		}
		private HttpClientInfo(String KEY, String URL, String method, Object...data) {
			this.KEY =		KEY;
			this.URL =		URL;
			this.method = 	method;
			this.data =		data;
		}
		
		
		/**
		 * ClientHelper에서 ServiceHelper로 전환 될때 validCheck로 유효성 검사
		 * @return
		 */
		HttpClientInfo forService() {
			validCheck();
			return new HttpClientInfo(KEY, URL, method);
		}
		/**
		 * HttpServiceHelper 생성시에 유효성 검사를 완료함<br>
		 * data 전달하면 됨
		 * 
		 * @return
		 */
		HttpClientInfo forRequest() {
			return new HttpClientInfo(KEY, URL, method, data);
		}

		//TODO : Null 예외 처리 할 것
		// URL, KEY 중에 NULL이 있거나, Request 응답이 없을때 throw 할 것
		// 여기서 request, httpClient 가 생성됨 ( null 체크해서 생성 할 것 )
		// 여기서 테스트 할수있게 HttpRequestHelper의 기능을 쪼개야 함
		void validCheck() {
			//method가 null이면 ClientHelper에서 요청 -> URL, KEY 만 체크
			//METHOD_LIST_LOG_OPTION 이면 URL, KEY 체크 시에 methodList 요청 할 것
		}
	}
	
	
	
}
