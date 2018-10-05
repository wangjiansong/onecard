package com.interlib.sso.updatedb;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateJavaDoLast {
	// /在执行数据库升级前做的事情
	public static void doJavaUpd_before(Connection con, Statement stmtSel,
			Statement stmtUpdate, ResultSet rs, ArrayList successInfoList,
			ArrayList failureInfoList) {

	}

	// /在执行数据库升级后做的事情
	public static void doJavaUpd_after(Connection con, Statement stmtSel,
			Statement stmtUpdate, ResultSet rs, ArrayList successInfoList,
			ArrayList failureInfoList) {
	}

	public static void main(String[] args) {
	}

	public UpdateJavaDoLast() {
	}
}