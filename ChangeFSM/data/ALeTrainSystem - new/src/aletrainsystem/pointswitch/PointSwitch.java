package aletrainsystem.pointswitch;

import aletrainsystem.enums.MotorPort;
import aletrainsystem.enums.PointConnectorEnum;
import aletrainsystem.models.messaging.PointSwitchOrder;
import no.ntnu.item.arctis.runtime.Block;

public class PointSwitch extends Block {

	public aletrainsystem.enums.PointConnectorEnum finalState;
	public aletrainsystem.models.messaging.PointSwitchOrder currentOrder;
	public aletrainsystem.enums.PointConnectorEnum currentState;
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

	public PointConnectorEnum getState(PointSwitchOrder order) {
		return order.getSwitchState();
	}

	public void logInitialized() {
		logger.info(String.format("Initialized pointswitch on port %s", motorPort.name()));
	}
	
}
