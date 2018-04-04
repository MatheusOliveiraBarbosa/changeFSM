package aletrainsystem.faketrainhardware;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import aletrainsystem.enums.SleeperColor;
import aletrainsystem.enums.SpeedLevel;
import no.ntnu.item.arctis.runtime.Block;

public class FakeTrainHardware extends Block {
	public SpeedLevel speedLevel;
	private JLabel speedLabel;
	
	public void initialize() {
		speedLevel = SpeedLevel.STOPPED;
		JFrame frame = new JFrame("Fake a sleeper!");
		frame.setSize(new Dimension(300, 400));
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(300, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(new JLabel("SpeedLevel:"));
		speedLabel = new JLabel(speedLevel.toString());
		panel.add("speed", speedLabel);
		for (SleeperColor sleeper : SleeperColor.values()) {
			JButton b = new JButton();
			switch (sleeper) {
			case BLACK:
				b.setBackground(Color.BLACK);
				break;
			case BLUE:
				b.setBackground(Color.BLUE);
				break;
			case BROWN:
				b.setBackground(Color.GRAY);
				break;
			case GREEN:
				b.setBackground(Color.GREEN);
				break;
			case NONE:
				break;
			case RED:
				b.setBackground(Color.RED);
				break;
			case WHITE:
				b.setBackground(Color.WHITE);
				break;
			case YELLOW:
				b.setBackground(Color.YELLOW);
				break;
			default:
				break;
			}
			b.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					sendToBlock("SLEEPER", sleeper);
				}
			});
			panel.add(sleeper.toString(), b);
		}
		
		panel.setLayout(new GridLayout(3, 4));
		
		frame.add(panel);
//		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		logger.info("Initialized");
	}
	
	public boolean isMoving() {
		return speedLevel != SpeedLevel.STOPPED;
	}

	public void setSpeedLevel(SpeedLevel level) {
		speedLevel = level;
		speedLabel.setText(level.toString());
	}
}
