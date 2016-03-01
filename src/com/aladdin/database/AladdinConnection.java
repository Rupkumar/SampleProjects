package com.aladdin.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AladdinConnection implements AutoCloseable
{
	private final AladdinConnectionPool pool;

	private final Connection conn;

	AladdinConnection(AladdinConnectionPool pool, Connection conn) throws SQLException
	{
		this.pool = pool;
		this.conn = conn;
	}

	Connection getConnection()
	{
		return this.conn;
	}

	public PreparedStatement preparedStatement(String sql) throws SQLException
	{
		return this.conn.prepareStatement(sql);
	}

	public Statement statement() throws SQLException
	{
		return this.conn.createStatement();
	}

	@Override
	public void close() throws Exception
	{
	    if (pool != null) {
	        pool.closeAladdinConnection(this);
	    }
	}
}