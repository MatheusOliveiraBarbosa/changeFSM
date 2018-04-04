package aletrainsystem.colorsensortest.component;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import aletrainsystem.enums.SleeperColor;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import no.ntnu.item.arctis.runtime.Block;

public class Component extends Block {
	
	public RightKey rightButtonListener;
	private ArrayList<String> log = new ArrayList<String>();
	
	public Component(){
		rightButtonListener = new RightKey();
		lejos.hardware.Button.RIGHT.addKeyListener(rightButtonListener);
	}
	
	public void appendTolog(SleeperColor sleeper) {
		log.add(System.currentTimeMillis() + ", " + sleeper.toString());
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

	public void writeLogToFile() {
		try {
			PrintWriter writer = new PrintWriter("colorsensorlog.txt", "UTF-8");
			for (String string : log) {
				writer.println(string);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void initColorReader() {
//		colorCount = new HashMap<Integer, Integer>();
//		colorSensor = new EV3ColorSensor(s1);
//		colorSensor.setFloodlight(6);
//		
//		Runnable readerThread = new Runnable() {			
//			public void run() {
//				int detectedColorID = colorSensor.getColorID();
//				readings++;
//				if (colorCount.containsKey(detectedColorID)) {
//					colorCount.put(detectedColorID, colorCount.get(detectedColorID)+1);			
//				}
//				else {
//					colorCount.put(detectedColorID, 1);
//				}
//				
//				if (colorCount.get(detectedColorID) > 5 && detectedColorID != previousDetectedColor) {
//					logger.info("Read color with ID " + detectedColorID);
//					previousDetectedColor = detectedColorID;
//					readings = 0;
//					colorCount.clear();		
//				}
//			}
//		};
//		
//		scheduler.scheduleAtFixedRate(readerThread, 5, 5, TimeUnit.MILLISECONDS);
//		logger.info("Initialized ColorReader");
//	}
	
//	public static void main(String[] args) {
//		Component c = new Component();
//		c.initColorReader();
//	}
}


