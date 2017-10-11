package br.ufes.inf.nemo.jbutler.ejb.domain;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Implementation of the equals() / hashCode() strategy proposed by the DomainObject interface. It also integrates with
 * the persistence framework, as it provides persistence mappings for EJB3.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 * @see br.ufes.inf.nemo.jbutler.ejb.domain.DomainObject
 */
@MappedSuperclass
public abstract class DomainObjectSupport implements DomainObject {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** Unique Universal Identifier (UUID). */
	@Basic
	@Column(nullable = false, length = 40)
	protected String uuid;

	/** Default constructor. */
	public DomainObjectSupport() {
		// Generates UUID during object construction.
		uuid = UUID.randomUUID().toString();
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.domain.DomainObject#getUuid() */
	@Override
	public String getUuid() {
		return uuid;
	}

	/**
	 * Setter for uuid.
	 * 
	 * @param uuid
	 *            The Unique Universal Identifier.
	 */
	protected void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object obj) {
		// Checks if the class is the same.
		if ((obj == null) || (!getClass().equals(obj.getClass()))) { return false; }
		DomainObjectSupport o = (DomainObjectSupport) obj;

		// Checks if the UUID is the same.
		return uuid.equals(o.uuid);
	}

	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	/**
	 * Base implementation of compareTo() method that compares two domain objects by UUID to be used by any subclass who
	 * wish to implement java.util.Comparable in case their criteria for comparison all tie. Notice that this class does
	 * not implement Comparable&lt;DomainObjectSupport&gt; because otherwise all subclasses wouldn't be able to
	 * implement Comparable themselves and, thus, restrict the class of object that can be compared.
	 * 
	 * @param o
	 *            The object to be compared to.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @return a negative value if this &lt; o, zero if this == 0 and a positive value if this &gt; o.
	 */
	public int compareTo(DomainObjectSupport o) {
		// Compares by UUID.
		return uuid.compareTo(o.uuid);
	}
}
