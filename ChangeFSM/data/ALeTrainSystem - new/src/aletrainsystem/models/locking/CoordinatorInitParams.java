package aletrainsystem.models.locking;

import java.util.Set;

public class CoordinatorInitParams extends Pair<TransactionId, Set<Lockable>> {
	public CoordinatorInitParams(TransactionId id, Set<Lockable> objectsToLock) {
		super(id, objectsToLock);
		// TODO Auto-generated constructor stub
	}

	public TransactionId id() {
		return first;
	}
	
	public Set<Lockable> objectsToLock() {
		return second;
	}
}
