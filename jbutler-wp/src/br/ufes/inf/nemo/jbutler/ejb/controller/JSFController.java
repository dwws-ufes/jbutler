package br.ufes.inf.nemo.jbutler.ejb.controller;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 * Abstract class that can be implemented by controller classes for JSF pages (managed beans). Provides useful methods,
 * such as adding field and global internationalized messages to the Faces context.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
public abstract class JSFController implements Serializable {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(JSFController.class.getCanonicalName());

	/** Maximum number of data table rows to show per page by default. */
	protected static final int MAX_DATA_TABLE_ROWS_PER_PAGE = 10;

	/** Default refresh rate for JSF polling components. */
	protected static final int DEFAULT_REFRESH_RATE = 2;

	/** The name of the message bundle variable to be used in i18n messages. */
	protected String bundleName;

	/** The resource bundle prefix for messages. */
	protected String bundlePrefix;

	/**
	 * Informs to other methods (and web pages) what is the maximum number of rows to be displayed in a data page. This
	 * method exists so it can be overridden by subclasses if desired.
	 * 
	 * @return The maximum number of rows to be displayed in a data table at a time.
	 */
	public int getMaxDataTableRowsPerPage() {
		return MAX_DATA_TABLE_ROWS_PER_PAGE;
	}

	/**
	 * Obtains the refresh rate for polling components under this controller.
	 * 
	 * @return The refresh rate value.
	 */
	public int getRefreshRate() {
		return DEFAULT_REFRESH_RATE;
	}

	/**
	 * Informs to other methods (and web pages) what is the maximum number of entities to be displayed in a listing
	 * page, divided by 2. This method exists so it can be overridden by subclasses if desired.
	 * 
	 * @return The maximum number of entities to be displayed in the page at a time.
	 */
	public int getHalfMaxDataTableRowsPerPage() {
		return MAX_DATA_TABLE_ROWS_PER_PAGE / 2;
	}

	/**
	 * Informs to other methods (and web pages) what is the maximum number of entities to be displayed in a listing
	 * page, multiplied by 2. This method exists so it can be overridden by subclasses if desired.
	 * 
	 * @return The maximum number of entities to be displayed in the page at a time.
	 */
	public int getDoubleMaxDataTableRowsPerPage() {
		return MAX_DATA_TABLE_ROWS_PER_PAGE * 2;
	}

	/**
	 * Obtains the current JSF context instance.
	 * 
	 * @return The FacesContext object associated with the current request.
	 */
	protected FacesContext getCurrentInstance() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * Obtains the current JSF external context.
	 * 
	 * @return The ExternalContext object associated with the current request.
	 */
	protected ExternalContext getExternalContext() {
		return getCurrentInstance().getExternalContext();
	}

	/**
	 * Obtains the current JSF EL context.
	 * 
	 * @return The ELContext object associated with the current request.
	 */
	protected ELContext getELContext() {
		return getCurrentInstance().getELContext();
	}

	/**
	 * Obtains the JSF Flash object.
	 * 
	 * @return The Flash object associated with the current request.
	 */
	protected Flash getFlash() {
		return getExternalContext().getFlash();
	}

	/**
	 * Obtains the JSF Application.
	 * 
	 * @return The Application object associated with the current request.
	 */
	protected Application getApplication() {
		return getCurrentInstance().getApplication();
	}

	/**
	 * Obtains the JSF Expression Factory.
	 * 
	 * @return The ExpressionFactory object associated with the current request.
	 */
	protected ExpressionFactory getExpressionFactory() {
		return getApplication().getExpressionFactory();
	}

	/**
	 * Informs to other methods what is the name of the variable that represents the resource bundle with i18n messages.
	 * This method may be overridden by subclasses if they don't follow the standard naming convention for Crud
	 * Controllers, which is: <code>com.yourdomain.yoursystem.package.controller.ManageObjectController</code> which
	 * would lead to a bundle variable name of <code>msgsPackage</code>.
	 * 
	 * @return The name of the resource bundle variable.
	 */
	public String getBundleName() {
		// If this method is not overridden, tries to guess the bundle name from the class name.
		if (bundleName == null) {
			// Starts with the fully-qualified name of the class (package and name).
			int idx;
			String pkg = "";
			String classFullName = getClass().getCanonicalName();

			// Searches for the name of the package according to the name convention (before ".controller.").
			idx = classFullName.indexOf(".controller.");
			if (idx != -1) {
				pkg = classFullName.substring(0, idx);
				idx = pkg.lastIndexOf('.');
				if (idx != -1) pkg = pkg.substring(idx + 1);
			}

			// Adds the "msgs" prefix and capitalizes the first letter.
			if (pkg.length() > 1) pkg = "msgs" + Character.toUpperCase(pkg.charAt(0)) + pkg.substring(1);

			// The bundle name is the result of the manipulation of the class' package.
			bundleName = pkg;
			logger.log(Level.INFO, "Bundle name not provided by subclass, thus guessing from naming convention: {0}", bundleName);
		}
		return bundleName;
	}

	/**
	 * Informs to other methods what is the default prefix for resource bundle messages for this controller. This method
	 * may be overridden by subclasses if they don't follow the standard naming convention for Crud Controllers, which
	 * is: <code>com.yourdomain.yoursystem.package-name.controller.ManageObjectController</code> which would lead to a
	 * prefix of <code>manageObject</code>.
	 * 
	 * @return The prefix for resource bundle keys.
	 */
	public String getBundlePrefix() {
		// If the bundle prefix is not specified by the subclass, tries to guess it from the class name.
		if (bundlePrefix == null) {
			// Starts with the fully-qualified name of the class (package and name).
			int idx;
			String service = "";
			String classFullName = getClass().getCanonicalName();

			// Searches for the name of the service according to convention (class name, removing trailing Controller).
			idx = classFullName.lastIndexOf(".");
			service = (idx == -1) ? classFullName : classFullName.substring(idx + 1);
			idx = service.indexOf("Controller");
			if (idx != -1) service = service.substring(0, idx);
			if (service.length() > 1) service = Character.toLowerCase(service.charAt(0)) + service.substring(1);

			// The bundle prefix is the name of class adapted as before.
			bundlePrefix = service;
			logger.log(Level.INFO, "Bundle prefix not provided by subclass, thus guessing from naming convention: {0}", bundlePrefix);
		}
		return bundlePrefix;
	}

	/**
	 * Obtains an internationalized message, given the bundle and key. If parameters are provided, the message is
	 * formatted with them.
	 * 
	 * @param bundleName
	 *            Name of the resource bundle (name of the .properties file, excluding the language suffix and the
	 *            .properties extension).
	 * @param key
	 *            The key that identifies the message in the resource bundle.
	 * @param params
	 *            Optional params that should be merged into the message (using {0}, {1}, ... placeholders) if present.
	 * 
	 * @return A string containing the message taken from the appropriate bundle (considering the context's locale),
	 *         formatted with the given params, if the bundle and key are found. Otherwise, returns the key itself.
	 */
	protected String getI18nMessage(String bundleName, String key, Object ... params) {
		// Retrieve the resource bundle.
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, bundleName);

		// Gets the message.
		try {
			String message = bundle.getString(key);

			// If there are params, format the message.
			if ((params != null) && (params.length > 0)) message = MessageFormat.format(message, params);

			// Return the message.
			return message;
		}
		catch (MissingResourceException e) {
			// If the message is not found, return the key.
			return key;
		}
	}

	/**
	 * Generic method for adding an internationalized message to the faces context. This class provides also lots of
	 * shortcuts that allow the user to choose global or field-related questions, specify summary-only or summary and
	 * detail, specify parameters or not, specify the severity or not, etc.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * @param detailParams
	 *            The parameters for the detail message.
	 */
	private void addI18nMessage(String fieldName, String bundleName, FacesMessage.Severity severity, String summaryKey, Object[] summaryParams, String detailKey, Object[] detailParams) {
		// Retrieve the messages from the bundle.
		String summary = (summaryKey == null) ? null : getI18nMessage(bundleName, summaryKey, summaryParams);
		String detail = (detailKey == null) ? null : getI18nMessage(bundleName, detailKey, detailParams);

		// Creates the faces message that will be added to the context according to the given parameters.
		FacesMessage message = null;
		if (severity != null) {
			message = new FacesMessage(severity, summary, detail);
		}
		else if (detail != null) {
			message = new FacesMessage(summary, detail);
		}
		else {
			message = new FacesMessage(summary);
		}

		// Add the message to the context. If the field name is null, the message is considered global.
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(fieldName, message);
	}

	/**
	 * Shortcut to addI18nMessage(): global message with specified severity and simple summary and detail messages.
	 * 
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addGlobalI18nMessage(String bundleName, FacesMessage.Severity severity, String summaryKey, String detailKey) {
		addI18nMessage(null, bundleName, severity, summaryKey, null, detailKey, null);
	}

	/**
	 * Shortcut to addI18nMessage(): global message with specified severity and including only a simple summary.
	 * 
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addGlobalI18nMessage(String bundleName, FacesMessage.Severity severity, String summaryKey) {
		addI18nMessage(null, bundleName, severity, summaryKey, null, null, null);
	}

	/**
	 * Shortcut to addI18nMessage(): global message without a specific severity and simple summary and detail messages.
	 * 
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addGlobalI18nMessage(String bundleName, String summaryKey, String detailKey) {
		addI18nMessage(null, bundleName, null, summaryKey, null, detailKey, null);
	}

	/**
	 * Shortcut to addI18nMessage(): global message without a specific severity and including only a simple summary.
	 * 
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle. \ *
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addGlobalI18nMessage(String bundleName, String summaryKey) {
		addI18nMessage(null, bundleName, null, summaryKey, null, null, null);
	}

	/**
	 * Shortcut to addI18nMessage(): global message with specified severity and parameterized summary and detail
	 * messages.
	 * 
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * @param detailParams
	 *            The parameters for the detail message.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addGlobalI18nMessage(String bundleName, FacesMessage.Severity severity, String summaryKey, Object[] summaryParams, String detailKey, Object[] detailParams) {
		addI18nMessage(null, bundleName, severity, summaryKey, summaryParams, detailKey, detailParams);
	}

	/**
	 * Shortcut to addI18nMessage(): global message with specified severity and including only a parameterized summary.
	 * 
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addGlobalI18nMessage(String bundleName, FacesMessage.Severity severity, String summaryKey, Object ... summaryParams) {
		addI18nMessage(null, bundleName, severity, summaryKey, summaryParams, null, null);
	}

	/**
	 * Shortcut to addI18nMessage(): global message without a specific severity and parameterized summary and detail
	 * messages.
	 * 
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * @param detailParams
	 *            The parameters for the detail message.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addGlobalI18nMessage(String bundleName, String summaryKey, Object[] summaryParams, String detailKey, Object[] detailParams) {
		addI18nMessage(null, bundleName, null, summaryKey, summaryParams, detailKey, detailParams);
	}

	/**
	 * Shortcut to addI18nMessage(): global message without a specific severity and including only a parameterized
	 * summary.
	 * 
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addGlobalI18nMessage(String bundleName, String summaryKey, Object ... summaryParams) {
		addI18nMessage(null, bundleName, null, summaryKey, summaryParams, null, null);
	}

	/**
	 * Shortcut to addI18nMessage(): field message with specified severity and simple summary and detail messages.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addFieldI18nMessage(String fieldName, String bundleName, FacesMessage.Severity severity, String summaryKey, String detailKey) {
		addI18nMessage(fieldName, bundleName, severity, summaryKey, null, detailKey, null);
	}

	/**
	 * Shortcut to addI18nMessage(): field message with specified severity and including only a simple summary.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addFieldI18nMessage(String fieldName, String bundleName, FacesMessage.Severity severity, String summaryKey) {
		addI18nMessage(fieldName, bundleName, severity, summaryKey, null, null, null);
	}

	/**
	 * Shortcut to addI18nMessage(): field message without a specific severity and simple summary and detail messages.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addFieldI18nMessage(String fieldName, String bundleName, String summaryKey, String detailKey) {
		addI18nMessage(fieldName, bundleName, null, summaryKey, null, detailKey, null);
	}

	/**
	 * Shortcut to addI18nMessage(): field message without a specific severity and including only a simple summary.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addFieldI18nMessage(String fieldName, String bundleName, String summaryKey) {
		addI18nMessage(fieldName, bundleName, null, summaryKey, null, null, null);
	}

	/**
	 * Shortcut to addI18nMessage(): field message with specified severity and parameterized summary and detail
	 * messages.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * @param detailParams
	 *            The parameters for the detail message.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addFieldI18nMessage(String fieldName, String bundleName, FacesMessage.Severity severity, String summaryKey, Object[] summaryParams, String detailKey, Object[] detailParams) {
		addI18nMessage(fieldName, bundleName, severity, summaryKey, summaryParams, detailKey, detailParams);
	}

	/**
	 * Shortcut to addI18nMessage(): field message with specified severity and including only a parameterized summary.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param severity
	 *            The severity of the message (one of the severity levels defined by JSF).
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addFieldI18nMessage(String fieldName, String bundleName, FacesMessage.Severity severity, String summaryKey, Object ... summaryParams) {
		addI18nMessage(fieldName, bundleName, severity, summaryKey, summaryParams, null, null);
	}

	/**
	 * Shortcut to addI18nMessage(): field message without a specific severity and parameterized summary and detail
	 * messages.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * @param detailKey
	 *            The key that identifies the message that will serve as detail in the resource bundle.
	 * @param detailParams
	 *            The parameters for the detail message.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addFieldI18nMessage(String fieldName, String bundleName, String summaryKey, Object[] summaryParams, String detailKey, Object[] detailParams) {
		addI18nMessage(fieldName, bundleName, null, summaryKey, summaryParams, detailKey, detailParams);
	}

	/**
	 * Shortcut to addI18nMessage(): field message without a specific severity and including only a parameterized
	 * summary.
	 * 
	 * @param fieldName
	 *            The name of the field to which the message will be attached. If null, the message is global.
	 * @param bundleName
	 *            The name of the bundle where to look for the message.
	 * @param summaryKey
	 *            The key that identifies the message that will serve as summary in the resource bundle.
	 * @param summaryParams
	 *            The parameters for the summary message.
	 * 
	 * @see br.ufes.inf.nemo.jbutler.ejb.controller.JSFController#addI18nMessage(java.lang.String, java.lang.String,
	 *      javax.faces.application.FacesMessage.Severity, java.lang.String, java.lang.Object[], java.lang.String,
	 *      java.lang.Object[])
	 */
	protected void addFieldI18nMessage(String fieldName, String bundleName, String summaryKey, Object ... summaryParams) {
		addI18nMessage(fieldName, bundleName, null, summaryKey, summaryParams, null, null);
	}
}
