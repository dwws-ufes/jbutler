package br.ufes.inf.nemo.marvin.core.application;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Named;

import br.ufes.inf.nemo.jbutler.ResourceUtil;
import br.ufes.inf.nemo.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import br.ufes.inf.nemo.marvin.core.domain.MarvinConfiguration;
import br.ufes.inf.nemo.marvin.core.persistence.MarvinConfigurationDAO;

/**
 * TODO: document this type.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Singleton
@Named("coreInfo")
public class CoreInformation implements Serializable {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(CoreInformation.class.getCanonicalName());

	/** The path (in the EJB module) for the quotes file. */
	private static final String QUOTES_FILE_PATH = "/META-INF/quotes.txt";

	/** TODO: document this field. */
	private static final String DEFAULT_DECORATOR_NAME = "default";

	/** The DAO for MarvinConfiguration objects. */
	@EJB
	private MarvinConfigurationDAO marvinConfigurationDAO;

	/** TODO: document this field. */
	private MarvinConfiguration currentConfig;

	/** Indicates if the system is properly installed. */
	private Boolean systemInstalled;

	/** Indicates the decorator being used. */
	private String decorator = DEFAULT_DECORATOR_NAME;

	/** List of quotes. */
	private List<String> quotes;

	/** Random number generator. */
	private Random random = new Random(System.currentTimeMillis());

	/** Initializer method. */
	@PostConstruct
	public void init() {
		// At first use, check if the system is installed.
		logger.log(Level.FINER, "Checking if the system has been installed...");

		// If the system is configured, it's installed.
		try {
			currentConfig = marvinConfigurationDAO.retrieveCurrentConfiguration();
			systemInstalled = true;
		}
		catch (PersistentObjectNotFoundException e) {
			systemInstalled = false;
		}

		// Load quotes.
		quotes = new ArrayList<>();
		File quotesFile = ResourceUtil.getResourceAsFile(QUOTES_FILE_PATH);
		try (Scanner scanner = new Scanner(quotesFile)) {
			while (scanner.hasNextLine())
				quotes.add(scanner.nextLine().trim());
		}
		catch (IOException e) {
			logger.log(Level.WARNING, "Could not load quotes from path: {0}", QUOTES_FILE_PATH);
			quotes.add("I didn't ask to be made.");
		}
	}

	/** Getter for currentConfig. */
	public MarvinConfiguration getCurrentConfig() {
		return currentConfig;
	}

	/** Getter for systemInstalled. */
	public Boolean getSystemInstalled() {
		return systemInstalled;
	}

	/** Getter for decorator. */
	public String getDecorator() {
		return decorator;
	}

	/** Gets a random quote from Marvin. */
	public String getQuote() {
		int idx = random.nextInt(quotes.size());
		return quotes.get(idx);
	}
}
