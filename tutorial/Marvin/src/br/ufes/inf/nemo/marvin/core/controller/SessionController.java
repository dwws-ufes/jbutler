package br.ufes.inf.nemo.marvin.core.controller;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.ufes.inf.nemo.jbutler.ejb.controller.JSFController;
import br.ufes.inf.nemo.marvin.core.application.CoreInformation;
import br.ufes.inf.nemo.marvin.core.application.SessionInformation;
import br.ufes.inf.nemo.marvin.core.domain.Academic;
import br.ufes.inf.nemo.marvin.core.exceptions.LoginFailedException;

/**
 * Session-scoped managed bean that provides to web pages the login service, indication if the user is logged in and the
 * current date/time.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
@Named
@SessionScoped
public class SessionController extends JSFController {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(SessionController.class.getCanonicalName());

	/** Information on the whole application. */
	@EJB
	private CoreInformation coreInformation;

	/** Information on the current visitor. */
	@EJB
	private SessionInformation sessionInformation;

	/** Input: e-mail for authentication. */
	private String email;

	/** Input: password for authentication. */
	private String password;

	/** Getter for email. */
	public String getEmail() {
		return email;
	}

	/** Setter for email. */
	public void setEmail(String email) {
		this.email = email;
	}

	/** Getter for password. */
	public String getPassword() {
		return password;
	}

	/** Setter for password. */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Indicates if the user has already been identified.
	 * 
	 * @return <code>true</code> if the user is logged in, <code>false</code> otherwise.
	 */
	public boolean isLoggedIn() {
		return sessionInformation.getCurrentUser() != null;
	}

	/**
	 * Provides the current authenticated user.
	 * 
	 * @return The Academic object that represents the user that has been authenticated in this session.
	 */
	public Academic getCurrentUser() {
		return sessionInformation.getCurrentUser();
	}

	/**
	 * Provides the current date/time.
	 * 
	 * @return A Date object representing the date/time the method was called.
	 */
	public Date getNow() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * Provides the expiration date/time for the user session. This makes it possible to warn the user when his session
	 * will expire.
	 * 
	 * @return A Date object representing the date/time of the user's session expiration.
	 */
	public Date getSessionExpirationTime() {
		Date expTime = null;

		// Attempts to retrieve this information from the external context.
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session != null) {
			logger.log(Level.FINEST, "Calculating session expiration time from the HTTP session...");
			long expTimeMillis = session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000;
			expTime = new Date(expTimeMillis);
		}

		// If it could not be retrieved from the external context, use default of 30 minutes.
		if (expTime == null) {
			logger.log(Level.FINEST, "HTTP Session not available. Using default expiration time: now plus 30 minutes...");
			expTime = new Date(System.currentTimeMillis() + 30 * 60000);
		}

		logger.log(Level.FINEST, "Calculated expiration time: {0}", expTime);
		return expTime;
	}

	/**
	 * Accesses the Login service to authenticate the user given his email and password.
	 */
	public String login() {
		try {
			// Uses the Login service to authenticate the user.
			logger.log(Level.FINEST, "User attempting login with email \"{0}\"...", email);
			sessionInformation.login(email, password);
		}
		catch (LoginFailedException e) {
			// Checks if it's a normal login exception (wrong username or password) or not.
			switch (e.getReason()) {
			case INCORRECT_PASSWORD:
			case UNKNOWN_USERNAME:
				// Normal login exception (invalid usernaem or password). Report the error to the user.
				logger.log(Level.INFO, "Login failed for \"{0}\". Reason: \"{1}\"", new Object[] { email, e.getReason() });
				addGlobalI18nMessage("msgsCore", FacesMessage.SEVERITY_ERROR, "login.error.nomatch.summary", "login.error.nomatch.detail");
				return null;

			default:
				// System failure exception. Report a fatal error and ask the user to contact the administrators.
				logger.log(Level.INFO, "System failure during login. Email: \"" + email + "\"; reason: \"" + e.getReason() + "\"", e);
				addGlobalI18nMessage("msgsCore", FacesMessage.SEVERITY_FATAL, "login.error.fatal.summary", new Object[0], "login.error.fatal.detail", new Object[] { new Date(System.currentTimeMillis()) });
				return null;
			}
		}

		// If everything is OK, redirect back to the home screen.
		return "/index.xhtml?faces-redirect=true";
	}
}
