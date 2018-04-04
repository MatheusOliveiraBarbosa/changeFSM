package aletrainsystem.headstate;

import aletrainsystem.enums.SleeperColor;
import no.ntnu.item.arctis.runtime.Block;


public class HeadState extends Block {

	public SleeperColor passed;
	public aletrainsystem.enums.PointSwitchConnectorEnum leavePointSwitch;
	public aletrainsystem.enums.PointSwitchConnectorEnum enterPointSwitch;

	public boolean passedRegularSleeper() {
		return passed == SleeperColor.regularSleeper();
	}
	
	public void enteredOnType(){
		enterPointSwitch = passed.convertToConnector();
	}
	
	public void leavedOnType(){
		leavePointSwitch = passed.convertToConnector();
	}
}
