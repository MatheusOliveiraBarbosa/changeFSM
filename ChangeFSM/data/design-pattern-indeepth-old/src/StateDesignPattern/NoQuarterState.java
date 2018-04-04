package StateDesignPattern;

public class NoQuarterState implements State{
	GumballMachine gumballMachine;
	
	public NoQuarterState(GumballMachine gumballMachine) {
		this.gumballMachine = gumballMachine;
	}
	
	public void insertQuater() {
		System.out.println("You inserted a quarter.");
		gumballMachine.setState(gumballMachine.getHasQuarterState());
	}
	
	public void ejectQuarter() {
		System.out.println("You haven't insert a quarter.");
	}
	
	public void turnCrank() {
		System.out.println("You turned, but there is no quarter.");
	}
	
	public void dispense() {
		System.out.println("You need to pay first.");
	}
}
