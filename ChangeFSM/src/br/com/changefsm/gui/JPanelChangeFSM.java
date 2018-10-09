package br.com.changefsm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import com.itextpdf.text.DocumentException;

import br.com.changefsm.exceptions.ChangeFSMException;
import br.com.changefsm.facade.ChangeFSM;

public class JPanelChangeFSM extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel guiPanel;

	private JButton guiButton_searchOldCode;
	private JTextArea guiTextArea_pathOld;

	private JButton guiButton_searchNewCode;
	private JTextArea guiTextArea_pathNew;

	private JTextArea guiTextArea_pathStateMachine;
	private JButton guiButton_searchStateMachine;

	private JFileChooser fileChooserProjects;
	private JFileChooser fileChooserStateMachine;
	private JFileChooser fileChooserGeneratedPDF;
	private JFileChooser fileChooserGeneratedXML;

	private JButton guiButton_runTech;
	private JButton guiButton_cleanFields;

	private JButton guiButton_generatePDF;
	private JButton guiButton_generateXML;
	private JTextArea guiTextArea_mainOutput;

	private final Border BLACK_BORDER = BorderFactory.createLineBorder(Color.black);

	private ChangeFSM changeFSM;

	public JPanelChangeFSM() {

		guiPanel = new JPanel(new BorderLayout());

		fileChooserProjects = new JFileChooser();
		fileChooserProjects.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		fileChooserGeneratedPDF = new JFileChooser();
		fileChooserGeneratedPDF.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		fileChooserGeneratedXML = new JFileChooser();
		fileChooserGeneratedXML.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		fileChooserStateMachine = new JFileChooser();
		fileChooserStateMachine.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooserStateMachine.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
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

		guiPanel.add(panels);
	}

	private JPanel panelTopicCode() {
		JPanel panelTopicCode = new JPanel(new BorderLayout());
		panelTopicCode.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add a pseudo padding

		JLabel guiTopic = new JLabel("Code Project");
		guiTopic.setHorizontalAlignment(SwingConstants.CENTER);
		guiTopic.setFont(new Font("Arial", Font.BOLD, 20));

		panelTopicCode.add(guiTopic);
		return panelTopicCode;
	}

	private JPanel panelSearchNewProject() {

		JPanel panelNewProject = new JPanel(new BorderLayout());
		panelNewProject.setBorder(new EmptyBorder(0, 5, 10, 5));

		JLabel guiLabel_newProject = new JLabel("New Project: ");

		guiTextArea_pathNew = new JTextArea(1, 20);
		guiTextArea_pathNew.setEditable(false);
		guiTextArea_pathNew.setBorder(BLACK_BORDER);

		guiButton_searchNewCode = new JButton("Search");
		guiButton_searchNewCode.addActionListener(this);

		panelNewProject.add(guiLabel_newProject, BorderLayout.LINE_START);
		panelNewProject.add(guiTextArea_pathNew, BorderLayout.CENTER);
		panelNewProject.add(guiButton_searchNewCode, BorderLayout.LINE_END);

		return panelNewProject;
	}

	private JPanel panelSearchOldProject() {
		JPanel panelOldProject = new JPanel(new BorderLayout());
		panelOldProject.setBorder(new EmptyBorder(0, 5, 10, 5));

		JLabel guiLabel_oldProject = new JLabel("Old Project:   ");

		guiTextArea_pathOld = new JTextArea(1, 30);
		guiTextArea_pathOld.setEditable(false);
		guiTextArea_pathOld.setBorder(BLACK_BORDER);

		guiButton_searchOldCode = new JButton("Search");
		guiButton_searchOldCode.addActionListener(this);

		panelOldProject.add(guiLabel_oldProject, BorderLayout.LINE_START);
		panelOldProject.add(guiTextArea_pathOld, BorderLayout.CENTER);
		panelOldProject.add(guiButton_searchOldCode, BorderLayout.LINE_END);

		return panelOldProject;
	}

	private JPanel panelTopicStateMachine() {
		JPanel panelTopicStateMachine = new JPanel(new BorderLayout());
		panelTopicStateMachine.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add a pseudo padding

		JLabel guiTopic = new JLabel("State Machine");
		guiTopic.setHorizontalAlignment(SwingConstants.CENTER);
		guiTopic.setFont(new Font("Arial", Font.BOLD, 30));

		panelTopicStateMachine.add(guiTopic);
		return panelTopicStateMachine;
	}

	private JPanel panelSearchStateMachine() {
		JPanel panelSearchStateMachine = new JPanel(new BorderLayout());
		panelSearchStateMachine.setBorder(new EmptyBorder(0, 5, 10, 5));

		JLabel guiLabel_StateMachine = new JLabel("State Machine (XML): ");

		guiTextArea_pathStateMachine = new JTextArea(1, 30);
		guiTextArea_pathStateMachine.setBorder(BLACK_BORDER);
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
		guiButton_cleanFields.setEnabled(false);
		guiButton_runTech = new JButton("Run");
		guiButton_runTech.addActionListener(this);
		guiButton_runTech.setEnabled(false);

		panelRunAndClean.add(guiButton_runTech);
		panelRunAndClean.add(guiButton_cleanFields);

		return panelRunAndClean;
	}

	private JPanel panelOutput() {
		JPanel panelOptionOutput = new JPanel();
		panelOptionOutput.setLayout(new BoxLayout(panelOptionOutput, BoxLayout.Y_AXIS));

		JPanel panelOutPut = new JPanel();
		panelOutPut.setBackground(Color.WHITE);
		guiTextArea_mainOutput = new JTextArea(3, 1);
		guiTextArea_mainOutput.setFont(new Font("Arial", Font.BOLD, 16));
		guiTextArea_mainOutput.setEditable(true);
		panelOutPut.add(guiTextArea_mainOutput);

		JPanel panelButtons = new JPanel();
		guiButton_generatePDF = new JButton("PDF");
		guiButton_generatePDF.addActionListener(this);
		guiButton_generateXML = new JButton("XML");
		guiButton_generateXML.addActionListener(this);
		guiButton_generatePDF.setEnabled(false);
		guiButton_generateXML.setEnabled(false);
		panelButtons.add(guiButton_generatePDF);
		panelButtons.add(guiButton_generateXML);
		panelOptionOutput.add(panelOutPut);
		panelOptionOutput.add(panelButtons);

		return panelOptionOutput;
	}

	private void sendMessage(Color color, String message) {
		guiTextArea_mainOutput.setForeground(color);
		guiTextArea_mainOutput.setText(message);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(guiButton_searchNewCode)) {
			int selectedOption = fileChooserProjects.showOpenDialog(JPanelChangeFSM.this);

			if (selectedOption == JFileChooser.APPROVE_OPTION) { // ------ BUTTON SEARCH NEW PATH(CODE)
				guiTextArea_pathNew.setText("");
				guiTextArea_pathNew.setText(fileChooserProjects.getSelectedFile().getAbsolutePath());
				if (!guiTextArea_pathNew.getText().isEmpty() && !guiTextArea_pathOld.getText().isEmpty()
						&& !guiTextArea_pathStateMachine.getText().isEmpty()) {
					guiButton_runTech.setEnabled(true);
				}
				guiButton_cleanFields.setEnabled(true);
				guiTextArea_mainOutput.setText("");
			}
		} else if (e.getSource().equals(guiButton_searchOldCode)) { // ------ BUTTON SEARCH OLD PATH(CODE)

			int selectedOption = fileChooserProjects.showOpenDialog(JPanelChangeFSM.this);

			if (selectedOption == JFileChooser.APPROVE_OPTION) {
				guiTextArea_pathOld.setText("");
				guiTextArea_pathOld.setText(fileChooserProjects.getSelectedFile().getAbsolutePath());
				if (!guiTextArea_pathNew.getText().isEmpty() && !guiTextArea_pathOld.getText().isEmpty()
						&& !guiTextArea_pathStateMachine.getText().isEmpty()) {
					guiButton_runTech.setEnabled(true);
				}
				guiButton_cleanFields.setEnabled(true);
				guiTextArea_mainOutput.setText("");

			}
		} else if (e.getSource().equals(guiButton_searchStateMachine)) { // ------ BUTTON SEARCH SM FILE (XML)

			int selectedOption = fileChooserStateMachine.showOpenDialog(JPanelChangeFSM.this);

			if (selectedOption == JFileChooser.APPROVE_OPTION) {
				guiTextArea_pathStateMachine.setText("");
				guiTextArea_pathStateMachine.setText(fileChooserStateMachine.getSelectedFile().getPath());
				if (!guiTextArea_pathNew.getText().isEmpty() && !guiTextArea_pathOld.getText().isEmpty()
						&& !guiTextArea_pathStateMachine.getText().isEmpty()) {
					guiButton_runTech.setEnabled(true);
				}
				guiButton_cleanFields.setEnabled(true);
				guiTextArea_mainOutput.setText("");
			}
		} else if (e.getSource().equals(guiButton_cleanFields)) { // ------ BUTTON CLEAN FIELDS
			guiTextArea_pathNew.setText("");
			guiTextArea_pathOld.setText("");
			guiTextArea_pathStateMachine.setText("");
			guiButton_runTech.setEnabled(false);
			guiButton_generatePDF.setEnabled(false);
			guiButton_generateXML.setEnabled(false);
			guiButton_cleanFields.setEnabled(false);
			changeFSM = new ChangeFSM();
			sendMessage(Color.orange, "\nCleaned Fields!");

		} else if (e.getSource().equals(guiButton_runTech)) { // ------ BUTTON RUN TECHNIQUE

			// TO-DO CALL TECHNIQUES
			if (!guiTextArea_pathNew.getText().isEmpty() && !guiTextArea_pathOld.getText().isEmpty()
					&& !guiTextArea_pathStateMachine.getText().isEmpty()) {
				try {
					changeFSM = new ChangeFSM();
					String pathOld = guiTextArea_pathOld.getText() + "\\"; // Because is a directory so need add <\>
					String pathNew = guiTextArea_pathNew.getText() + "\\";
					String pathSM = guiTextArea_pathStateMachine.getText();
					changeFSM.identifyAndClassifyUpdateSM(pathOld, pathNew, pathSM);
					sendMessage(Color.GREEN, "\nSuccess"); // <\n> is used to centralize the text
					guiButton_generatePDF.setEnabled(true);
					guiButton_generateXML.setEnabled(true);
				} catch (ChangeFSMException cfe) {
					sendMessage(Color.RED, cfe.getMessage());
				} catch (Exception excep) {
					sendMessage(Color.RED, "UNEXPECTED ERROR!");
				} 

			}
		} else if (e.getSource().equals(guiButton_generatePDF)) { // ------ BUTTON GENERATE PDF

			int selectedOption = fileChooserGeneratedPDF.showOpenDialog(JPanelChangeFSM.this);
			if (selectedOption == JFileChooser.APPROVE_OPTION) {
				try {
					changeFSM.generatePDF(fileChooserGeneratedPDF.getSelectedFile().getPath() + "\\");
					sendMessage(Color.BLUE, "\nPDF Generated Successfully");
				} catch (DocumentException e1) {
					sendMessage(Color.RED, "ERROR during creation of the PDF file!");
				} catch( FileNotFoundException fileExcep ) {
					sendMessage(Color.RED, "This file is open. Please close it!");
				}
			}

		} else if (e.getSource().equals(guiButton_generateXML)) { // ------ BUTTON GENERATE XML

			int selectedOption = fileChooserGeneratedXML.showOpenDialog(JPanelChangeFSM.this);
			if (selectedOption == JFileChooser.APPROVE_OPTION) {
				try {
					changeFSM.generateXML(fileChooserGeneratedXML.getSelectedFile().getPath() + "\\");
					sendMessage(Color.BLUE, "\nXML Generated Successfully");
				} catch (JAXBException e1) {
					System.err.println(e1.getMessage());
					sendMessage(Color.RED, "ERROR during creation of the XML file!");
				}
			}
		}
	}

	public JPanel getPanel() {
		return guiPanel;
	}

	public void setPanel(JPanel panel) {
		this.guiPanel = panel;
	}

}
