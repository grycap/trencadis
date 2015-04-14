/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.middleware.operations.EOUIDGeneratorService;

import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 *
 * @author root
 */
public class TRENCADIS_NEW_EOUID extends
		TRENCADIS_OPERATION_EOUIDGENERATOR_SERVICE {

	public TRENCADIS_NEW_EOUID(TRENCADIS_SESSION session) throws Exception {
		super(session);
	}

	/**
	 * This method starts the new EOUID operation.
	 *
	 * @throws Exception
	 */
	public String execute() throws Exception {

		String xmlInputNewEOUID = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "</INPUT>";

		String xmlOutputNewOperator = this.EOUIDGeneratorService
				.xmlGetNext(xmlInputNewEOUID);

		trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.xmlOutputGetNext.XmlWrapper new_eouid__wrapper = new trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.xmlOutputGetNext.XmlWrapper(
				xmlOutputNewOperator, false);
		new_eouid__wrapper.wrap();

		if (new_eouid__wrapper.get_OUTPUT().get_STATUS().equals("-1")) {
			throw new Exception("Can not add report: "
					+ new_eouid__wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {
			return new_eouid__wrapper.get_OUTPUT().get_EOUID().toString();
		}
	}

}
