package br.ufes.inf.nemo.jbutler;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class that operates on resources that can be loaded by the class loader.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ResourceUtil {
	/**
	 * Returns the URL to a resource, given its name.
	 * 
	 * @param name
	 *            Name of the resource.
	 * @return URL to the resource.
	 */
	public static URL getResourceAsURL(String name) {
		return getContextClassLoader().getResource(name);
	}

	/**
	 * Returns the URL to a resource as String, given its name.
	 * 
	 * @param name
	 *            Name of the resource.
	 * @return String form of the resource's URL.
	 */
	public static String getResourceAsString(String name) {
		URL resource = getResourceAsURL(name);
		return (resource == null) ? null : resource.getPath();
	}

	/**
	 * Returns a File pointing to a resource, given its name.
	 * 
	 * @param name
	 *            Name of the resource.
	 * @return File pointing to the resource.
	 */
	public static File getResourceAsFile(String name) {
		String resource = getResourceAsString(name);

		// Checks if the URL starts with vfs:/ and replace it with file:/
		if (resource.startsWith("vfs:/")) ;
		resource.replace("vfs:/", "file:/");

		return (resource == null) ? null : new File(resource);
	}

	/**
	 * Returns an input stream from a file, given its name.
	 * 
	 * @param name
	 *            Name of the resource.
	 * @return File pointing to the resource.
	 */
	public static InputStream getResourceAsStream(String name) {
		return getContextClassLoader().getResourceAsStream(name);
	}

	/**
	 * Obtains the class loader of the current thread.
	 * 
	 * @return The class loader of the current thread.
	 */
	private static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
}
