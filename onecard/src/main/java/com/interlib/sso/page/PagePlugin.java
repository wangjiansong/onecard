package com.interlib.sso.page;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class PagePlugin implements Interceptor {

	//private final String dialect = "mysql";	//数据库方言  
	private final String dialect = "oracle";	//数据库方言
	private final String pageSqlId = ".*List"; //mapper.xml中需要拦截的ID(正则匹配)
	
	@Override
	public Object intercept(Invocation ivk) throws Throwable { 
		if(ivk.getTarget() instanceof RoutingStatementHandler){
			RoutingStatementHandler statementHandler = (RoutingStatementHandler)ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) this.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) this.getValueByFieldName(delegate, "mappedStatement");
			if(mappedStatement.getId().matches(pageSqlId)){ //拦截需要分页的SQL
				
				BoundSql boundSql = delegate.getBoundSql();
				
				Object parameterObject = boundSql.getParameterObject();//分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
				
				if(parameterObject==null){
					throw new NullPointerException("parameterObject尚未实例化！");
				}else{
					Connection connection = (Connection) ivk.getArgs()[0];
					String sql = boundSql.getSql();
					String countSql = "select count(0) as tmp_count from (" + sql+ ") "; //记录统计
					
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),countSql,boundSql.getParameterMappings(),parameterObject);
					//System.out.println(countBS.getSql());
					setParameters(countStmt,mappedStatement,countBS,parameterObject);
					
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					//System.out.println(count);
					PageEntity page = null;
					if(parameterObject instanceof Page){	//参数就是Page实体
						page = (PageEntity) parameterObject;
						page.setEntityOrField(true);	 //见com.flf.entity.Page.entityOrField 注释
						page.setTotalResult(count);
					}else{	//参数为某个实体，该实体拥有Page属性
						//Field pageField = this.getFieldByFieldName(parameterObject,"page");
						//if(pageField!=null){
							page = (PageEntity) this.getValueByFieldName(parameterObject,"page"); 
							if(page==null)
								page = new PageEntity();
							page.setEntityOrField(false); //见com.flf.entity.Page.entityOrField 注释
							page.setTotalResult(count);
							this.setValueByFieldName(parameterObject,"page", page); //通过反射，对实体对象设置分页对象
						//	}else{
						//		throw new NoSuchFieldException(parameterObject.getClass().getName()+"不存在 page 属性！");
						//	}
					}
					//System.out.println("page.getSortCol()"+page.getSortCol());
					if(page.getSortCol()!=null && !page.getSortCol().equals("")){
						//------------排序--------------
						int oLen = sql.indexOf("order by");//给我写小写
						if(oLen!=-1){
							sql = sql.substring(0, oLen+8) + 
									" " + page.getSortCol() + " " + page.getSortType() + "," +
									sql.substring(oLen+8);
						}else{
							sql += " order by " + page.getSortCol() + " " + page.getSortType();
						}
					}
					String pageSql = generatePageSql(sql, page);
					 
					//System.out.println(pageSql);
					this.setValueByFieldName(boundSql, "sql", pageSql); //将分页sql语句反射回BoundSql.
				}
			}
//			else{
//				System.out.println("xintercept:"); 
//				BoundSql boundSql = delegate.getBoundSql();
//				System.out.println("sql:\r\n"+boundSql.getSql());
//			}
		}
		return ivk.proceed();
	}
	
	/**
	 * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setParameters(PreparedStatement ps,
			MappedStatement mappedStatement,
			BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null: configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter "+ propertyName + " of statement "+ mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}
	
	/**
	 * 根据数据库方言，生成特定的分页sql
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generatePageSql(String sql, PageEntity page){
		//这种分页方式有问题2016-12-27
//		if(page!=null && !isEmpty(dialect)){
//			StringBuffer pageSql = new StringBuffer();
//			if("mysql".equals(dialect)){
//				pageSql.append(sql);
//				pageSql.append(" limit "+page.getCurrentResult()+","+page.getShowCount());
//			}else if("oracle".equals(dialect)){
//				pageSql.append("select * from (select tmp_tb.*, ROWNUM row_id from (");
//				pageSql.append(sql);
//				pageSql.append(") tmp_tb where ROWNUM<=");
//				pageSql.append(page.getCurrentResult()+page.getShowCount());
//				pageSql.append(") where row_id>");
//				pageSql.append(page.getCurrentResult());
//			}
//			return pageSql.toString();
//		}else{
//			return sql;
//		}
		
		if(page!=null && !isEmpty(dialect)){
			StringBuffer pageSql = new StringBuffer();
			if("mysql".equals(dialect)){
				pageSql.append(sql);
				pageSql.append(" limit "+page.getCurrentResult()+","+page.getShowCount());
			}else if("oracle".equals(dialect)){
				pageSql.append("select * from (select tmp_tb.*, ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(") tmp_tb  ");
				pageSql.append(") where row_id>");
				pageSql.append(page.getCurrentResult());
				pageSql.append( " and row_id <= ");
				pageSql.append(page.getCurrentResult()+page.getShowCount());
			}
			return pageSql.toString();
		}else{
			return sql;
		}
		
	}
	
	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties p) {
//		dialect = p.getProperty("dialect");
//		if (this.isEmpty(dialect)) {
//			try {
//				throw new PropertyException("dialect property is not found!");
//			} catch (PropertyException e) { 
//				e.printStackTrace();
//			}
//		}
//		pageSqlId = p.getProperty("pageSqlId");
//		if (this.isEmpty(pageSqlId)) {
//			try {
//				throw new PropertyException("pageSqlId property is not found!");
//			} catch (PropertyException e) { 
//				e.printStackTrace();
//			}
//		}
	}

	/**
	 * 获取obj对象fieldName的Field
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	private Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 获取obj对象fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("rawtypes")
	private Object getValueByFieldName(Object obj, String fieldName)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {  
		if(obj instanceof Map){ 
			return ((Map)obj).get(fieldName);
		}else{
			Field field = getFieldByFieldName(obj, fieldName);
			Object value = null;
			if(field!=null){
				if (field.isAccessible()) {
					value = field.get(obj);
				} else {
					field.setAccessible(true);
					value = field.get(obj);
					field.setAccessible(false);
				}
			}
			return value;
		}
	}

	/**
	 * 设置obj对象fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setValueByFieldName(Object obj, String fieldName,
			Object value) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		if(obj instanceof Map){
			((Map)obj).put(fieldName, value);
		}else{
			Field field = obj.getClass().getDeclaredField(fieldName);
			if (field.isAccessible()) {
				field.set(obj, value);
			} else {
				field.setAccessible(true);
				field.set(obj, value);
				field.setAccessible(false);
			}
		}
	}
	
	private boolean isEmpty(String s){
		return s==null || "".equals(s) || "null".equals(s);
	}
}
