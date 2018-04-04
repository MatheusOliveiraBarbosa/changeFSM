package Factory;

public class Main {
	public static void main(String[] args) {
		PizzaStore pStore = new NYPizzaStore();
		pStore.orderPizza("chicagoStyleCheese");
	}
}
