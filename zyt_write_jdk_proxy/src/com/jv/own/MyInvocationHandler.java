package com.jv.own;

import java.lang.reflect.Method;

public interface MyInvocationHandler {
 
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable;
 
}