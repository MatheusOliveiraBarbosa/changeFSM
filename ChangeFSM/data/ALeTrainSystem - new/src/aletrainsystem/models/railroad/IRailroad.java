package aletrainsystem.models.railroad;

import aletrainsystem.models.locking.Lockable;
import aletrainsystem.models.navigation.RouteElement;
import aletrainsystem.pointswitch.Point;

public interface IRailroad {
	public static final String PROPERTYNAME = "MAP_FILE";
	public static final String STATIONLISTKEY = "SERVED_STATIONS";
		
	public boolean isStation(String railLegId);
	
	public boolean isStation(RegularLeg railLeg);
	
	public RouteElement getRouteElement(String id);
	
	public Point findOrAddPoint(String railComponentId);
	
	public StartLeg getRailSystemStartLeg();
	
	public Lockable getLockableResource(String id);
}
