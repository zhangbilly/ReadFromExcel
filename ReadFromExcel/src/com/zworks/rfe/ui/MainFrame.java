package com.zworks.rfe.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.zworks.rfe.ui.ChooseFilePanel;
import com.zworks.rfe.ui.DBSettingPanel;
import com.zworks.rfe.ui.MappingPanel;
import com.zworks.rfe.ui.PreviewPanel;
import com.zworks.rfe.util.DBUtil;

public class MainFrame extends JFrame implements ActionListener {

	// panel
	private ChooseFilePanel chooseFilePanel;
	private MappingPanel mappingPanel;
	private PreviewPanel previewPanel;

	private DBSettingPanel dbSettingPanel;
	private JPanel mainPanel;
	private JPanel controlPanel;
	private HashMap<String, String> map;
	private File file;
	private HashMap<String, String> dbSetting;

	private JButton preStep;
	private JButton nextStep;
	private JButton finishButton;
	private CardLayout card;
	private int index = 1;

	public MainFrame() {
		// 选择文件Panel
		chooseFilePanel = new ChooseFilePanel(this);
		// 映射Panel
		mappingPanel = new MappingPanel();
		// 预览Panel
		previewPanel = new PreviewPanel();
		// 数据库设置Panel
		dbSettingPanel = new DBSettingPanel();
		// 主Panel
		card = new CardLayout();
		mainPanel = new JPanel(card);
		mainPanel.add(chooseFilePanel);
		mainPanel.add(mappingPanel);
		mainPanel.add(previewPanel);
		mainPanel.add(dbSettingPanel);
		// ControlPanel
		controlPanel = new JPanel();
		preStep = new JButton("上一步");
		preStep.addActionListener(this);

		nextStep = new JButton("下一步");
		nextStep.addActionListener(this);

		finishButton = new JButton("完成");
		finishButton.addActionListener(this);
		controlPanel.add(nextStep);

		// Frame
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.SOUTH);
		this.setSize(chooseFilePanel.getPreferredSize());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == nextStep) {
			if (index == 1) {
				file = chooseFilePanel.getFile();
				if (file == null) {

					return;
				}
				//mappingPanel.reset(file);
				card.next(mainPanel);
				controlPanel.add(preStep,0);
				resetSize(mappingPanel);
				index = 2;

			} else if (index == 2) {
				map = mappingPanel.getMapping();
				if (map == null) {
					return;
				}
				previewPanel.setNameToCodeMap(map);
				previewPanel.setFile(file);
				previewPanel.setTableName(mappingPanel.getTableName());
				previewPanel.reset(mappingPanel.getColumns());

				card.next(mainPanel);
				resetSize(previewPanel);
				index++;
			} else if (index == 3) {
				card.next(mainPanel);
				controlPanel.remove(nextStep);
				controlPanel.add(finishButton);
				resetSize(dbSettingPanel);
				index++;
			}
		} else if (e.getSource().equals(preStep)) {
			switch (index) {
			case 2:
				controlPanel.remove(preStep);
				card.previous(mainPanel);
				resetSize(chooseFilePanel);
				index--;
				break;
			case 3:
				card.previous(mainPanel);
				resetSize(mappingPanel);
				index--;
				break;
			case 4:
				card.previous(mainPanel);
				resetSize(previewPanel);
				controlPanel.remove(finishButton);
				controlPanel.add(nextStep);
				index--;
				break;

			}
		}else if(e.getSource().equals(finishButton)){
			dbSetting = dbSettingPanel.getDBSetting();
			DBUtil.getConnection(dbSetting);
			int count[] = DBUtil.executeUpdate(previewPanel.getSql());
			int success = 0;
			int failed = 0;
			for(int i=0;i<count.length;i++){
				if(count[i]==Statement.EXECUTE_FAILED)
					failed++;
				else if(count[i]>0){
					success +=count[i];
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append("共成功更新：");
			sb.append(success);
			sb.append("行记录；\n");
			sb.append("共失败：");
			sb.append(failed);
			sb.append("行记录；\n");

			if(JOptionPane.showConfirmDialog(this, sb.toString(),"退出？",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
				this.dispose() ;
			}
		}

	}
	public void resetSize(JPanel jpanel){
		this.setSize(jpanel.getPreferredSize());
		setLocationRelativeTo(null);
	}
	public MappingPanel getMappingPanel(){
		return mappingPanel;
	}

}
