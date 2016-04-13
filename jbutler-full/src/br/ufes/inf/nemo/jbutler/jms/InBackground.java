package br.ufes.inf.nemo.jbutler.jms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * CDI qualifier for events that will be handled in the background after caught by the background event dispatcher in a
 * JMS queue.
 * 
 * This class is part of a solution that is based on a blog post last accessed on September 7th, 2012:
 * http://weblogs.java
 * .net/blog/jjviana/archive/2010/04/14/decoupling-event-producers-and-event-consumers-java-ee-6-using-cdi-a
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE })
public @interface InBackground {}
