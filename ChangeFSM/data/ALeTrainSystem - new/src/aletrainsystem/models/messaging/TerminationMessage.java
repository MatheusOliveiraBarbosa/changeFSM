package aletrainsystem.models.messaging;

import java.io.Serializable;

public class TerminationMessage implements Serializable {
	private String sender;
	
	public TerminationMessage(String sender) {
		this.sender = sender;
	}
	
	public String getSender() {
		return sender;
	}
}
