package com.interlib.sso.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.interlib.sso.dao.BaseDAO;
import com.interlib.sso.page.MybatisPageUtils;
import com.interlib.sso.page.Page;
import com.interlib.sso.page.PageRequest;
import com.interlib.sso.page.util.MybatisPageQueryUtils;
import com.interlib.sso.page.util.PageList;


/**
 * DAO抽象基类, 实现BaseDAO, 已实现一些通用数据操作方法
 * @author Joe
 *
 * @param <E> domain类型
 * @param <PK> 主键类型
 */
public abstract class AbstractMybatisBaseDAO<E, PK extends Serializable> implements BaseDAO<E, PK> {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private final static int DEFAULT_PAGE_SIZE = 20;
	
	/**
	private SqlSession sqlSession;
	
	public void SetSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	*/
	
	public abstract SqlSession getSqlSession();

    public abstract String getMybatisMapperNamespace();
    
	public AbstractMybatisBaseDAO() {
		/** 也可以通过泛型直接获取E的子类class类型
		this.entityClass = null;
        Class c = getClass();
        Type type = c.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
            this.entityClass = (Class<E>) parameterizedType[0];
        }
        */
	}

	/**
	 * 用于子类覆盖,在insert,update之前调用
	 * @param e
	 */
    protected void prepareObjectForSaveOrUpdate(E e) {
    }
    
	public String getFindByPrimaryKeyStatement() {
		return getMybatisMapperNamespace() + ".get";
	}
	
	public String getInsertStatement() {
		return getMybatisMapperNamespace() + ".insert";
	}
	public String getUpdateStatement() {
		return getMybatisMapperNamespace() + ".update";
	}
	public String getDeleteStatement() {
		return getMybatisMapperNamespace() + ".delete";
	}
	public String getTotalCountStatement() {
		return getMybatisMapperNamespace() + ".getTotalCount";
	}
	public String getAllListStatement() {
		return getMybatisMapperNamespace() + ".getAllList";
	}
	public String getExistByIdStatement() {
		return getMybatisMapperNamespace() + ".existById";
	}
	public String getDeleteByIdsStatement() {
		return getMybatisMapperNamespace() + ".deleteByIds";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E get(PK id) {
		Object object = getSqlSession().selectOne(getFindByPrimaryKeyStatement(), id);
		System.out.println(object);
		return (E) object;
	}

	@Override
	public E save(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		getSqlSession().insert(getInsertStatement(), entity);
		return entity;
	}

	@Override
	public void update(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		getSqlSession().update(getUpdateStatement(), entity);
	}

	@Override
	public E saveOrUpdate(E entity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void delete(PK id) {
		getSqlSession().delete(getDeleteStatement(), id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getAllList() {
		return getSqlSession().selectList(getAllListStatement());
	}
	
	@Override
	public Integer getTotalCount() throws DataAccessException {
		return (Integer) getSqlSession().selectOne(getTotalCountStatement());
	}

	@Override
	public void deleteByIds(PK[] ids) throws DataAccessException {
		getSqlSession().delete(getDeleteByIdsStatement(), ids);
	}

	@Override
	public boolean existById(PK id)
			throws DataAccessException {
		int count = 
				(Integer) getSqlSession().selectOne(getExistByIdStatement(), id);
		return count > 0;
	}
	
	@Override
	public Page findPage(PageRequest query) {
		return MybatisPageQueryUtils.pageQuery(getSqlSession(), 
				getMybatisMapperNamespace() + ".findPage", query);
	}
	
	@Override
	public PageList<E> findPage(Map<String, Object> parameter) {
		int pageNo = 1;
		int pageSize = DEFAULT_PAGE_SIZE;
		if(parameter.size() > 0) {
			try {
				Object pageNoObj = parameter.get("pageNo");
				if(pageNoObj instanceof String) {
					pageNo = Integer.parseInt((String) pageNoObj);
				} else {
					pageNo = (Integer) pageNoObj;
				}
				Object pageSizeObj = parameter.get("pageSize");
				if(pageSizeObj instanceof String) {
					pageSize = Integer.parseInt((String) pageSizeObj);
				} else {
					pageSize = (Integer) pageSizeObj;
				}
			} catch (Exception e) {
				log.error("pageNo or pageSize cast exception.", e);
			}
		}
		return MybatisPageUtils.pageQuery(getSqlSession(), 
				getMybatisMapperNamespace() + ".findPage", 
				parameter, pageNo, pageSize);
	}
	
	@Override
	public List<Map<String, Object>> queryPageList(E entity) {
		return getSqlSession().selectList(getMybatisMapperNamespace()+".queryPageList", entity);
	}
}
