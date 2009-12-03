/**

 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.queuemanager.smm;

import javax.jms.JMSException;

import nl.queuemanager.core.jms.JMSQueue;
import nl.queuemanager.core.jms.JMSDestination.TYPE;

import com.sonicsw.mq.common.runtime.IQueueData;

/**
 * SonicMQ based implementation of {@link JMSQueue}.
 * 
 * @author Gerco Dries (gdr@progaia-rs.nl)
 */
public class SonicMQQueue extends SonicMQDestination implements JMSQueue {
	private final IQueueData queue;

	protected SonicMQQueue(SonicMQBroker broker, IQueueData queueData) {
		super(broker);
		
		if(queueData == null)
			throw new IllegalArgumentException("queueData must not be null");
		
		queue = queueData;
	}

	public int getMessageCount() {
		return queue.getMessageCount();
	}

	public String getName() {
		return queue.getQueueName();
	}
	
	public String getQueueName() throws JMSException {
		return getName();
	}

	public TYPE getType() {
		return TYPE.QUEUE;
	}
}
