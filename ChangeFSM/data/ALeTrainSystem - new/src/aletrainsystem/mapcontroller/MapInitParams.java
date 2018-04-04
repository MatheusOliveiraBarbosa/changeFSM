package aletrainsystem.mapcontroller;

import aletrainsystem.models.TrainId;
import aletrainsystem.models.railroad.Railroad;

public class MapInitParams {

	private Railroad railroad;
	private Integer size;
	private TrainId id;

	public MapInitParams(Railroad railroad, Integer sizeOfParentObject, TrainId id) {
		this.railroad = railroad;
		this.size = sizeOfParentObject;
		this.id = id;
	}
	
	public Railroad railroad() {
		return railroad;
	}
	
	public int sizeOfParentObject(){
		return size;
	}
	
	public TrainId id() {
		return id;
	}
	
}
