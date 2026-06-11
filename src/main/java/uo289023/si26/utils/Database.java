package uo289023.si26.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;

public class Database extends DbUtil {
	private static final String APP_PROPERTIES = "src/main/resources/application.properties";
	private static final String SQL_SCHEMA = "src/main/resources/schema.sql";
	private static final String SQL_LOAD = "src/main/resources/data.sql";
	private String driver;
	private String url;
	private static boolean databaseCreated = false;

	public Database() {
		Properties prop = new Properties();
		try (FileInputStream fs = new FileInputStream(APP_PROPERTIES)) {
			prop.load(fs);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		driver = prop.getProperty("datasource.driver");
		url = prop.getProperty("datasource.url");
		if (driver == null || url == null)
			throw new ApplicationException("Driver and/or url configuration not found in application.properties");
		DbUtils.loadDriver(driver);
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void createDatabase(boolean onlyOnce) {
		if (!databaseCreated || !onlyOnce) {
			executeScript(SQL_SCHEMA);
			databaseCreated = true;
		}
	}

	public void loadDatabase() {
		executeScript(SQL_LOAD);
	}
}
