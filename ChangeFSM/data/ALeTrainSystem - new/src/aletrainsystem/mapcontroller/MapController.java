package aletrainsystem.mapcontroller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import aletrainsystem.enums.PointConnectorEnum;
import aletrainsystem.enums.SpeedLevel;
import aletrainsystem.models.TrainId;
import aletrainsystem.models.messaging.PointSwitchOrder;
import aletrainsystem.models.navigation.Position;
import aletrainsystem.models.navigation.Route;
import aletrainsystem.models.navigation.RouteElement;
import aletrainsystem.models.railroad.RailBrick;
//import aletrainsystem.models.railroad.RailComponent;
import aletrainsystem.models.railroad.StartLeg;
import aletrainsystem.pointswitch.PointConnector;
import no.ntnu.item.arctis.runtime.Block;

public class MapController extends Block {

	public TrainId id;
	public aletrainsystem.models.navigation.Position position;
	public aletrainsystem.models.navigation.Route currentRoute;
	public aletrainsystem.models.navigation.RouteElement currentRouteElement;
	public aletrainsystem.models.railroad.Railroad map;
	public aletrainsystem.pointswitch.PointConnector direction;

	public Position init(MapInitParams params) {
		map = params.railroad();
		id = params.id();
		StartLeg start = map.getRailSystemStartLeg();
		
		direction = start.getConnector().point().getConnector(PointConnectorEnum.ENTRY);  
//		position = new Position(new RailComponent[] {start.getConnector()}, params.sizeOfParentObject());
		position = new Position(start.getStartOfLeg(params.sizeOfParentObject()), params.sizeOfParentObject());
		
		logger.info("Initializing");
		return position;
	}

	public SpeedLevel slowCommand() {
		return SpeedLevel.LOW;
	}

	public SpeedLevel stopCommand() {
		return SpeedLevel.STOPPED;
	}

	public void initCurrentRouteElement(Route route) {
		logger.info("Waiting for lock on route ".concat(route.toString()));
		currentRouteElement = route.getFirstElement();
	}

	public boolean isRouteLocked() {
		for (RouteElement routeElement : currentRoute) {
			if (!id.equals(routeElement.getLockableResource().checkLock())) {
				return false;
			}
		}
		
		logger.info("Lock acquired for route ".concat(currentRoute.toString()));
		return true;
	}

	public SpeedLevel driveCommand() {
		return SpeedLevel.MEDIUM;
	}

	public void logLockingErr() {
		logger.error("Expected lock on " + currentRoute + " not successfull!");
	}

	public MapController getThisInstance() {
		return this;
	}

	public void logInit() {
		logger.info("Initialized");
	}

	public ArrayList<RouteElement> removeFromCurrentRoute(ArrayList<RouteElement> elements) {
		elements.forEach(e -> currentRoute.remove(e));
		currentRouteElement = currentRoute.getFirstElement();
		return elements;
	}

	public RailBrick nextbrick(RailBrick r) {
		return r;
	}

	public Set<PointSwitchOrder> generateOrdersFromRoute() {
		Set<PointSwitchOrder> orders = new HashSet<>();
		for (RouteElement routeElement : currentRoute) {
			if (routeElement instanceof PointConnector) {
				PointConnector connector = (PointConnector) routeElement;
				if (connector.getType() == PointConnectorEnum.DIVERT 
						|| connector.getType() == PointConnectorEnum.THROUGH) {
					orders.add(new PointSwitchOrder(connector.id(), connector.getType()));
				}
			}
		}
		logger.info("Generated orders: " + orders.toString());
		return orders;
	}
}
