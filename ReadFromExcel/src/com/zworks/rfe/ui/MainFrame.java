package com.zworks.rfe.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.zworks.rfe.ui.ChooseFilePanel;
import com.zworks.rfe.ui.DBSettingPanel;
import com.zworks.rfe.ui.MappingPanel;
import com.zworks.rfe.ui.PreviewPanel;

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
	private CardLayout card;
	private int index = 1;

	public MainFrame() {
		// 选择文件Panel
		chooseFilePanel = new ChooseFilePanel();
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
		controlPanel.add(preStep);
		controlPanel.add(nextStep);

		// Frame
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.SOUTH);
		this.setSize(300, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				mappingPanel.reset(file);
				card.next(mainPanel);
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
				index++;
			} else if (index == 3) {
				card.next(mainPanel);
				index++;
			} else if (index == 4) {
				dbSetting = dbSettingPanel.getDBSetting();

			}
		}

	}
}
