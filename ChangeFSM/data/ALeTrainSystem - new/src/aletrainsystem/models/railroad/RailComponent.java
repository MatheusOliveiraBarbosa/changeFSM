package aletrainsystem.models.railroad;

import aletrainsystem.models.RailComponentId;
import aletrainsystem.models.navigation.RouteElement;
import aletrainsystem.pointswitch.PointConnector;

public interface RailComponent {
	public RailComponentId id();
	public RouteElement partOfElement();
	public RailComponent lookAhead(PointConnector direction);
}
