package com.jv.own;
 
import com.jv.own.UserDao;
import java.lang.reflect.Method;
 
import com.jv.own.MyInvocationHandler;
 
public class MyProxy implements UserDao{
 
	private MyInvocationHandler handler;
	public MyProxy(MyInvocationHandler handler){
		this.handler = handler;
	}
 
	public com.jv.own.User query(java.lang.String p1){
		try{
			Object[] args = new Object[]{p1};
			Method method = com.jv.own.UserDao.class.getMethod("query",Class.forName("java.lang.String"));
			return (com.jv.own.User)handler.invoke(this,method,args);
		}catch(Throwable e){
			return null;
		}
	}
}
