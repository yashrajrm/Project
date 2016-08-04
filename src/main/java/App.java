import javax.jms.JMSException;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger LOG= LoggerFactory.getLogger(App.class);

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		BrokerService broker=new BrokerService();
		try
		{
			broker.addConnector("tcp://localhost:61616");
			broker.setPopulateJMSXUserID(true);
			broker.start();
			if(broker.isPopulateJMSXUserID())
				LOG.info("jmsxuserid is true");
			else
				LOG.info("jmsxuserid is false");
			Producer producer=new Producer();
			LOG.info("Consumer created");
			Consumer consumer=new Consumer("TestQueue");
			producer.sendMessage("TestQueue", "hello buddy");
			Thread.sleep(4000);
		}
		catch (JMSException e)
		{
			// TODO Auto-generated catch block
			LOG.debug("Error occured while sending message",e);
		}
		catch (Exception e) {
			// TODO: handle exception
			LOG.debug("error releasing resources",e);
		}
	}

}
