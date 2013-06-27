package com.klwork.common.domain.repository;

import java.io.Serializable;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.common.dao.BaseMybaitsDao;
import com.klwork.common.utils.ClassNameUtil;
import com.klwork.common.utils.ReflectionUtils;
import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;
import com.klwork.explorer.web.SpringProcessEngineConfiguration;

/**
 * 
 * 
 * @author ww
 * 
 */
public class MbDomainRepositoryImp<T, PK extends Serializable> implements
		DomainRepository<T, PK> {
	@Autowired
	private BaseMybaitsDao dao;

	protected Class<T> entityClass;
	
	protected static IdGenerator idGenerator;

	/**
	 * 不事先初始化，目的是为了仅在使用时再创建Logger对象。
	 */
	private transient Logger logger;

	/**
	 * 得到Logger对象
	 */
	protected Logger getLogger() {
		if (logger == null) {
			logger = LoggerFactory.getLogger(getClass());
		}
		return logger;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public MbDomainRepositoryImp() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}
	
	
	public BaseMybaitsDao getDao() {
		return dao;
	}

	public void setDao(BaseMybaitsDao dao) {
		this.dao = dao;
	}

	public SqlSession getDbSqlSession() {
		return dao.getSqlSession();
	}
	
	@Override
	public int insert(final T entity) {
		String deleteStatement = getInsertStatement(entity);
		return dao.insert(deleteStatement, entity);
	}
	
	public String getNextId() {
		String id = getIdGenerator().getNextId();
		return id;
	}

	@Override
	public T find(final PK id) {
		String selectStatement = getSelectByIdStatement(entityClass);
		T persistentObject = (T) (dao.selectOne(selectStatement, id));
		return persistentObject;
	}

	@Override
	public int update(final T transientObject) {
		String updateStatement = getUpdateStatement(transientObject);
		return dao.update(updateStatement, transientObject);
	}

	@Override
	public int delete(final T persistentObject) {
		String deleteStatement = getDeleteStatement(persistentObject.getClass());
		return dao.delete(deleteStatement, persistentObject);
	}

	@Override
	public int deleteById(final PK id) {
		String deleteStatement = getDeleteStatement(getEntityClass());
		return dao.delete(deleteStatement, id);
	}

	public String getInsertStatement(T object) {
		return getStatement(object.getClass(), "insert");
	}

	public String getUpdateStatement(T object) {
		return getStatement(object.getClass(), "update");
	}

	public String getDeleteStatement(Class<?> persistentObjectClass) {
		return getStatement(persistentObjectClass, "delete");
	}

	public String getSelectStatement(Class<?> persistentObjectClass) {
		return getStatement(persistentObjectClass, "select");
	}
	
	public String getSelectByIdStatement(Class<?> persistentObjectClass) {
		return getStatement(persistentObjectClass, "select","ById");
	}

	private String getStatement(Class<?> persistentObjectClass, String prefix) {
		String statement = prefix
				+ ClassNameUtil
						.getClassNameWithoutPackage(persistentObjectClass);
		statement = statement.substring(0, statement.length());
		return statement;
	}
	
	private String getStatement(Class<?> persistentObjectClass, String prefix,String suffix) {
		String statement = prefix
				+ ClassNameUtil
						.getClassNameWithoutPackage(persistentObjectClass);
		statement = statement.substring(0, statement.length()) + suffix;
		return statement;
	}

	public static IdGenerator getIdGenerator() {
		return idGenerator;
	}

	public static void setIdGenerator(IdGenerator idGenerator2) {
		idGenerator = idGenerator2;
	}
	
	
}
