package br.ufes.inf.nemo.jbutler.ejb.application.filters;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Multiple-choice filters can be used when the amount of choices to filter the elements are finite and refer to
 * properties that are reachable from the element. In this case, fieldName and options have to be set, the latter
 * containing the different options for filtering. In the persistence layer, the database query will search for any
 * entities that have the given fieldName's id equal the search param, which must also be an id. E.g.: searching a
 * product selecting the category of the product (CD, Book, DVD, etc.).
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @param <T>
 *            Type of the objects that compose the list of options to choose from.
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public class MultipleChoiceFilter<T> extends AbstractFilter<T> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** List of objects in case of multiple choice filter. */
	protected List<T> options;

	/** Association between labels and options values. */
	protected Map<String, String> optionsLabels;

	/** Reversed options labels map, useful for view technologies like JSF. */
	protected Map<String, String> reversedOptionsLabels;

	/**
	 * Constructor from superclass, using fields.
	 * 
	 * @param key
	 *            The filter type unique identifier.
	 * @param fieldName
	 *            The field that will be filtered.
	 * @param label
	 *            The label for user interfaces.
	 * @param options
	 *            List of objects in case of multiple choice filter.
	 * @param optionsLabels
	 *            Association between labels and options values.
	 */
	public MultipleChoiceFilter(String key, String fieldName, String label, List<T> options, Map<String, String> optionsLabels) {
		super(key, fieldName, label);
		this.options = options;
		this.optionsLabels = optionsLabels;
		reversedOptionsLabels = reverseMap(optionsLabels);
	}

	/**
	 * Constructor from superclass, using fields.
	 * 
	 * @param key
	 *            The filter type unique identifier.
	 * @param fieldName
	 *            The field that will be filtered.
	 * @param label
	 *            The label for user interfaces.
	 * @param options
	 *            List of objects in case of multiple choice filter.
	 * @param optionsLabels
	 *            Association between labels and options values.
	 * @param criteria
	 *            The criteria to be applied to the filter query.
	 */
	public MultipleChoiceFilter(String key, String fieldName, String label, List<T> options, Map<String, String> optionsLabels, Criterion ... criteria) {
		super(key, fieldName, label, criteria);
		this.options = options;
		this.optionsLabels = optionsLabels;
		reversedOptionsLabels = reverseMap(optionsLabels);
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.AbstractFilter#getOptions() */
	@Override
	public List<T> getOptions() {
		return options;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.AbstractFilter#getOptionLabel(java.lang.String) */
	@Override
	public String getOptionLabel(String key) {
		return optionsLabels.get(key);
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.Filter#getType() */
	@Override
	public FilterType getType() {
		return FilterType.MULTIPLE_CHOICE;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.AbstractFilter#getOptionsLabels() */
	@Override
	public Map<String, String> getOptionsLabels() {
		return optionsLabels;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.AbstractFilter#getReversedOptionsLabels() */
	@Override
	public Map<String, String> getReversedOptionsLabels() {
		return reversedOptionsLabels;
	}

	/**
	 * Given a source map, returns a reversed map, i.e., a map that has values as keys and keys as values.
	 * 
	 * @param map
	 *            The source map.
	 * 
	 * @return A reversed version of the source map.
	 */
	private Map<String, String> reverseMap(Map<String, String> map) {
		Map<String, String> reverseMap = new LinkedHashMap<String, String>();
		if (map != null) for (Map.Entry<String, String> entry : map.entrySet())
			reverseMap.put(entry.getValue(), entry.getKey());
		return reverseMap;
	}
}
