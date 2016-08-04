import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {

	private static final Logger LOG = LoggerFactory.getLogger(Producer.class);
	ActiveMQConnectionFactory connectionFactory=null;
	Destination destination = null;
	Connection connection = null;
	Session session = null;

	public Producer()
	{
		connectionFactory= new ActiveMQConnectionFactory("tcp://192.168.0.187:61616");
		connectionFactory.setAlwaysSessionAsync(true);
		try
		{
			LOG.debug("producer created...............");
			connection =connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e)
		{
			// TODO Auto-generated catch block
			LOG.debug("Error occured while getting connection", e);
			try
			{
				connection.close();
				session.close();
			} catch (JMSException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void sendMessage(String reciever,String payload) throws JMSException
	{
		Queue queue = session.createQueue(reciever);
		Message message=session.createTextMessage(payload);
		MessageProducer producer=session.createProducer(queue);
		producer.send(message);
		LOG.info("message sent to "+reciever);
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
			LOG.debug("Error occured while closing connection",e);
		}
	}
}
