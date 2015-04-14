package trencadis.middleware.operations;

import org.globus.axis.util.Util;

import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This is an abstract class for all the operations in this package. All
 * operations have a session.
 */
public abstract class TRENCADIS_OPERATION_GENERIC {

	protected TRENCADIS_SESSION _session;

	/**
	 * A simple constructor that receives the session of the operation.
	 * 
	 * @param session
	 *            {@link trencadis.middleware.login.TRENCADIS_SESSION
	 *            TRENCADIS_SESSION} object used by the child operation class
	 */
	public TRENCADIS_OPERATION_GENERIC(TRENCADIS_SESSION session) {
		_session = session;

		// Secure Connections
		Util.registerTransport();
	}

	/**
	 * Getter method for the
	 * {@link trencadis.middleware.login.TRENCADIS_SESSION TRENCADIS_SESSION}
	 * object of the operation.
	 * 
	 * @return {@link trencadis.middleware.login.TRENCADIS_SESSION
	 *         TRENCADIS_SESSION} object used by the child operation class
	 */
	public TRENCADIS_SESSION getSession() {
		return _session;
	}

}
