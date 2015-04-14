package trencadis.infrastructure.SQLDatabaseconnectionPool;

import java.sql.*;
import java.util.*;

class ConnectionReaper extends Thread {

    private SQLDatabaseConnectionPool pool;
    private final long delay=300000;

    ConnectionReaper(SQLDatabaseConnectionPool pool) {
        this.pool=pool;
    }

    public void run() {
        while(true) {
           try {
              sleep(delay);
           } catch( InterruptedException e) { }
           pool.reapConnections();
        }
    }
}

public class SQLDatabaseConnectionPool {

   private Vector<SQLDatabaseConnectionWrapper> connections;
   private String url, user, password;
   final private long timeout=60000;
   private ConnectionReaper reaper;
   final private int poolsize=10;

   public SQLDatabaseConnectionPool(String url, String user, String password) {
      this.url = url;
      this.user = user;
      this.password = password;
      connections = new Vector<SQLDatabaseConnectionWrapper>(poolsize);
      reaper = new ConnectionReaper(this);
      reaper.start();
   }

   public synchronized void reapConnections() {

      long stale = System.currentTimeMillis() - timeout;
      Enumeration<SQLDatabaseConnectionWrapper> connlist = connections.elements();
    
      while((connlist != null) && (connlist.hasMoreElements())) {
          SQLDatabaseConnectionWrapper conn = (SQLDatabaseConnectionWrapper)connlist.nextElement();

          if((conn.inUse()) && (stale >conn.getLastUse()) && 
                                            (!conn.validate())) {
 	      removeConnection(conn);
         }
      }
   }

   public synchronized void closeConnections() {
        
      Enumeration<SQLDatabaseConnectionWrapper> connlist = connections.elements();

      while((connlist != null) && (connlist.hasMoreElements())) {
          SQLDatabaseConnectionWrapper conn = (SQLDatabaseConnectionWrapper)connlist.nextElement();
          removeConnection(conn);
      }
   }

   private synchronized void removeConnection(SQLDatabaseConnectionWrapper conn) {
       connections.removeElement(conn);
   }


   public synchronized Connection getConnection() throws SQLException {

       SQLDatabaseConnectionWrapper c;
       for(int i = 0; i < connections.size(); i++) {
           c = (SQLDatabaseConnectionWrapper)connections.elementAt(i);
           if (c.lease()) {
              return c;
           }
       }

       Connection conn = DriverManager.getConnection(url, user, password);
       c = new SQLDatabaseConnectionWrapper(conn, this);
       c.lease();
       connections.addElement(c);
       return c;
  } 

   public synchronized void returnConnection(SQLDatabaseConnectionWrapper conn) {
      conn.expireLease();
   }
}
