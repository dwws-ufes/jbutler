package br.ufes.inf.nemo.marvin.core.application;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import br.ufes.inf.nemo.jbutler.TextUtils;
import br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import br.ufes.inf.nemo.marvin.core.domain.Academic;
import br.ufes.inf.nemo.marvin.core.exceptions.LoginFailedException;
import br.ufes.inf.nemo.marvin.core.persistence.AcademicDAO;

/**
 * Stateful session bean implementing the session information component. See the implemented interface documentation for
 * details.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @see br.org.feees.sigme.core.application.SessionInformation
 */
@SessionScoped
@Stateful
public class SessionInformationBean implements SessionInformation {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(SessionInformationBean.class.getCanonicalName());

	/** The DAO for Academic objects. */
	@EJB
	private AcademicDAO academicDAO;

	/** The current user logged in. */
	private Academic currentUser;

	/** @see br.org.feees.sigme.core.application.SessionInformation#getCurrentUser() */
	@Override
	public Academic getCurrentUser() {
		return currentUser;
	}

	/** @see br.org.feees.sigme.core.application.SessionInformation#login(java.lang.String, java.lang.String) */
	@Override
	public void login(String username, String password) throws LoginFailedException {
		try {
			// Obtains the user given the e-mail address (that serves as username).
			logger.log(Level.FINER, "Authenticating user with username \"{0}\"...", username);
			Academic user = academicDAO.retrieveByEmail(username);

			// Creates the MD5 hash of the password for comparison.
			String md5pwd = TextUtils.produceMd5Hash(password);

			// Checks if the passwords match.
			String pwd = user.getPassword();
			if ((pwd != null) && (pwd.equals(md5pwd))) {
				logger.log(Level.FINEST, "Passwords match for user \"{0}\".", username);

				// Login successful. Registers the current user in the session.
				logger.log(Level.FINE, "Academic \"{0}\" successfully logged in.", username);
				currentUser = user;
				pwd = null;

				// Registers the user login.
				Date now = new Date(System.currentTimeMillis());
				logger.log(Level.FINER, "Setting last login date for academic with username \"{0}\" as \"{1}\"...", new Object[] { currentUser.getEmail(), now });
				currentUser.setLastLoginDate(now);
				academicDAO.save(currentUser);
			}
			else {
				// Passwords don't match.
				logger.log(Level.INFO, "Academic \"{0}\" not logged in: password didn't match.", username);
				throw new LoginFailedException(LoginFailedException.LoginFailedReason.INCORRECT_PASSWORD);
			}
		}
		catch (PersistentObjectNotFoundException e) {
			// No academic was found with the given username.
			logger.log(Level.INFO, "User \"{0}\" not logged in: no registered academic found with given username.", username);
			throw new LoginFailedException(e, LoginFailedException.LoginFailedReason.UNKNOWN_USERNAME);
		}
		catch (MultiplePersistentObjectsFoundException e) {
			// Multiple academics were found with the same username.
			logger.log(Level.WARNING, "User \"{0}\" not logged in: there are more than one registered academic with the given username.", username);
			throw new LoginFailedException(e, LoginFailedException.LoginFailedReason.MULTIPLE_USERS);
		}
		catch (EJBTransactionRolledbackException e) {
			// Unknown original cause. Throw the EJB exception.
			logger.log(Level.WARNING, "User \"" + username + "\" not logged in: unknown cause.", e);
			throw e;
		}
		catch (NoSuchAlgorithmException e) {
			// No MD5 hash generation algorithm found by the JVM.
			logger.log(Level.SEVERE, "Logging in user \"" + username + "\" triggered an exception during MD5 hash generation.", e);
			throw new LoginFailedException(LoginFailedException.LoginFailedReason.MD5_ERROR);
		}
	}
}
