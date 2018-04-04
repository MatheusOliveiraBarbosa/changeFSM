package aletrainsystem.models.Navigation;

import aletrainsystem.models.TrainId;

public abstract class RouteElement {
	
	private TrainId lockedBy = null;
	
	public abstract RouteElement[] getNext(RouteElement previous);
	public abstract int length();
	
	public TrainId releaseLock() {
		TrainId released = lockedBy;
		lockedBy = null;
		return released;
	}
	
	public TrainId checkLock() {
		return new TrainId(lockedBy.getValue());
	}
	
	public boolean setLock(TrainId trainId) {
		if (lockedBy == null) {
			lockedBy = trainId;
			return true;
		}
		
		return false;
	}
	
	public boolean isLocked() {
		return lockedBy != null;
	}
}
