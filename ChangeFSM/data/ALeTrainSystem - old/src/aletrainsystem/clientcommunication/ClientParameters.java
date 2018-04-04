package aletrainsystem.clientcommunication;

import java.net.Inet4Address;

public class ClientParameters {
	private Inet4Address brokerAddress;
	private String[] queues;
	
	public ClientParameters(Inet4Address brokerAddress, String[] queues){
		this.brokerAddress = brokerAddress;
		this.queues = queues;
	}
	
	public Inet4Address getBrokerAddress(){
		return this.brokerAddress;
	}
	
	public String[] getQueues(){
		return this.queues;
	}
}
