package br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions;

import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;

/**
 * Abstract class representing checked exceptions that are thrown by query methods in the DAOs.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
public abstract class CheckedQueryException extends Exception {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The entity class. */
	private Class<? extends PersistentObject> entityClass;

	/** The parameters used in the query. */
	private Object[] params;

	/**
	 * Constructor from superclass, using fields.
	 * 
	 * @param cause
	 *            The cause for this exception.
	 * @param entityClass
	 *            The persistent query that was queried when the exception occurred.
	 * @param params
	 *            The parameters used in the query that caused the exception.
	 */
	public CheckedQueryException(Throwable cause, Class<? extends PersistentObject> entityClass, Object ... params) {
		super(cause);
		this.entityClass = entityClass;
		this.params = params;
	}

	/**
	 * Getter for entityClass.
	 * 
	 * @return The persistent query that was queried when the exception occurred.
	 */
	public Class<? extends PersistentObject> getEntityClass() {
		return entityClass;
	}

	/**
	 * Setter for entityClass.
	 * 
	 * @param entityClass
	 *            The persistent query that was queried when the exception occurred.
	 */
	public void setEntityClass(Class<? extends PersistentObject> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Getter for params.
	 * 
	 * @return The parameters used in the query that caused the exception.
	 */
	public Object[] getParams() {
		return params;
	}

	/**
	 * Setter for params.
	 * 
	 * @param params
	 *            The parameters used in the query that caused the exception.
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}
}
