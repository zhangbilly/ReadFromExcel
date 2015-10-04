package com.zworks.rfe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class DBUtil {
	public static final String DBTYPE = "dbtype";
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String DBNAME = "dbname";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String DBTYPE_MYSQL = "mysql";

	private static Connection connnection;
	private static Statement stmt;

	public static Connection getConnection(HashMap<String, String> dbSetting) {
		if (dbSetting.get(DBUtil.DBTYPE).equals(DBTYPE_MYSQL)) {
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
			// jdbc:mysql://127.0.0.1:3306/test
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
				connnection = DriverManager.getConnection(dburl,
						dbSetting.get(DBUtil.USERNAME),
						dbSetting.get(DBUtil.PASSWORD));
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			return connnection;
		}
		return null;

	}

	public static int[] executeUpdate(List<String> sql) {
		int count[]=null;
    	try {
			stmt = connnection.createStatement();
			for(int i=0;i<sql.size();i++){
				stmt.addBatch(sql.get(i));
			}
			count = stmt.executeBatch();
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeAll();
	}
		return count;
	

    }
    private static void closeAll() {  

        // 关闭Connection 对象  
        if (connnection != null) {  
            try {  
                connnection.close();  
            } catch (SQLException e) {  
                System.out.println(e.getMessage());  
            }  
        }  
        if(stmt!=null){
        	try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }  

}
