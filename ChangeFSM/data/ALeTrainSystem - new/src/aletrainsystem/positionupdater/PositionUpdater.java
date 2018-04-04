package aletrainsystem.positionupdater;

import java.util.ArrayList;

import aletrainsystem.enums.PointConnectorEnum;
import aletrainsystem.mapcontroller.MapController;
import aletrainsystem.models.navigation.RouteElement;
import aletrainsystem.models.railroad.RailBrick;
import aletrainsystem.models.railroad.RailComponent;
import aletrainsystem.models.railroad.RailLeg;
import aletrainsystem.pointswitch.PointConnector;
import no.ntnu.item.arctis.runtime.Block;

public class PositionUpdater extends Block {

	public MapController parent;
	public aletrainsystem.models.railroad.RailComponent passedComponent;
	public java.util.ArrayList<aletrainsystem.models.navigation.RouteElement> passedElements;
	public aletrainsystem.enums.PointConnectorEnum connector;
	
	public RailLeg getCurrentLeg() {
		RailBrick brick = null;
		if (!parent.position.headIsInPointSwitch()) {
			 brick = (RailBrick)parent.position.head();
		}
		else {
			brick = (RailBrick)parent.position.getPreviousBrick();
		}
		
		logger.info("Now at ".concat(brick.parentLeg().toString()));		
		return brick.parentLeg();
	}
	
	public boolean lastElementOnSubRoute(RailComponent component) {
		if (parent.currentRoute == null) {
			return false;
		}
		
		RouteElement element = null;
		
		if (component instanceof PointConnector) {
			PointConnector connector = (PointConnector) component;
			element = connector;
		}
		else if (component instanceof RailBrick) {
			RailBrick brick = (RailBrick) component;
			element = brick.parentLeg();
		}
		
		return element.equals(parent.currentRoute.getLastElement());
	}
	
	public RailComponent jumpToNext(PointConnectorEnum connectorType) {
		RailComponent lastPopped = null;
		
		while (!parent.position.headIsInPointSwitch()) {
			lastPopped = parent.position.moveInDirection(parent.direction);
		}
		
//		if (parent.currentRoute != null) {
//			parent.direction = (PointConnector)parent.currentRoute.getNextEndOfLeg();
//		}
//		else {
//			parent.direction = ((PointConnector)getHead()).point().getConnector(PointConnectorEnum.ENTRY);
//		}
		
		return lastPopped;
	}
	
	public RailBrick castToRailBrick(RailComponent component) {
		if (component instanceof RailBrick) {
			RailBrick brick = (RailBrick) component;
			return brick;
		}
		return null;
	}
	
	public RailComponent movePosition() {
		return parent.position.moveInDirection(parent.direction);
	}
	
	public RailComponent getHead() {
		return parent.position.head();
	}
	
	public RouteElement hasPassedElement(RailComponent component) {
		RouteElement elementInScope = component.partOfElement();
		if (!parent.position.isTouchingLockable(elementInScope.getLockableResource()))
			return elementInScope;
		return null;
	}

	public void addToPassedElements(RouteElement element) {
		if (element != null)
			passedElements.add(element);
	}

	public void clearVariables() {
		passedComponent = null;
		if (passedElements == null)
			passedElements = new ArrayList<>();
		passedElements.clear();
	}
	
	public boolean hasPassedElements(){
		return passedElements.size() > 0;
	}
	
	public void logIfUnexpectedConnector(PointConnectorEnum connectorType) {
		if (!(parent.position.head() instanceof PointConnector)) {
			logger.warn(
					"Unexpected type! Expected '".
					concat(parent.position.head().toString()).
					concat("', actual '").
					concat(connectorType.toString()));
		}

		else {			
			PointConnector pointInFront = (PointConnector) parent.position.head();
			if (pointInFront.getType() != connectorType) {
				logger.warn(
						"Unexpected connector type! Expected '".
						concat(pointInFront.getType().toString()).
						concat("', actual '").
						concat(connectorType.toString()));
			}
		}
	}

	public void logInit() {
		logger.info("Initialized");
	}

	public void logUnLock() {
		if (passedElements.size() > 0) {
			logger.info("Unlocking ".concat(passedElements.toString()));			
		}
	}

	public void updateDirection() {
		parent.direction = parent.currentRoute.getNextDirection(parent.direction);
	}
	
	public boolean isHeadEqualToDirection() {
		return parent.direction == parent.position.head();
	}
	
	public boolean isNextComponentPointConnector() {
		return parent.position.lookAhead(parent.direction) instanceof PointConnector;
	}
}
