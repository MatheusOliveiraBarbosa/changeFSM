package aletrainsystem.models.locking;

import java.util.Set;

import aletrainsystem.models.TrainId;

public class LockHandlerInitParams extends Pair<TrainId, Set<TrainId>> {

	public LockHandlerInitParams(TrainId id, Set<TrainId> participants) {
		super(id, participants);
	}
	
	public TrainId id() {
		return first();
	}
	
	public Set<TrainId> participants() {
		return second();
	}

}
