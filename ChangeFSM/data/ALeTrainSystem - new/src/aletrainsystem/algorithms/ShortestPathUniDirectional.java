package aletrainsystem.algorithms;

import aletrainsystem.models.navigation.Position;
import aletrainsystem.models.navigation.Route;
import aletrainsystem.models.navigation.RouteElement;
import aletrainsystem.models.railroad.IRailroad;
import aletrainsystem.pointswitch.PointConnector;

public interface ShortestPathUniDirectional {
	Route findSingleShortestPath(IRailroad railroad, Position position, RouteElement destination, PointConnector direction);
}
