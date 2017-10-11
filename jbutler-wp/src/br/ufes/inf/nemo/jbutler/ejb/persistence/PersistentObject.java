/**
 * 
 */
package br.ufes.inf.nemo.jbutler.ejb.persistence;

import br.ufes.inf.nemo.jbutler.ejb.domain.DomainObject;

/**
 * Optional interface for domain objects that may become persistent.
 * 
 * Most persistence frameworks (ORM frameworks) suggest that persistent objects have an identifier attribute that works
 * as primary-key in the database. This interface provides access to this attribute. Another suggestion is the inclusion
 * of a versioning attribute for the implementation of optmistic locks for data access. This interface also provides
 * access to this attribute.
 * 
 * Furthermore, this interface inherits the concepts from the DomainObject to force the implementation of a good
 * progamming practice: the definition of equals() and hashCode() in all domain classes.
 * 
 * A standard implementation is provided by PersistentObjectSupport, implementing EJB 3's standard annotations for
 * persistence.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 * @see br.ufes.inf.nemo.jbutler.ejb.domain.DomainObject
 */
public interface PersistentObject extends DomainObject {
	/**
	 * Returns the object persistence identifier (primary-key).
	 * 
	 * @return Object identifier.
	 */
	Long getId();

	/**
	 * Returns the versioning attribute (column).
	 * 
	 * @return Versioning attribute.
	 */
	Long getVersion();

	/**
	 * Checks if the object is persistent.
	 * 
	 * @return <code>true</code> if the object is persistent, <code>false</code> otherwise.
	 */
	boolean isPersistent();
}
