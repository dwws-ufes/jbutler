package br.ufes.inf.nemo.jbutler.ejb.application;

import java.io.Serializable;

/**
 * Represents a validation error that can happen during a CRUD operation. It provides to the controller the key for
 * retrieving the error message in the resource bundles and parameters to be inserted in the message. It can also
 * provide a field name, so the controller attaches the message to a specific field of the form.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public class CrudValidationError implements Serializable {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** Name of the field this error is related to. */
	private String fieldName;

	/** Error message key, so the controller can look at the resource bundle for it. */
	private String messageKey;

	/** Message parameters that are inserted in the error message by the controller. */
	private Object[] messageParams;

	/**
	 * Constructor using fields.
	 * 
	 * @param messageKey
	 *            User-friendly error message key.
	 * @param messageParams
	 *            Parameters for user-friendly error message.
	 */
	public CrudValidationError(String messageKey, Object[] messageParams) {
		this.messageKey = messageKey;
		this.messageParams = messageParams;
	}

	/**
	 * Constructor using fields.
	 * 
	 * @param fieldName
	 *            Name of the field to which this message is related.
	 * @param messageKey
	 *            User-friendly error message key.
	 * @param messageParams
	 *            Parameters for user-friendly error message.
	 */
	public CrudValidationError(String fieldName, String messageKey, Object[] messageParams) {
		this.fieldName = fieldName;
		this.messageKey = messageKey;
		this.messageParams = messageParams;
	}

	/**
	 * Getter for fieldName.
	 * 
	 * @return The name of the field to which this message is related.
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Setter for fieldName.
	 * 
	 * @param fieldName
	 *            The name of the field to which this message is related.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Getter for messageKey.
	 * 
	 * @return The user-friendly error message key.
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * Setter for messageKey.
	 * 
	 * @param messageKey
	 *            The user-friendly error message key.
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * Getter for messageParams.
	 * 
	 * @return The parameters for user-friendly error message.
	 */
	public Object[] getMessageParams() {
		return messageParams;
	}

	/**
	 * Setter for messageParams.
	 * 
	 * @param messageParams
	 *            The parameters for user-friendly error message.
	 */
	public void setMessageParams(Object[] messageParams) {
		this.messageParams = messageParams;
	}
}
