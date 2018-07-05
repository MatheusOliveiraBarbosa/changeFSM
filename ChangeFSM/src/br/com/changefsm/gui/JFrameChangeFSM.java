package br.com.changefsm.gui;

import javax.swing.JFrame;

public class JFrameChangeFSM {
	
	private static JFrameChangeFSM instance;
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private final String NAME_APP = "ChangeFSM";
	private final String VERSION = "V0.1";
	private JPanelChangeFSM panelChangeFSM;
	
	private JFrameChangeFSM() {
		this.panelChangeFSM = new JPanelChangeFSM();
		JFrame frame = new JFrame(NAME_APP + " " + VERSION);
		frame.add(this.panelChangeFSM.getPanel());
		frame.setResizable(false);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		JFrameChangeFSM frame = getInstance();
	}
	
	/**
	 * This method is responsible for implemented SINGLETON pattern,
	 * where verify if INSTANCE is initialized and return the same instance, 
	 * else create a new JFrameChangeFSM object and return;
	 * @return just only instance of this class
	 */
	public static JFrameChangeFSM getInstance() {
		return  (instance != null) ? instance :  ( instance = new JFrameChangeFSM() );	
	}

}
