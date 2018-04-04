package aletrainsystem.models.Navigation;

import java.util.LinkedList;
import java.util.Iterator;

import aletrainsystem.models.RailComponentId;
import aletrainsystem.models.railroad.PointSwitch;
import aletrainsystem.models.railroad.PointSwitchConnector;
import aletrainsystem.models.railroad.RailBrick;
import aletrainsystem.models.railroad.RailComponent;
import aletrainsystem.models.railroad.RailLeg;

public class Position implements Iterable<RailComponent> {

	private LinkedList<RailComponent> parts;
	private int sizeOfParentObject;
	
	public Position(RailComponent[] arg0, int sizeOfParentObject) {
		parts = new LinkedList<RailComponent>();
		this.sizeOfParentObject = sizeOfParentObject;
		for (RailComponent railPart : arg0) {
			parts.add(railPart);
		}
	}
		
	public RailComponent head(){
		return parts.getFirst();
	}
	
	public RailBrick getPreviousBrick(){
		for (RailComponent railComponent : parts) {
			if (railComponent != head() && railComponent instanceof RailBrick)
				return (RailBrick) railComponent;
		}
		return null;
	}
	
	public void moveTo(RailComponent part) {
		parts.addLast(part);
		if (parts.size() > sizeOfParentObject) {
			parts.removeFirst();
		}
	}
	
	public void moveInDirection(PointSwitch direction) {
		if (head() instanceof RailBrick) {
			RailBrick frontBrick = (RailBrick)head();
			moveTo(frontBrick.parentLeg().getNextComponent(frontBrick, direction));
		}
	}
	
	public boolean headIsInPointSwitch(){
		return head() instanceof PointSwitch;
	}
	
	public void turnAround(){
		LinkedList<RailComponent> reversedParts = new LinkedList<>();
		for (RailComponent railComponent : parts) {
			reversedParts.addFirst(railComponent);
		}
		
		parts = reversedParts;
	}
	
	public PointSwitchConnector findPointSwitchConnector(){
		if (!headIsInPointSwitch()) {
			return null;
		}
		
		PointSwitch point = (PointSwitch)head();
		RailLeg previousLeg = ((RailBrick)parts.get(1)).parentLeg();
		return previousLeg.getConnector(point);
	}

	@Override
	public Iterator<RailComponent> iterator() {
		Iterator<RailComponent> ipart = parts.iterator();
		return ipart;
	}
}
