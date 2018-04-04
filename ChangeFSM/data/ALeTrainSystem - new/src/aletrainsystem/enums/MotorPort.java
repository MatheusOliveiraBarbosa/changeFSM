package aletrainsystem.enums;

public enum MotorPort {
	A("MOTOR_PORT_A"), 
	B("MOTOR_PORT_B"), 
	C("MOTOR_PORT_C"), 
	D("MOTOR_PORT_D");
	
	private String propertyName;
	private MotorPort(String propertyName){
		this.propertyName = propertyName;
	}
	
	public String getPropertyName(){
		return this.propertyName;
	}
}
