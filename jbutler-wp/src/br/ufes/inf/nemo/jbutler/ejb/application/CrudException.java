package br.ufes.inf.nemo.jbutler.ejb.application;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Superclass of exceptions that can happen during any CRUD operation. Extends regular exceptions by providing a
 * collection of error messages that are to be displayed to the user by the controller. Also implements iterator,
 * iterating through the error messages.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public class CrudException extends Exception implements Iterable<CrudValidationError> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(CrudException.class.getCanonicalName());

	/** Collection of validation errors. */
	private Collection<CrudValidationError> validationErrors = new HashSet<CrudValidationError>();

	/**
	 * Constructor from superclass that adds a global validation message.
	 * 
	 * @param message
	 *            Exception message (passed on to superclass).
	 * @param messageKey
	 *            User-friendly error message key.
	 * @param messageParams
	 *            Parameters for user-friendly error message.
	 */
	public CrudException(String message, String messageKey, Object[] messageParams) {
		super(message);
		addValidationError(messageKey, messageParams);
	}

	/**
	 * Constructor from superclass that adds a validation message to a specific field.
	 * 
	 * @param message
	 *            Exception message (passed on to superclass).
	 * @param fieldName
	 *            Name of the field to which this message is related.
	 * @param messageKey
	 *            User-friendly error message key.
	 * @param messageParams
	 *            Parameters for user-friendly error message.
	 */
	public CrudException(String message, String fieldName, String messageKey, Object[] messageParams) {
		super(message);
		addValidationError(fieldName, messageKey, messageParams);
	}

	/**
	 * Adds a global validation error to the collection of validation errors.
	 * 
	 * @param messageKey
	 *            User-friendly error message key.
	 * @param messageParams
	 *            Parameters for user-friendly error message.
	 */
	public void addValidationError(String messageKey, Object[] messageParams) {
		logger.log(Level.FINEST, "Adding global validation message with key \"{0}\"...", messageKey);
		validationErrors.add(new CrudValidationError(messageKey, messageParams));
	}

	/**
	 * Adds a validation error of a specific field to the collection of validation errors.
	 * 
	 * @param fieldName
	 *            Name of the field to which this message is related.
	 * @param messageKey
	 *            User-friendly error message key.
	 * @param messageParams
	 *            Parameters for user-friendly error message.
	 */
	public void addValidationError(String fieldName, String messageKey, Object[] messageParams) {
		logger.log(Level.FINEST, "Adding field validation message with key \"{0}\" to field \"{1}\"...", new Object[] { messageKey, fieldName });
		validationErrors.add(new CrudValidationError(fieldName, messageKey, messageParams));
	}

	/** @see java.lang.Iterable#iterator() */
	@Override
	public Iterator<CrudValidationError> iterator() {
		return validationErrors.iterator();
	}
}
