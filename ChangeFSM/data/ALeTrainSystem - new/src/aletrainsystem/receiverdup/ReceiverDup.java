package aletrainsystem.receiverdup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import ntnu.no.rabbitamqp.util.*;
import no.ntnu.item.arctis.runtime.Block;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiverDup extends Block {
	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Channel channel;
	private String queue_name;
	private String exchange_name;
	private Consumer consumer;
	private String defaultExchange = "test";

	public void init(final HashMap<AMQPProperties, String> properties) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (factory == null) {
					exchange_name = properties.containsKey(AMQPProperties.EXCHANGENAME)
							? properties.get(AMQPProperties.EXCHANGENAME) : defaultExchange;
					factory = new ConnectionFactory();
					configureFactory(factory, properties);
					try {
						connection = factory.newConnection();
						channel = connection.createChannel();
						channel.exchangeDeclare(exchange_name, "topic");
						queue_name = channel.queueDeclare().getQueue();
					} catch (Exception e) {
						logger.error(e.getMessage());
						sendToBlock("FAILED", e.getMessage());
						return;
					}
				}
				consumer = new DefaultConsumer(channel) {
					@Override
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
							byte[] body) throws IOException {
						Message message = new Message(properties, envelope, System.currentTimeMillis());
						String value = decodeBody(body);
						if (value == null) {
							sendToBlock("ERROR", "Failed to deserialize message body");
							logger.error("Failed to deserialize message body");
							return;
						}
						message.setJsonBody(value);
						sendToBlock("RECEIVED", message);
					}
				};
				try {
					channel.basicConsume(queue_name, true, consumer);
					sendToBlock("READY", new Tuple<String, Connection>(exchange_name, connection));
				} catch (IOException e) {
					logger.error(e.getMessage());
					sendToBlock("FAILED", e.getMessage());
				}
			}
		};
		runAsync(r);
	}

	private String decodeBody(byte[] body) {
		try {
			return new String(body, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public void addTopic(String topic) {
		try {
			channel.queueBind(queue_name, exchange_name, topic);
		} catch (IOException e) {
			logger.error(e.getMessage());
			sendToBlock("ERROR", e.getMessage());
		}
	}

	public void addTopics(List<String> topics) {
		for (String t : topics)
			addTopic(t);
	}

	public void removeTopic(String topic) {
		try {
			channel.queueUnbind(queue_name, exchange_name, topic);
		} catch (IOException e) {
			logger.error(e.getMessage());
			sendToBlock("ERROR", e.getMessage());
		}
	}

	public void removeTopics(List<String> topics) {
		for (String t : topics)
			removeTopic(t);
	}

	public void stop() {
		try {
			channel.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendToBlock("ERROR", e.getMessage());
		}
	}

	private void configureFactory(ConnectionFactory factory, HashMap<AMQPProperties, String> properties) {
		factory.setHost(properties.containsKey(AMQPProperties.HOSTNAME) ? properties.get(AMQPProperties.HOSTNAME)
				: "192.168.0.100");
		if (properties.containsKey(AMQPProperties.PORT) && properties.get(AMQPProperties.PORT) != null) {
			try {
				int port = Integer.parseInt(properties.get(AMQPProperties.PORT));
				factory.setPort(port);
			} catch (NumberFormatException e) {
				logger.error("Port property is not an integer, PORT = " + properties.get(AMQPProperties.PORT));
			}
		}
		if (properties.containsKey(AMQPProperties.USERNAME))
			factory.setUsername(properties.get(AMQPProperties.USERNAME));
		if (properties.containsKey(AMQPProperties.PASSWORD))
			factory.setPassword(properties.get(AMQPProperties.PASSWORD));
	}

	public ReceiverDup() {
	}
}
