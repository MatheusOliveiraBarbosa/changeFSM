package aletrainsystem.messageunwrapper;

import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.rabbitmq.client.Envelope;

import aletrainsystem.models.locking.Request;
import aletrainsystem.models.locking.Response;

import aletrainsystem.models.messaging.PointSwitchOrder;
import aletrainsystem.models.messaging.GreetingMessage;
import aletrainsystem.models.messaging.JoinMessage;
import aletrainsystem.models.messaging.TerminationMessage;

import no.ntnu.item.arctis.runtime.Block;
import ntnu.no.rabbitamqp.util.Message;

public class MessageUnwrapper extends Block {

	public java.lang.Object parsedMessage;
	public ntnu.no.rabbitamqp.util.Message message;

	public boolean isJoinMessage() {
		return parsedMessage instanceof JoinMessage;
	}

	public boolean isGreetingMessage() {
		return parsedMessage instanceof GreetingMessage;
	}

	public boolean isTerminationMessage() {
		return parsedMessage instanceof TerminationMessage;
	}

	public boolean isRequest() {
		return parsedMessage instanceof Request;
	}

	public boolean isResponse() {
		return parsedMessage instanceof Response;
	}

	public boolean isPointSwitchOrder() {
		return parsedMessage instanceof PointSwitchOrder;
	}

	public Object parseMessage(Message message) {
		String json = message.getJsonBody();
		String className;
		try {
			className = getClassNameFromEnvelope(message.getEnvelope());
			Gson g = new Gson();
			switch (className) {
			case "JoinMessage":
				parsedMessage = g.fromJson(json, JoinMessage.class);
				break;
			case "GreetingMessage":
				parsedMessage = g.fromJson(json, GreetingMessage.class);
				break;
			case "TerminationMessage":
				parsedMessage = g.fromJson(json, TerminationMessage.class);
				break;
			case "Request":
				parsedMessage = g.fromJson(json, Request.class);
				break;
			case "Response":
				parsedMessage = g.fromJson(json, Response.class);
				break;
			case "PointSwitchOrder":
				parsedMessage = g.fromJson(json, PointSwitchOrder.class);
				break;
			default:
				parsedMessage = null;
				break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("Could not get class name from envelope: " + message.getEnvelope().getRoutingKey().toString());
			e.printStackTrace();
		}

		return parsedMessage;
	}

	public JoinMessage castToJoinMessage(Object o) {
		return (JoinMessage)o;
	}

	public GreetingMessage castToGreetingMessage(Object o) {
		return (GreetingMessage)o;
	}

	public TerminationMessage castToTerminationMessage(Object o) {
		return (TerminationMessage)o;
	}

	public Request castToRequest(Object o) {
		return (Request)o;
	}

	public Response castToResponse(Object o) {
		return (Response)o;
	}

	public PointSwitchOrder castToPointSwitchOrder(Object o) {
		return (PointSwitchOrder)o;
	}

	private String getClassNameFromEnvelope(Envelope envelope) {
		String s = envelope.getRoutingKey();
		String[] a = s.split(Pattern.quote("."));
		return a[a.length-1];
	}

	public void logError() {
		logWarn("Could not interprete type for JSON: ".
				concat(message.getJsonBody()).
				concat(", should be of type ").
				concat(getClassNameFromEnvelope(message.getEnvelope())));
	}

}
