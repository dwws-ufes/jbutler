package br.ufes.inf.nemo.jbutler.ejb.application;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;

import br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter;
import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject;

/**
 * TODO: document this type.
 *
 * @param <T>
 *          Persistent class that is managed by the service.
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@PermitAll
public abstract class ListingServiceBean<T extends PersistentObject> implements ListingService<T> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ListingServiceBean.class.getCanonicalName());

	/**
	 * Method that provides a mandatory filter based on some business logic. For instance, some users may not be
	 * authorized to list all entities, but just a subset. This filter is used by list(), filter(), count() and
	 * countFiltered() in order to return only the entities that match the mandatory filter. Default implementation
	 * returns null and is ignored by the above methods. Subclasses should override it to provide this feature.
	 * 
	 * @return A mandatory filter to be applied in counting and listing entities. The Map.Entry class is used to return
	 *         both the filter and the value to be applied in filtering.
	 */
	protected Map.Entry<Filter<?>, String> getMandatoryFilter() {
		return null;
	}

	/**
	 * Logs operations over many entities, i.e., listing of entities. Default implementation does nothing, so logging is
	 * optional in the subclasses.
	 * 
	 * @param operation
	 *          The opration that is being logged.
	 * @param entities
	 *          The entities that are being manipulated.
	 * @param interval
	 *          Array of size 2 with the interval [a, b) (retrieves objects from index a through b-1).
	 */
	protected void log(CrudOperation operation, List<T> entities, int ... interval) {
		logger.log(Level.FINE, "Logging (for operations over multiple entities) not overridden by subclass. No need for this type of logging.");
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.ListingService#authorize() */
	public void authorize() {
		logger.log(Level.FINE, "Authorization not overridden by subclass. No need for authorization.");
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.ListingService#count() */
	@Override
	public long count() {
		Map.Entry<Filter<?>, String> mandatoryFilter = getMandatoryFilter();

		logger.log(Level.FINER, "Retrieving the object count (mandatory filter: {0})...", new Object[] { (mandatoryFilter == null ? "none" : mandatoryFilter.getClass().getSimpleName()) });
		if (mandatoryFilter == null) return getDAO().retrieveCount();
		else return getDAO().retrieveFilteredCount(mandatoryFilter.getKey(), mandatoryFilter.getValue());
	}

	/**
	 * @see br.ufes.inf.nemo.jbutler.ejb.application.ListingService#countFiltered(br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter,
	 *      java.lang.String)
	 */
	@Override
	public long countFiltered(Filter<?> filter, String value) {
		Map.Entry<Filter<?>, String> mandatoryFilter = getMandatoryFilter();

		logger.log(Level.FINER, "Retrieving a filtered object count (filter \"{0}\" with value \"{1}\"; mandatory filter: {0})...", new Object[] { filter.getKey(), value, (mandatoryFilter == null ? "none" : mandatoryFilter.getClass().getSimpleName()) });
		if (mandatoryFilter == null) return getDAO().retrieveFilteredCount(filter, value);
		else return getDAO().retrieveFilteredCount(new Filter<?>[] { filter, mandatoryFilter.getKey() }, new String[] { value, mandatoryFilter.getValue() });
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.ListingService#list(int[]) */
	@Override
	public List<T> list(int ... interval) {
		Map.Entry<Filter<?>, String> mandatoryFilter = getMandatoryFilter();

		List<T> entities = (mandatoryFilter == null) ? getDAO().retrieveSome(interval) : getDAO().retrieveSomeWithFilter(mandatoryFilter.getKey(), mandatoryFilter.getValue(), interval);
		log(CrudOperation.LIST, entities, interval);
		return entities;
	}

	/**
	 * @see br.ufes.inf.nemo.jbutler.ejb.application.ListingService#filter(br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter,
	 *      java.lang.String, int[])
	 */
	@Override
	public List<T> filter(Filter<?> filter, String filterParam, int ... interval) {
		Map.Entry<Filter<?>, String> mandatoryFilter = getMandatoryFilter();

		List<T> entities = (mandatoryFilter == null) ? getDAO().retrieveSomeWithFilter(filter, filterParam, interval) : getDAO().retrieveSomeWithFilters(new Filter<?>[] { filter, mandatoryFilter.getKey() }, new String[] { filterParam, mandatoryFilter.getValue() }, interval);
		log(CrudOperation.LIST, entities, interval);
		return entities;
	}

	/**
	 * @see br.ufes.inf.nemo.jbutler.ejb.application.ListingService#fetchLazy(br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObject)
	 */
	@Override
	public T fetchLazy(T entity) {
		// Default implementation is to return the entity itself (there are no lazy attributes).
		logger.log(Level.FINEST, "Using default implementation for fetchLazy(): returning the same entity, unchanged");
		return entity;
	}
}
