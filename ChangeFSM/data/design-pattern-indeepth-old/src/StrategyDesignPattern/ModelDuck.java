package StrategyDesignPattern;

public class ModelDuck extends Duck{
	public ModelDuck() {
		flyBehavior = new FlyWithWing();
		quackBehavior = new Quack();
	}
	
	public void display() {
		System.out.println("Hello world, I'm a model duck.");
	}
}
