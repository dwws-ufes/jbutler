package br.ufes.inf.nemo.jbutler.ejb.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;

/**
 * A generic JSF converter for any domain class that uses the "CRUD mini-framework", i.e., implements PersistentObject.
 * This class converts the entity to its (Long) id and vice-versa and it's useful for "select one/many" components like
 * radio buttons, menus, lists, etc.
 * 
 * <i>This class is part of the Engenho de Software CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @param <T>
 *            Persistent class to/from which the conversion happens.
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 * @see br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject
 */
public class PersistentObjectConverterFromId<T extends PersistentObject> implements Converter, Serializable {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(PersistentObjectConverterFromId.class.getCanonicalName());

	/** The DAO used to retrieve objects given their IDs. */
	private BaseDAO<T> dao;

	/** The persistent class being handled by this converter. */
	private Class<T> persistentClass;

	/**
	 * Constructor using fields.
	 * 
	 * @param dao
	 *            The DAO used to retrieve objects given their IDs.
	 */
	public PersistentObjectConverterFromId(BaseDAO<T> dao) {
		this.dao = dao;
		persistentClass = dao.getDomainClass();
	}

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		T entity = null;
		logger.log(Level.FINEST, "Trying to convert to an instance of {0} from the id: {1}", new Object[] { persistentClass.getSimpleName(), value });

		// Checks for nulls and empties.
		if ((value != null) && (value.trim().length() > 0)) {
			// Loads the entity given the id.
			try {
				Long id = Long.valueOf(value);
				entity = dao.retrieveById(id);
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "Value is not a number (Long): {0}", value);
				return null;
			}
		}

		// Logs the result and returns.
		logger.log(Level.FINE, "Returning: {0}", entity);
		return entity;
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		logger.log(Level.FINEST, "Trying to convert an instance of {0}: {1}", new Object[] { persistentClass.getSimpleName(), value });

		// Checks that the supplied value is an entity of the class referred to by the converter.
		if ((value != null) && (value.getClass().equals(persistentClass))) {
			@SuppressWarnings("unchecked")
			T entity = (T) value;

			// Checks for null id and returns the id converted to string.
			if (entity.getId() != null) return entity.getId().toString();
		}

		// If it doesn't pass one of the previous checks, return an empty string.
		return "";
	}
}
