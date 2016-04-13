package br.ufes.inf.nemo.jbutler.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * Stateless session-bean that observes CDI events that are supposed to be handled in the background and places them in
 * a JMS queue for a message-driven bean to handle in a separate thread.
 * 
 * This class is part of a solution that is based on a blog post last accessed on September 7th, 2012:
 * http://weblogs.java
 * .net/blog/jjviana/archive/2010/04/14/decoupling-event-producers-and-event-consumers-java-ee-6-using-cdi-a
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Stateless
public class BackgroundEventSender {
	/** The logger. */
	private static final Logger logger = Logger.getLogger(BackgroundEventSender.class.getCanonicalName());

	/** JMS connection factory used to create JMS connections. */
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	/** JMS connection used to access the queue. */
	private Connection connection;

	/** JMS session during which events are sent to the queue. */
	private Session session;

	/** Queue to which the events are submitted. */
	@Resource(mappedName = "java:/queue/backgroundEventQueue")
	private Queue backgroundEventQueue;

	/** Message producer, responsible for actually sending the events to the queue. */
	private MessageProducer producer;

	/**
	 * Initialization method, creates the connection, opens the session and creates the message producer, assigning it
	 * to the background event queue.
	 */
	@PostConstruct
	public void init() {
		try {
			logger.log(Level.FINE, "Initializing the background event sender.");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(backgroundEventQueue);
			connection.start();
		}
		catch (JMSException e) {
			logger.log(Level.SEVERE, "Caught exception while trying to initialize the background event sender. Background event queue will NOT work.", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Disposes the background event sender by closing the JMS connection (and supposably all related elements, such as
	 * its session and the message producer).
	 */
	@PreDestroy
	public void destroy() {
		try {
			logger.log(Level.FINE, "Disposing the background event sender.");
			if (connection != null) connection.close();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Caught exception while trying to dispose the background event sender. An investigation on the reasons for this error is recommended.", e);
		}
	}

	/**
	 * Sends an event to the background event JMS queue for processing in a background thread. This method has been
	 * designed not to be called directly, but to observe CDI events which extend the class BackgroundEvent (annotated
	 * with @InForeground).
	 * 
	 * @param event
	 *            Event to the sent to the background event queue.
	 */
	public void sendEvent(@Observes @InForeground BackgroundEvent event) {
		try {
			logger.log(Level.INFO, "Encapsulating an event of class {0} and sending it to the background event queue for processing in a background thread.", (event == null) ? null : event.getClass().getName());
			ObjectMessage msg = session.createObjectMessage();
			msg.setObject(event);
			producer.send(msg);
		}
		catch (JMSException e) {
			logger.log(Level.SEVERE, "Caught exception while trying to send a background event to its queue. The message has most likely been lost.", e);
			throw new RuntimeException(e);
		}
	}
}
