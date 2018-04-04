package aletrainsystem.trainhardware;

import aletrainsystem.enums.SpeedLevel;
import no.ntnu.item.arctis.runtime.Block;

public class TrainHardware extends Block {

	public SpeedLevel speedLevel;
	
	public boolean isMoving() {
		return speedLevel != SpeedLevel.STOPPED;
	}

}
