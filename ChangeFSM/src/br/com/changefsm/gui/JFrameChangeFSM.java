package br.com.changefsm.gui;

import javax.swing.JFrame;

public class JFrameChangeFSM {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String NAME_APP = "ChangeFSM";
	private static final String VERSION = "V0.1";
	
	private JFrameChangeFSM() {
	}
	
	public static void main(String[] args) {
		JPanelChangeFSM panelChangeFSM = new JPanelChangeFSM();
		JFrame frame = new JFrame(NAME_APP + " " + VERSION);
		frame.add(panelChangeFSM.getPanel());
		frame.setResizable(false);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.pack();
		frame.setVisible(true);
	}
	

}
