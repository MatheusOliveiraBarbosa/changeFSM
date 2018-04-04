package Factory;

public class NYPizzaStore extends PizzaStore {
	Pizza createPizza(String item) {
		if (item.equals("cheese")) {
			return new NYStyleCheesePizza();
		} else if (item.equals("chicagoStyleCheese")) {
			return new ChicagoStyleCheesePizza();
		} else
			return null;
	}
}
