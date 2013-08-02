package com.klwork.business.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.DictDefQuery;
import com.klwork.business.domain.repository.DictDefRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class DictDefService {
	@Autowired
	private DictDefRepository rep;

	public void createDictDef(DictDef dictDef) {
		dictDef.setId(rep.getNextId());
		rep.insert(dictDef);
	}

	public void deleteDictDef(DictDef dictDef) {
	}

	public int updateDictDef(DictDef dictDef) {
		return 0;
	}

	public List<DictDef> findDictDefByQueryCriteria(DictDefQuery query,
			ViewPage<DictDef> page) {
		return rep.findDictDefByQueryCriteria(query, page);
	}

	public DictDef findDictDefById(String id) {
		return rep.find(id);
	}
	
	public int count(DictDefQuery query) {
		return rep.findDictDefCountByQueryCriteria(query);
	}
}