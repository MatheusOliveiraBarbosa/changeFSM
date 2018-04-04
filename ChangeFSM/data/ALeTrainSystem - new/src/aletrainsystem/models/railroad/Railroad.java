package aletrainsystem.models.railroad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aletrainsystem.enums.PointConnectorEnum;
import aletrainsystem.models.RailComponentId;
import aletrainsystem.models.locking.Lockable;
import aletrainsystem.models.navigation.RouteElement;
import aletrainsystem.pointswitch.Point;
import aletrainsystem.pointswitch.PointConnector;

public class Railroad implements IRailroad {

	private Map<String, Point> points;
	private Map<String, RegularLeg> legs;
	private Set<PointConnector> connectedPointConnectors;
	private StartLeg railSystemEntryPoint;

	protected Railroad(){
		points = new HashMap<>();
		legs = new HashMap<>();
		connectedPointConnectors = new HashSet<>();
	}
	
	@Override
	public Lockable getLockableResource(String id) {
		Lockable lockable = null;
		if (id.contains(".")) {
			lockable = legs.get(id);
			if (lockable == null && id.equals(railSystemEntryPoint.id())) {
				lockable = railSystemEntryPoint;
			}
		}
		else
			lockable = points.get(id);
		
		return lockable;
	}
	
	public List<Lockable> getReservedResources() {
		List<Lockable> resources = new ArrayList<Lockable>();
		for (Lockable lockable : points.values()) {
			if (lockable.checkReservation() != null)
				resources.add(lockable);
		}
		
		return resources;
	}
	
	public List<Lockable> getLockedResources() {
		List<Lockable> resources = new ArrayList<Lockable>();
		for (Lockable lockable : points.values()) {
			if (lockable.checkLock() != null)
				resources.add(lockable);
		}
		
		return resources;
	}

	Map<String, RegularLeg> getRailLegs(){
		return legs;
	}
	
	@Override
	public RouteElement getRouteElement(String id) {
		if (legs.containsKey(id)) {
			return legs.get(id);
		}
		else if (railSystemEntryPoint.id().equals(id)) {
			return railSystemEntryPoint;
		}
		
		String pointId = id.substring(0, id.length()-2);
		String connector = id.substring(id.length()-2, id.length()-1);
		if (points.containsKey(pointId)) {
			return points.get(pointId).getConnector(PointConnectorEnum.getConnectorFromShortHand(connector));
		}
		
		return null;
	}

	Map<String, Point> getPointSwitches(){
		return points;
	}

	protected void addPointSwitch(Point pointSwitch) {
		points.put(pointSwitch.id().toString(), pointSwitch);
	}

	protected void addRailLeg(RegularLeg railLeg){
		legs.put(railLeg.id().toString(), railLeg);
		railLeg.getConnectors().forEach(c -> connectedPointConnectors.add(c));
	}

	protected void setRailSystemStartLeg(StartLeg railSystemEntryPoint) {
		this.railSystemEntryPoint = railSystemEntryPoint;
	}

	public StartLeg getRailSystemStartLeg() {
		return railSystemEntryPoint;
	}

	public boolean isStation(RailLegId railLegId) {
		return isStation(railLegId.value());
	}

	public boolean isStation(String railLegId) {
		return isStation(findRailLeg(railLegId));
	}

	public boolean isStation(RegularLeg railLeg){
		if (railLeg == null || !legs.containsKey((railLeg.id().toString()))){
			return false;
		}

		ConnectorPair connectors = railLeg.getConnectors();
		if (connectors.bothOfType(PointConnectorEnum.DIVERT)){
			RailLegId parallelRailLegId = new RailLegId(
					connectors.first().point().getConnector(PointConnectorEnum.THROUGH), 
					connectors.second().point().getConnector(PointConnectorEnum.THROUGH));
			if (findRailLeg(parallelRailLegId) != null){
				return true;
			}
		}
		else if (connectors.bothOfType(PointConnectorEnum.THROUGH)) {
			RailLegId parallelRailLegId = new RailLegId(
					connectors.first().point().getConnector(PointConnectorEnum.DIVERT), 
					connectors.second().point().getConnector(PointConnectorEnum.DIVERT));
			if (findRailLeg(parallelRailLegId) != null){
				return true;
			}
		}

		return false;
	}

	public RegularLeg findRailLeg(String railLegId) {
		return legs.get(railLegId);
	}

	public RegularLeg findRailLeg(RailLegId railLegId){
		return findRailLeg(railLegId.value());
	}

	protected boolean hasRailLegWithConnector(PointConnector connector) {
		return connectedPointConnectors.contains(connector);
	}

	public Point findPoint(String pointId) {
		return points.get(pointId);
	}

	public Point findPoint(RailComponentId pointId) {
		return points.get(pointId.toString());
	}

	public Point findOrAddPoint(String pointId) {
		Point result = points.get(pointId);
		if (result == null) {
			result = new Point(new RailComponentId(pointId));
			points.put(pointId, result);
		}
		return result;
	}
}
