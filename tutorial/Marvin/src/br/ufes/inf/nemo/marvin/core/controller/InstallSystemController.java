package br.ufes.inf.nemo.marvin.core.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufes.inf.nemo.jbutler.ejb.controller.JSFController;
import br.ufes.inf.nemo.marvin.core.application.InstallSystemService;
import br.ufes.inf.nemo.marvin.core.domain.Academic;
import br.ufes.inf.nemo.marvin.core.domain.MarvinConfiguration;
import br.ufes.inf.nemo.marvin.core.exceptions.SystemInstallFailedException;

/**
 * TODO: document this type.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Named
@ConversationScoped
public class InstallSystemController extends JSFController {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** Path to the folder where the view files (web pages) for this action are placed. */
	private static final String VIEW_PATH = "/core/installSystem/";

	/** The logger. */
	private static final Logger logger = Logger.getLogger(InstallSystemController.class.getCanonicalName());

	/** The JSF conversation. */
	@Inject
	private Conversation conversation;

	/** The "Install System" service. */
	@EJB
	private InstallSystemService installSystemService;

	/** Input: the administrator being registered during the installation. */
	private Academic admin = new Academic();

	/** Input: the repeated password for the admininstrator registration. */
	private String repeatPassword;

	/** Input: system configuration information. */
	private MarvinConfiguration config = new MarvinConfiguration();

	/** Getter for repeatPassword. */
	public String getRepeatPassword() {
		return repeatPassword;
	}

	/** Setter for repeatPassword. */
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	/** Getter for admin. */
	public Academic getAdmin() {
		return admin;
	}

	/** Getter for config. */
	public MarvinConfiguration getConfig() {
		return config;
	}

	/**
	 * Analyzes the name that was given to the administrator and, if the short name field is still empty, suggests a
	 * value for it based on the given name.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void suggestShortName() {
		// If the name was filled and the short name is still empty, suggest the first name as short name.
		String name = admin.getName();
		String shortName = admin.getShortName();
		if ((name != null) && ((shortName == null) || (shortName.length() == 0))) {
			int idx = name.indexOf(" ");
			admin.setShortName((idx == -1) ? name : name.substring(0, idx).trim());
			logger.log(Level.FINE, "Suggested \"{0}\" as short name for \"{1}\"", new Object[] { admin.getShortName(), name });
		}
		else logger.log(Level.FINEST, "Short name not suggested: empty name or short name already filled (name is \"{0}\", short name is \"{1}\")", new Object[] { name, shortName });
	}

	/**
	 * Checks if both password fields have the same value.
	 * 
	 * This method is intended to be used with AJAX.
	 */
	public void ajaxCheckPasswords() {
		checkPasswords();
	}

	/**
	 * Checks if the contents of the password fields match.
	 * 
	 * @return <code>true</code> if the passwords match, <code>false</code> otherwise.
	 */
	private boolean checkPasswords() {
		if (((repeatPassword != null) && (!repeatPassword.equals(admin.getPassword()))) || ((repeatPassword == null) && (admin.getPassword() != null))) {
			logger.log(Level.INFO, "Password and repeated password are not the same");
			addGlobalI18nMessage("msgsCore", FacesMessage.SEVERITY_WARN, "installSystem.error.passwordsDontMatch.summary", "installSystem.error.passwordsDontMatch.detail");
			return false;
		}
		return true;
	}

	/**
	 * Begins the installation process.
	 * 
	 * @return The path to the web page that shows the first step of the installation process.
	 */
	public String begin() {
		logger.log(Level.FINEST, "Beginning conversation. Current conversation transient? -> {0}", new Object[] { conversation.isTransient() });

		// Begins the conversation, dropping any previous conversation, if existing.
		if (!conversation.isTransient()) conversation.end();
		conversation.begin();

		// Go to the first view.
		return VIEW_PATH + "index.xhtml?faces-redirect=true";
	}

	/**
	 * Registers the administrator as one of the steps of system installation and moves to the next step.
	 * 
	 * @return The path to the web page that shows the next step in the installation process.
	 */
	public String registerAdministrator() {
		logger.log(Level.FINEST, "Received input data:\n\t- admin.name = {0}\n\t- admin.email = {1}", new Object[] { admin.getName(), admin.getEmail() });

		// Check if passwords don't match. Add an error in that case.
		if (!checkPasswords()) return null;

		// Proceeds to the next view.
		return VIEW_PATH + "config.xhtml?faces-redirect=true";
	}

	/**
	 * Saves the SMTP configuration information and ends the installation process.
	 * 
	 * @return The path to the web page that shows the next step in the installation process.
	 */
	public String saveConfig() {
		logger.log(Level.FINEST, "Previously received data:\n\t- admin.name = {0}\n\t- admin.email = {1}", new Object[] { admin.getName(), admin.getEmail() });
		logger.log(Level.FINEST, "Received input data:\n\t- config.institutionAcronym = {0}", config.getInstitutionAcronym());

		// Installs the system.
		try {
			installSystemService.installSystem(config, admin);
		}
		catch (SystemInstallFailedException e) {
			logger.log(Level.SEVERE, "System installation threw exception", e);
			addGlobalI18nMessage("msgsCore", FacesMessage.SEVERITY_FATAL, "installSystem.error.installFailed.summary", "installSystem.error.installFailed.detail");
			return null;
		}

		// Ends the conversation.
		conversation.end();

		// Proceeds to the final view.
		return VIEW_PATH + "done.xhtml?faces-redirect=true";
	}
}
