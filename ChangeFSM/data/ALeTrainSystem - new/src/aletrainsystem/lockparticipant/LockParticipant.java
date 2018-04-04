package aletrainsystem.lockparticipant;

import aletrainsystem.models.TrainId;
import aletrainsystem.models.locking.Lockable;
import aletrainsystem.models.locking.Request;
import aletrainsystem.models.locking.RequestType;
import aletrainsystem.models.locking.Response;
import no.ntnu.item.arctis.runtime.Block;

public class LockParticipant extends Block {
	
	public final static String ENTITY_ID = "id";
	
	public Request request;
	public RequestType requestType;

	public aletrainsystem.models.locking.Response response;

	public aletrainsystem.models.railroad.Railroad resources;

	public LockParticipant() {
	}

	public Response generateResponse() {
		TrainId participant = null;
		if (hasProperty(ENTITY_ID)) {
			Object c = getProperty(ENTITY_ID);
			if (c != null && c instanceof Class<?>) {
				participant = (TrainId) c;
			}
		}
				
		boolean success = false;
		if (isReserve())
			success = isReservedForCoordinator();
		else if (isPerform())
			success = isLockedForCoordinator();
		
		return new Response(participant, request, success);
	}

	public boolean isReserve() {
		return request.type() == RequestType.RESERVE;
	}

	public boolean isPerform() {
		return request.type() == RequestType.PERFORM;
	}

	public boolean isAbort() {
		return request.type() == RequestType.ABORT;
	}

	public boolean isRelease() {
		return request.type() == RequestType.RELEASE;
	}
	
	public boolean isInternalResponse() {
		return response.respondent().equals(response.collector());
	}

	public void reserveLock() {
		for (String lockableId : request.lockableIDs()) {
			Lockable localInstance = getLocalInstance(lockableId);
			TrainId lockOwner = localInstance.checkLock();
			if (lockOwner == null || lockOwner.equals(request.collector())) {
				localInstance.reserveLock(request.collector());
			}
		}
	}

	public boolean isReservedForCoordinator() {
		for (String lockableId : request.lockableIDs()) {
			Lockable localInstance = getLocalInstance(lockableId);
			TrainId lockOwner = localInstance.checkReservation();
			if (lockOwner == null || !lockOwner.equals(request.collector())) {
				return false;
			}
		}
		return true;
	}

	public boolean isReservedForOther() {
		for (String lockableId : request.lockableIDs()) {
			Lockable localInstance = getLocalInstance(lockableId);
			TrainId lockOwner = localInstance.checkReservation();
			if (lockOwner == null || lockOwner.equals(request.collector()))
				return false;
		}
		return true;
	}

	public void lock() {
		for (String lockableId : request.lockableIDs()) {
			Lockable localInstance = getLocalInstance(lockableId);
			TrainId lockOwner = localInstance.checkReservation();
			if (lockOwner == null || lockOwner.equals(request.collector())) {
				localInstance.performLock(request.collector());
			}
		}
	}

	public void abortReservation() {
		for (String lockableId : request.lockableIDs()) {
			Lockable localInstance = getLocalInstance(lockableId);
			TrainId lockOwner = localInstance.checkReservation();
			if (lockOwner == null || lockOwner.equals(request.collector())) {
				localInstance.releaseReservation();
			}
		}
	}

	public boolean isLockedForCoordinator() {
		for (String lockableId : request.lockableIDs()) {
			Lockable localInstance = getLocalInstance(lockableId);
			TrainId lockOwner = localInstance.checkLock();
			if (lockOwner == null || !lockOwner.equals(request.collector())) {
				return false;
			}
		}
		return true;
	}

	public void releaseLock() {
		for (String id : request.lockableIDs()) {
			getLocalInstance(id).unLock();
		}
	}

	public void logInit() {
		logger.info("Initialized");
	}
	
	private Lockable getLocalInstance(String id) {
		return resources.getLockableResource(id);
	}

	public void received() {
		logger.info(
				request.transactionId().toString().
				concat(": Received ").
				concat(request.type().toString()));
	}
}
