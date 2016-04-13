package br.ufes.inf.nemo.jbutler.ejb.application;

/**
 * Enumeration of different CRUD operations.
 * 
 * <i>This class is part of the Engenho de Software CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public enum CrudOperation {
	/** Create a new object (INSERT in SQL). */
	CREATE,

	/** Retrieve an existing object (SELECT in SQL). */
	RETRIEVE,

	/** Update the attributes of an existing object (UPDATE in SQL). */
	UPDATE,

	/** Deletes an entire existing object (DELETE in SQL). */
	DELETE,

	/** Retrieves many/all existing objects of the class (SELECT in SQL). */
	LIST;
}
