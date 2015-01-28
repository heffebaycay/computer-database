package fr.heffebaycay.cdb.dao.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;
import fr.heffebaycay.cdb.util.AppSettings;

/**
 * The <i>DaoManager</i> object can be used to access the various objects that
 * allow some CRUD actions on the <i>Computer</i> and <i>Company</i> objects 
 */
@Component
public class DaoManager {

  private ThreadLocal<Connection> threadLocalConnection;

  private static final Logger     LOGGER = LoggerFactory.getLogger(DaoManager.class);

  private DriverManagerDataSource mgrDataSource;

  @Autowired
  public DaoManager(DriverManagerDataSource pMgrDataSource) {

    LOGGER.debug("DaoManager(): Creating instance of DaoManager");

    this.mgrDataSource = pMgrDataSource;

    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Failed to load MySQL JDBC driver.", e);
    }

    threadLocalConnection = new ThreadLocal<Connection>();

  }

  public Connection getConnection() {

    if (threadLocalConnection.get() == null) {
      // No connection available for current Thread
      try {
        threadLocalConnection.set(mgrDataSource.getConnection());
      } catch (SQLException e) {
        LOGGER.warn("Failed to get DB connection.", e);
      }
    }

    return threadLocalConnection.get();
  }

  public void closeConnection() {
    LOGGER.debug("closeConnection(): About to close connection");
    try {
      getConnection().close();
      threadLocalConnection.remove();
    } catch (SQLException e) {
      LOGGER.warn("closeConnection(): SQLException: ", e);
    }
  }

  public void closeStatement(Statement stmt) {
    try {
      if (stmt != null) {
        stmt.close();
      }
    } catch (SQLException e) {
      LOGGER.warn("closeStatement(): SQLException: ", e);
    }
  }

}
