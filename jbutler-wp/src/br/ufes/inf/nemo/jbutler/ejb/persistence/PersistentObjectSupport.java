package br.ufes.inf.nemo.jbutler.ejb.persistence;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import br.ufes.inf.nemo.jbutler.ejb.domain.DomainObjectSupport;

/**
 * Standard implementation for persistent objects, implementing EJB 3's standard annotations for persistence. This
 * implementation extends on the DomainObjectSupport implementation to provide equals() and hashCode() based on UUIDs.
 * 
 * <i>This class is part of the Engenho de Software CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @see br.ufes.inf.nemo.jbutler.ejb.domain.DomainObjectSupport
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
@MappedSuperclass
public abstract class PersistentObjectSupport extends DomainObjectSupport implements PersistentObject {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** Identifier attribute (primary-key). */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** Versioning attribute. */
	@Version
	@Column(nullable = false)
	private Long version;

	/** @see br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject#getId() */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Setter for id.
	 * 
	 * @param id
	 *            The identifier attribute (primary-key).
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject#getVersion() */
	@Override
	public Long getVersion() {
		return version;
	}

	/**
	 * Setter for version.
	 * 
	 * @param version
	 *            The versioning attribute.
	 */
	protected void setVersion(Long version) {
		this.version = version;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject#isPersistent() */
	@Override
	public boolean isPersistent() {
		return (id != null);
	}

	/** @see java.lang.Object#toString() */
	@Override
	public String toString() {
		return "Instance of " + getClass().getName() + " (id: " + id + "; uuid: " + uuid + ")";
	}
}
