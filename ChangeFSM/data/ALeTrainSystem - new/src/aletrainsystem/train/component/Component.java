package aletrainsystem.train.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aletrainsystem.models.TrainId;
import aletrainsystem.models.messaging.GreetingMessage;
import aletrainsystem.models.messaging.TerminationMessage;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import no.ntnu.item.arctis.runtime.Block;
import ntnu.no.rabbitamqp.util.AMQPProperties;

public class Component extends Block {

	public GreetingMessage newNullGreeting() {
		logger.info("Timed out while waiting for greeting message, assuming this is the first train to enter the system");
		return null;
	}

	public HashMap<AMQPProperties, String> getInitParams() {
		HashMap<AMQPProperties, String> params = new HashMap<>();
		
		params.put(AMQPProperties.USERNAME, (String) getProperty("USERNAME"));
		params.put(AMQPProperties.PASSWORD, (String) getProperty("PASSWORD"));
		params.put(AMQPProperties.HOSTNAME, (String) getProperty("HOSTNAME"));
		params.put(AMQPProperties.EXCHANGENAME, (String) getProperty("EXCHANGE_NAME"));
		
		return params;
	}

	public List<String> getTopics() {
		String trainId = (String) getProperty(TrainId.ID_KEY);
		
		ArrayList<String> topics = new ArrayList<>();
		topics.add("trains.common.#");
		topics.add("common.#");
		topics.add("trains.".concat(trainId).concat(".#"));
		
		logger.info("Subscribing to " + topics.toString());
		return topics;
	}

	public void logWarning(String e) {
		logWarn(e);
	}

	public void initEvent() {
		lejos.hardware.Button.ESCAPE.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(Key k) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(Key k) {
				sendToBlock("BACKBUTTONPRESSED");				
			}
		});;
	}
	
	public void receivedTermination(TerminationMessage t) {
		logger.info("Received termination message from " + t.getSender() + ", halting system NOW");
	}

}
