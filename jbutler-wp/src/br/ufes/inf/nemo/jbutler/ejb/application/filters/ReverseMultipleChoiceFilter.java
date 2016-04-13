package br.ufes.inf.nemo.jbutler.ejb.application.filters;

import java.util.List;
import java.util.Map;

/**
 * Reverse multiple-choice filters are like normal multiple-choice filter, but the field is not accessible from the CRUD
 * entity and an "IN" query has to be done. E.g.: searching a company selecting a region, given that the Region class
 * has a set of cities and a company is located in a city. In the persistence level, the query would be something like
 * this (example in HQL): "from Company c where c.city in (select r.cities from Region r where r.id = :param)". The
 * property fieldName is set with the name of the field in the CRUD entity ("city"), options is filled with the
 * different choices (the region objects) and subFieldName is set with the field to get in the subquery ("cities").
 * 
 * <i>This class is part of the Engenho de Software CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @param <T>
 *            Type of the objects that compose the list of options to choose from.
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public class ReverseMultipleChoiceFilter<T> extends MultipleChoiceFilter<T> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** Field to select in the subquery when using a reverse multiple-choice filter. */
	protected String subFieldName;

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
	 * @param subFieldName
	 *            Field to select in the subquery when using a reverse multiple-choice filter.
	 */
	public ReverseMultipleChoiceFilter(String key, String fieldName, String label, List<T> options, Map<String, String> optionsLabels, String subFieldName) {
		super(key, fieldName, label, options, optionsLabels);
		this.subFieldName = subFieldName;
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
	 * @param subFieldName
	 *            Field to select in the subquery when using a reverse multiple-choice filter.
	 * @param criteria
	 *            The criteria to be applied to the filter query.
	 */
	public ReverseMultipleChoiceFilter(String key, String fieldName, String label, List<T> options, Map<String, String> optionsLabels, String subFieldName, Criterion ... criteria) {
		super(key, fieldName, label, options, optionsLabels, criteria);
		this.subFieldName = subFieldName;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.AbstractFilter#getSubFieldNames() */
	@Override
	public String getSubFieldNames() {
		return subFieldName;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.application.filters.MultipleChoiceFilter#getType() */
	@Override
	public FilterType getType() {
		return FilterType.REVERSE_MULTIPLE_CHOICE;
	}
}
