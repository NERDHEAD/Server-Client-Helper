package com.nerdhead.restful.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessLogFilter implements Filter {
	
	private Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		/* logger.info("////////// 필터 생성 //////////"); */
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//	// request 정보에서 사용자의 정보를 추출
//	// URL + QueryString + header 정보
//		
//	// request 객체
//		HttpServletRequest req = (HttpServletRequest)request;
//		
//	// 주소 remote정보
//		String remoteAddr = StringUtils.defaultString(req.getRemoteAddr(), "-");
//	// URI
//		String uri = StringUtils.defaultString(req.getRequestURI(), "-");
//	// queryString
//		String queryString = StringUtils.defaultString(req.getQueryString(), "");
//		
//	// Header 정보
//		String referer = StringUtils.defaultString(req.getHeader("Referer"), "-");
//		String agent = StringUtils.defaultString(req.getHeader("User-Agent"), "-");
//		
//		String fullUri = uri;
//		fullUri += StringUtils.isNotEmpty(queryString)?"?"+queryString:"";
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append(":");
//		sb.append(remoteAddr);
//		sb.append(":");
//		sb.append(fullUri);
//		sb.append(":");
//		sb.append(referer);
//		sb.append(":");
//		sb.append(agent);
//		
//		logger.info("////////// 필터 이동중 //////////");
//		logger.info("AccessLogger :\t"+sb.toString());
		
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		logger.info("////////// 필터 종료 //////////");
	}

}
