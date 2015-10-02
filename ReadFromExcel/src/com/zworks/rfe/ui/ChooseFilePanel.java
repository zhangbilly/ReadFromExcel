package com.zworks.rfe.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChooseFilePanel extends JPanel implements ActionListener{

	private JFileChooser jfc;
	private JLabel chooseFile;
	private JButton nextStep;
	private JButton openFile;
	private File file;
	
	public ChooseFilePanel(){
		
		chooseFile = new JLabel("选择文件：");
		nextStep = new JButton("下一步：");
		openFile = new JButton("打开文件");
		openFile.addActionListener(this);
		this.setLayout(new FlowLayout());
		
		this.add(chooseFile);
		this.add(openFile);
		this.add(nextStep);
		this.setSize(300, 200);
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChooseFilePanel();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(openFile)){
			jfc=new JFileChooser();  
	        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );  
	        jfc.showDialog(new JLabel(), "选择");  
	        file=jfc.getSelectedFile();  
	        if(file!=null){
	        	System.out.println("文件:"+file.getAbsolutePath());
	        	System.out.println(jfc.getSelectedFile().getName());  
	        }
		}

          
        
        
	}
	public File getFile(){
		return file;
	}

}
