package aletrainsystem.traincontroller;

import aletrainsystem.models.TrainId;
import aletrainsystem.models.railroad.Railroad;
import aletrainsystem.models.railroad.RailroadBuilder;
import no.ntnu.item.arctis.runtime.Block;

public class TrainController extends Block {
	
	public aletrainsystem.models.TrainId id;
	public aletrainsystem.models.railroad.Railroad railroad;
	public void logAndThrow(String errorMessage) {
		logger.error(errorMessage);
	}
	
	
	public void setInitParamsFromProperties() {
		id = new TrainId((String) getProperty(TrainId.PROPERTYNAME));
		railroad = RailroadBuilder.build((String) getProperty(Railroad.PROPERTYNAME));
	}
}
