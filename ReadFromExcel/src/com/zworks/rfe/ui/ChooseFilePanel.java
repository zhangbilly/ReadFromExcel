package com.zworks.rfe.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChooseFilePanel extends JPanel implements ActionListener{

	private JFileChooser jfc;
	private JLabel chooseFile;
	private JButton openFile;
	private JTextField chosenFile;
	private File file;
	private MainFrame jframe;
	
	public ChooseFilePanel(MainFrame jframe){
		this.jframe = jframe;
		chooseFile = new JLabel("选择文件：");
		chosenFile = new JTextField(25);
		chosenFile.setEditable(false);
		openFile = new JButton("打开文件");
		openFile.addActionListener(this);
		this.setLayout(new FlowLayout());
		
		this.add(chooseFile);
		this.add(chosenFile);
		this.add(openFile);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(openFile)){
			jfc=new JFileChooser();  
	        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );  
	        if(JFileChooser.APPROVE_OPTION==jfc.showDialog(new JLabel(), "选择") ){
		        
		        if(file==null||!jfc.getSelectedFile().equals(file)){
		        	file=jfc.getSelectedFile();  
		        	chosenFile.setText(file.getAbsolutePath());
		        	Runnable r =new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							jframe.getMappingPanel().reset(file);
						}
					};
					r.run();
		        }
	        }

	        
		}

          
        
        
	}
	public File getFile(){
		return file;
	}
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(600,200);
	}

}
