package aletrainsystem.models.railroad;

import aletrainsystem.models.RailComponentId;
import bluebrick4j.model.BrickType;

public class RailBrick implements RailComponent {
	
	private RailComponentId id;
	private final RailLeg parentLeg;
	private final int sleepers;
	
	public RailBrick(String id, RailLeg parent, BrickType brickType) {
		this.id = new RailComponentId(id);
		parentLeg = parent;
		if (brickType == BrickType.CURVED || brickType == BrickType.STRAIGHT) {
			this.sleepers = 4;
		}
		else {
			this.sleepers = 10000;
		}
	}
	
	public RailLeg parentLeg(){
		return this.parentLeg;
	}
	
	public int sleepers(){
		return sleepers;
	}

	@Override
	public RailComponentId id() {
		return id;
	}
}
