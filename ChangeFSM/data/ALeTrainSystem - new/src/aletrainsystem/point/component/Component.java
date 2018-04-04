package aletrainsystem.point.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rabbitmq.client.impl.AMQBasicProperties;

import aletrainsystem.enums.MotorPort;
import aletrainsystem.models.messaging.TerminationMessage;
import no.ntnu.item.arctis.runtime.Block;
import ntnu.no.rabbitamqp.util.AMQPProperties;

public class Component extends Block {

	public void logAndThrow(String errorMessage) {
		logger.error(errorMessage);
	}

	public HashMap<AMQPProperties, String> prepareCommParams() {
		HashMap<AMQPProperties, String> params = new HashMap<>();
		
		params.put(AMQPProperties.USERNAME, (String) getProperty("USERNAME"));
		params.put(AMQPProperties.PASSWORD, (String) getProperty("PASSWORD"));
		params.put(AMQPProperties.HOSTNAME, (String) getProperty("HOSTNAME"));
		params.put(AMQPProperties.EXCHANGENAME, (String) getProperty("EXCHANGE_NAME"));
		
		return params;
	}

	public List<String> getTopics() {
		List<String> topics = new ArrayList<String>();
		topics.add("common.#");
		for (MotorPort motorPort : MotorPort.values()) {
			if (this.hasProperty(motorPort.getPropertyName())){
				String pointId = (String) this.getProperty(motorPort.getPropertyName());
				if (Integer.valueOf(pointId) != 0) {
					topics.add("points.".concat(pointId).concat(".#"));
				}
			}
		}
		
		logger.info("Subscribing to ".concat(topics.toString()));
		return topics;
	}

	public void logWarning(String e) {
		logger.warn(e);
	}

	public void receivedTermination(TerminationMessage t) {
		logger.info("Received termination message from " + t.getSender() + ", halting system NOW");
	}

}
