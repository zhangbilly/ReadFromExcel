package com.zworks.rfe.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.zworks.rfe.util.ExcelUtil;

public class MappingPanel extends JPanel{
	private JLabel l_tableName;
	private JTextField t_tableName;
	Map<String,JTextField> m_columns;
	List<String> columns;
	boolean initialized =false;
	File file;
	
	public MappingPanel(){
		m_columns = new HashMap<String, JTextField>();
	}
	public void reset(File file){
		if(!initialized){
			this.file = file;
			columns = ExcelUtil.getColumnNames(file);
			this.setLayout(new GridLayout(columns.size()+1, 2));
			l_tableName = new JLabel("表名");
			t_tableName = new JTextField();
			this.add(l_tableName);
			this.add(t_tableName);
			for(String s:columns){
				this.add(new JLabel(s));
				m_columns.put(s, new JTextField());
				this.add(m_columns.get(s));
			}
			initialized = true;
		}else if(file!=null&&!this.file.equals(file)){
			this.file = file;
			this.removeAll();
			columns = ExcelUtil.getColumnNames(file);
			this.setLayout(new GridLayout(columns.size()+1, 2));
			l_tableName = new JLabel("表名");
			t_tableName = new JTextField();
			this.add(l_tableName);
			this.add(t_tableName);
			for(String s:columns){
				this.add(new JLabel(s));
				m_columns.put(s, new JTextField());
				this.add(m_columns.get(s));
			}
		}

	}
	public HashMap<String,String> getMapping(){
		HashMap<String,String> map = new HashMap<String, String>();
		for(String s :m_columns.keySet()){
			map.put(s, m_columns.get(s).getText());
		}
		return map;
	}
	public List<String> getColumns() {
		return columns;
	}
	public String getTableName(){
		return t_tableName.getText();
	}
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(300,200);
	}
}
