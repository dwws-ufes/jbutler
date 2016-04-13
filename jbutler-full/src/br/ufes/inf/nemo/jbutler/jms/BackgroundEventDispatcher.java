package br.ufes.inf.nemo.jbutler.jms;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Message-driven bean that watches the background event queue and processes the queued events in a FIFO fashion.
 * 
 * Processing the events actually means raising a CDI event to be observed by some application-specific class with
 * the @InBackground annotation.
 * 
 * This class is part of a solution that is based on a blog post last accessed on September 7th, 2012:
 * http://weblogs.java
 * .net/blog/jjviana/archive/2010/04/14/decoupling-event-producers-and-event-consumers-java-ee-6-using-cdi-a
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@MessageDriven(name = "BackgroundEventDispatcher", activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/backgroundEventQueue"), @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class BackgroundEventDispatcher implements MessageListener {
	/** The logger. */
	private static final Logger logger = Logger.getLogger(BackgroundEventDispatcher.class.getCanonicalName());

	/** CDI event producer, able to fire a CDI event to be observed by the application-specific class. */
	@Inject
	@InBackground
	private Event<BackgroundEvent> event;

	/** @see javax.jms.MessageListener#onMessage(javax.jms.Message) */
	@Override
	public void onMessage(Message message) {
		// Checks that the message is not null.
		if (message == null) throw new RuntimeException("Background event dispatcher received null message.");

		// Checks that the message sent through JSM contains an object.
		if (!(message instanceof ObjectMessage)) throw new RuntimeException("Background event dispatcher received invalid message type via JMS background event queue: " + message.getClass().getName());

		// Obtains the object from the message.
		ObjectMessage msg = (ObjectMessage) message;
		try {
			Serializable eventObject = msg.getObject();

			// Checks that the object is not null.
			if (eventObject == null) throw new RuntimeException("Background event dispatcher received null message content.");

			// Checks that the object represents a background event.
			if (!(eventObject instanceof BackgroundEvent)) throw new RuntimeException("Background event dispatcher received invalid message content type (expected a background event): " + eventObject.getClass().getName());

			// If everything is OK so far, fires the CDI event to activate its application-specific handling.
			BackgroundEvent backgroundEvent = (BackgroundEvent) eventObject;
			event.fire(backgroundEvent);
		}
		catch (JMSException e) {
			logger.log(Level.SEVERE, "Caught exception while trying to dispatch a background event to its application-specific handling. The message has most likely been lost.", e);
			throw new RuntimeException(e);
		}
	}
}
