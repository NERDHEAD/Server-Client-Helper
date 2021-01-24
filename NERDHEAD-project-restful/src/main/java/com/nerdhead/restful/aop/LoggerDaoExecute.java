package com.nerdhead.restful.aop;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerDaoExecute {
	public void before(JoinPoint j) {
		Logger logger = LoggerFactory.getLogger(j.getTarget() + "");
		logger.debug("메소드 실행 전");

		Object[] args = j.getArgs();

		if (args != null) {
			logger.debug("method 시작: \t {}", j.getSignature().getName());
			int cnt = 0;
			for (Object obj : args) {
				logger.debug((cnt++) + "번째:\t" + String.valueOf(obj));
			}
			logger.debug("-------------------------------");
		}
	}

	public void afterReturning(JoinPoint j) {
		Logger logger = LoggerFactory.getLogger(j.getTarget() + "");
		logger.debug("종료: \t{}", j.getSignature().getName());
	}

	public void afterThrowing(JoinPoint j) {
		Logger logger = LoggerFactory.getLogger(j.getTarget() + "" + j.getKind());
		logger.debug("에러 발생: \t{}", j.getArgs());
		logger.debug("에러 발생: \t{}", j.toString());
	}
}
