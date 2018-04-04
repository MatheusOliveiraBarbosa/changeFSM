package aletrainsystem.models.navigation;

import aletrainsystem.models.locking.Lockable;

public abstract class RouteElement {
	public abstract RouteElement[] getNext(RouteElement previous);
	public abstract int length();
	public abstract Lockable getLockableResource();
}
