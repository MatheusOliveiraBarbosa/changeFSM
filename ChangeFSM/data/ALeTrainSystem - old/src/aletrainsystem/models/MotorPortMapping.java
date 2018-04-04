package aletrainsystem.models;

import aletrainsystem.enums.MotorPort;

public class MotorPortMapping {

	private MotorPort port;
	private RailComponentId pointSwitchId;
	
	public MotorPortMapping(MotorPort motorPort, RailComponentId id){
		this.port = motorPort;
		this.pointSwitchId = id;
	}
	
	public RailComponentId getPointSwitchId() {
		return pointSwitchId;
	}
	
	public MotorPort getPort() {
		return port;
	}
	
}
