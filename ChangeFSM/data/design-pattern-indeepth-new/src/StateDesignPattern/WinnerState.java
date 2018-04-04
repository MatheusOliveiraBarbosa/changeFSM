package StateDesignPattern;

public class WinnerState implements State{
	GumballMachine gumballMachine;
	
	public WinnerState(GumballMachine gumballMachine) {
		this.gumballMachine = gumballMachine;
	}
	
	public void insertQuater() {
		System.out.println("Please wait, we're already give you a gumball.");
	}
	
	public void ejectQuarter() {
		System.out.println("Sorry, you already turn the crank.");
	}
	
	public void turnCrank() {
		System.out.println("Turn crank twice doesn't work.");
	}
	
	public void dispense() {
		System.out.println("Congratulations, you are a winner. You'll get one more ball");
		gumballMachine.releaseBall();
		gumballMachine.releaseBall();
		int count = gumballMachine.getCount();
		if (count == 0) {
			System.out.println("Oops, out of gumballs.");
			gumballMachine.setState(gumballMachine.getSoldOutState());
		} else {
			gumballMachine.setState(gumballMachine.getNoQuarterState());
		}
	}
}
