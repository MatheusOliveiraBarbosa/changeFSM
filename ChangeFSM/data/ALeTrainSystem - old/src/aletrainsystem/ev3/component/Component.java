package aletrainsystem.ev3.component;

import aletrainsystem.enums.PointSwitchConnectorEnum;
import aletrainsystem.models.RailComponentId;
import aletrainsystem.models.PointSwitchOrder;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import no.ntnu.item.arctis.runtime.Block;

public class Component extends Block {
	
	public RightKey rightButtonListener;
	public LefttKey leftButtonListener;
	public DownKey downButtonListener;
	private int i, j;
	public lejos.hardware.Button lol;
	
	public Component(){
		i = 0; j = 0;
		rightButtonListener = new RightKey();
		lejos.hardware.Button.RIGHT.addKeyListener(rightButtonListener);
		leftButtonListener = new LefttKey();
		lejos.hardware.Button.LEFT.addKeyListener(leftButtonListener);
		downButtonListener = new DownKey();
		lejos.hardware.Button.DOWN.addKeyListener(downButtonListener);
	}

	public PointSwitchOrder newOrderLeft() {
		return new PointSwitchOrder(new RailComponentId(i++%4+1), PointSwitchConnectorEnum.DIVERT);
	}
	
	public PointSwitchOrder newOrderRight() {
		return new PointSwitchOrder(new RailComponentId(j++%4+1), PointSwitchConnectorEnum.THROUGH);
	}

	public void printDone(PointSwitchOrder order) {
	}
	
	
	public class RightKey implements KeyListener{

		@Override
		public void keyPressed(Key k) {
			sendToBlock("RIGHTBUTTONDOWN");			
		}

		@Override
		public void keyReleased(Key k) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class LefttKey implements KeyListener{

		@Override
		public void keyPressed(Key k) {
			sendToBlock("LEFTBUTTONDOWN");			
		}

		@Override
		public void keyReleased(Key k) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class DownKey implements KeyListener{

		@Override
		public void keyPressed(Key k) {
			sendToBlock("DOWNBUTTONDOWN");			
		}

		@Override
		public void keyReleased(Key k) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
