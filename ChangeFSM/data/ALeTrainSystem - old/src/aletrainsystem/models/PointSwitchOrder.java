package aletrainsystem.models;

import java.util.Date;

import aletrainsystem.enums.MotorPort;
import aletrainsystem.enums.PointSwitchConnectorEnum;

public class PointSwitchOrder {
	private PointSwitchConnectorEnum switchState;
	private RailComponentId pointSwitchId;
	private Date timestamp;
	private MotorPort motorPort;
	
	public PointSwitchOrder(RailComponentId pointSwitchId, PointSwitchConnectorEnum switchState){
		this.pointSwitchId = pointSwitchId;
		this.switchState = switchState;
		timestamp = new Date();
	}
	
	public RailComponentId getPointSwitchId(){
		return pointSwitchId;
	}
	
	public PointSwitchConnectorEnum getSwitchState(){
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
	
}
