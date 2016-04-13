package br.ufes.inf.nemo.jbutler.ejb.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Meta-model for the DomainObjectSupport domain class, which allows DAOs to perform programmatic queries using JPA2's
 * Criteria API.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @see br.ufes.inf.nemo.jbutler.ejb.domain.DomainObjectSupport
 */
@StaticMetamodel(DomainObjectSupport.class)
public class DomainObjectSupport_ {
	public static volatile SingularAttribute<DomainObjectSupport, String> uuid;
}
