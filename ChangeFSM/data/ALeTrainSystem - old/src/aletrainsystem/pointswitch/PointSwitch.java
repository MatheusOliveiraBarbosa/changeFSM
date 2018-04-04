package aletrainsystem.pointswitch;

import aletrainsystem.enums.MotorPort;
import aletrainsystem.enums.PointSwitchConnectorEnum;
import aletrainsystem.models.PointSwitchOrder;
import no.ntnu.item.arctis.runtime.Block;

public class PointSwitch extends Block {

	public aletrainsystem.enums.PointSwitchConnectorEnum finalState;
	public aletrainsystem.models.PointSwitchOrder currentOrder;
	public aletrainsystem.enums.PointSwitchConnectorEnum currentState;
	public aletrainsystem.enums.MotorPort motorPort;
	
	public static String getAlias(MotorPort port){
		return port.name();
	}
	
	public static String getAlias(PointSwitchOrder order){
		return order.getMotorPort().name();
	}
	
	public boolean isAlreadyInFinalState(){
		return finalState == currentState;
	}

	public PointSwitchConnectorEnum getState(PointSwitchOrder order) {
		return order.getSwitchState();
	}

	public void logInitialized() {
		logger.info(String.format("Initialized pointswitch on port %s", motorPort.name()));
	}
	
}
