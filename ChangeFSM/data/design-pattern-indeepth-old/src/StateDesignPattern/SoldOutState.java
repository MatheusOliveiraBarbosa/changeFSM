package StateDesignPattern;

public class SoldOutState implements State{
	GumballMachine gumballMachine;
	
	public SoldOutState(GumballMachine gumballMachine) {
		this.gumballMachine = gumballMachine;
	}
	
	public void insertQuater() {
		System.out.println("Oops, gumball sold out.");
	}
	
	public void ejectQuarter() {
		System.out.println("Oops, gumball sold out.");
	}
	
	public void turnCrank() {
		System.out.println("Oops, gumball sold out.");
	}
	
	public void dispense() {
		System.out.println("Oops, gumball sold out.");
	}
}
