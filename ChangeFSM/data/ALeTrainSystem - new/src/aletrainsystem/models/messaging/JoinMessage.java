package aletrainsystem.models.messaging;

import java.io.Serializable;

import aletrainsystem.models.TrainId;

public class JoinMessage implements Serializable {
	private TrainId id;
	
	public JoinMessage(TrainId id) {
		this.id = id;
	}
	
	public TrainId id() {
		return id;
	}
}
