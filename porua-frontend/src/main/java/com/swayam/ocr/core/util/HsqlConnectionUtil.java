package com.swayam.ocr.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HsqlConnectionUtil {

	private static final Logger LOG = LoggerFactory.getLogger(HsqlConnectionUtil.class);

	private static final String INIT_SCRIPT = "/com/swayam/ocr/res/sql/CreateTable.sql";

	private final String jdbcUrl;
	private final String user;
	private final String password;

	public static final HsqlConnectionUtil INSTANCE = new HsqlConnectionUtil();

	private HsqlConnectionUtil() {
		Properties jdbcProperties = new Properties();
		try {
			jdbcProperties.load(HsqlConnectionUtil.class.getResourceAsStream("/jdbc.properties"));
		} catch (IOException e) {
			new RuntimeException("error loading properties file for jdbc", e);
		}

		String hsqlDbfile = jdbcProperties.getProperty("hsqlDbfile");

		LOG.info("hsqlDbfile={}", hsqlDbfile);

		jdbcUrl = String.format(jdbcProperties.getProperty("jdbcUrl"), hsqlDbfile);
		user = jdbcProperties.getProperty("user");
		password = jdbcProperties.getProperty("password");

		InputStream is = HsqlConnectionUtil.class.getResourceAsStream(INIT_SCRIPT);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();

		try {

			while (true) {

				String line = reader.readLine();

				if (line == null) {
					break;
				}

				sb.append(line).append('\n');

			}

		} catch (IOException e) {
			LOG.error("could not read init script", e);
		}

		if (sb.length() > 0) {

			try {

				Connection con = getDbConnection();
				Statement stat = con.createStatement();

				String sql = sb.toString();

				LOG.debug("initial sql {}", sql);

				stat.execute(sql);

				stat.close();
				con.close();

			} catch (SQLException e) {
				LOG.error("could not execute init script", e);
			}

		}

	}

	public Connection getDbConnection() throws SQLException {
		Connection con = DriverManager.getConnection(jdbcUrl, user, password);
		return con;
	}

}
