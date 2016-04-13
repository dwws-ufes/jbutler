package br.ufes.inf.nemo.jbutler.jms;

import java.io.Serializable;

/**
 * Abstract class that represents events that should be treated in the background.
 * 
 * An event sender observes CDI events of this kind and send them via JMS to a message-driven bean that will process the
 * event in a separate thread.
 * 
 * This class is part of a solution that is based on a blog post last accessed on September 7th, 2012:
 * http://weblogs.java
 * .net/blog/jjviana/archive/2010/04/14/decoupling-event-producers-and-event-consumers-java-ee-6-using-cdi-a
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class BackgroundEvent implements Serializable {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;
}
