package com.jv.own;
 
public class UserDaoImpl implements UserDao {
	@Override
	public User query(String name) {
		System.out.println("UserDaoImpl execute query");
		return null;
	}
}