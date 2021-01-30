package com.nerdhead.restful.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//TODO : MethodList 가져오는 기능 추가 할 것



public class InvokeHelper {

	public <T> InvokeClassHelper<T> clazz(T service) {
		return new InvokeClassHelper<T>(service);
	}
	
	public class InvokeClassHelper<T>{
		private final T service;
		
		private InvokeClassHelper(T service) {
			this.service = service;
		}
		public InvokeMethodHelper<T> method(String methodStr) {
			return new InvokeMethodHelper<T>(service, methodStr);
		}
	}
	
	public class InvokeMethodHelper<T>{
		private final T service;
		private final String methodStr;
		
		private InvokeMethodHelper(T service, String methodStr) {
			this.service = service;
			this.methodStr = methodStr;
		}
		public InvokeRunHelper<T> parameter(Object... varargs) {
				return new InvokeRunHelper<T>(service,methodStr, varargs);
		}
	}
	
	
	public class InvokeRunHelper<T>{
		private final T 		service;
		private final String 	methodStr;
		private final Object[] 	varargs;
		
		private InvokeRunHelper(T service, String methodStr, Object... varargs) {
			this.service = 		service;
			this.methodStr = 	methodStr;
			this.varargs = 		varargs;
		}
		
		public Object run() throws NoSuchMethodException {
			//service		: 실행할 Class
			//method		: 실행할 method
			Method method;
			//parameterTypes		: method에 전달될 형태(.class)
			Class<?>[] parameterTypes;
			//varargs		: parameter
			//result		: method 실행 후 return 결과
			Object result;
			try {
				// Process 1 : Null check
				if(varargs==null) {
					method = service.getClass().getDeclaredMethod(methodStr);
					result = method.invoke(service);
				}else {
					parameterTypes=getParameterTypes(varargs);
					method = service.getClass().getDeclaredMethod(methodStr, parameterTypes);
					result = method.invoke(service, varargs);
				}
				
				if(method.getReturnType().equals(Void.TYPE)) {
					return Void.TYPE;
				}else {
					return result;
				}
				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				// TODO : method 종류를 return?
				// TODO : ERROR_MSG 리턴 할 수 있게?
				throw new NoSuchMethodException("Cannot find "+service.getClass().getName()+"."+methodStr);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
		
		private Class<?>[] getParameterTypes(Object[] varags) {
			Class<?>[] parameterTypes=new Class[varags.length];
			for (int i = 0; i < varags.length; i++) {
				parameterTypes[i]=varags[i].getClass();
			}
			
			return parameterTypes;
		}
		
	}
	
	
}