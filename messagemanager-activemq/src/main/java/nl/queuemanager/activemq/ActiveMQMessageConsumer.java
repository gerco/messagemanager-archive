package nl.queuemanager.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public class ActiveMQMessageConsumer implements MessageConsumer {

	private MessageListener messageListener;
	
	public MessageListener getMessageListener() throws JMSException {
		return messageListener;
	}

	public void setMessageListener(MessageListener listener) throws JMSException {
		this.messageListener = listener;
	}
	
	public String getMessageSelector() throws JMSException {
		return null;
	}

	public Message receive() throws JMSException {
		return null;
	}

	public Message receive(long timeout) throws JMSException {
		return null;
	}

	public Message receiveNoWait() throws JMSException {
		return null;
	}

	public void close() throws JMSException {
	}

}
