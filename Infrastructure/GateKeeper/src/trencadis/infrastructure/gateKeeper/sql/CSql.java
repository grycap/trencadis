package trencadis.infrastructure.gateKeeper.sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import trencadis.infrastructure.SQLDatabaseconnectionPool.SQLDatabaseConnectionPoolDriver;

public class CSql {

    private Connection db; //Conection of Databse SQL for consult index
    private String poolName;
    
    /*****************************************************************************************************************/
    /* Constructor Class                                                                                             */
    /*****************************************************************************************************************/
    public CSql(String driver, String poolName, String URLBegin, 
    			String strDBHostName, String strDBPort, String strDBName,
                String strDBUser, String strDBPassword) {
        try {
    		new SQLDatabaseConnectionPoolDriver(
    				driver,
    				poolName,
    				URLBegin
    				+ strDBHostName
    				+ ":" + strDBPort
    				+ "/" + strDBName,
    				strDBUser, strDBPassword);
        
    		this.poolName = poolName + "jdcpool";
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw new RuntimeException("ERROR to CSql. " + ex.toString());
        }

    }

    /*****************************************************************************************************************/
    /* Method that consult in the database if a group is accexped or not accepted                                    */
    /* Return:
    /*      ERROR: Error Ocurred
    /*      NOT FOUND: Group not found in the Database
    /*      OK: Access OK
    /*      DENIED: Access denied
    /*****************************************************************************************************************/
    public String strGroupAccess(String strGroup) {
        try {
            boolean bAccess = false;

            String strSQL = "SELECT baccess "        +
                             "FROM tblaccessgroups " +
                             "WHERE stridgroup = '" + strGroup + "'";
            
            db = DriverManager.getConnection(poolName);
            
            Statement st = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(strSQL);
            
            db.close();
            
            if (rs.first()) bAccess = rs.getBoolean("baccess");
            else return "NOT FOUND";

            if (bAccess) return "OK";
            else return "DENIED";

        } catch (Exception ex) {
            return "ERROR. " + ex.toString();
        }

    }
    /****************************************************************************************************************
     * Method that consult in the database if a group is accepted or not accepted                                    
     * Return:
     *      ERROR: Error Occurred
     *      NOT FOUND: Group not found in the Database
     *      OK: Access OK
     *      DENIED: Access denied
     ****************************************************************************************************************/
    public String strDNAccess(String strDN) {

    	try {
            boolean bAccess = false;

            String strSQL = "SELECT baccess "       +
                             "FROM tblaccessusers " +
                             "WHERE strdnuser = '"  + strDN + "'";

            db = DriverManager.getConnection(poolName);
            
            Statement st = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(strSQL);
            
            db.close();
            
            if (rs.first()) bAccess = rs.getBoolean("baccess");
            else return "NOT FOUND";

            if (bAccess) return "OK";
            else return "DENIED";

        } catch (Exception ex) {
            return "ERROR. " + ex.toString();
        }

    }

}
