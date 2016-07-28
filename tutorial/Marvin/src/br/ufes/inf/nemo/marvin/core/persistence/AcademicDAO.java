package br.ufes.inf.nemo.marvin.core.persistence;

import javax.ejb.Local;

import br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import br.ufes.inf.nemo.marvin.core.domain.Academic;

/**
 * TODO: document this type.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Local
public interface AcademicDAO extends BaseDAO<Academic> {
	/**
	 * TODO: document this method.
	 * 
	 * @param email
	 * @return
	 * @throws PersistentObjectNotFoundException
	 * @throws MultiplePersistentObjectsFoundException
	 */
	Academic retrieveByEmail(String email) throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException;
}
