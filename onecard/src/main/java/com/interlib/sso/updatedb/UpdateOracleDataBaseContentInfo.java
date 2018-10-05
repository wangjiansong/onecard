package com.interlib.sso.updatedb;

import java.util.ArrayList;
import java.util.Iterator;

class SubField {
	private String subFieldName = "";
	private String subFieldType = "";
	private String subFieldSize = "";

	public void setSubFieldName(String subFieldName) {
		this.subFieldName = subFieldName;
	}

	public String getSubFieldName() {
		return subFieldName;

	}

	public void setSubFieldType(String subFieldType) {
		this.subFieldType = subFieldType;
	}

	public String getSubFieldType() {
		return subFieldType;
	}

	public void setSubFieldSize(String subFieldSize) {
		this.subFieldSize = subFieldSize;
	}

	public String getSubFieldSize() {
		return subFieldSize;
	}

}

public class UpdateOracleDataBaseContentInfo {

	private String time = "";
	private String comment = "";
	private String method = "";
	private String tableName = "";
	private String sql = "";
	private ArrayList sqlsList = new ArrayList();
	private ArrayList subFieldList = new ArrayList();
	private SubField subField = new SubField();
	private ArrayList updateDBContentInfoList = new ArrayList();

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	public void setSqlsList(ArrayList sqlsList) {
		this.sqlsList = sqlsList;
	}

	public ArrayList getSqlsList() {
		return sqlsList;
	}

	public void setSubField(SubField subField) {
		this.subField = subField;
	}

	public SubField getSubField() {
		return subField;
	}

	public void setSubFieldList(SubField subField) {
		this.subFieldList.add(subField);
	}

	public ArrayList getSubFieldList() {
		return subFieldList;
	}

	public void setUpdateDBContentInfoList(
			UpdateOracleDataBaseContentInfo updateDBContentInfo) {
		this.updateDBContentInfoList.add(updateDBContentInfo);
	}

	public ArrayList getUpdateDBContentInfoList() {
		return updateDBContentInfoList;
	}

	public String toString() {
		String sqlsStr = "";
		String subFieldListStr = "";
		Iterator sqlsTemp = sqlsList.iterator();
		Iterator subFieldListTemp = subFieldList.iterator();
		SubField subFieldTemp = new SubField();
		int m = 1, n = 1;
		while (sqlsTemp.hasNext()) {
			sqlsStr += "sqls" + m + "=" + (String) sqlsTemp.next() + "\n";
			m++;
		}
		while (subFieldListTemp.hasNext()) {
			subFieldTemp = (SubField) subFieldListTemp.next();
			subFieldListStr += "subFieldName" + n + "="
					+ subFieldTemp.getSubFieldName() + "\n";
			subFieldListStr += "subFieldType" + n + "="
					+ subFieldTemp.getSubFieldType() + "\n";
			subFieldListStr += "subFieldSize" + n + "="
					+ subFieldTemp.getSubFieldSize() + "\n";
			n++;
		}

		// return
		// "time="+getTime()+"\ncomment="+getComment()+"\nmethod="+getMethod()+"\ntableName="+getTableName()+"\nsql="+getSql()+"\n"+sqlsStr+"subFieldName="+subField.getSubFieldName()+"\nsubFieldType="+subField.getSubFieldType()+"\nsubFieldSize="+subField.getSubFieldSize();
		return "time=" + getTime() + "\ncomment=" + getComment() + "\nmethod="
				+ getMethod() + "\ntableName=" + getTableName() + "\nsql="
				+ getSql() + "\n" + sqlsStr + subFieldListStr;
	}

	public static void main(String[] args) {
		UpdateOracleDataBaseContentInfo updateDBContentInfo = new UpdateOracleDataBaseContentInfo();
		SubField subField1 = new SubField();

		updateDBContentInfo.setTime("2005-12-12");
		updateDBContentInfo.setComment("�޸�");
		updateDBContentInfo.setMethod("modify");
		updateDBContentInfo.setTableName("reader");
		updateDBContentInfo.setSql("modify reader set ");

		ArrayList sqlsStr1 = new ArrayList();
		sqlsStr1.add("test1");
		sqlsStr1.add("test2");
		updateDBContentInfo.setSqlsList(sqlsStr1);

		subField1.setSubFieldName("fieldName1");
		subField1.setSubFieldType("int");
		subField1.setSubFieldSize("100");
		updateDBContentInfo.setSubFieldList(subField1);

		subField1 = new SubField();
		subField1.setSubFieldName("fieldName2");
		subField1.setSubFieldType("float");
		subField1.setSubFieldSize("200");
		updateDBContentInfo.setSubFieldList(subField1);

		System.out.println(updateDBContentInfo.toString());

	}
}