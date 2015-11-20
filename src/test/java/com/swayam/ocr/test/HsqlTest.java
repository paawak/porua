package com.swayam.ocr.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HsqlTest {
	
	private Connection con;
	
	@Before
	public void initConnection() throws SQLException {
		
		String dbLoc = System.getProperty("user.home") + "/.ocr/db/testdb";
		
		con = DriverManager.getConnection("jdbc:hsqldb:file:" + dbLoc +
				";shutdown=true", "SA", "");
		
		String createTable = "CREATE MEMORY TABLE IF NOT EXISTS test ( id TINYINT GENERATED ALWAYS AS IDENTITY(START WITH 1) PRIMARY KEY, name VARCHAR(20) )";
		
		con.createStatement().execute(createTable);
		
	}
	
	@Test
	public void insertData() throws SQLException {
		
		
		
		for (int i = 1; i <= 10; i++) {
			
			String sql = "INSERT INTO test VALUES (DEFAULT, 'NAME" + i + "')";
			System.out.println(sql); 
			con.createStatement().executeUpdate(sql);
			
		}
		
	}
	
	@Test
	public void fetchData() throws SQLException {
		
		String query = "SELECT * FROM test";
		
		ResultSet res = con.createStatement().executeQuery(query);
		
		while (res.next()) {
			System.out.println(res.getInt(1) + "----" + res.getString(2)); 
		}
		
	}
	
	@After
	public void closeConnection() throws SQLException {
		
		if (con != null) {
			
			con.close();
			
		}
		
	}

}
