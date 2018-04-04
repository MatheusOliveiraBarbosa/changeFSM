package aletrainsystem.colorsensortest;

import java.util.HashMap;

import org.slf4j.Logger;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

public class ColorReader implements Runnable {
	
	EV3ColorSensor colorSensor;
	private Port s1 = LocalEV3.get().getPort("S1");
	private HashMap<Integer, Integer> colorCount;
	private int readings;
	private int previousDetectedColor;
	private Logger logger;
	
	public ColorReader(Logger logger) {
		this.logger = logger;
		colorCount = new HashMap<Integer, Integer>();
		colorSensor = new EV3ColorSensor(s1);
		colorSensor.setFloodlight(6);
	}
	
	@Override
	public void run() {
		int detectedColorID = colorSensor.getColorID();
		readings++;
		if (colorCount.containsKey(detectedColorID)) {
			colorCount.put(detectedColorID, colorCount.get(detectedColorID)+1);			
		}
		else {
			colorCount.put(detectedColorID, 1);
		}
		
		if (colorCount.get(detectedColorID) > 5 && detectedColorID != previousDetectedColor) {
			logger.info("Read color with ID " + detectedColorID);
			previousDetectedColor = detectedColorID;
			readings = 0;
			colorCount.clear();
		}
	}		
}