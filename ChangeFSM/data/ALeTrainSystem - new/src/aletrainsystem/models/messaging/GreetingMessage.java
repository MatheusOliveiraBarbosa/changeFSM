package aletrainsystem.models.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import aletrainsystem.models.TrainId;
import aletrainsystem.models.locking.Lockable;
import aletrainsystem.models.railroad.Railroad;

public class GreetingMessage {
	private TrainId entrant;
	private Set<TrainId> trainsInSystem;
	private Map<String, TrainId> reservedResources;
	private Map<String, TrainId> lockedResources;
	
	public GreetingMessage(TrainId entrant, Set<TrainId> trainsInSystem, Railroad latestMap) {
		this.trainsInSystem = trainsInSystem;

		populateResources(latestMap);
	}

	private void populateResources(Railroad latestMap) {
		populateLockedResources(latestMap);
		populateReservedResources(latestMap);		
	}

	private void populateLockedResources(Railroad latestMap) {
		lockedResources = new HashMap<>();
		for (Lockable lockedResource : latestMap.getLockedResources()) {
			lockedResources.put(lockedResource.id().toString(), lockedResource.checkReservation());
		}
	}

	private void populateReservedResources(Railroad latestMap) {
		reservedResources = new HashMap<>();
		for (Lockable reservedResource : latestMap.getReservedResources()) {
			if (!lockedResources.containsKey(reservedResource.id().toString())) {
				reservedResources.put(reservedResource.id().toString(), reservedResource.checkReservation());
			}
		}
	}
	
	public Map<String, TrainId> getReservedResources() {
		return reservedResources;
	}
	
	public Map<String, TrainId> getLockedResources() {
		return lockedResources;
	}
	
	public TrainId entrant() {
		return entrant;
	}
	
	public Set<TrainId> trainsInSystem() {
		return trainsInSystem;
	}
}
