package aletrainsystem.routecontroller;

import aletrainsystem.algorithms.GreedyAlgorithm;
import aletrainsystem.algorithms.ShortestPathUniDirectional;
import aletrainsystem.enums.SleeperColor;
import aletrainsystem.models.RailComponentId;
import aletrainsystem.models.Navigation.RouteElement;
import aletrainsystem.models.Navigation.Position;
import aletrainsystem.models.Navigation.Route;
import aletrainsystem.models.railroad.PointSwitch;
import aletrainsystem.models.railroad.RailBrick;
import aletrainsystem.models.railroad.Railroad;
import no.ntnu.item.arctis.runtime.Block;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RouteController extends Block {

	public Railroad railroad;
	public RouteElement nextDestination;
	private Position currentPosition;
	private PointSwitch direction;
	private int sleeperCount;

	public void initialize(Railroad r) {
	}

	public Route findRoute(RouteElement destination) {
		ShortestPathUniDirectional algorithm = new GreedyAlgorithm();
		return algorithm.findSingleShortestPath(railroad, currentPosition, destination, direction);
	}
	
	public void updatePosition(SleeperColor s) {
		if (s != SleeperColor.regularSleeper()) {
			// Do stuff, new signal color detected
		}
		else {
			sleeperCount++;
			RailBrick brickInFront = (RailBrick)currentPosition.head();
			if (sleeperCount >= brickInFront.sleepers()) {
				currentPosition.moveInDirection(direction);
			}
			
		}
	}
	
	public boolean isCrossingPointSwitch() {
		return currentPosition.headIsInPointSwitch();
	}

}
