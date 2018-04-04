package aletrainsystem.models.locking;

import aletrainsystem.models.TrainId;

public interface LockingMessage {

	TrainId collector();

	TransactionId transactionId();

	RequestType type();

}