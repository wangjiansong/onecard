package com.interlib.sso.updatedb;

import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import org.dom4j.*;
import org.dom4j.io.*;
import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;

public class UpdateOracleDataBase {
	private OracleDataBaseInfo dbInfo;
	private UpdateOracleDataBaseContentInfo updateInfo = new UpdateOracleDataBaseContentInfo();

	private Connection conn;
	private Statement stmtSel;
	private Statement stmtUpdate;
	private ResultSet rs;

	private String sql;
	private Iterator sqlsIter;

	private String sqlUpdate;
	private SubField subField;
	private String method = "";

	private ArrayList failureInfoList = new ArrayList();
	private int failureInfoCount = 0;
	private int warmInfoCount = 0;
	private ArrayList successInfoList = new ArrayList();
	private int successInfoCount = 0;

	private File fileByXml;
	private SAXReader reader;
	private Document doc;
	private Element root;
	private Element content;
	private Element sqls;
	private Element sqlEl;
	private Element subFieldEl;

	public UpdateOracleDataBase(String xmlFileName, String connFileName) {
		try {
			dbInfo = new OracleDataBaseInfo(connFileName);
			/* 数据库连接 */
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			System.out.println("连接数据库---开始!!!");
			Date d1 = new Date();
			if (dbInfo.getUrl() == null || dbInfo.getUrl().equals("")) {
				conn = DriverManager.getConnection("jdbc:oracle:thin:@"
						+ dbInfo.getHostName() + ":" + dbInfo.getPort() + ":"
						+ dbInfo.getSID(), dbInfo.getUserName(),
						dbInfo.getPassword());
			} else {// RAC模式
				conn = DriverManager.getConnection(dbInfo.getUrl(),
						dbInfo.getUserName(), dbInfo.getPassword());
			}

			stmtUpdate = conn.createStatement();
			stmtSel = conn.createStatement();
			Date d2 = new Date();
			System.out.println("连接数据库---用时: " + (d2.getTime() - d1.getTime())
					/ 1000 + " 秒!!!");

			/* 读者xml配置文件 */
			fileByXml = new File(xmlFileName);
			FileInputStream in = new FileInputStream(fileByXml);
			Reader readerIn = new InputStreamReader(in, "utf-8");
			reader = new SAXReader();
			doc = reader.read(readerIn);
			root = doc.getRootElement();

		} catch (Exception e) {
			System.out.println("error:" + dbInfo.toString());
			e.printStackTrace();
		}
	}

	/*------------------------升级数据库的六种方法----开始------------------------*/
	/* 1. createTable 创建表 */
	private void createTable(UpdateOracleDataBaseContentInfo updateDBContentInfo) {
		if (searchExistInfo(updateDBContentInfo, sql)) {
			failureInfoList.add("(" + (++failureInfoCount) + ") "
					+ updateDBContentInfo.getComment() + " "
					+ updateDBContentInfo.getTableName() + " 警告：此表已存在!!!");
		} else {
			try {
				stmtUpdate = conn.createStatement();
				stmtUpdate.execute(updateDBContentInfo.getSql());
				successInfoList.add("(" + (++successInfoCount) + ") "
						+ updateDBContentInfo.getComment() + " "
						+ updateDBContentInfo.getTableName() + " 成功!!!");
			} catch (Exception e) {
				if (e.getMessage().contains("ORA-00955")) {
					failureInfoList.add("(" + (++warmInfoCount) + ") "
							+ updateDBContentInfo.getComment() + " "
							+ updateDBContentInfo.getTableName() + " 警告："
							+ e.getMessage());
				} else {
					failureInfoList.add("(" + (++failureInfoCount) + ") "
							+ updateDBContentInfo.getComment() + " "
							+ updateDBContentInfo.getTableName()
							+ " 失败 原因：SQL语句有误!!!" + e.getMessage() + ","
							+ updateDBContentInfo.getSql());
				}
				System.out.println(e.getMessage());
			}
		}
	}

	/* 2. deleteContent 删除表内容 */
	private void deleteContent(
			UpdateOracleDataBaseContentInfo updateDBContentInfo) {
		commonUpdate(updateDBContentInfo);
	}

	/* 3. InsertContent 增加表内容 */
	private void InsertContent(
			UpdateOracleDataBaseContentInfo updateDBContentInfo) {
		if ((searchExistInfo(updateDBContentInfo, sql))
				&& (updateDBContentInfo.getTableName().equals("subject") && (searchExistTableContent(
						updateDBContentInfo, "subject")))) {
			failureInfoList.add("(" + (++failureInfoCount) + ") "
					+ updateDBContentInfo.getComment() + " "
					+ updateDBContentInfo.getTableName()
					+ " 失败 原因：subject表有内容!!!");
		} else if ((searchExistInfo(updateDBContentInfo, sql))
				&& (updateDBContentInfo.getTableName().equals("SJHM") && (searchExistTableContent(
						updateDBContentInfo, "SJHM")))) {
			failureInfoList
					.add("(" + (++failureInfoCount) + ") "
							+ updateDBContentInfo.getComment() + " "
							+ updateDBContentInfo.getTableName()
							+ " 失败 原因：SJHM表有内容!!!");
		} else if ((searchExistInfo(updateDBContentInfo, sql))
				&& (updateDBContentInfo.getTableName().equals("DOBSURNAME") && (searchExistTableContent(
						updateDBContentInfo, "DOBSURNAME")))) {
			failureInfoList.add("(" + (++failureInfoCount) + ") "
					+ updateDBContentInfo.getComment() + " "
					+ updateDBContentInfo.getTableName()
					+ " 失败 原因：DOBSURNAME表有内容!!!");
		} else if (searchExistInfo(updateDBContentInfo, sql)) {
			sqlsIter = updateDBContentInfo.getSqlsList().iterator();
			int successAmount = 0;
			int failureAmount = 0;
			int warmAmount = 0;
			while (sqlsIter.hasNext()) {
				try {
					stmtUpdate.execute((String) (sqlsIter.next()));
					successAmount++;
				} catch (Exception e) {
					if (e.getMessage().contains("ORA-00001")) {
						warmAmount++;
						failureInfoList.add("(" + (++warmInfoCount) + ") "
								+ updateDBContentInfo.getComment() + " "
								+ updateDBContentInfo.getTableName() + " 总共有："
								+ warmAmount + " 条警告：" + e.getMessage());
					} else {
						failureAmount++;
						failureInfoList.add("(" + (++failureInfoCount) + ") "
								+ updateDBContentInfo.getComment() + " "
								+ updateDBContentInfo.getTableName() + " 总共有："
								+ failureAmount + " 条失败 原因 ：SQL语句有误!!!"
								+ e.getMessage());
					}
					System.out.println(e.getMessage());
				}
			}
			successInfoList.add("(" + (++successInfoCount) + ") "
					+ updateDBContentInfo.getComment() + " "
					+ updateDBContentInfo.getTableName() + "总共有: "
					+ successAmount + " 条插入成功!!!");
		} else
			failureInfoList.add("(" + (++failureInfoCount) + ") "
					+ updateDBContentInfo.getComment() + " "
					+ updateDBContentInfo.getTableName() + " 警告：此表不存在!!!");

	}

	/* 4. modifySubFieldContent 修改表字段内容 */
	private void modifySubFieldContent(
			UpdateOracleDataBaseContentInfo updateDBContentInfo) {
		commonUpdate(updateDBContentInfo);
	}

	/* 5. addSubField 增加表字段 */
	private void addSubField(UpdateOracleDataBaseContentInfo updateDBContentInfo) {
		commonUpdate(updateDBContentInfo, "add");
	}

	private void modifySubField(
			UpdateOracleDataBaseContentInfo updateDBContentInfo) {
		commonUpdate(updateDBContentInfo, "modify");
	}

	/*---------------------------升级数据库的六种方法----结束------------------------*/

	/* 检查是否存在内容 */
	private boolean searchExistTableContent(
			UpdateOracleDataBaseContentInfo updateDBContentInfo,
			String tableName) {
		String sqlTableContent = "select count(*) as amount from " + tableName;
		return searchExistInfo(updateDBContentInfo, sqlTableContent);
	}

	/* 检查表或字段在 */
	private boolean searchExistInfo(
			UpdateOracleDataBaseContentInfo updateDBContentInfo, String sql) {
		try {
			rs = stmtSel.executeQuery(sql);
			if (rs.next()) {
				if (rs.getInt("amount") > 0)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/* 删除和修改表字段内容共用程序 */
	private void commonUpdate(
			UpdateOracleDataBaseContentInfo updateDBContentInfo) {
		if (searchExistInfo(updateDBContentInfo, sql)) {
			try {
				stmtUpdate.execute(updateDBContentInfo.getSql());
				successInfoList.add("(" + (++successInfoCount) + ") "
						+ updateDBContentInfo.getComment() + " "
						+ updateDBContentInfo.getTableName() + " 成功!!!");
			} catch (Exception e) {
				if ("ORA-01430".equals(e.getMessage())) {
					failureInfoList.add("(" + (++warmInfoCount) + ") "
							+ updateDBContentInfo.getComment() + " "
							+ updateDBContentInfo.getTableName()
							+ " 警告：表中已存在要添加的列!");
				} else {
					if (e.getMessage().contains("ORA-01430")) {
						failureInfoList.add("(" + (++warmInfoCount) + ") "
								+ updateDBContentInfo.getComment() + " "
								+ updateDBContentInfo.getTableName() + " 警告："
								+ e.getMessage());
					} else if (e.getMessage().contains("ORA-01451")) {
						failureInfoList.add("(" + (++warmInfoCount) + ") "
								+ updateDBContentInfo.getComment() + " "
								+ updateDBContentInfo.getTableName() + " 警告："
								+ e.getMessage());
					} else {
						failureInfoList.add("(" + (++failureInfoCount) + ") "
								+ updateDBContentInfo.getComment() + " "
								+ updateDBContentInfo.getTableName()
								+ " 失败 原因：SQL语句有误!!!" + e.getMessage());
					}
				}
				System.out.println(e.getMessage());
			}
		} else {
			failureInfoList.add("(" + (++failureInfoCount) + ") "
					+ updateDBContentInfo.getComment() + " "
					+ updateDBContentInfo.getTableName() + " 警告：此表不存在!!!");
		}
	}

	/* 增加和修改表字段共用程序 */
	private void commonUpdate(
			UpdateOracleDataBaseContentInfo updateDBContentInfo, String alterStr) {
		if (searchExistInfo(updateDBContentInfo, sql)) {
			try {
				sqlsIter = updateDBContentInfo.getSubFieldList().iterator();
				while (sqlsIter.hasNext()) {
					subField = (SubField) sqlsIter.next();
					sqlUpdate = "select count(*) as amount from all_tab_columns where owner=upper('"
							+ dbInfo.getUserName()
							+ "') and table_name=upper('"
							+ updateDBContentInfo.getTableName()
							+ "') and column_name = upper('"
							+ subField.getSubFieldName() + "')";
					if (alterStr.equals("add")) {
						if (!searchExistInfo(updateDBContentInfo, sqlUpdate)) {
							stmtUpdate.execute("alter table "
									+ updateDBContentInfo.getTableName() + " "
									+ alterStr + "("
									+ subField.getSubFieldName() + " "
									+ subField.getSubFieldType() + "("
									+ subField.getSubFieldSize() + "))");
							successInfoList.add("(" + (++successInfoCount)
									+ ") " + updateDBContentInfo.getComment()
									+ " " + updateDBContentInfo.getTableName()
									+ " 成功!!!");
						} else {
							failureInfoList.add("(" + (++failureInfoCount)
									+ ") " + updateDBContentInfo.getComment()
									+ " " + updateDBContentInfo.getTableName()
									+ " 警告：" + subField.getSubFieldName()
									+ " 字段已存在!!!");
						}
					} else if (alterStr.equals("modify")) {
						if (searchExistInfo(updateDBContentInfo, sqlUpdate)) {
							stmtUpdate.execute("alter table "
									+ updateDBContentInfo.getTableName() + " "
									+ alterStr + "("
									+ subField.getSubFieldName() + " "
									+ subField.getSubFieldType() + "("
									+ subField.getSubFieldSize() + "))");
							successInfoList.add("(" + (++successInfoCount)
									+ ") " + updateDBContentInfo.getComment()
									+ " " + updateDBContentInfo.getTableName()
									+ " 成功!!!");
						} else {
							failureInfoList.add("(" + (++failureInfoCount)
									+ ") " + updateDBContentInfo.getComment()
									+ " " + updateDBContentInfo.getTableName()
									+ " 警告：" + subField.getSubFieldName()
									+ " 字段不存在!!!");
						}
					}
				}
			} catch (Exception e) {
				failureInfoList.add("(" + (++failureInfoCount) + ") "
						+ updateDBContentInfo.getComment() + " "
						+ updateDBContentInfo.getTableName()
						+ " 失败  原因：SQL语句有误!!!" + e.getMessage());
				System.out.println(e.getMessage());
			}
		} else {
			failureInfoList.add("(" + (++failureInfoCount) + ") "
					+ updateDBContentInfo.getComment() + " "
					+ updateDBContentInfo.getTableName() + " 警告：此表不存在!!!");
		}
	}

	/* 升级处理 */
	private void setXMLInitInfo() {
		ArrayList sqlsList;
		SubField subField;
		UpdateOracleDataBaseContentInfo updateDBContentInfo1;
		System.out.println("数据库升级---开始");
		for (Iterator i = root.elementIterator("content"); i.hasNext();) {
			updateDBContentInfo1 = new UpdateOracleDataBaseContentInfo();
			sqlsList = new ArrayList();

			content = (Element) i.next();
			updateDBContentInfo1.setTime(content.elementText("time"));
			updateDBContentInfo1.setComment(content.elementText("comment"));
			updateDBContentInfo1.setMethod(content.elementText("method"));
			updateDBContentInfo1.setTableName(content.elementText("tableName"));
			updateDBContentInfo1.setSql(content.elementText("sql"));

			for (Iterator j = content.elementIterator("sqls"); j.hasNext();) {
				sqls = (Element) j.next();
				for (Iterator k = sqls.elementIterator("sql"); k.hasNext();) {
					sqlEl = (Element) k.next();
					sqlsList.add(sqlEl.getText());
				}

			}
			updateDBContentInfo1.setSqlsList(sqlsList);

			for (Iterator k = content.elementIterator("subField"); k.hasNext();) {
				subFieldEl = (Element) k.next();
				subField = new SubField();
				subField.setSubFieldName(subFieldEl.elementText("subFieldName"));
				subField.setSubFieldType(subFieldEl.elementText("subFieldType"));
				subField.setSubFieldSize(subFieldEl.elementText("subFieldSize"));
				updateDBContentInfo1.setSubFieldList(subField);
			}
			updateInfo.setUpdateDBContentInfoList(updateDBContentInfo1);
			// System.out.println(updateDBContentInfo1.toString());

		}
	}

	/* 输出操作信息 */
	private void outputMessage() {
		FileWriter fw = null;
		try {
			fw = new FileWriter("UpdateFailueInfo.txt");
			Iterator failureInfo = failureInfoList.iterator();
			while (failureInfo.hasNext()) {
				fw.write(failureInfo.next().toString() + "\n");
			}
			fw.close();
			fw = new FileWriter("UpdateSuccessInfo.txt");
			Iterator successInfo = successInfoList.iterator();
			while (successInfo.hasNext()) {
				fw.write(successInfo.next().toString() + "\n");
			}
			fw.close();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/* 关闭数据库连接 */
	private void connClose() {
		try {
			if (stmtUpdate != null)
				stmtUpdate.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			if (stmtSel != null)
				stmtSel.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void processUpdate() {
		/*
		 * method 提供5种方法,分别为: 1. createTable 创建表 2. deleteContent 删除表内容 3.
		 * InsertContent 增加表内容 4. modifySubFieldContent 修改表字段内容 5. addSubField
		 * 增加表字段 ORA-01430: 表中已存在要添加的列 6. modifySubField 修改表字段
		 */
		setXMLInitInfo();
		Iterator sqlsIterProcess = updateInfo.getUpdateDBContentInfoList()
				.iterator();
		Date d3 = new Date();
		// --start 通过xml升级前做的事情 下面是只能用java处理的升级,下面是特殊处理方式
		UpdateJavaDoLast.doJavaUpd_before(conn, stmtSel, stmtUpdate, rs,
				successInfoList, failureInfoList);
		// --end

		while (sqlsIterProcess.hasNext()) {
			updateInfo = (UpdateOracleDataBaseContentInfo) sqlsIterProcess
					.next();
			method = updateInfo.getMethod();
			sql = "select count(*) as amount from tabs where table_name = upper('"
					+ updateInfo.getTableName() + "')";
			switch (Integer.parseInt(method)) {
			case 1:
				createTable(updateInfo);
				break;
			case 2:
				deleteContent(updateInfo);
				break;
			case 3:
				InsertContent(updateInfo);
				break;
			case 4:
				modifySubFieldContent(updateInfo);
				break;
			case 5:
				addSubField(updateInfo);
				break;
			case 6:
				modifySubField(updateInfo);
				break;
			}
		}

		// --start 通过xml升级后做的事情 下面是只能用java处理的升级,下面是特殊处理方式
		UpdateJavaDoLast.doJavaUpd_after(conn, stmtSel, stmtUpdate, rs,
				successInfoList, failureInfoList);
		// --end

		Date d4 = new Date();
		System.out.println("数据库升级---结束,用时: " + (d4.getTime() - d3.getTime())
				/ 1000 + " 秒!");
		/* 输出错误信息 */
		outputMessage();
		connClose();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * if(args.length == 2){ UpdateOracleDataBase updateObj = new
		 * UpdateOracleDataBase(args[0],args[1]); updateObj.processUpdate();
		 * }else{ System.out.println("请设置正确的参数，xml升级文件和数据库连接文件"); }
		 */
		UpdateOracleDataBase updateObj = new UpdateOracleDataBase(
				"updateOracleDataBaseXML.xml",
				"..//WEB-INF//classes//jdbc.properties");
		updateObj.processUpdate();
	}

}