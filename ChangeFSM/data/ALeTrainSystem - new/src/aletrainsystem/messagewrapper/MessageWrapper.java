package aletrainsystem.messagewrapper;

import java.util.HashSet;
import java.util.Set;

import aletrainsystem.models.locking.Request;
import aletrainsystem.models.locking.Response;
import aletrainsystem.models.messaging.GreetingMessage;
import aletrainsystem.models.messaging.JoinMessage;
import aletrainsystem.models.messaging.PointSwitchOrder;
import aletrainsystem.models.messaging.TerminationMessage;
import no.ntnu.item.arctis.runtime.Block;
import ntnu.no.rabbitamqp.util.Message;

public class MessageWrapper extends Block {

	public java.util.Iterator<Message> messageIterator;

	public Message joinToAmqpMessage(JoinMessage join) {
		Message message = new Message("trains.common.".concat(join.getClass().getSimpleName()), join);
		return message;
	}

	public Message greetingToAmqpMessage(GreetingMessage greeting) {
		Message message = new Message("trains.".
							concat(greeting.entrant().toString()).
							concat(".").
							concat(greeting.getClass().getSimpleName()), greeting);
		return message;
	}
	
	public Message terminationToAmqpMessage(TerminationMessage termination) {
		Message message = new Message("common.".concat(termination.getClass().getSimpleName()), termination);
		return message;
	}

	public Message requestToAmqpMessage(Request request) {
		Message message = new Message("trains.common.".concat(request.getClass().getSimpleName()), request);
		return message;
	}

	public Message responseToAmqpMessage(Response response) {
		Message message = new Message("trains.".
								concat(response.collector().toString()).
								concat(".").
								concat(response.getClass().getSimpleName()), 
							response);
		return message;
	}

	public Set<Message> pointSwitchOrdersToAmqpMessages(Set<PointSwitchOrder> orders) {
		Set<Message> messages = new HashSet<>();
		for (PointSwitchOrder order : orders) {
			Message message = new Message("points.".
							concat(order.getPointId().toString()).
							concat(".").
							concat(order.getClass().getSimpleName()), 
					order);
			messages.add(message);
		}
		
		return messages;
	}

	public Message iterateMessages() {
		if (messageIterator.hasNext()) {
			Message next = messageIterator.next();
			return next;
		}
		
		return null;
	}

	public void saveIterator(Set<Message> messages) {
		messageIterator = messages.iterator();
	}

	public boolean iteratorHasNext() {
		return messageIterator.hasNext();
	}
}
