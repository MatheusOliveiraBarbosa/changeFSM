package aletrainsystem.models;

public class TrainId {

	private final String value;
	public static final String PROPERTYNAME = "TRAIN_ID";
	
	public TrainId(String value){
		if (isValid(value)) this.value = value;
		else throw new IllegalArgumentException("Invalid train id: ".concat(value));
	}


	public String getValue() {
		return value;
	}
	
	private boolean isValid(String value) {
		
		return true;
	}
}
