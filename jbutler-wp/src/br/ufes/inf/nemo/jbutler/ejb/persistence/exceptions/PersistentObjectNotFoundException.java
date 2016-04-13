package br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions;

import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;

/**
 * This exception represents the case in a DAO where a single result was expected but none was found. It is the checked
 * exception version of <code>javax.persistence.NoResultException</code>.
 * 
 * It is necessary because since NoResultException is not checked and our DAOs are EJBs, the EJB container considers the
 * exception as an EJBException and does all sorts of exception handling we find undesirable in many cases.
 * 
 * @see javax.persistence.NoResultException
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
public class PersistentObjectNotFoundException extends CheckedQueryException {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

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
	public PersistentObjectNotFoundException(Throwable cause, Class<? extends PersistentObject> entityClass, Object ... params) {
		super(cause, entityClass, params);
	}
}
