package aletrainsystem.models.Navigation;

import java.util.LinkedList;

public class Route {
	protected LinkedList<RouteElement> viaPoints;
	
	public Route() {
		viaPoints = new LinkedList<>();
	}
	
	public Route(Route copyFrom) {
		viaPoints = (LinkedList<RouteElement>) copyFrom.viaPoints.clone();
	}
	
	public void add(RouteElement destination){
		viaPoints.addLast(destination);
	}
	
	public int componentLength() {
		return viaPoints.size();
	}
	
	public int brickLength() {
		int i = 0;
		for (RouteElement destination : viaPoints) {
			i += destination.length();
		}
		
		return i;
	}
}
