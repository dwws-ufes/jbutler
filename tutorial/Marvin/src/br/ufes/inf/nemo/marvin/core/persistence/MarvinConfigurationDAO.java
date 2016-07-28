package br.ufes.inf.nemo.marvin.core.persistence;

import javax.ejb.Local;

import br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import br.ufes.inf.nemo.marvin.core.domain.MarvinConfiguration;

/**
 * Interface for a DAO for objects of the MarvinConfiguration domain class.
 * 
 * Using a mini CRUD framework for EJB3, basic DAO operation definitions are inherited from the superclass, whereas
 * operations that are specific to the managed domain class (if any) are specified in this class.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @see br.ufes.inf.nemo.util.ejb3.persistence.BaseDAO
 */
@Local
public interface MarvinConfigurationDAO extends BaseDAO<MarvinConfiguration> {
	/**
	 * TODO: document this method.
	 * 
	 * @return
	 * @throws PersistentObjectNotFoundException
	 */
	MarvinConfiguration retrieveCurrentConfiguration() throws PersistentObjectNotFoundException;
}
