package trencadis.infrastructure.SQLDatabaseconnectionPool;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;


public class SQLDatabaseConnectionPoolDriver implements Driver {

    public final String URL_PREFIX;
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 0;
    private SQLDatabaseConnectionPool pool;

    public SQLDatabaseConnectionPoolDriver(String driver, String poolName, String url, 
                                String user, String password) 
                            throws ClassNotFoundException, 
                                   InstantiationException,
                                   IllegalAccessException,
                                   SQLException
    {
    	URL_PREFIX = poolName;
        DriverManager.registerDriver(this);
        Class.forName(driver).newInstance();
        pool = new SQLDatabaseConnectionPool(url, user, password);
    }

    public Connection connect(String url, Properties props) 
                                       throws SQLException {
        if(!url.startsWith(URL_PREFIX)) {
             return null;
        }
        return pool.getConnection();
    }

    public boolean acceptsURL(String url) {
        return url.startsWith(URL_PREFIX);
    }

    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    public DriverPropertyInfo[] getPropertyInfo(String str, Properties props) {
        return new DriverPropertyInfo[0];
    }

    public boolean jdbcCompliant() {
        return false;
    }

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
