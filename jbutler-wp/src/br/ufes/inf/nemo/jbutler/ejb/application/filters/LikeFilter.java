package br.ufes.inf.nemo.jbutler.ejb.application.filters;

/**
 * The Like filter is the same as the simple filter, but uses the LIKE operator on Strings instead of the = operator.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public class LikeFilter extends SimpleFilter {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor from superclass.
	 * 
	 * @param key
	 *            The filter type unique identifier.
	 * @param fieldName
	 *            The field that will be filtered.
	 * @param label
	 *            The label for user interfaces.
	 */
	public LikeFilter(String key, String fieldName, String label) {
		super(key, fieldName, label);
	}

	/**
	 * Constructor from superclass.
	 * 
	 * @param key
	 *            The filter type unique identifier.
	 * @param fieldName
	 *            The field that will be filtered.
	 * @param label
	 *            The label for user interfaces.
	 * @param criteria
	 *            The criteria to be applied to the filter query.
	 */
	public LikeFilter(String key, String fieldName, String label, Criterion ... criteria) {
		super(key, fieldName, label, criteria);
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.SimpleFilter#getType() */
	@Override
	public FilterType getType() {
		return FilterType.LIKE;
	}
}
