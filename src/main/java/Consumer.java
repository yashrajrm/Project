import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

	private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);

	Destination destination = null;
	ActiveMQConnectionFactory connectionFactory = null;
	Connection connection = null;
	Session session = null;
	Queue queue = null;
	MessageConsumer messageConsumer = null;

	public Consumer(String consumerName)
	{
		// TODO Auto-generated constructor stub
		connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.187:61616");
		try
		{
			connectionFactory.setAlwaysSessionAsync(true);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = session.createQueue(consumerName);
			messageConsumer = session.createConsumer(queue);
			LOG.info("registering message listener");
			messageConsumer.setMessageListener(new MessageListener() {

				public void onMessage(Message message)
				{
					// TODO Auto-generated method stub
					if (message instanceof TextMessage)
					{
						try
						{
							TextMessage textMessage = (TextMessage) message;
							if(message.propertyExists("JMSXUserID"))
								LOG.info("jmsxuserid set");
							else
								LOG.info("jmsxuserid not set");
							LOG.info(textMessage.getText().toString() + " - sent from "
									+ message.getStringProperty("JMSXUserID"));
						} catch (JMSException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		} catch (JMSException e)
		{
			// TODO Auto-generated catch block
			LOG.debug("Error occured while establishing connection", e);
			try
			{
				connection.close();
				session.close();
			} catch (JMSException e1)
			{
				// TODO Auto-generated catch block
				LOG.debug("error releasing resourses", e);
			}

		}
	}

	public void closeConnection()
	{
		try
		{
			connection.close();
			session.close();
		} catch (JMSException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
