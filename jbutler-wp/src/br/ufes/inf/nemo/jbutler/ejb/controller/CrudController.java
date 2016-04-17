package br.ufes.inf.nemo.jbutler.ejb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;

import br.ufes.inf.nemo.jbutler.ReflectionUtil;
import br.ufes.inf.nemo.jbutler.ejb.application.CrudException;
import br.ufes.inf.nemo.jbutler.ejb.application.CrudService;
import br.ufes.inf.nemo.jbutler.ejb.application.CrudValidationError;
import br.ufes.inf.nemo.jbutler.ejb.application.ListingService;
import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;

/**
 * Base class for classes that provide controller functionality for CRUD use cases.
 * 
 * This class integrates with the CrudServiceLocal (EJB3) interface and must deal with a subclass of PersistentObject,
 * which is specified as a generic parameter, along with the class of its ID. It provides basic controller functions,
 * mediating the communication between CRUD web pages and CRUD service classes.
 * 
 * <i>This class is part of the Engenho de Software CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @param <T>
 *            Entity manipulated by the CRUD use case.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 * @see br.ufes.inf.nemo.jbutler.ejb.application.CrudService
 * @see br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject
 */
public abstract class CrudController<T extends PersistentObject> extends ListingController<T> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(CrudController.class.getCanonicalName());

	/** Output: if the data is read-only. */
	protected boolean readOnly = false;

	/** Output: the list of entities to delete. */
	protected SortedSet<T> trashCan = new TreeSet<T>();

	/**
	 * Getter for readOnly.
	 * 
	 * @return <code>true</code> if the data is read-only, <code>false</code> otherwise.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Getter for trashCan, encapsulated in a List so it can be shown in a dataTable.
	 * 
	 * @return The list of entities to delete.
	 */
	public List<T> getTrashCan() {
		return new ArrayList<T>(trashCan);
	}

	/**
	 * Obtains the name of the form field in the HTML page, given the name of the property it represents. It defaults to
	 * "form:" + propertyName, but it can be overridden in case of need.
	 * 
	 * @param propertyName
	 *            The name of the property.
	 * @return The name of the field.
	 */
	protected String getFieldName(String propertyName) {
		return "form:" + propertyName;
	}

	/**
	 * Provides the CRUD service class to other methods that need it. This method must be overridden by subclasses, each
	 * one providing its specific CRUD service.
	 * 
	 * @return A service class that complies to the CRUD specification.
	 */
	protected abstract CrudService<T> getCrudService();

	/** @see br.ufes.inf.nemo.jbutler.ejb.controller.ListingController#getListingService() */
	protected ListingService<T> getListingService() {
		return getCrudService();
	}

	/**
	 * Method called by the constructor to initialize the entity. Could be overridden by subclasses to also initialize
	 * auxiliary objects.
	 * 
	 * @return A blank new entity belonging to the entity class.
	 */
	@SuppressWarnings("unchecked")
	protected T createNewEntity() {
		Class<T> clazz = (Class<T>) ReflectionUtil.determineTypeArgument(getClass());
		try {
			return clazz.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "Could not automatically create a new instance of the domain entity inferred from the generic type parameters.", e);
			return null;
		}
	}

	/**
	 * Method called by the retrieve and update scenarios to check if all is OK with the retrieved entity. This method
	 * is intended to be overridden by subclasses to implement specific behavior, such as management of sub-entities.
	 */
	protected void checkSelectedEntity() {
		logger.log(Level.INFO, "checkSelectedEntity() not overridden by subclass. Doing nothing");
	}

	/**
	 * Prepares the entity to be saved. This method is intended to be overridden by subclasses which implement specific
	 * behavior, such as management of sub-entities.
	 */
	protected void prepEntity() {
		logger.log(Level.INFO, "prepEntity() not overridden by subclass. Doing nothing");
	}

	/**
	 * Returns a summarized string representation of the selected entity, so we can inform the user what has been just
	 * created or updated. The basic implementation just returns the default string representation of the entity
	 * (toString()), therefore if that representation is not summarized enough for a faces message, it is advised to
	 * override it.
	 * 
	 * @return A string summarizing the selected entity.
	 */
	protected String summarizeSelectedEntity() {
		logger.log(Level.INFO, "summarizeSelectedEntity() not overridden by subclass. Returning the entity's toString(): {0}", selectedEntity);
		return "" + selectedEntity;
	}

	/**
	 * Builds a string with the contents of the trash, so we can inform the user what has been just deleted. The basic
	 * implementation just returns the size of the trash, therefore it is advised to override it.
	 * 
	 * @return A string representation of the contents of the trash.
	 */
	protected String listTrash() {
		logger.log(Level.INFO, "listTrash() not overridden by subclass. Returning trashCan size: {0}", trashCan.size());
		return "" + trashCan.size();
	}

	/**
	 * Retrieves an existing entity from the business layer, given its ID. Sets it as the selected entity. This method
	 * is intended to be used internally.
	 * 
	 * @param id
	 *            Persistent id of the entity to be retrieved.
	 */
	public void retrieveExistingEntity(Long id) {
		// Checks if we're creating a new entity or updating/visualizing an existing one.
		if (id != null) {
			// Retrieve the selected entity again from the business layer. This merges it to the current session and
			// updates
			// its data, avoiding some problems such as stale data and lazy-loading.
			logger.log(Level.INFO, "Retrieving from the application layer entity with id {0}", id);
			selectedEntity = getCrudService().retrieve(id);

			// Asks the CRUD service to fetch any lazy collection that possibly exists.
			selectedEntity = getCrudService().fetchLazy(selectedEntity);

			// Check if the entity is sane (no nulls where there shouldn't be).
			checkSelectedEntity();
		}
	}

	/**
	 * Moves the selected entity to the trash can for possible future deletion.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void trash() {
		// Proceed only if there is a selected entity.
		if (selectedEntity == null) {
			logger.log(Level.WARNING, "Method trash() called, but selectedEntity is null!");
			return;
		}

		// Adds the selected entity to the trash can so the user can confirm the deletion.
		logger.log(Level.INFO, "Adding {0} (id {1}) to the trash can for future deletion.", new Object[] { selectedEntity, selectedEntity.getId() });
		trashCan.add(selectedEntity);
	}

	/**
	 * Cancel deletion and cleans the trash can.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void cancelDeletion() {
		// Removes all entities from the trash and cancel their deletion.
		logger.log(Level.INFO, "Deletion has been cancelled. Clearing trash can");
		trashCan.clear();

		// Clears the selection.
		selectedEntity = null;
	}

	/**
	 * Displays the form for the creation of a new entity.
	 * 
	 * @return The view path for the input form.
	 */
	public String create() {
		logger.log(Level.INFO, "Displaying form for entity creation");

		// Sets the data as read-write.
		readOnly = false;

		// Resets the entity so we can create a new one.
		selectedEntity = createNewEntity();

		// Goes to the form.
		return getViewPath() + "form.xhtml?faces-redirect=" + getFacesRedirect();
	}

	/**
	 * Shortcut to retrieve(null).
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.CrudController#retrieve(Long)
	 * @return The view path for the input form.
	 */
	public String retrieve() {
		return retrieve(null);
	}

	/**
	 * Displays the form with the data of the selected entity. Sets the data as read-only.
	 * 
	 * @param id
	 *            The persistence id of the selected entity that may need to be retrieved.
	 * 
	 * @return The view path for the input form.
	 */
	public String retrieve(Long id) {
		logger.log(Level.INFO, "Displaying form for entity retrieval");

		// Sets the data as read-only.
		readOnly = true;

		// Retrieves the existing entity that was selected, if not already done by the JSF component.
		if (selectedEntity == null) retrieveExistingEntity(id);
		else {
			// Asks the CRUD service to fetch any lazy collection that possibly exists.
			selectedEntity = getCrudService().fetchLazy(selectedEntity);
			checkSelectedEntity();
		}

		// Goes to the form.
		return getViewPath() + "form.xhtml?faces-redirect=" + getFacesRedirect();
	}

	/**
	 * Shortcut to update(null).
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.CrudController#update(Long)
	 * @return The view path for the input form.
	 */
	public String update() {
		return update(null);
	}

	/**
	 * Displays the form with the data of the selected entity for updating the entity (leaves it read-write).
	 * 
	 * @param id
	 *            The persistence id of the selected entity that may need to be retrieved.
	 * 
	 * @return The view path for the input form.
	 */
	public String update(Long id) {
		logger.log(Level.INFO, "Displaying form for entity update");

		// Sets the data as read-write.
		readOnly = false;

		// Retrieves the existing entity that was selected, if not already done by the JSF component.
		if (selectedEntity == null) retrieveExistingEntity(id);
		else {
			// Asks the CRUD service to fetch any lazy collection that possibly exists.
			selectedEntity = getCrudService().fetchLazy(selectedEntity);
			checkSelectedEntity();
		}

		// Goes to the form.
		return getViewPath() + "form.xhtml?faces-redirect=" + getFacesRedirect();
	}

	/**
	 * Saves (create or update) the entity based on the data sent from the form.
	 * 
	 * @return The view path of the listing if no problems occurred. Otherwise, return null to go back to the form.
	 */
	public String save() {
		logger.log(Level.INFO, "Saving entity...");

		// Prepare the entity for saving.
		prepEntity();

		// Checks if we want to create or update the entity. Validates the operation first and stops in case of errors.
		try {
			if (selectedEntity.getId() == null) {
				getCrudService().validateCreate(selectedEntity);
				getCrudService().create(selectedEntity);
				addGlobalI18nMessage(getBundleName(), FacesMessage.SEVERITY_INFO, getBundlePrefix() + ".text.createSucceeded", summarizeSelectedEntity());
			}
			else {
				getCrudService().validateUpdate(selectedEntity);
				getCrudService().update(selectedEntity);
				addGlobalI18nMessage(getBundleName(), FacesMessage.SEVERITY_INFO, getBundlePrefix() + ".text.updateSucceeded", summarizeSelectedEntity());
			}
		}
		catch (CrudException crudException) {
			// Adds an error message to each validation error included in the exception.
			for (CrudValidationError error : crudException) {
				logger.log(Level.WARNING, "Exception while saving " + selectedEntity, crudException.getMessage());

				// Checks if the field name was specified. If it was, attach the message to the form field.
				if (error.getFieldName() != null) addFieldI18nMessage(getFieldName(error.getFieldName()), getBundleName(), FacesMessage.SEVERITY_ERROR, error.getMessageKey(), error.getMessageParams());
				else addGlobalI18nMessage(getBundleName(), FacesMessage.SEVERITY_ERROR, error.getMessageKey(), error.getMessageParams());
			}

			// Goes back to the same page, i.e., the form.
			return null;
		}

		// Goes back to the listing.
		return list();
	}

	/**
	 * Deletes all the entities in the trash can. If there are any problems deleting entities, they will be displayed in
	 * the listing page afterwards.
	 * 
	 * @return The view path of the listing.
	 */
	public String delete() {
		logger.log(Level.INFO, "Deleting entities...");
		List<Object> notDeleted = new ArrayList<Object>();

		// Deletes the entities that are in the trash can. Validates each exclusion, but don't stop in case of errors.
		for (T entity : trashCan)
			try {
				getCrudService().validateDelete(entity);
				getCrudService().delete(entity);
			}
			catch (CrudException crudException) {
				// Displays error messages as global. Don't stop the deletion process.
				logger.log(Level.WARNING, "Exception while deleting " + entity, crudException.getMessage());
				for (CrudValidationError error : crudException)
					addGlobalI18nMessage(getBundleName(), FacesMessage.SEVERITY_ERROR, error.getMessageKey(), error.getMessageParams());

				// Marks the entity to be removed from the trash can, so we won't list it in the successfull message.
				notDeleted.add(entity);
			}

		// Writes the status message (only if at least one entity was deleted successfully). Empties it afterwards.
		trashCan.removeAll(notDeleted);
		if (!trashCan.isEmpty()) {
			addGlobalI18nMessage(getBundleName(), FacesMessage.SEVERITY_INFO, getBundlePrefix() + ".text.deleteSucceeded", listTrash());
			trashCan.clear();
		}

		// Goes back to the listing.
		return list();
	}
}
