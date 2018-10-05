package com.interlib.sso.page;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.interlib.sso.common.beanutils.PropertyUtils;
import com.interlib.sso.page.util.PageList;
import com.interlib.sso.page.util.PageQuery;
import com.interlib.sso.page.util.Paginator;

@SuppressWarnings({"rawtypes","unchecked"})
public class MybatisPageUtils {

	public static PageList<?> pageQuery(SqlSession sqlSession, String statement, PageQuery parameter) {
		String countStatement = statement + ".count";

		PageList pageList = pageQuery(sqlSession, statement, countStatement, parameter, parameter.getPage(),
				parameter.getPageSize());
        return pageList;
	}
	
	public static PageList pageQuery(SqlSession sqlSession, String statement,
			Object parameter, int pageNo, int pageSize) {
		String countStatement = statement + ".count";

		return pageQuery(sqlSession, statement, countStatement, parameter, pageNo,
				pageSize);
	}

	public static PageList pageQuery(SqlSession sqlSession, String statement,
			String countStatement, Object parameter, int pageNo,
			int pageSize) {
		Assert.isTrue(pageSize > 0,"pageSize > 0 must be true");
		Number totalItems = (Number) sqlSession.selectOne(countStatement,parameter);

		if (totalItems != null && totalItems.intValue() > 0) {
			Paginator paginator = new Paginator(pageNo, pageSize, totalItems.intValue());
			RowBounds rowBounds = new RowBounds(paginator.getOffset(), paginator.getLimit());
			List list = sqlSession.selectList(statement, attachPageQueryVariable(parameter,paginator), rowBounds);
			PageList page = new PageList(list, paginator);
			return page;
		}

		return new PageList(0,pageSize,0);
	}

	public static Map attachPageQueryVariable(Object parameter,Paginator p) {
		Map map = toParameterMap(parameter);
		map.put("startRow", p.getStartRow());
		map.put("endRow", p.getEndRow());
		map.put("offset", p.getOffset());
		map.put("limit", p.getLimit());
		return map;
	}
	
	public static Map toParameterMap(Object parameter) {
		if(parameter instanceof Map) {
			return (Map)parameter;
		}else {
			try {
				return PropertyUtils.describe(parameter);
			} catch (Exception e) {
				ReflectionUtils.handleReflectionException(e);
				return null;
			}
		}
	}
	
}
