package com.jv.own;
 
import java.lang.reflect.Method;
 
public class MyOwnInvocationHandler implements MyInvocationHandler{
	private UserDao target;
	public MyOwnInvocationHandler(UserDao target){
		this.target = target;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("invoke before");
		method.invoke(target,args);
		System.out.println("invoke after");
		return null;
	}
}