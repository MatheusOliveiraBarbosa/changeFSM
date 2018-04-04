package aletrainsystem.models.messaging;

import java.util.Date;

import aletrainsystem.enums.MotorPort;
import aletrainsystem.enums.PointConnectorEnum;
import aletrainsystem.models.RailComponentId;

public class PointSwitchOrder {
	private PointConnectorEnum switchState;
	private RailComponentId pointId;
	private Date timestamp;
	private MotorPort motorPort;
	
	public PointSwitchOrder(RailComponentId pointId, PointConnectorEnum switchState){
		this.pointId = pointId;
		this.switchState = switchState;
		timestamp = new Date();
	}
	
	public RailComponentId getPointId(){
		return pointId;
	}
	
	public PointConnectorEnum getSwitchState(){
		return switchState;
	}
	
	public MotorPort getMotorPort(){
		return motorPort;
	}
	
	public Date getTimestamp(){
		return timestamp;
	}
	
	public void setMotorPort(MotorPort port){
		motorPort = port;
	}
	
	@Override
	public String toString() {
		return pointId.toString() + ":" + switchState.toString();
	}
	
}
