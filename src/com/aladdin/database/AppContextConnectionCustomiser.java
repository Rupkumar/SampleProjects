package com.aladdin.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.bfm.util.BFMUtil;
import com.bfm.util.spring.BFMJdbcDaoSupport;
import com.mchange.v2.c3p0.ConnectionCustomizer;

public class AppContextConnectionCustomiser extends BFMJdbcDaoSupport implements ConnectionCustomizer {

	private static final Logger LOGGER = Logger.getLogger(AppContextConnectionCustomiser.class);

	public void onAcquire(Connection c, String pdsIdt) throws SQLException {

		Statement statement = null;

		String defaultWebServer = BFMUtil.getDefaultWebServer().getHost();
		String user = BFMUtil.getUser();
		
		LOGGER.debug("defaultWebServer = "+ defaultWebServer.toString());
    		
		try {
		    if (defaultWebServer.equals("webster.bfm.com") ||
		            defaultWebServer.equals("tst.blackrock.com") ||
		            defaultWebServer.equals("dev.blackrock.com")) {
		        
    			String applicationName = System.getProperty("applicationName");
    			statement = c.createStatement();
    			statement.execute("select set_appcontext('audit','program','" + applicationName + "')");
                statement.execute("select set_appcontext('audit','user_id','" + user + "')");
    			LOGGER.info("Set app context to '" + applicationName + "' and '" + user + "' for new connection");
		    }
		} catch (SQLException e) {
			LOGGER.error("Failed to create app context for connection", e);
			throw e;

		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	public void onDestroy(Connection c, String pdsIdt) {
	}

	public void onCheckOut(Connection c, String pdsIdt) {
	}

	public void onCheckIn(Connection c, String pdsIdt) {
	}
}
