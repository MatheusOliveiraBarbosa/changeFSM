package StrategyDesignPattern;

public class Mute implements QuackBehavior{
	public void quack() {
		System.out.println("I'm muttttte");
	}
}
