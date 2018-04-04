package aletrainsystem.models.messaging;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class TerminationClient {

    private static final String EXCHANGE_NAME = "aletrain";
    private static final String TOPIC ="common.";
    private final static String HOST = "192.168.0.100";

    
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        
        ConnectionFactory factory = new ConnectionFactory();
       factory.setHost(HOST);
        factory.setUsername("henrihs");
        factory.setPassword("12345ev3");
       Connection connection = factory.newConnection();
       Channel channel = connection.createChannel();

       channel.exchangeDeclare(EXCHANGE_NAME, "topic");
       String msg;
       TerminationMessage message = new TerminationMessage("henrihs");
       Gson g = new Gson();
	   msg = g.toJson(message);
	   String topic = TOPIC.concat(TerminationMessage.class.getSimpleName());
	   System.out.println("Publishing termination message on behalf of henrihs");
       channel.basicPublish(EXCHANGE_NAME, topic, null, msg.getBytes() );
       
       
    }
}