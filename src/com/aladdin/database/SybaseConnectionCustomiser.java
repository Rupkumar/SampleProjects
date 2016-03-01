package com.aladdin.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ConnectionCustomizer;

public class SybaseConnectionCustomiser extends AppContextConnectionCustomiser implements ConnectionCustomizer {

    private static final Logger LOGGER = Logger.getLogger(SybaseConnectionCustomiser.class);
    
    private String sql;
    
    
    public SybaseConnectionCustomiser() {
        try {
            //sql = getSql("/sql/create_temp_tables.sql", null);
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialise SybaseConnectionCustomiser", e);
        }
    }
    
    @Override
    public void onAcquire(Connection c, String pdsIdt) throws SQLException {
    	super.onAcquire(c, pdsIdt);
        
        try {
            Statement statement = c.createStatement();
            //statement.execute(sql);
            LOGGER.info("Created temporary tables for new connection");
            
        } catch (SQLException e) {
            LOGGER.error("Failed to create temporary tables for connection", e);
            throw e;
        }
    } 
}
