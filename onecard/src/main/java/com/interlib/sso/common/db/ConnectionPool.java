package com.interlib.sso.common.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public class ConnectionPool {
	
	private static BasicDataSource dataSource = null;
	
	public ConnectionPool(){
		
	}
	
	@SuppressWarnings("rawtypes")
	public static Properties loadParams(String s) throws IOException {
		Properties properties = new Properties();
		ResourceBundle resourcebundle = ResourceBundle.getBundle(s);
		Enumeration enumeration = resourcebundle.getKeys();
		String s1;
		for (; enumeration.hasMoreElements(); properties.put(s1, resourcebundle
				.getObject(s1)))
			s1 = (String) enumeration.nextElement();

		return properties;
	}
	
	public static void init(){
		if (dataSource != null) {
            try {
                dataSource.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
            dataSource = null;
        }
		
		try {
			Properties p = loadParams("jdbc");
			Properties jdbcProp = new Properties();
			for(Object keyObj : p.keySet()) {
				String key = (String) keyObj;
				Object value = p.get(keyObj);
				if(key.startsWith("jdbc.")) {
					key = key.replaceFirst("jdbc.", "");
					jdbcProp.put(key, value);
				} else {
					jdbcProp.put(key, value);
				}
			}
			dataSource = (BasicDataSource) BasicDataSourceFactory.
					createDataSource(jdbcProp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized Connection getConnection() throws SQLException {
		if (dataSource == null) {
            init();
        }
        Connection conn = null;
        if (dataSource != null) {
            conn = dataSource.getConnection();
        }
        return conn;
	}
	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		Connection con = getConnection();
    	System.out.println(con);
	}

}
