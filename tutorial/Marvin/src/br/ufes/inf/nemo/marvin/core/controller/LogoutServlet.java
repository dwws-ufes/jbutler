package br.ufes.inf.nemo.marvin.core.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A servlet that serves to invalidate the user's session and, therefore, log her out of the system.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 */
@WebServlet(name = "LogoutServlet", urlPatterns = { "/logout" })
public class LogoutServlet extends HttpServlet {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(LogoutServlet.class.getCanonicalName());

	/**
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.log(Level.FINER, "Invalidating a user session...");

		// Destroys the session for this user.
		HttpSession session = request.getSession(false);
		if (session != null) session.invalidate();

		// Redirects back to the initial page.
		response.sendRedirect(request.getContextPath());
	}
}
