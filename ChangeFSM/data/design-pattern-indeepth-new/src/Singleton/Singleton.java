package Singleton;

public class Singleton {

	private static Singleton unique = new Singleton();
	private int count = 0;
	
	private Singleton() {}
	
	public static Singleton getInstance() {
		return unique;
	}
	
	public int count() {
		return count++;
	}
	
	public static void main(String[] args) {
		Singleton instance1 = Singleton.getInstance();
		System.out.println(instance1.count());
		
		Singleton instance2 = Singleton.getInstance();
		System.out.println(instance2.count());
		System.out.println(instance2.count());
		System.out.println(instance2.count());
		System.out.println(instance1.count());
	}

}
