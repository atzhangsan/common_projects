package com.jv.own;
 
 
public class TestMyProxy {
	public static void main(String[] args) {
		MyInvocationHandler handler = new MyOwnInvocationHandler(new UserDaoImpl());
		try {
			UserDao dao = (UserDao)MyProxyUtil.newInstance(com.jv.own.UserDao.class.getClassLoader(),new Class[]{UserDao.class},handler);
			dao.query("a");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
