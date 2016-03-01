package com.aladdin.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aladdin.database.DatabaseHelper;

@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSource writeDataSource;

    @Autowired
    private DataSource readDataSource;
    
    @Autowired
    private DataSource readGpxDataSource;

    //@Bean
    public DatabaseHelper createDatabaseHelper() {
        return DatabaseHelper.setup(writeDataSource, readDataSource, readGpxDataSource);
    }

}