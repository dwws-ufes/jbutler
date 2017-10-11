package br.ufes.inf.nemo.jbutler.ejb.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;

import br.ufes.inf.nemo.jbutler.ReflectionUtil;
import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;

/**
 * Abstract application class that implements CrudServiceLocal, providing general functionality that can be reused by
 * application classes that are specific to the application being developed.
 * 
 * Concrete subclasses must implement abstract methods to fill in the blanks that are application-specific, such as what
 * is the DAO class and how to create an empty new entity.
 * 
 * This abstract class provides empty implementations for validate methods to make them optional for the bean developer.
 * One very important such method is
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @param <T>
 *            Persistent class that is managed by the service.
 * @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService
 * @see br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO
 * @see br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
@PermitAll
public abstract class CrudServiceBean<T extends PersistentObject> extends ListingServiceBean<T> implements CrudService<T> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(CrudServiceBean.class.getCanonicalName());
	
	/** The domain class. */
	private Class<T> domainClass;

	/** Constructor. */
	@SuppressWarnings("unchecked")
	public CrudServiceBean() {
		domainClass = (Class<T>)ReflectionUtil.determineTypeArgument(getClass());
	}
	
	/**
	 * Creates an empty entity to be stored. As this is class-specific, this method must be overridden by the
	 * subclasses.
	 * 
	 * @return An empty entity.
	 */
	protected T createNewEntity() {
		try {
			return domainClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "Could not automatically create a new instance of the domain entity inferred from the generic type parameters.", e);
			return null;
		}
	}

	/**
	 * Callback method that allows subclasses to intercept the moment exactly before the persisting (creating or
	 * updating) of the entity object in order to perform any checks that might be necessary due to manipulation of this
	 * object in the presentation layer.
	 * 
	 * @param newEntity
	 *            The entity to be persisted.
	 * @param oldEntity
	 *            The old entity from the database, in case of updates.
	 * 
	 * @return The entity object that is going to be persisted.
	 */
	protected T validate(T newEntity, T oldEntity) {
		logger.log(Level.FINE, "Validation not overridden by subclass. No need for validation.");
		return newEntity;
	}

	/**
	 * Logs operations over one entity, i.e., creation, retrieval, udpate or deletion of an entity. Default
	 * implementation does nothing, so logging is optional in the subclasses.
	 * 
	 * @param operation
	 *            The operation that is being logged.
	 * @param entity
	 *            The entity that is being manipulated.
	 */
	protected void log(CrudOperation operation, T entity) {
		logger.log(Level.FINE, "Logging (for operations over single entities) not overridden by subclass. No need for this type of logging.");
	}

	/**
	 * Helper method that adds a validation error to an existing CRUD exception or creates a new one in case it doesn't
	 * exist yet. This method creates a global message.
	 * 
	 * @param crudException
	 *            The possibly-existing CRUD exception.
	 * @param message
	 *            The developer-friendly message for the exception.
	 * @param messageKey
	 *            The key to find the error message in the resource bundle.
	 * @param messageParams
	 *            The params to be inserted in the error message.
	 * 
	 * @return The newly-created CRUD exception or the old CRUD exception with a new validation error.
	 */
	protected CrudException addGlobalValidationError(CrudException crudException, String message, String messageKey, Object ... messageParams) {
		logger.log(Level.FINER, "Adding a validation error with key \"{0}\"...", messageKey);

		if (crudException == null) {
			crudException = new CrudException(message, messageKey, messageParams);
		}
		else {
			crudException.addValidationError(messageKey, messageParams);
		}
		return crudException;
	}

	/**
	 * Helper method that adds a validation error to an existing CRUD exception or creates a new one in case it doesn't
	 * exist yet. This method attaches the message to a specific field in the form.
	 * 
	 * @param crudException
	 *            The possibly-existing CRUD exception.
	 * @param message
	 *            The developer-friendly message for the exception.
	 * @param fieldName
	 *            The name of the field to which the message should be attached.
	 * @param messageKey
	 *            The key to find the error message in the resource bundle.
	 * @param messageParams
	 *            The params to be inserted in the error message.
	 * 
	 * @return The newly-created CRUD exception or the old CRUD exception with a new validation error.
	 */
	protected CrudException addFieldValidationError(CrudException crudException, String message, String fieldName, String messageKey, Object ... messageParams) {
		logger.log(Level.FINER, "Adding field validation error with key \"{0}\" to field \"{1}\"...", new Object[] { messageKey, fieldName });

		if (crudException == null) {
			crudException = new CrudException(message, fieldName, messageKey, messageParams);
		}
		else {
			crudException.addValidationError(fieldName, messageKey, messageParams);
		}
		return crudException;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService#validateCreate(br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject) */
	@Override
	public void validateCreate(T entity) throws CrudException {
		logger.log(Level.FINE, "Validation of CREATE not overridden by subclass. No need for this type of validation.");
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService#validateUpdate(br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject) */
	@Override
	public void validateUpdate(T entity) throws CrudException {
		logger.log(Level.FINE, "Validation of UPDATE not overridden by subclass. No need for this type of validation.");
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService#validateDelete(br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject) */
	@Override
	public void validateDelete(T entity) throws CrudException {
		logger.log(Level.FINE, "Validation of DELETE not overridden by subclass. No need for this type of validation.");
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService#create(br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject) */
	@Override
	public void create(T entity) {
		// Validates the entity before persisting.
		entity = validate(entity, null);

		// Save the entity.
		log(CrudOperation.CREATE, entity);
		getDAO().save(entity);
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService#retrieve(java.lang.Long) */
	@Override
	public T retrieve(Long id) {
		// Retrieves the real entity from the database.
		T entity = getDAO().retrieveById(id);
		log(CrudOperation.RETRIEVE, entity);
		return entity;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService#update(br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject) */
	@Override
	public void update(T entity) {
		// Validates the entity before persisting.
		entity = validate(entity, getDAO().retrieveById(entity.getId()));

		// Save the entity.
		log(CrudOperation.UPDATE, entity);
		getDAO().save(entity);
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService#delete(br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject) */
	@Override
	public void delete(T entity) {
		// Retrieves the real entity from the database.
		entity = getDAO().retrieveById(entity.getId());
		if (entity != null) {
			// Deletes the entity.
			getDAO().delete(entity);
			log(CrudOperation.DELETE, entity);
		}
	}
}
