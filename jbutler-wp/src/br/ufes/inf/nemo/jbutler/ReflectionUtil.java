package br.ufes.inf.nemo.jbutler;

import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class with methods related to reflection (class manipulation).
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
public final class ReflectionUtil {
	/** The logger. */
	private static final Logger logger = Logger.getLogger(ReflectionUtil.class.getCanonicalName());

	/**
	 * Determines the actual type argument used by a concrete class that extends an abstract class which defines a
	 * generic type parameter.
	 * 
	 * Useful in the following cases for JButler (could be used in similar ones as well):
	 * <ul>
	 * <li><code>class SomeEntityJPADAO extends BaseJPADAO&lt;SomeEntity&gt;</code></li>
	 * <li><code>class ManageSomeEntityServiceBean extends CrudServiceBean&lt;SomeEntity&gt;</code></li>
	 * </ul>
	 * 
	 * Once this method became available, no longer the concrete classes needed to implement the
	 * <code>getDomainClass()</code> method, as their abstract superclasses were able to provide code that automatically
	 * determines the domain class from the generic type argument, calling this method.
	 * 
	 * @param clazz
	 *            The concrete class that specifies the generic type argument T.
	 * @return The actual type argument paramter for type T.
	 */
	public static Class<?> determineTypeArgument(Class<?> clazz) {
		// Retrieves the superclass as a parameterized type in order to have access to the generic parameters.
		ParameterizedType genericSuperclass = (ParameterizedType) clazz.getGenericSuperclass();

		// Retrieves the list of generic type arguments of the class and return the first one (if exists).
		java.lang.reflect.Type[] genericTypes = genericSuperclass.getActualTypeArguments();
		Class<?> result = genericTypes.length == 0 ? null : (Class<?>) genericTypes[0];
		logger.log(Level.INFO, "Determined \"{0}\" as the generic type parameter for class \"{1}\"", new Object[] { result, clazz });		
		return result;
	}
}
