package StateDesignPattern;

public class GumballSimulator {

	public static void main(String[] args) {
		GumballMachine gumballMachine = new GumballMachine(5);
		
		gumballMachine.insertQuater();
		gumballMachine.turnCrank();
		
		gumballMachine.insertQuater();
		gumballMachine.turnCrank();
		
		gumballMachine.insertQuater();
		gumballMachine.ejectQuarter();
		gumballMachine.turnCrank();
	}

}
