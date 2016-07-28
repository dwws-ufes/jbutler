package br.ufes.inf.nemo.marvin.people.persistence;

import javax.ejb.Local;

import br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.nemo.marvin.people.domain.Telephone;

/**
 * Interface for a DAO for objects of the Telephone domain class.
 * 
 * Using a mini CRUD framework for EJB3, basic DAO operation definitions are inherited from the superclass, whereas
 * operations that are specific to the managed domain class (if any) are specified in this class.
 * 
 * <i>This class is part of the Engenho de Software "Legal Entity" mini framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @see org.feees.sigme.people.persistence.Telephone
 * @see br.ufes.inf.nemo.util.ejb3.persistence.BaseDAO
 */
@Local
public interface TelephoneDAO extends BaseDAO<Telephone> {}
