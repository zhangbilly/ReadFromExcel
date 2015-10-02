package com.zworks.rfe.ui;

import java.awt.GridLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.zworks.rfe.util.DBUtil;

public class DBSettingPanel extends JPanel{
	private File file;
	private HashMap<String,String> nameTocodeMap;
	private String tableName;
	private HashMap<String,String> dbSetting;
	
	private JLabel l_dbType;
	private JComboBox<String> cb_dbType;
	
	private JLabel l_host;
	private JTextField tf_host;
	
	private JLabel l_port;
	private JTextField tf_port;
	
	private JLabel l_dbName;
	private JTextField tf_dbName;
	
	private JLabel l_userName;
	private JTextField tf_userName;
	
	private JLabel l_password;
	private JTextField tf_password;
	
	public DBSettingPanel(){
		l_dbType = new JLabel("数据库类型：");
		cb_dbType = new JComboBox<String>(new String[]{"mysql"});
		
		l_host = new JLabel("主机：");
		tf_host = new JTextField();
		
		l_port = new JLabel("端口：");
		tf_port = new JTextField();
		
		l_dbName = new JLabel("数据库名：");
		tf_dbName = new JTextField();
		
		l_userName = new JLabel("用户名：");
		tf_userName = new JTextField();
		
		l_password = new JLabel("密码：");
		tf_password = new JTextField();
		
		this.setLayout(new GridLayout(6,2));
		this.add(l_dbType);
		this.add(cb_dbType);
		this.add(l_host);
		this.add(tf_host);
		this.add(l_port);
		this.add(tf_port);
		this.add(l_dbName);
		this.add(tf_dbName);
		this.add(l_userName);
		this.add(tf_userName);
		this.add(l_password);
		this.add(tf_password);
		
	}
	public HashMap<String,String> getDBSetting(){
		if(dbSetting==null){
			dbSetting = new HashMap<String,String>();
		}
		dbSetting.put(DBUtil.DBTYPE, (String) cb_dbType.getSelectedItem());
		dbSetting.put(DBUtil.HOST, tf_host.getText());
		dbSetting.put(DBUtil.PORT, tf_port.getText());
		dbSetting.put(DBUtil.DBNAME, tf_dbName.getText());
		dbSetting.put(DBUtil.USERNAME, tf_userName.getText());
		dbSetting.put(DBUtil.PASSWORD, tf_password.getText());
		return dbSetting;

	}
}
