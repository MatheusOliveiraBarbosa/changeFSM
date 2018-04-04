package aletrainsystem.models.railroad;

import java.util.ArrayList;

import aletrainsystem.enums.TrackStatus;
import aletrainsystem.models.Navigation.RouteElement;
import aletrainsystem.models.railroad.PointSwitchConnector;

public class RailLeg extends RouteElement {
	
	private ConnectorPair connectors;
	private RailLegId trackId;
	private ArrayList<RailBrick> railBricks;
	private TrackStatus status;
	
	public RailLeg() {
		status = TrackStatus.UNDER_CONSTRUCTION; 
	}
		
	public RailLeg(PointSwitchConnector connector1, PointSwitchConnector connector2){
		addConnectors(connector1, connector2);
		trackId = new RailLegId(connector1, connector2);
		status = TrackStatus.NORMAL;
	}
	
	public RailLegId getId() {
		return trackId;
	}
		
	public ConnectorPair getConnectors(){
		return connectors;
	}
	
	private void addConnectors(PointSwitchConnector connector1, PointSwitchConnector connector2){
		connectors = new ConnectorPair(connector1, connector2);
		if (connector1 != null) {
			connector1.setConnectedRailLeg(this);
		}
		
		if (connector2 != null) {
			connector2.setConnectedRailLeg(this);
		}
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
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RailLeg)) {
			return false;
		}
		
		RailLeg track = (RailLeg) other;
		
		return (trackId.equals(track.trackId) && length() == track.length());
	}

	public TrackStatus getStatus() {
		return status;
	}

	public void setStatus(TrackStatus status) {
		this.status = status;
	}

	public void addRailPiece(RailBrick railBrick) {
		railBricks.add(railBrick);
	}
	
	public RailComponent getNextComponent(RailComponent previous, PointSwitch direction) {
		int previousIndex = railBricks.indexOf(previous);
		int relativeDirection = getRelativeDirection(direction);
		int nextIndex = previousIndex + relativeDirection;
		if (nextIndex >= 0 && nextIndex < railBricks.size()) {
			return railBricks.get(nextIndex);			
		}
		else if (relativeDirection > 0) {
			return connectors.second().getPointSwitch();
		}
		else if (relativeDirection < 0) {
			return connectors.first().getPointSwitch();
		}
		
		throw new IllegalArgumentException("Could not relate to PointSwitch with Id " + direction.toString());
	}
	
	public PointSwitchConnector getConnector(PointSwitch pointSwitch) {
		if (connectors.first().getPointSwitch().equals(pointSwitch)) {
			return connectors.first();
		}
		else if (connectors.second().getPointSwitch().equals(pointSwitch)) {
			return connectors.second();
		}
		return null;
	}
	
	@Override
	public RouteElement[] getNext(RouteElement previous) {
		if (previous != connectors.first()) {
			return new RouteElement[] {connectors.second()};
		}
		else if (previous != connectors.second()) {
			return new RouteElement[] {connectors.first()};
		}
		else
			return null;
	}
	
	private int getRelativeDirection(PointSwitch direction){
		if (connectors.first().getPointSwitch().equals(direction)){
			return -1;
		}
		else if (connectors.second().getPointSwitch().equals(direction)){
			return 1;
		}
		
		return 0;
	}

	public PointSwitchConnector getOppositeConnector(PointSwitchConnector connector){
		return getOppositeConnector(connector.getPointSwitch());
	}
	
	public PointSwitchConnector getOppositeConnector(PointSwitch pointSwitch) {
		if (pointSwitch == connectors.first().getPointSwitch()) {
			return connectors.second();
		}
		else if (pointSwitch == connectors.second().getPointSwitch()) {
			return connectors.first();
		}
		return null;
	}
}
