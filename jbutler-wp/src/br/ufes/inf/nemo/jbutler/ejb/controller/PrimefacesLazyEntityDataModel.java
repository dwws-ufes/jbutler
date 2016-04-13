package br.ufes.inf.nemo.jbutler.ejb.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;

import br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;
import br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;

/**
 * Abstract implementation of a PrimeFaces' lazy data model for persistent entities.
 *
 * @param <T>
 *            Type of the objects that populate the data model.
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class PrimefacesLazyEntityDataModel<T extends PersistentObject> extends LazyDataModel<T> implements SelectableDataModel<T> {
	/** Serialization id. */
	private static final long serialVersionUID = 3299810696986238264L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(PrimefacesLazyEntityDataModel.class.getCanonicalName());

	/** If not used in a CRUD, the controller should provide the DAO that can access the row data. */
	private BaseDAO<T> entityDAO;

	/**
	 * Constructor from superclass.
	 * 
	 * @param entityDAO
	 *            The DAO for objects of the type that populate the data model.
	 */
	public PrimefacesLazyEntityDataModel(BaseDAO<T> entityDAO) {
		this.entityDAO = entityDAO;
	}

	/** @see org.primefaces.model.LazyDataModel#getRowKey(java.lang.Object) */
	@Override
	public Object getRowKey(T object) {
		logger.log(Level.FINEST, "Obtaining the row key of object \"{0}\" from the data model", object);
		return object.getUuid();
	}

	/** @see org.primefaces.model.LazyDataModel#getRowData(java.lang.String) */
	@Override
	public T getRowData(String rowKey) {
		logger.log(Level.FINEST, "Obtaining the row data for key \"{0}\" from the data model", rowKey);

		try {
			return entityDAO.retrieveByUuid(rowKey);
		}
		catch (PersistentObjectNotFoundException e) {
			logger.log(Level.WARNING, "Trying to obtain row data from entity with UUID {0} but no entity with that UUID was found");
			return null;
		}
		catch (MultiplePersistentObjectsFoundException e) {
			logger.log(Level.WARNING, "Trying to obtain row data from entity with UUID {0} but multiple entities with that UUID were found");
			return null;
		}
	}
}
