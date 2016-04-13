package br.ufes.inf.nemo.jbutler.ejb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.ufes.inf.nemo.jbutler.ejb.application.ListingService;
import br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter;
import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;

/**
 * Base class for classes that provide controller functionality for listing use cases.
 * 
 * This class integrates with the ListingService (EJB3) interface and must deal with a subclass of PersistentObject,
 * which is specified as a generic parameter, along with the class of its ID. It provides basic functionality for
 * listing peristent objects.
 * 
 * <i>This class is part of the Engenho de Software CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @param <T>
 *            Entity manipulated by the listing use case.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 * @see br.ufes.inf.nemo.jbutler.ejb.application.ListingService
 * @see br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject
 */
public abstract class ListingController<T extends PersistentObject> extends JSFController {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ListingController.class.getCanonicalName());

	/** The view path where the web pages are located. */
	protected String viewPath;

	/** List navigation: the index of the first entity being displayed. */
	protected int firstEntityIndex = -1;

	/** List navigation: the index of the last entity being displayed. */
	protected int lastEntityIndex;

	/** List navigation: the number of existing entities in the persistence media. */
	protected long entityCount;

	/** Output: the list of existing entities. */
	protected List<T> entities;

	/** Input/Output: the selected entity among the list of existing entities. */
	protected T selectedEntity;

	/** Primefaces lazy data model for use with a lazy p:dataTable component. */
	protected LazyDataModel<T> lazyEntities;

	/** Output: available filters. */
	protected List<Filter<?>> filters;

	/** Output: filter label, what is shown in the web page (when multiple-choice, avoids displaying the id). */
	protected String filterLabel;

	/** Input: the selected filter's ID. */
	protected String filterKey;

	/** Output: the currently selected filter. */
	protected Filter<?> filter;

	/** Output: a flag indicating if filtering is on or not. */
	protected boolean filtering = false;

	/** Input: the filter parameter. */
	protected String filterParam;

	/** Internal control: a map to locate the filter given its field name. */
	protected Map<String, Filter<?>> filtersMap = new TreeMap<String, Filter<?>>();

	/**
	 * Getter for firstEntityIndex.
	 * 
	 * @return The index of the first entity being displayed.
	 */
	public int getFirstEntityIndex() {
		return firstEntityIndex;
	}

	/**
	 * Getter for lastEntityIndex.
	 * 
	 * @return The index of the last entity being displayed.
	 */
	public int getLastEntityIndex() {
		return lastEntityIndex;
	}

	/**
	 * Getter for filters.
	 * 
	 * @return The available filters.
	 */
	public List<Filter<?>> getFilters() {
		// Lazily initialize the list of filters.
		if (filters == null) {
			filters = new ArrayList<Filter<?>>();
			initFilters();
		}
		return filters;
	}

	/**
	 * Getter for filterLabel.
	 * 
	 * @return The filter label, what is shown in the web page (when multiple-choice, avoids displaying the id).
	 */
	public String getFilterLabel() {
		return filterLabel;
	}

	/**
	 * Getter for filter.
	 * 
	 * @return The currently selected filter.
	 */
	public Filter<?> getFilter() {
		return filter;
	}

	/**
	 * Getter for filtering.
	 * 
	 * @return A flag indicating if filtering is on or not.
	 */
	public boolean isFiltering() {
		return filtering;
	}

	/**
	 * Getter for filterKey.
	 * 
	 * @return The selected filter's ID.
	 */
	public String getFilterKey() {
		return filterKey;
	}

	/**
	 * Setter for filterKey.
	 * 
	 * @param filterKey
	 *            The selected filter's ID.
	 */
	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	/**
	 * Getter for filterParam.
	 * 
	 * @return The filter parameter.
	 */
	public String getFilterParam() {
		return filterParam;
	}

	/**
	 * Setter for filterParam.
	 * 
	 * @param filterParam
	 *            The filter parameter.
	 */
	public void setFilterParam(String filterParam) {
		this.filterParam = filterParam;
	}

	/**
	 * Getter for entityCount.
	 * 
	 * @return The number of existing entities in the persistence media.
	 */
	public long getEntityCount() {
		return entityCount;
	}

	/**
	 * Getter for entities.
	 * 
	 * @return The list of existing entities.
	 */
	public List<T> getEntities() {
		if (entities == null) goFirst();
		return entities;
	}

	/**
	 * Getter for lazyEntities.
	 * 
	 * @return Primefaces lazy data model for use with a lazy p:dataTable component.
	 */
	public LazyDataModel<T> getLazyEntities() {
		if (lazyEntities == null) {
			count();
			lazyEntities = new PrimefacesLazyEntityDataModel<T>(getListingService().getDAO()) {
				/** Serialization id. */
				private static final long serialVersionUID = 1117380513193004406L;

				/**
				 * @see org.primefaces.model.LazyDataModel#load(int, int, java.lang.String,
				 *      org.primefaces.model.SortOrder, java.util.Map)
				 */
				@Override
				public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					firstEntityIndex = first;
					lastEntityIndex = first + pageSize;
					retrieveEntities();
					return entities;
				}
			};
			lazyEntities.setRowCount((int) entityCount);
		}

		return lazyEntities;
	}

	/**
	 * Getter for selectedEntity.
	 * 
	 * @return The selected entity among the list of existing entities.
	 */
	public T getSelectedEntity() {
		// Forces authorization check at the CRUD service.
		getListingService();

		return selectedEntity;
	}

	/**
	 * Setter for selectedEntity.
	 * 
	 * @param selectedEntity
	 *            The selected entity among the list of existing entities.
	 */
	public void setSelectedEntity(T selectedEntity) {
		this.selectedEntity = selectedEntity;
		logger.log(Level.FINEST, "Entity \"{0}\" has been selected", selectedEntity);
	}

	/**
	 * Informs to other methods what is the view path where the web pages are to be located. This method may be
	 * overridden by subclasses if they don't follow the standard naming convention for Crud Controllers, which is:
	 * <code>com.yourdomain.yoursystem.package.controller.ManageObjectController</code> which would lead to a view path
	 * of <code>/package/manageObject/</code>.
	 * 
	 * @return The view path string.
	 */
	public String getViewPath() {
		// If this method is not overridden, tries to guess the view path from the class name.
		if (viewPath == null) {
			// Starts with the fully-qualified name of the class (package and name).
			int idx;
			String pkg = "", service = "";
			String classFullName = getClass().getCanonicalName();

			// Searches for the name of the package according to the name convention (before ".controller.").
			idx = classFullName.indexOf(".controller.");
			if (idx != -1) {
				pkg = classFullName.substring(0, idx);
				idx = pkg.lastIndexOf('.');
				if (idx != -1) pkg = pkg.substring(idx + 1);
				pkg = "/" + pkg;
			}

			// Searches for the name of the service according to convention (class name, removing trailing Controller).
			idx = classFullName.lastIndexOf(".");
			service = (idx == -1) ? classFullName : classFullName.substring(idx + 1);
			idx = service.indexOf("Controller");
			if (idx != -1) service = service.substring(0, idx);
			if (service.length() > 1) service = Character.toLowerCase(service.charAt(0)) + service.substring(1);

			// Builds the view path with the name of the package and class.
			viewPath = pkg + "/" + service + "/";
			logger.log(Level.INFO, "View path not provided by subclass, thus guessing from naming convention: {0}", viewPath);
		}
		return viewPath;
	}

	/**
	 * Provides the listing service class to other methods that need it. This method must be overridden by subclasses,
	 * each one providing its specific listing service.
	 * 
	 * @return A service class that complies to the listing specification.
	 */
	protected abstract ListingService<T> getListingService();

	/**
	 * Initializes the collection of filters that indicate to the view what kind of filtering can be done with the list
	 * of entities.
	 */
	protected abstract void initFilters();

	/**
	 * Adds a filter to this controller.
	 * 
	 * @param filter
	 *            The filter object representing a kind of filtering.
	 */
	protected void addFilter(Filter<?> filter) {
		logger.log(Level.INFO, "Adding filter: {0} ({1})", new Object[] { filter.getKey(), filter.getType() });
		if (this.filter == null) this.filter = filter;
		filtersMap.put(filter.getKey(), filter);
		filters.add(filter);
	}

	/**
	 * Retrieves the entity count and stores it for future use.
	 */
	protected void count() {
		logger.log(Level.INFO, "Counting entities. Filtering is {0}", (filtering ? "ON" : "OFF"));

		// Checks if there's an active filter.
		if (filtering)
			// There is. Count only filtered entities.
			entityCount = getListingService().countFiltered(filter, filterParam);
		else
			// There's not. Count all entities.
			entityCount = getListingService().count();

		// Since the entity count might have changed, force reloading of the lazy entity model.
		lazyEntities = null;

		// Updates the index of the last entity and checks if it has gone over the limit.
		lastEntityIndex = firstEntityIndex + MAX_DATA_TABLE_ROWS_PER_PAGE;
		if (lastEntityIndex > entityCount) lastEntityIndex = (int) entityCount;
	}

	/**
	 * Retrieves a collection of entities, respecting the selected range. Makes the collection available to the view.
	 * This method is intended to be used internally.
	 */
	protected void retrieveEntities() {
		// Checks if the last entity index is over the number of entities and correct it.
		if (lastEntityIndex > entityCount) lastEntityIndex = (int) entityCount;

		// Checks if there's an active filter.
		if (filtering) {
			// There is. Retrieve not only within range, but also with filtering.
			logger.log(Level.INFO, "Retrieving from the application layer {0} of a total of {1} entities: interval [{2}, {3}) using filter \"{4}\" and search param \"{5}\"", new Object[] { (lastEntityIndex - firstEntityIndex), entityCount, firstEntityIndex, lastEntityIndex, filter.getKey(), filterParam });
			entities = getListingService().filter(filter, filterParam, firstEntityIndex, lastEntityIndex);
		}
		else {
			// There's not. Retrieve all entities within range.
			logger.log(Level.INFO, "Retrieving from the application layer {0} of a total of {1} entities: interval [{2}, {3})", new Object[] { (lastEntityIndex - firstEntityIndex), entityCount, firstEntityIndex, lastEntityIndex });
			entities = getListingService().list(firstEntityIndex, lastEntityIndex);
		}

		// Adjusts the last entity index.
		lastEntityIndex = firstEntityIndex + entities.size();
	}

	/**
	 * Sets the indices to the first page of entities and retrieve them.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void goFirst() {
		// Move the first entity index to zero to show the first page.
		firstEntityIndex = 0;

		// Always counts the entities in this method, as it can be called via AJAX from the pages. This also sets the
		// last
		// entity index.
		count();

		// Retrieve the entities from the application layer.
		retrieveEntities();
	}

	/**
	 * Sets the indices to the previous page of entities and retrieve them.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void goPrevious() {
		// Only moves to the previous page if there is one.
		if (firstEntityIndex > 0) {
			// Shift the first entity index backward by the max number of entities in a page.
			firstEntityIndex -= MAX_DATA_TABLE_ROWS_PER_PAGE;

			// Checks if, by any chance, the above shifting took the first entity index too far and correct it.
			if (firstEntityIndex < 0) firstEntityIndex = 0;

			// Always counts the entities in this method, as it can be called via AJAX from the pages. This also sets
			// the last
			// entity index.
			count();

			// Retrieve the entities from the application layer.
			retrieveEntities();
		}
	}

	/**
	 * Sets the indices to the next page of entities and retrieve them.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void goNext() {
		// Always counts the entities in this method, as it can be called via AJAX from the pages.
		count();

		// Only moves to the next page if there is one.
		if (lastEntityIndex < entityCount) {
			// Shift the first entity index forward by the max number of entities in a page.
			firstEntityIndex += MAX_DATA_TABLE_ROWS_PER_PAGE;

			// Set the last entity index to a full page of entities starting from the first index.
			lastEntityIndex = firstEntityIndex + MAX_DATA_TABLE_ROWS_PER_PAGE;

			// Retrieve the entities from the application layer.
			retrieveEntities();
		}
	}

	/**
	 * Sets the indices to the last page of entities and retrieve them.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void goLast() {
		// Always counts the entities in this method, as it can be called via AJAX from the pages.
		count();

		// Checks for the trivial case of no entities.
		if (entityCount == 0) firstEntityIndex = lastEntityIndex = 0;
		else {
			// Calculates how many entities there are in the last page (the remainder of dividing the count by the max
			// entities in a page).
			int remainder = ((int) entityCount % MAX_DATA_TABLE_ROWS_PER_PAGE);

			// Check if the remainder is zero, in which case the last page is full. Otherwise, the remainder is the
			// number of
			// entities in
			// the last page. Sets the first and last index accordingly.
			firstEntityIndex = (remainder == 0) ? (int) entityCount - MAX_DATA_TABLE_ROWS_PER_PAGE : (int) entityCount - remainder;
			lastEntityIndex = (int) entityCount;
		}

		// Retrieve the entities from the application layer.
		retrieveEntities();
	}

	/**
	 * Changes the filter so the filter bar can be reloaded properly.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void changeFilter() {
		// If filtering, cancel it.
		if (filtering) cancelFilter();

		// Clears the selection.
		selectedEntity = null;

		// Gets the filter from the map and stores in the appropriate property.
		if ((filterKey != null) && (filterKey.length() > 0)) {
			logger.log(Level.INFO, "Changing filter to: {0}", filterKey);
			filterParam = null;
			filter = filtersMap.get(filterKey);
			filtering = false;
		}
	}

	/**
	 * Filters the list of entities using a given criteria.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void filter() {
		// Checks if the necessary parameters are not null or empty.
		if ((filterKey != null) && (filterKey.length() > 0) && (filterParam != null) && (filterParam.length() > 0)) {
			// Gets the filter from the map and stores in the appropriate property.
			logger.log(Level.INFO, "Filtering entities using filter {0} and param \"{1}\"", new Object[] { filterKey, filterParam });
			filter = filtersMap.get(filterKey);
			filterLabel = filter.getOptionLabel(filterParam);
			filtering = true;

			// Clears the selection.
			selectedEntity = null;

			// After performing a search, always go to the first page of entities.
			goFirst();
		}
	}

	/**
	 * Clears the filtering information.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void cancelFilter() {
		logger.log(Level.INFO, "Clearing filter information");
		filtering = false;

		// Clears the selection.
		selectedEntity = null;

		// After canceling a search, always go to the first page of entities.
		goFirst();
	}

	/**
	 * Indicates if the JSF framework should use REDIRECT after processing the main CRUD functionalities.
	 * 
	 * @return <code>true</code>, if REDIRECT should be used, <code>false</code> otherwise.
	 */
	public boolean getFacesRedirect() {
		return true;
	}

	/**
	 * Displays the list of entities, which then provides access to every other CRUD functionality.
	 * 
	 * @return The view path for the listing of entities.
	 */
	public String list() {
		logger.log(Level.INFO, "Listing entities...");

		// Clears the selection.
		selectedEntity = null;

		// Gets the entity count.
		count();

		// Checks if the index of the listing should be changed and reload the page.
		if (firstEntityIndex < 0) goFirst();
		else if (lastEntityIndex > entityCount) goLast();
		else retrieveEntities();

		// Goes to the listing.
		return getViewPath() + "list.xhtml?faces-redirect=" + getFacesRedirect();
	}
}
