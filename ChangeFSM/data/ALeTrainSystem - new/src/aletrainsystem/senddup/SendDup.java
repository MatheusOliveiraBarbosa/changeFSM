package aletrainsystem.senddup;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;
import no.ntnu.item.arctis.runtime.Block;
import ntnu.no.rabbitamqp.util.Message;
import ntnu.no.rabbitamqp.util.Tuple;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class SendDup extends Block {
	private Connection connection = null;
	private Channel channel = null;
	private String exchange;
	private String topic;
	private Message msg;

	public void send(final String m) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					channel.basicPublish(exchange, topic, null, m.getBytes());
					sendToBlock("SENT", msg);
				} catch (IOException e) {
					logger.error(e.getMessage());
					sendToBlock("ERROR", e.getMessage());
				}
			}
		};
		runAsync(r);
	}

	public void init(Tuple<String, Connection> parameters) {
		if (parameters != null) {
			exchange = parameters.getValue1();
			this.connection = parameters.getValue2();
			try {
				channel = connection.createChannel();
				channel.exchangeDeclare(exchange, "topic");
			} catch (IOException e) {
				logger.error(e.getMessage());
				sendToBlock("ERROR", e.getMessage());
			}
		}
	}

	public void stop() {
		try {
			channel.close();
			connection.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendToBlock("ERROR", e.getMessage());
		}
	}

	public void setMessageProperties(Message m) {
		this.msg = m;
		this.topic = m.getTopic();
	}

	public Object getMessage(Message m) {
		return m.getBody();
	}
}
