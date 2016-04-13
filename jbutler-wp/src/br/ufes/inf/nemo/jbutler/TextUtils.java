package br.ufes.inf.nemo.jbutler;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class with methods related to text manipulation.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
public final class TextUtils {
	/** The logger. */
	private static final Logger logger = Logger.getLogger(TextUtils.class.getCanonicalName());

	/**
	 * Produces the MD5 hash for a given string. Useful for generating hashes of passwords, for example.
	 * 
	 * @param str
	 *            Any string.
	 * 
	 * @return A string containing the MD5 hash of the string given as parameter.
	 * 
	 * @throws NoSuchAlgorithmException
	 *             If the MD5 conversion algorithm can't be found in the JVM implementation.
	 */
	public static String produceMd5Hash(String str) throws NoSuchAlgorithmException {
		// Check for nulls.
		if (str == null) return null;

		// Gets the digest instance from the JVM.
		MessageDigest md = MessageDigest.getInstance("MD5");

		// Produces the numeric hash of the string.
		BigInteger hash = new BigInteger(1, md.digest(str.getBytes()));
		String s = hash.toString(16);

		// If the hash doesn't have 32 digits, prepend a zero.
		if (s.length() % 2 != 0) s = "0" + s;

		logger.log(Level.FINEST, "MD5 hash produced: {0}", s);
		return s;
	}

	/**
	 * Produces the hash for a given string, given the name of the hash generation algorithm. Useful for generating
	 * hashes of passwords, for example.
	 * 
	 * @param str
	 *            Any string.
	 * @param algorithm
	 *            The name of the hash generation algorithm to instantiate.
	 * 
	 * @return A string containing the hash (produced with the given algorithm) of the string given as parameter.
	 * 
	 * @throws NoSuchAlgorithmException
	 *             If the conversion algorithm can't be found in the JVM implementation.
	 * @throws UnsupportedEncodingException
	 */
	public static String produceHash(String str, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// Check for nulls.
		if (str == null) return null;

		// Gets the digest instance from the JVM.
		MessageDigest md = MessageDigest.getInstance(algorithm);

		// Produces the numeric hash of the string.
		BigInteger hash = new BigInteger(1, md.digest(str.getBytes("UTF-8")));
		String s = hash.toString(16);

		// If the hash doesn't have an even number of digits, prepend a zero.
		if (s.length() % 2 != 0) s = "0" + s;

		logger.log(Level.FINEST, "{0} hash produced: {1}", new Object[] { algorithm, s });
		return s;
	}
}
