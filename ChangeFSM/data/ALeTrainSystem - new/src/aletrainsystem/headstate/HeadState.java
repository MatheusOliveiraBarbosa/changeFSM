package aletrainsystem.headstate;

import java.util.Arrays;
import java.util.List;

import aletrainsystem.enums.SleeperColor;
import no.ntnu.item.arctis.runtime.Block;


public class HeadState extends Block {
	
	private List<SleeperColor> VALID_COLORS = Arrays.asList(
			SleeperColor.BLACK, 
			SleeperColor.GREEN, 
			SleeperColor.RED, 
			SleeperColor.YELLOW 
			);
	
	public SleeperColor passed;
	public aletrainsystem.enums.PointConnectorEnum enterPointSwitch;
	public aletrainsystem.enums.PointConnectorEnum leavePointSwitch;
	public boolean passedRegularSleeper() {
		return passed == SleeperColor.regularSleeper();
	}
	
	public boolean isValidColor() {
		return VALID_COLORS.contains(passed);
	}
	
	public boolean passedDifferentThanEnteredOn() {
		return !passedRegularSleeper() && 
				passed.convertToConnector() != enterPointSwitch;
	}
	
	public void enteredOnType(){
		enterPointSwitch = passed.convertToConnector();
	}
	
	public void leavedOnType(){
		leavePointSwitch = passed.convertToConnector();
	}
}
