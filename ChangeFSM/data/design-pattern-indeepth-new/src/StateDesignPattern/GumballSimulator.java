package StateDesignPattern;

public class GumballSimulator {

	public static void main(String[] args) {
		GumballMachine gumballMachine = new GumballMachine(100);
		
		for (int i = 0; i < 20; i++) {
			gumballMachine.insertQuater();
			gumballMachine.turnCrank();
			System.out.println(gumballMachine.getCount());
			System.out.println("");
		}
	}

}
