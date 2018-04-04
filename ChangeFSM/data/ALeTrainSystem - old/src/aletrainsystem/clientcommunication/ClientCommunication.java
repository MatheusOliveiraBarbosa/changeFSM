package aletrainsystem.clientcommunication;

import java.util.HashSet;
import java.util.Set;

import com.bitreactive.library.amqp.AmqpMessage;
import com.bitreactive.library.amqp.BrokerAddress;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient.Parameter;

import no.ntnu.item.arctis.runtime.Block;

public class ClientCommunication extends Block {

	public aletrainsystem.clientcommunication.ClientParameters parameters;

	public AmqpMessage ComposeAmqpMessage(String queueInfo, String className, String serializedObject) {
		AmqpMessage message = new AmqpMessage(parameters.getBrokerAddress().toString(), queueInfo, className, serializedObject);
		return message;
	}
	
	public String Decode(AmqpMessage message){
		
		return null;
	}

	public String getClassName(Object obj) {
		return obj.getClass().getName();
	}

	public Parameter convertParams(ClientParameters params) {
		HashSet<BrokerAddress> addr = new HashSet<BrokerAddress>();
		for (String queue : params.getQueues()) {
			addr.add(new BrokerAddress(params.getBrokerAddress().toString(), queue));
		}
		
		return new Parameter(addr);
	}

	public Set<BrokerAddress> generateSubscription(String queue) {
		Set<BrokerAddress> subscription = new HashSet<BrokerAddress>();
		subscription.add(new BrokerAddress(parameters.getBrokerAddress().toString(), queue));
		return subscription;
	}


}
