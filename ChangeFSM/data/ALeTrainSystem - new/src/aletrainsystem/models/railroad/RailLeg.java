package aletrainsystem.models.railroad;

import java.util.ArrayList;

import aletrainsystem.models.TrainId;
import aletrainsystem.models.locking.Lockable;
import aletrainsystem.models.navigation.RouteElement;
import aletrainsystem.pointswitch.PointConnector;

public abstract class RailLeg extends RouteElement implements Lockable {
	
	protected ArrayList<RailBrick> railBricks;
	
	protected RailLeg() {
		railBricks = new ArrayList<>();
	}
	
	public int length(){
		return railBricks.size();
	}
	
	public int getSleepersCount(){
		int sleepers = 0;
		for (RailBrick railBrick : railBricks) {
			sleepers += railBrick.sleepers();
		}
		
		return sleepers;
	}
	
	public void addRailBrick(RailBrick railBrick) {
		railBricks.add(railBrick);
	}
	
	public abstract RailComponent getNextComponent(RailComponent previous, PointConnector direction);
	
	public abstract PointConnector getOppositeConnector(PointConnector connector);
	
	private TrainId lockedBy = null;
	private TrainId reservedBy = null;
	
	@Override
	public TrainId checkLock() {
		return lockedBy;
	}
		
	@Override
	public TrainId checkReservation() {
		return reservedBy;
	}
	
	@Override
	public void reserveLock(TrainId owner) {
		if (reservedBy == null)
			reservedBy = owner;
	}
	
	@Override
	public void releaseReservation() {
		reservedBy = null;		
	}
	
	@Override
	public void performLock(TrainId owner) {
		if (reservedBy == null || reservedBy.equals(owner))
			lockedBy = owner;		
	}
	
	@Override
	public void unLock() {
		reservedBy = null;
		lockedBy = null;
	}
	
	@Override
	public Lockable getLockableResource() {
		return this;
	}
	
	public String toString() {
		return id().toString();
	}
}
