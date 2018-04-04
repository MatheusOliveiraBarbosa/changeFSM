package StateDesignPattern;

import java.util.Random;

public class HasQuarterState implements State{
	Random random = new Random(System.currentTimeMillis());
	GumballMachine gumballMachine;
	
	public HasQuarterState(GumballMachine gumballMachine) {
		this.gumballMachine = gumballMachine;
	}
	
	public void insertQuater() {
		System.out.println("You can't insert anther quarter.");
	}
	
	public void ejectQuarter() {
		System.out.println("Quarter returned.");
		gumballMachine.setState(gumballMachine.getNoQuarterState());
	}
	
	public void turnCrank() {
		System.out.println("You turned...");
		if (random.nextInt(10) == 0 && gumballMachine.getCount() > 1) {
			gumballMachine.setState(gumballMachine.getWinnerState());
		} else {
			gumballMachine.setState(gumballMachine.getSoldState());
		}
	}
	
	public void dispense() {
		System.out.println("No gumball dispensed");
	}
}
