package br.ufes.inf.nemo.jbutler.ejb.application;

import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;

/**
 * Interface for application classes (service classes) that implement CRUD use cases (use cases that define five
 * scenarios: list, create, retrieve, update and delete). A CRUD use case, and therefore its implementing class, refers
 * to a class, that is manipulated in that use case.
 * 
 * This interface defines methods that service classes should provide implementation for. It uses a generic types,
 * <code>T</code>, to represent the class that is manipulated by the use case, which must implement PersistentObject.
 * 
 * For instance, to implement a CRUD service for a Product class, use:
 * <code>public class ProductCrudService implements CrudServiceLocal&lt;Product&gt;</code>. Product must implement
 * PersistentObject.
 * 
 * This interface can be used together with interfaces/classes that are used in the presentation layer, forming a simple
 * CRUD framework that integrates with Java EE 6 and makes the task of implementing CRUD use cases easier. It also
 * provides a default implementation, CrudService.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @param <T>
 *            Persistent class that is managed by the service.
 * @see br.ufes.inf.nemo.jbutler.ejb.application.CrudServiceBean
 * @see br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public interface CrudService<T extends PersistentObject> extends ListingService<T> {
	/**
	 * Checks if there are errors with object creation.
	 * 
	 * @param entity
	 *            Entity object coming from the presentation layer, containing the data that is going to be sent to the
	 *            create() method for entity creation.
	 * 
	 * @throws CrudException
	 *             In case business constraints aren't followed.
	 */
	void validateCreate(T entity) throws CrudException;

	/**
	 * Checks if there are errors with object update.
	 * 
	 * @param entity
	 *            Entity object coming from the presentation layer, containing the ID of the existing entity and the
	 *            data, both of which are going to be sent to the update() method for entity update.
	 * 
	 * @throws CrudException
	 *             In case business constraints aren't followed.
	 */
	void validateUpdate(T entity) throws CrudException;

	/**
	 * Checks if there are errors with object deletion.
	 * 
	 * @param entity
	 *            Entity object coming from the presentation layer, containing the ID of the existing entity that is
	 *            going to be sent to the delete() method for entity deletion.
	 * 
	 * @throws CrudException
	 *             In case business constraints aren't followed.
	 */
	void validateDelete(T entity) throws CrudException;

	/**
	 * Creates a new entity of the manipulated class, i.e., persists a new instance.
	 * 
	 * @param entity
	 *            A new entity object to be persisted.
	 */
	void create(T entity);

	/**
	 * Retrieves an existing entity from the persistent store, given its ID.
	 * 
	 * @param id
	 *            The ID of the existing entity.
	 * 
	 * @return The existing entity or <code>null</code>, if the given ID doesn't correspond to any entities.
	 */
	T retrieve(Long id);

	/**
	 * Updates the data of an existing entity in the persistent store.
	 * 
	 * @param entity
	 *            An existing entity object to be persisted.
	 */
	void update(T entity);

	/**
	 * Deletes an existing entity, i.e., removes it from the persistent store.
	 * 
	 * @param entity
	 *            An existing entity object to be deleted.
	 */
	void delete(T entity);
}
