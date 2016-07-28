package br.ufes.inf.nemo.marvin.people.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObjectSupport;

/**
 * Domain class that represents contact types, which help categorize different types of telephone numbers, such as home,
 * work, etc.
 * 
 * <i>This class is part of the Engenho de Software "Legal Entity" mini framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
@Entity
public class ContactType extends PersistentObjectSupport implements Comparable<ContactType> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** Type of contact: home, work, cell, fax, etc. */
	@Basic
	@NotNull
	@Size(max = 50)
	protected String type;

	/** Getter for type. */
	public String getType() {
		return type;
	}

	/** Setter for type. */
	public void setType(String type) {
		this.type = type;
	}

	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(ContactType o) {
		// Compare the names of the types.
		if (type == null) return 1;
		if ((o == null) || (o.type == null)) return -1;
		int cmp = type.compareTo(o.type);
		if (cmp != 0) return cmp;

		// If it's the same name, check if it's the same entity.
		return uuid.compareTo(o.uuid);
	}

	/** @see br.ufes.inf.nemo.util.ejb3.persistence.PersistentObjectSupport#toString() */
	@Override
	public String toString() {
		return type;
	}
}
