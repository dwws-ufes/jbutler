package br.ufes.inf.nemo.jbutler.ejb.application.filters;

/**
 * For a boolean filter, only the fieldName has to be set. A boolean field (e.g., checkbox) should be shown at the GUI
 * and the user can switch true/false. At the persistence level, the database query will search for the search parameter
 * in the given fieldName using the operator = and a boolean value. E.g.: searching for users that are active or not.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class BooleanFilter extends AbstractFilter<Void> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor from superclass.
	 * 
	 * @param key
	 *          The filter type unique identifier.
	 * @param fieldName
	 *          The field that will be filtered.
	 * @param label
	 *          The label for user interfaces.
	 */
	public BooleanFilter(String key, String fieldName, String label) {
		super(key, fieldName, label);
	}

	/**
	 * Constructor from superclass.
	 * 
	 * @param key
	 *          The filter type unique identifier.
	 * @param fieldName
	 *          The field that will be filtered.
	 * @param label
	 *          The label for user interfaces.
	 * @param criteria
	 *          The criteria to be applied to the filter query.
	 */
	public BooleanFilter(String key, String fieldName, String label, Criterion ... criteria) {
		super(key, fieldName, label, criteria);
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getType() */
	@Override
	public FilterType getType() {
		return FilterType.BOOLEAN;
	}
}
