package com.interlib.sso.common.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
	
	public static void closeCon(Connection con) {
		if(con != null) {
			try {
				con.close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeRS(ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeS(Statement s){
		if(s != null){
			try {
				s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeRSAndS(ResultSet rs, Statement s){
		closeRS(rs);
		closeS(s);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
