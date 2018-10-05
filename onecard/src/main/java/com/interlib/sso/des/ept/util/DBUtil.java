package com.interlib.sso.des.ept.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	
	private static final String DRIVER = "oracle.jdbc.OracleDriver";
	private static String URL = "";
	private static String USERNAME = "";
	private static String PASSWORD = "";
	private static Connection connection;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	
	public static void init() {
		URL = PropertyUtil.getProp().getProperty("jdbc.url");
		USERNAME = PropertyUtil.getProp().getProperty("jdbc.username");
		PASSWORD = PropertyUtil.getProp().getProperty("jdbc.password");
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("数据库驱动注册失败！");
			e.printStackTrace();
		}
		connection = getConnection();
	}
	
	private static Connection getConnection() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.out.println("获取连接失败！");
			e.printStackTrace();
		}
		return connection;
	}
	
	public static List<Map<String,String>> getBaseDataList(String sql) {
		List<Map<String,String>> list = null;
		ResultSet resultSet = getBaseResultSet(sql);
		if (resultSet == null) {
			return null;
		}
		list = new ArrayList<Map<String,String>>();
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			Map<String,String> map = null;
			String colName = null;
			while (resultSet.next()) {
				map = new HashMap<String,String>();
				for (int i=1; i<=columnCount; i++) {
					colName = metaData.getColumnLabel(i);
					map.put(colName, resultSet.getObject(i).toString());
				}
				list.add(map);
			}
		} catch (SQLException e) {
			System.out.println("数据结果集处理失败！");
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("查询数据库游标关闭失败！");
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public static boolean execUpdate(String sql, List<Object> params) {
		boolean flag = false;
		int result = -1;
		try {
			pstmt = connection.prepareStatement(sql);
			if (params != null && !params.isEmpty()) {
				int size = params.size();
				for (int i = 0; i < size; i++) {
					pstmt.setObject(i+1, params.get(i));
				}
			}
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("更新数据库失败！");
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("更新数据库游标关闭失败！");
					e.printStackTrace();
				}
			}
		}
		flag = result > 0 ? true : false;
		return flag;
	}
	
	private static ResultSet getBaseResultSet(String sql) {
		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("数据库查询异常！");
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void closeDB() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println("关闭结果集异常！");
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				System.out.println("关闭statement异常！");
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("关闭连接异常！");
				e.printStackTrace();
			}
		}
	}
	
}
