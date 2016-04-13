package br.ufes.inf.nemo.jbutler.ejb.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.ListDataModel;

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
public class PrimefacesSelectableEntityDataModel<T extends PersistentObject> extends ListDataModel<T> implements SelectableDataModel<T> {
	/** The logger. */
	private static final Logger logger = Logger.getLogger(PrimefacesSelectableEntityDataModel.class.getCanonicalName());

	/** If not used in a CRUD, the controller should provide the DAO that can access the row data. */
	private BaseDAO<T> entityDAO;

	/**
	 * Constructor from superclass, using fields.
	 * 
	 * @param entities
	 *            Elements that populate the data model.
	 * @param entityDAO
	 *            The DAO for objects of the type that populate the data model.
	 */
	public PrimefacesSelectableEntityDataModel(List<T> entities, BaseDAO<T> entityDAO) {
		super(entities);
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
