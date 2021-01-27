package com.nerdhead.restful.util;

import java.util.List;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

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
		private JacksonHelper jHelper;
		private final HttpClientInfo info;
		
		private HttpRequestHelper(HttpClientInfo info) {
			this.info=info;
			this.jHelper=new JacksonHelper();
		}
		public Object request() {
			//TODO : 데이터 생성 및 요청/응답 처리
			JSONObject json=generateData();
			
			return null;
		}
		
		public JSONObject generateData() {
			JSONObject json = new JSONObject();
			
			
			return json;
		}
	}
	
	
	public class HttpClientInfo{
		CloseableHttpClient httpClient;
		HttpPost request;

		boolean METHOD_LIST_LOG_OPTION  = false;
		List<String> methodList;

		String KEY;
		String URL;
		String method;
		Object[] data;
		
		
		private HttpClientInfo() {}
		private HttpClientInfo(String KEY, HttpPost request) {
			this.KEY=KEY;
			this.request=request;
			this.httpClient = HttpClientBuilder.create().build();
		}
		private HttpClientInfo(String KEY, HttpPost request, CloseableHttpClient httpClient, Object...data) {
			this.KEY=KEY;
			this.request=request;
			this.data=data;
			this.httpClient=httpClient;
		}
		
		
		/**
		 * ClientHelper에서 ServiceHelper로 전환 될때 validCheck로 유효성 검사
		 * @return
		 */
		HttpClientInfo forService() {
			validCheck();
			return new HttpClientInfo(KEY, request);
		}
		/**
		 * HttpServiceHelper 생성시에 유효성 검사를 완료함<br>
		 * data 전달하면 됨
		 * 
		 * @return
		 */
		HttpClientInfo forRequest() {
			return new HttpClientInfo(KEY, request, httpClient, data);
		}

		//TODO : Null 예외 처리 할 것
		// URL, KEY 중에 NULL이 있거나, Request 응답이 없을때 throw 할 것
		// 여기서 request, httpClient 가 생성됨 ( null 체크해서 생성 할 것 )
		void validCheck() {
			//method가 null이면 ClientHelper에서 요청 -> URL, KEY 만 체크
			//METHOD_LIST_LOG_OPTION 이면 URL, KEY 체크 시에 methodList 요청 할 것
		}
	}
	
	
	
}
