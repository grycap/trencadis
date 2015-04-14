/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.TRENCADIS_JAVA_API_SQL_Ontologies_Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import trencadis.infrastructure.SQLDatabaseconnectionPool.SQLDatabaseConnectionPoolDriver;

public class TRENCADIS_JAVA_API_SQL_Ontologies_Database {

	private String strError = "";

	public TRENCADIS_JAVA_API_SQL_Ontologies_Database(String db_host_name,
			String db_host_port, String db_db_name, String db_user,
			String db_password) {
		try {
			/**************************************************************/
			/***** CONECTION POOL TO SQL ONTOLOGIES DATABASE **************/
			new SQLDatabaseConnectionPoolDriver("org.postgresql.Driver",
					"jdbc:ontologiesServerPool:", "jdbc:postgresql://"
							+ db_host_name + ":" + db_host_port + "/"
							+ db_db_name, db_user, db_password);
			/**************************************************************/
		} catch (Exception ex) {
			ex.printStackTrace();
			strError = ex.toString();
		}

	}

	public String strGetError() {
		return strError;
	}

	public int iInsertOntology(String strIDOntology, String strDescription,
			String xml_DICOM_SR_Template) {
		try {

			Connection con = DriverManager.getConnection("jdbc:ontologiesServerPool:jdcpool");
			Statement stmt = con.createStatement();

			xml_DICOM_SR_Template = xml_DICOM_SR_Template.replaceAll("'", "''");

			String strSQL = "INSERT INTO tblontologies VALUES (" + "'"
					+ strIDOntology + "'," + "'" + strDescription + "'," + "'"
					+ xml_DICOM_SR_Template + "')";

			stmt.executeUpdate(strSQL);

			con.close();

			return 0;
		} catch (Exception ex) {
			strError = ex.toString();
			ex.printStackTrace();
			return -1;
		}
	}

	public int iRemoveOntology(String strIDOntology) {
		try {

			Connection con = DriverManager
					.getConnection("jdbc:ontologiesServerPool:jdcpool");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM tblontologies WHERE like(idontology,'"
					+ strIDOntology + "')");
			con.close();

			return 0;
		} catch (Exception ex) {
			strError = ex.toString();
			return -1;
		}
	}

	public String xmlGetOntology(String strIDOntology) {
		try {
			String xmlResult = "";
			Connection con = DriverManager.getConnection("jdbc:ontologiesServerPool:jdcpool");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM tblOntologies WHERE like(idontology,'"
							+ strIDOntology + "')");
			con.close();

			if (rs.next()) {
				xmlResult += "<ONTOLOGY IDOntology=\""
						+ rs.getString("idontology") + "\" Description=\""
						+ rs.getString("Description") + "\">";
				xmlResult += "\t<xml_DICOM_SR_Template>";
				xmlResult += rs.getString("xml_DICOM_SR_Template") + "\n";
				xmlResult += "\t</xml_DICOM_SR_Template>";
				xmlResult += "</ONTOLOGY>";

				return xmlResult;
			} else {
				strError = "Ontology " + strIDOntology
						+ "not found in database.";
				return "-1";
			}

		} catch (Exception ex) {
			strError = ex.toString();
			return "-1";
		}
	}

	public String xmlGetAllOntologies() {
		try {
			String xmlResult = "";

			Connection con = DriverManager.getConnection("jdbc:ontologiesServerPool:jdcpool");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM tblOntologies");
			con.close();
			xmlResult += "\t<ONTOLOGIES>";
			while (rs.next()) {
				xmlResult += "<ONTOLOGY IDOntology=\""
						+ rs.getString("IDOntology") + "\" Description=\""
						+ rs.getString("Description") + "\">";
				xmlResult += "\t<xml_DICOM_SR_Template>";
				xmlResult += rs.getString("xml_DICOM_SR_Template") + "\n";
				xmlResult += "\t</xml_DICOM_SR_Template>";
				xmlResult += "</ONTOLOGY>";
			}
			xmlResult += "</ONTOLOGIES>\n";

			return xmlResult;

		} catch (Exception ex) {
			strError = ex.toString();
			return "-1";
		}
	}

	public int iNumberOfOntologies() {
		try {
			Connection con = DriverManager.getConnection("jdbc:ontologiesServerPool:jdcpool");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT count(*) as NOntologies FROM tblOntologies");
			con.close();
			rs.first();
			return rs.getInt("NOntologies");

		} catch (Exception ex) {
			strError = ex.toString();
			return -1;
		}
	}

	public int iUpdateOntologiesServerServiceInfo(int[] iId,
			String[] strDescripcion) {
		try {

			Connection con = DriverManager.getConnection("jdbc:ontologiesServerPool:jdcpool");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT IDOntology, Description FROM tblOntologies");
			con.close();
			int iIndex = 0;
			while (rs.next()) {
				iId[iIndex] = rs.getInt("IDOntology");
				strDescripcion[iIndex] = rs.getString("Description");
				iIndex++;
			}

			return iIndex;

		} catch (Exception ex) {
			strError = ex.toString();
			return -1;
		}
	}

	public static void main(String[] args) {
		// TODO code application logic here
	}
}
