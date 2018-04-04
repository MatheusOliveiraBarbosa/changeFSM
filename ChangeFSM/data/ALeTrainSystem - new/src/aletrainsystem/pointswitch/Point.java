package aletrainsystem.pointswitch;

import java.util.HashMap;
import java.util.Map;

import aletrainsystem.enums.PointConnectorEnum;
import aletrainsystem.models.RailComponentId;
import aletrainsystem.models.TrainId;
import aletrainsystem.models.locking.Lockable;

public class Point implements Lockable {
	private Map<PointConnectorEnum, PointConnector> connectors;
	private RailComponentId id;
	
	public Point(RailComponentId id){
		this.id = id;
		connectors = new HashMap<PointConnectorEnum, PointConnector>();
		for (PointConnectorEnum connectorType : PointConnectorEnum.values()) {
			addConnector(new PointConnector(this, connectorType));
		}
	}
	
	public RailComponentId id(){
		return id;
	}
	
	
	public Map<PointConnectorEnum, PointConnector> getConnectors() {
		return connectors;
	}
	
	public PointConnector getConnector(PointConnectorEnum type){
		return connectors.get(type);
	}

	public void addConnector(PointConnector connector) {
		connectors.put(connector.getType(), connector);
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Point) {
			Point other = (Point) arg0;
			if (id.equals(other.id()))
				return true;
		}
		
		return false;
	}
	
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
	public String toString() {
		return id().toString();
	}
}
