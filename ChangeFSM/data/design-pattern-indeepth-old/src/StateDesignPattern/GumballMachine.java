package StateDesignPattern;

public class GumballMachine {

	State soldOutState;
	State noQuarterState;
	State hasQuarterState;
	State soldState;
	
	State state = soldOutState;
	int count = 0;
	
	public GumballMachine(int numGumballs) {
		soldOutState = new SoldOutState(this); 
		noQuarterState = new NoQuarterState(this);
		hasQuarterState = new HasQuarterState(this);
		soldState = new SoldState(this);
		count = numGumballs;
		
		if (numGumballs > 0) {
			state = noQuarterState;
		}
	}
	
	public void insertQuater() {
		state.insertQuater();
	}
	
	public void ejectQuarter() {
		state.ejectQuarter();
	}
	
	public void turnCrank() {
		state.turnCrank();
		state.dispense();
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public void releaseBall() {
		System.out.println("A gumball comes out....");
		if (count != 0) {
			count--;
		}
	}
	
	public int getCount() {
		return count;
	} 
	
	public State getSoldOutState() {
		return soldOutState;
	}
	
	public State getSoldState() {
		return soldState;
	}
	
	public State getNoQuarterState() {
		return noQuarterState;
	}
	
	public State getHasQuarterState() {
		return hasQuarterState;
	}
}
