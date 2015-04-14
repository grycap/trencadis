/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.TRENCADIS_JAVA_API_SQL_Keys_Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import trencadis.infrastructure.SQLDatabaseconnectionPool.SQLDatabaseConnectionPoolDriver;


public class TRENCADIS_JAVA_API_SQL_Keys_Database {
    
    private String strError = "";
    
    public TRENCADIS_JAVA_API_SQL_Keys_Database(String db_host_name, String db_host_port, String db_db_name, String db_user,  String db_password){
        try{
                /**************************************************************/
                /***** CONECTION POOL TO SQL ONTOLOGIES DATABASE **************/                       
		new SQLDatabaseConnectionPoolDriver(
				"org.postgresql.Driver",
				"jdbc:ontologiesServerPool:",
				"jdbc:postgresql://"
				+ db_host_name	+ ":" + db_host_port
				+ "/" + db_db_name,
				db_user, db_password
		);
                /**************************************************************/
        }catch(Exception ex){
            ex.printStackTrace();
            strError = ex.toString();
        }
        
    }
    public String strGetError(){
        return strError;
    }
    
    public int iInsertSharePair(String strEOUID, Vector vOntology,
				int iIDPartKey, String strPartKey,
				String strUserDN, String strMac){
         Connection con = null;
        try{
            
            con = DriverManager.getConnection("jdbc:ontologiesServerPool:jdcpool");
             
            BEGIN_TRANSACTION(con);
                                                    			                                                        	        	
	    // validate data
            if (!this.validateData(strEOUID, vOntology, strMac)){
                strError = "Invalid data.";
                return -1;
            }   
            
            Statement stmt = con.createStatement();                                                           
            int rowsInserted = stmt.executeUpdate(
            		"INSERT INTO tblkeyshares VALUES ("+
            		"'"+ strEOUID   +"', "+
            		"'"+ iIDPartKey +"', "+
            		"'"+ strPartKey +"', "+
            		"'"+ strUserDN  +"', "+
            		"'"+ strMac     +"', "+
            		"now())");
            
            if (rowsInserted != 1) {
            	ROLLBACK_TRANSACTION(con);
                strError = "No rows were inserted into tblkeyshares . No change was made to the database.";
            	return -1;
            } else {
            	java.util.Iterator it = vOntology.iterator();
            	while (it.hasNext()) {
            		int rowsInserted2 = stmt.executeUpdate(
            				"INSERT INTO tblOntologies VALUES ("+
            				"'"+ strEOUID +"', "+
            				"'"+ Integer.toString((Integer) it.next()) +"')");
            		if (rowsInserted2 != 1) {
                                strError = "No rows were inserted into tblOntologies . No change was made to the database.";
            			ROLLBACK_TRANSACTION(con);            			
            		}
            	}
            }
            COMMIT_TRANSACTION(con);
            
            con.close();
            return 0;
        }
        catch (Exception ex) {
            ROLLBACK_TRANSACTION(con);
            strError = "Database transaction failed with error: " + ex.getMessage();
            return -1;
        }           
    }
    
    public String strGetSharePair(String EOUID, String IDOntology) {
    	    	
	Statement  stmt  = null;
        String strResult = "";
                                
        try {        	
                Connection conn = DriverManager.getConnection("jdbc:ontologiesServerPool:jdcpool");
        	conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        	conn.setReadOnly(true);
		
                stmt = conn.createStatement(ResultSet.CONCUR_UPDATABLE,
		     		            ResultSet.TYPE_SCROLL_SENSITIVE);        	
		// validate data
		if (!validateData(EOUID, Integer.parseInt(IDOntology))){
		 	strError = "Invalid data.";
                        return null;
                }

                // query the database, storing the result
                // in an object of type ResultSet
                ResultSet rs = stmt.executeQuery(
            		"SELECT tblkeyshares.shareid,     "  +
            		"       tblkeyshares.share,       "  +
            		"       tblkeyshares.mac          "  +
            		"FROM tblkeyshares, tblOntologies "  +
            		"WHERE "                             + 
                        "     tblkeyshares.guid     = '" + EOUID + "'    AND " +
            		"     tblkeyshares.guid     = tblOntologies.guid AND " +
            		"     tblkeyshares.ontology = '" + IDOntology          + "' " +
            		"LIMIT 1");
            if (rs.first()) {
                strResult = rs.getString("shareid").trim() + "," + 
                            rs.getString("share").trim() + "," +
                            rs.getString("mac").trim();
                return strResult;
            }else {            	
                strError = "No key pair found in the database. Using ontology = " + IDOntology + ".";
                return null;
            }
        }
        catch (Exception ex) {
            strError = "Database transaction failed with error: " + ex.getMessage();
            return null;
        }          
    }
      
     
     
    public int iUpdateSharePair(String strEOUID, String strIDOntology, int iIDPartKey,
                                String strPartKey, String strUserDN, String strMac) {
    	
    	Connection conn = null;
        Statement  stmt = null;
    	        
        try {
            conn = DriverManager.getConnection("jdbc:ontologiesServerPool:jdcpool");        	
            BEGIN_TRANSACTION(conn);
                 
            stmt = conn.createStatement();
        	
	   // validate data
	   if (!validateData(strEOUID, Integer.parseInt(strIDOntology), strMac)){
		strError = "Invalid data.";
                return -1;
           }
		    
            int rowsUpdateted  = stmt.executeUpdate(
		    		"UPDATE tblkeyshares "              +
		    		"SET shareID = '"+ iIDPartKey + "', "+
		    		"    share   = '"+ strPartKey + "', "+
                                "    userdn  = '"+ strUserDN  + "', "+
                                "    mac     = '"+ strMac     + "', "+
                                "    date    = now() "+
                                "FROM tblOntologies "+
		    		"WHERE tblkeyshares.guid       = '" + strEOUID +"'    AND " +
		    		"      tblkeyshares.guid      =  tblOntologies.guid  AND " +
		    		"      tblOntologies.ontology = '"+ strIDOntology +"'");
            if (rowsUpdateted != 1) {            	
                ROLLBACK_TRANSACTION(conn);
                strError = "No rows were updated. No change was made to the database.";
            	return -1;
                
            }
            COMMIT_TRANSACTION(conn);
            
            conn.close();
            return 0;
        }
        catch (Exception ex) {
            ROLLBACK_TRANSACTION(conn);
            strError = "Database transaction failed with error: " + ex.getMessage();
            return -1;            
        } 
    }
     
     
     
     
     
     
    private static boolean validateData(String uuidStr, int ontology) {
		return ((DataValidator.validateUUID(uuidStr)) &&
				(ontology >= 0));
    }
    
    private static boolean validateData(String uuidStr, int ontology,
    		String macStr) {
		return ((DataValidator.validateUUID(uuidStr)) &&
				(ontology >= 0) &&
				(DataValidator.validateMAC(macStr)));
    }
    
    private static boolean validateData(String uuidStr, Vector<Integer> ontVector,
    		String macStr) {
		return ((DataValidator.validateUUID(uuidStr)) &&
				(DataValidator.validateOntology(ontVector)) &&
				(DataValidator.validateMAC(macStr)));
    }    
    
  /*****************************************************************************************************/
  /* Metodos para realizar las transacciones                                                           */
  /*****************************************************************************************************/
  private void BEGIN_TRANSACTION(Connection con) {
    try{
      String strSQL = "BEGIN TRANSACTION";
      Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
      st.execute(strSQL);
      st.close();
    }catch(java.sql.SQLException sqlex){
      throw new RuntimeException("Error BEGIN TRANSACTION", sqlex);
    }
  }
  private void COMMIT_TRANSACTION(Connection con) {
    try{
      String strSQL = "COMMIT TRANSACTION";
      Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
      st.execute(strSQL);
      st.close();
    }catch(java.sql.SQLException sqlex){
      throw new RuntimeException("Error COMMIT TRANSACTION", sqlex);
    }
  }
  private void ROLLBACK_TRANSACTION(Connection con) {
    try{
      String strSQL = "ROLLBACK TRANSACTION";
      Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
      st.execute(strSQL);
      st.close();
    }catch(java.sql.SQLException sqlex){
      throw new RuntimeException("Error ROLLBACK TRANSACTION", sqlex);
    }
  }
  
  public static void main(String[] args) {
        // TODO code application logic here
  }
}
