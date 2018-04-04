package aletrainsystem.models;

public class RailComponentId implements Comparable<RailComponentId> {
	private final long id;
	
	public RailComponentId(long id){
		this.id = id;
	}
	
	public RailComponentId(String id){
		this.id = Integer.valueOf(id);
	}
	
	public long value(){
		return id;
	}
	
	@Override
	public boolean equals(Object other){
		return this.value() == ((RailComponentId)other).value();
	}
	
	@Override
	public String toString(){
		return String.valueOf(id);
	}
	
	@Override
	public int compareTo(RailComponentId other) {
		return (int) (other.value() - value());
	}
}
