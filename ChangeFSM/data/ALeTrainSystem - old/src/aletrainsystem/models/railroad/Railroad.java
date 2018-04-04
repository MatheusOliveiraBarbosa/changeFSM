package aletrainsystem.models.railroad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aletrainsystem.enums.PointSwitchConnectorEnum;
import aletrainsystem.models.RailComponentId;
import aletrainsystem.models.Navigation.RouteElement;
import aletrainsystem.models.Navigation.Route;

public class Railroad {
	
	public static final String PROPERTYNAME = "MAP_FILE";
	
	private Map<Long, PointSwitch> pointSwitches;
	private Map<String, RailLeg> railLegs;
	private Set<PointSwitchConnector> connectedPointSwitchConnectors;
	private PointSwitchConnector railSystemEntryPoint;
	
	protected Railroad(){
		pointSwitches = new HashMap<>();
		railLegs = new HashMap<>();
		connectedPointSwitchConnectors = new HashSet<>();
	}
	
	Map<String, RailLeg> getRailLegs(){
		return railLegs;
	}
	
	Map<Long, PointSwitch> getPointSwitches(){
		return pointSwitches;
	}
	
	protected void addPointSwitch(PointSwitch pointSwitch) {
		pointSwitches.put(pointSwitch.id().value(), pointSwitch);
	}
	
	protected void addRailLeg(RailLeg railLeg){
		railLegs.put(railLeg.getId().value(), railLeg);
		railLeg.getConnectors().forEach(c -> connectedPointSwitchConnectors.add(c));
	}
	
	protected void setRailSystemEntryPoint(PointSwitchConnector railSystemEntryPoint) {
		this.railSystemEntryPoint = railSystemEntryPoint;
	}
	
	public PointSwitchConnector getRailSystemEntryPoint() {
		return railSystemEntryPoint;
	}
	
	public boolean isStation(RailLegId railLegId) {
		return isStation(railLegId.value());
	}
	
	public boolean isStation(String railLegId) {
		return isStation(findRailLeg(railLegId));
	}
		
	public boolean isStation(RailLeg railLeg){
		if (railLeg == null || !railLegs.containsKey(railLeg.getId().value())){
			return false;
		}
		
		ConnectorPair connectors = railLeg.getConnectors();
		if (connectors.bothOfType(PointSwitchConnectorEnum.DIVERT)){
			RailLegId parallelRailLegId = new RailLegId(
					connectors.first().getPointSwitch().getConnector(PointSwitchConnectorEnum.THROUGH), 
					connectors.second().getPointSwitch().getConnector(PointSwitchConnectorEnum.THROUGH));
			if (findRailLeg(parallelRailLegId) != null){
				return true;
			}
		}
		
		return false;
	}
	
	public RailLeg findRailLeg(String railLegId) {
		return railLegs.get(railLegId);
	}
	
	public RailLeg findRailLeg(RailLegId railLegId){
		return findRailLeg(railLegId.value());
	}
	
	protected boolean hasRailLegWithConnector(PointSwitchConnector connector) {
		return connectedPointSwitchConnectors.contains(connector);
	}
	
	public PointSwitch findPointSwitch(String pointSwitchId) {
		return pointSwitches.get(pointSwitchId);
	}
	
	public PointSwitch findPointSwitch(RailComponentId pointSwitchId) {
		return pointSwitches.get(pointSwitchId.value());
	}
	
	public PointSwitch findOrAddPointSwitch(long pointSwitchId) {
		PointSwitch result = pointSwitches.get(pointSwitchId);
		if (result == null) {
			result = new PointSwitch(new RailComponentId(pointSwitchId));
			pointSwitches.put(pointSwitchId, result);
		}
		return result;
	}
}
