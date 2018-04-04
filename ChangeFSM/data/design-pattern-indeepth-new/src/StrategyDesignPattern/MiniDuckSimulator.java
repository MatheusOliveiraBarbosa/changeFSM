package StrategyDesignPattern;

public class MiniDuckSimulator {
	public static void main(String[] args) {
		Duck duck = new ModelDuck();
		duck.display();
		duck.performFly();
		duck.performQuack();
		duck.performSwim();
		System.out.println("========Change fly behavoir========");
		duck.setFlyBehavior(new FlyRocketPowered());
		duck.performFly();
	}
}
