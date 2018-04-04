package br.com.changefsm.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JPanelChangeFSM extends JPanel implements ActionListener {

	private JPanel guiPanel;
	
	private JButton guiButton_searchOldCode;
	private JTextArea guiTextArea_pathOld;
	
	private JButton guiButton_searchNewCode;
	private JTextArea guiTextArea_pathNew;
	
	private JTextArea guiTextArea_pathStateMachine;
	private JButton guiButton_searchStateMachine;

	private JFileChooser fileChooserProjects;
	private JFileChooser fileChooserStateMachine;
	
	private JButton guiButton_runTech;
	private JButton guiButton_cleanFields;
	
	private JButton guiButton_generatePDF;
	private JButton guiButton_updateXML;
	private JTextArea guiTextArea_mainOutput;

 	public JPanelChangeFSM() {
		guiPanel = new JPanel(new BorderLayout());

		fileChooserProjects = new JFileChooser();
		fileChooserProjects.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
		fileChooserStateMachine = new JFileChooser();
		fileChooserStateMachine.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooserStateMachine.setFileFilter(new FileNameExtensionFilter("XML Files","xml"));
		fileChooserStateMachine.setAcceptAllFileFilterUsed(false);
		
		initPanels();
		
	}

	
	private void initPanels() {
		JPanel panels = new JPanel();
		panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));
		
		panels.add(panelTopicCode());
		panels.add(panelSearchNewProject());
		panels.add(panelSearchOldProject());

		panels.add(panelTopicStateMachine());
		panels.add(panelSearchStateMachine());
		panels.add(panelRunAndClean());
		panels.add(panelOutput());
		
		guiPanel.add(panels, BorderLayout.CENTER);
	}


	private JPanel panelTopicCode() {
		JPanel panelTopicCode = new JPanel(new BorderLayout());
		JLabel guiTopic = new JLabel("Code Project");
		guiTopic.setFont(new Font("Arial", Font.BOLD, 18));
		panelTopicCode.add(guiTopic, BorderLayout.LINE_START);
		return panelTopicCode;
	}
	
	private JPanel panelSearchNewProject() {
		
		JPanel panelNewProject = new JPanel(new BorderLayout());
		
		JLabel guiLabel_newProject = new JLabel("New Project: ");
		
		guiTextArea_pathNew = new JTextArea(1, 20);
		guiTextArea_pathNew.setEditable(false);
		
		guiButton_searchNewCode = new JButton("Search");
		guiButton_searchNewCode.addActionListener(this);
		
		panelNewProject.add(guiLabel_newProject, BorderLayout.LINE_START);
		panelNewProject.add(guiTextArea_pathNew, BorderLayout.CENTER);
		panelNewProject.add(guiButton_searchNewCode, BorderLayout.LINE_END);
		
		return panelNewProject;	
	}
	
	private JPanel panelSearchOldProject() {
		JPanel panelOldProject = new JPanel(new BorderLayout());
		
		JLabel guiLabel_oldProject = new JLabel("Old Project: ");
		
		guiTextArea_pathOld = new JTextArea(1, 20);
		guiTextArea_pathOld.setEditable(false);
		
		guiButton_searchOldCode = new JButton("Search");
		guiButton_searchOldCode.addActionListener(this);
		
		panelOldProject.add(guiLabel_oldProject, BorderLayout.LINE_START);
		panelOldProject.add(guiTextArea_pathOld, BorderLayout.CENTER);
		panelOldProject.add(guiButton_searchOldCode, BorderLayout.LINE_END);
		
		return panelOldProject;
	}
	
	private JPanel panelTopicStateMachine() {
		JPanel panelTopicStateMachine = new JPanel(new BorderLayout());
		JLabel guiTopic = new JLabel("State Machine");
		guiTopic.setFont(new Font("Arial", Font.BOLD, 18));
		panelTopicStateMachine.add(guiTopic);
		return panelTopicStateMachine;
	}
	
	private JPanel panelSearchStateMachine() {		
		JPanel panelSearchStateMachine = new JPanel(new BorderLayout());
		
		JLabel guiLabel_StateMachine = new JLabel("XML State Machine: ");
		
		guiTextArea_pathStateMachine = new JTextArea(1, 20);
		guiTextArea_pathStateMachine.setEditable(false);
		
		guiButton_searchStateMachine = new JButton("Search");
		guiButton_searchStateMachine.addActionListener(this);
		
		panelSearchStateMachine.add(guiLabel_StateMachine, BorderLayout.LINE_START);
		panelSearchStateMachine.add(guiTextArea_pathStateMachine, BorderLayout.CENTER);
		panelSearchStateMachine.add(guiButton_searchStateMachine, BorderLayout.LINE_END);
		return panelSearchStateMachine;

	}
	
	private JPanel panelRunAndClean() {
		JPanel panelRunAndClean = new JPanel();
		
		guiButton_cleanFields = new JButton("Clean");
		guiButton_cleanFields.addActionListener(this);
		guiButton_runTech = new JButton("Run");
		
		panelRunAndClean.add(guiButton_runTech);
		panelRunAndClean.add(guiButton_cleanFields);
		
		return panelRunAndClean;
	}
	
	private JPanel panelOutput() {
		JPanel panelOutPut = new JPanel();
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));
		
		guiButton_generatePDF = new JButton("PDF");
		guiButton_updateXML = new JButton("XML");
		
		guiTextArea_mainOutput = new JTextArea(5,30);
		guiTextArea_mainOutput.setEditable(false);
		
		panelOutPut.add(guiTextArea_mainOutput);
		panelButtons.add(guiButton_generatePDF);
		panelButtons.add(guiButton_updateXML);
		panelOutPut.add(panelButtons);
		
		return panelOutPut;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(guiButton_searchNewCode)) {
			int auxOld = fileChooserProjects.showOpenDialog(JPanelChangeFSM.this);

			if (auxOld == JFileChooser.APPROVE_OPTION) {
				guiTextArea_pathNew.setText("");
				guiTextArea_pathNew.append(fileChooserProjects.getSelectedFile().getAbsolutePath());
			}
		} 
		else if (e.getSource().equals(guiButton_searchOldCode)) {

			int auxNew = fileChooserProjects.showOpenDialog(JPanelChangeFSM.this);

			if (auxNew == JFileChooser.APPROVE_OPTION) {
				guiTextArea_pathOld.setText("");
				guiTextArea_pathOld.append(fileChooserProjects.getSelectedFile().getAbsolutePath());

			}
		}
		else if (e.getSource().equals(guiButton_searchStateMachine)) {

			int auxNew = fileChooserStateMachine.showOpenDialog(JPanelChangeFSM.this);

			if (auxNew == JFileChooser.APPROVE_OPTION) {
				guiTextArea_pathStateMachine.setText("");
				guiTextArea_pathStateMachine.append(fileChooserStateMachine.getSelectedFile().getPath());

			}
		}
		else if (e.getSource().equals(guiButton_cleanFields)) {
			
			guiTextArea_pathNew.setText("");
			guiTextArea_pathOld.setText("");
			guiTextArea_pathStateMachine.setText("");

		}
		
	}

	public JPanel getPanel() {
		return guiPanel;
	}

	public void setPanel(JPanel panel) {
		this.guiPanel = panel;
	}

}
