package br.ufes.inf.nemo.jbutler.ejb.application.filters;

import java.io.Serializable;

/**
 * Represents a criterion to be applied to a filter, other than the main filtering criterion.
 * 
 * <i>This class is part of the JButler CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public class Criterion implements Serializable {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The name of the field to which the criterion applies. */
	protected String fieldName;

	/** The type of criterion: is null, is not null, equals, like, etc. */
	protected CriterionType type;

	/** The value to be compared to in case of criterions of types equals, like, etc. */
	protected Object param;

	/**
	 * Constructor using fields.
	 * 
	 * @param fieldName
	 *            The name of the field to which the criterion applies.
	 * @param type
	 *            The type of criterion: is null, is not null, equals, like, etc.
	 */
	public Criterion(String fieldName, CriterionType type) {
		this.fieldName = fieldName;
		this.type = type;
	}

	/**
	 * Constructor using fields.
	 * 
	 * @param fieldName
	 *            The name of the field to which the criterion applies.
	 * @param type
	 *            The type of criterion: is null, is not null, equals, like, etc.
	 * @param param
	 *            The value to be compared to in case of criterions of types equals, like, etc.
	 */
	public Criterion(String fieldName, CriterionType type, Object param) {
		this(fieldName, type);
		this.param = param;
	}

	/**
	 * Getter for fieldName.
	 * 
	 * @return The name of the field to which the criterion applies.
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Getter for type.
	 * 
	 * @return The type of criterion: is null, is not null, equals, like, etc.
	 */
	public CriterionType getType() {
		return type;
	}

	/**
	 * Getter for param.
	 * 
	 * @return The value to be compared to in case of criterions of types equals, like, etc.
	 */
	public Object getParam() {
		return param;
	}
}
