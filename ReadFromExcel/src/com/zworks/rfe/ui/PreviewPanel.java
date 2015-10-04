package com.zworks.rfe.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import com.zworks.rfe.util.ExcelUtil;

public class PreviewPanel extends JPanel implements ActionListener{
	private JLabel l_queryItem;
	private ButtonGroup bg_queryItem;
	private Map<String,JRadioButton> map;
	private JPanel p_queryItem;
	private JTextArea ta_sql;
	private List<String> columns;
	private HashMap<String,String> nameToCodeMap;
	private File file;
	private String tableName;
	private List<String> sql;
	public PreviewPanel(){
		
	}
	public void reset(List<String> columns){
		this.columns = columns;
		map = new HashMap<String, JRadioButton>();
		bg_queryItem = new ButtonGroup();
		for(String s:columns){
			map.put(s, new JRadioButton(s));
			map.get(s).addActionListener(this);
			bg_queryItem.add(map.get(s));
		}
		p_queryItem = new JPanel();
		l_queryItem = new JLabel("Ñ¡Ôñ²éÑ¯×Ö¶Î");
		p_queryItem.add(l_queryItem);
		for(JRadioButton rb:map.values()){
			p_queryItem.add(rb);
		}
		ta_sql = new JTextArea();
		setLayout(new BorderLayout());
		add(p_queryItem,BorderLayout.NORTH);
		add(ta_sql,BorderLayout.CENTER);
		
		
		
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for(JRadioButton rb:map.values()){
			if(rb.isSelected()){
				resetSql(rb.getText());
			}
		}
	}
	public void resetSql(String s){
		
		sql = ExcelUtil.getPreviewSql(file,columns.indexOf(s),tableName,columns,nameToCodeMap);
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<sql.size();i++){
			sb.append(sql.get(i));
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length()-1);
		ta_sql.setText(sb.toString());
	}
	public HashMap<String, String> getNameToCodeMap() {
		return nameToCodeMap;
	}
	public void setNameToCodeMap(HashMap<String, String> nameToCodeMap) {
		this.nameToCodeMap = nameToCodeMap;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<String> getSql(){
		return sql;
	}
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(800,600);
	}
}
