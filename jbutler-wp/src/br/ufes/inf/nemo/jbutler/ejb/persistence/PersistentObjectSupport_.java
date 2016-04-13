package br.ufes.inf.nemo.jbutler.ejb.persistence;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import br.ufes.inf.nemo.jbutler.ejb.domain.DomainObjectSupport_;

/**
 * Meta-model for the DomainObjectSupport domain class, which allows DAOs to perform programmatic queries using JPA2's
 * Criteria API.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @see br.ufes.inf.nemo.jbutler.ejb.domain.DomainObjectSupport
 */
@StaticMetamodel(PersistentObjectSupport.class)
public class PersistentObjectSupport_ extends DomainObjectSupport_ {
	public static volatile SingularAttribute<PersistentObjectSupport, Long> id;
	public static volatile SingularAttribute<PersistentObjectSupport, Long> version;
}
