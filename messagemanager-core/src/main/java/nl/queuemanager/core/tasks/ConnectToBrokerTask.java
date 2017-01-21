package nl.queuemanager.core.tasks;

import javax.jms.JMSSecurityException;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import lombok.extern.java.Log;
import nl.queuemanager.core.configuration.CoreConfiguration;
import nl.queuemanager.core.jms.JMSDomain;
import nl.queuemanager.core.task.Task;
import nl.queuemanager.core.util.Credentials;
import nl.queuemanager.core.util.UserCanceledException;
import nl.queuemanager.jms.JMSBroker;

@Log
public class ConnectToBrokerTask extends Task {
	private final JMSDomain domain;
	private final JMSBroker broker;
	private final CoreConfiguration configuration;

	@Inject
	ConnectToBrokerTask(
			JMSDomain domain, 
			CoreConfiguration configuration,
			EventBus eventBus,
			@Assisted JMSBroker broker) 
	{
		super(broker, eventBus);
		this.domain = domain;
		this.broker = broker;
		this.configuration = configuration;
	}

	@Override
	public void execute() throws Exception {
		Credentials credentials = configuration.getBrokerCredentials(broker);
		
		// Try to connect infinitely, until the user cancels or we succeed.
		while(true) {
			try {
				domain.connectToBroker(broker, credentials);
				break;
			} catch (JMSSecurityException e) {
				credentials = domain.getCredentials(broker, credentials, e);
				if(credentials == null)
					throw new UserCanceledException();
			}
		}

		// If we have specified alternate credentials, save them.
		if(credentials != null) {
			configuration.setBrokerCredentials(broker, credentials);
		}
	}

	@Override
	public String toString() {
		return "Connecting to broker " + broker;
	}
}
