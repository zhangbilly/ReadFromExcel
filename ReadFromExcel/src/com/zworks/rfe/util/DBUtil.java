package com.zworks.rfe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class DBUtil {
	public static final String DBTYPE = "dbtype";
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String DBNAME = "dbname";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String DBTYPE_MYSQL = "mysql";
	
	private Connection connnection;
	
	
	
    public Connection getConnection(HashMap<String,String> dbSetting) {
    	if(dbSetting.get(DBUtil.DBTYPE).equals(DBTYPE_MYSQL)){
    		try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
    		//jdbc:mysql://127.0.0.1:3306/test
        	StringBuffer sb = new StringBuffer();
        	sb.append("jdbc:mysql://");
        	sb.append(dbSetting.get(DBUtil.HOST));
        	sb.append(":");
        	sb.append(dbSetting.get(DBUtil.PORT));
        	sb.append("/");
        	sb.append(dbSetting.get(DBUtil.DBNAME));
        	String dburl = sb.toString();
            try {

                // 获取连接  
                connnection = DriverManager.getConnection(dburl, dbSetting.get(DBUtil.USERNAME),dbSetting.get(DBUtil.PASSWORD));  
            } catch (SQLException e) {  
                System.out.println(e.getMessage());  
            }  
            return connnection; 
    	}
    	return null;
 
    }  
  

}
