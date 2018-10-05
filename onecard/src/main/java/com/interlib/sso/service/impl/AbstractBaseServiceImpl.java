package com.interlib.sso.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.interlib.sso.dao.BaseDAO;
import com.interlib.sso.page.Page;
import com.interlib.sso.page.PageRequest;
import com.interlib.sso.page.util.PageList;
import com.interlib.sso.service.BaseService;

/**
 * 抽象Service实现类,已实现部分通用操作
 * @author Joe
 *
 * @param <E> domain类型
 * @param <PK> 主键类型
 */
public abstract class AbstractBaseServiceImpl<E, PK extends Serializable> implements BaseService<E, PK> {
	
	private BaseDAO<E, PK> baseDAO;
	
	public void setBaseDAO(BaseDAO<E, PK> baseDAO) {
		this.baseDAO = baseDAO;
	}
	//TODO 这里看是否需要改为抽象方法,必须由子类实现
	public BaseDAO<E, PK> getBaseDAO() {
		return baseDAO;
	}

	/**
	 * 根据id获取对象
	 * @param id
	 * @return Entity
	 */
	public E get(PK id) throws DataAccessException {
		return baseDAO.get(id);
	}
	
	/**
	 * 取得所有Entity
	 * @return List<E>
	 */
	public List<E> getAllList() throws DataAccessException {
		return baseDAO.getAllList();
	}
	
	/**
	 * 取得对象总数量
	 * @return
	 * @throws DataAccessException
	 */
	public Integer getTotalCount() throws DataAccessException {
		return baseDAO.getTotalCount();
	}
	
	/**
	 * 新增Entity
	 * @param entity
	 */
	public E save(E entity) throws DataAccessException {
		return baseDAO.save(entity);
	}
	
	/**
	 * 保存修改Entity
	 * @param entity
	 */
	public void update(E entity) throws DataAccessException {
		baseDAO.update(entity);
	}
	
	/**
	 * 新增或者保存修改Entity
	 * @param entity
	 */
	public E saveOrUpdate(E entity) throws DataAccessException {
		return baseDAO.saveOrUpdate(entity);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(PK id) throws DataAccessException {
		baseDAO.delete(id);
	}
	
	/**
	 * 根据多个id删除
	 * @param ids
	 * @throws DataAccessException
	 */
	public void deleteByIds(PK[] ids) throws DataAccessException {
		baseDAO.deleteByIds(ids);
	}
	
	@Override
	public boolean existById(PK id) throws DataAccessException {
		return baseDAO.existById(id);
	}
	
	@Override
	public Page findPage(PageRequest query) {
		return baseDAO.findPage(query);
	}
	
	@Override
	public PageList<E> findPage(Map<String, Object> parameter) {
		return baseDAO.findPage(parameter);
	}
	
	@Override
	public List<Map<String, Object>> queryPageList(E entity) {
		return baseDAO.queryPageList(entity);
	}
}
