package br.ufes.inf.nemo.jbutler.ejb.application.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Abstract superclass for filters, implements the basic features all filters have: an unique identifier, the name of
 * the field that will be filtered, the label to be used in the GUI and a list of extra criteria to be applied.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE).</i>
 * 
 * @param <T>
 *            Type of the objects that compose the list of options to choose from.
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.2
 */
public abstract class AbstractFilter<T> implements Filter<T>, Comparable<AbstractFilter<?>> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The filter type unique identifier. */
	protected String key;

	/** The field that will be filtered. */
	protected String fieldName;

	/** The label for user interfaces. */
	protected String label;

	/** The criteria to be applied to the filter query. */
	protected List<Criterion> criteria = new ArrayList<Criterion>();

	/**
	 * Constructor using fields.
	 * 
	 * @param key
	 *            The filter type unique identifier.
	 * @param fieldName
	 *            The field that will be filtered.
	 * @param label
	 *            The label for user interfaces.
	 */
	public AbstractFilter(String key, String fieldName, String label) {
		this.key = key;
		this.fieldName = fieldName;
		this.label = label;
	}

	/**
	 * Constructor using fields.
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
	public AbstractFilter(String key, String fieldName, String label, Criterion ... criteria) {
		this(key, fieldName, label);
		this.criteria.addAll(Arrays.asList(criteria));
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getFieldName() */
	@Override
	public String getFieldName() {
		return fieldName;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getKey() */
	@Override
	public String getKey() {
		return key;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getLabel() */
	@Override
	public String getLabel() {
		return label;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getOptions() */
	@Override
	public List<T> getOptions() {
		return null;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getOptionLabel(java.lang.String) */
	@Override
	public String getOptionLabel(String key) {
		return key;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getOptionsLabels() */
	@Override
	public Map<String, String> getOptionsLabels() {
		return null;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getReversedOptionsLabels() */
	@Override
	public Map<String, String> getReversedOptionsLabels() {
		return null;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getSubFieldNames() */
	@Override
	public String getSubFieldNames() {
		return null;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getCriteria() */
	@Override
	public List<Criterion> getCriteria() {
		return criteria;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getEnum(java.lang.String) */
	@Override
	public Enum<?> getEnum(String value) {
		return null;
	}
	
	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#isSupportsText() */
	public boolean isSupportsText() {
		switch (getType()) {
		case LIKE:
		case MANY_TO_MANY:
		case SIMPLE:
			return true;
		default:
			return false;
		}
	}	
	
	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#isSupportsBoolean() */
	public boolean isSupportsBoolean() {
		switch (getType()) {
		case BOOLEAN:
			return true;
		default:
			return false;
		}
	}
	
	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#isSupportsChoice() */
	public boolean isSupportsChoice() {
		switch (getType()) {
		case ENUM_MULTIPLE_CHOICE:
		case MULTIPLE_CHOICE:
		case REVERSE_MULTIPLE_CHOICE:
			return true;
		default:
			return false;
		}
	}
	
	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(AbstractFilter<?> o) {
		int cmp = (label.compareTo(o.label));
		return (cmp == 0) ? fieldName.compareTo(o.fieldName) : cmp;
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractFilter<?>)) { return false; }
		AbstractFilter<?> o = (AbstractFilter<?>) obj;
		return fieldName.equals(o.fieldName);
	}

	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		// Use the same criterion as equals(): the class and the field name.
		return getClass().getName().hashCode() + (this.fieldName != null ? this.fieldName.hashCode() : 0);
	}
}
