package br.ufes.inf.nemo.marvin.core.application;

import java.io.Serializable;

import javax.ejb.Local;

import br.ufes.inf.nemo.marvin.core.domain.Academic;
import br.ufes.inf.nemo.marvin.core.exceptions.LoginFailedException;

/**
 * Local EJB interface for the session information component. This bean is responsible for storing information on each
 * different user of the system, such as the Academic object that represents the logged in user. It should also provide
 * an authentication method for the controller, identifying users of the system.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
@Local
public interface SessionInformation extends Serializable {
	/**
	 * Obtains the currently logged in user.
	 * 
	 * @return The Academic object that represents the user that is currently logged in.
	 */
	Academic getCurrentUser();

	/**
	 * Authenticates a user given her username and password. If the user is correctly authenticated, she should be
	 * available as a Academic object through the getCurrentUser() method.
	 * 
	 * @param username
	 *            The username that identifies the user in the system.
	 * @param password
	 *            The password that authenticates the user.
	 * 
	 * @throws LoginFailedException
	 *             If the username is unknown or the password is incorrect.
	 */
	void login(String username, String password) throws LoginFailedException;
}
