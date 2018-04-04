package StrategyDesignPattern;

public class MallardDuck extends Duck{
	public MallardDuck() {
		flyBehavior = new FlyWithWing();
		quackBehavior = new Quack();
	}
	
	public void display() {
		System.out.println("Hello world, I'm a mallard duck.");
	}
}
