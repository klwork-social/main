package com.klwork.common.dao;

import org.hibernate.Query;

public interface QuerySpecialHandler {
	
	/**
	 * 对query进行初始化
	 * @param query
	 */
	public void init(Query query);

}
