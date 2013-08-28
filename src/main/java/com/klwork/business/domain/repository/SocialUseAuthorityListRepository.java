package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUseAuthorityList;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "socialUseAuthorityListRepository")
public class SocialUseAuthorityListRepository extends
		MbDomainRepositoryImp<SocialUseAuthorityList, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<SocialUseAuthorityList> findSocialUseAuthorityListByQueryCriteria(T query,
			ViewPage page) {
		List<SocialUseAuthorityList> list = getDao().selectList(
				"selectSocialUseAuthorityListByQueryCriteria", query, page);
		if (page != null) {
			int count = findSocialUseAuthorityListCountByQueryCriteria(query);
			page.setTotalSize(count);
			// 总数
			page.setPageDataList(list);
		}
		return list;
	}

	/**
	 * 查询总数
	 * 
	 * @param query
	 * @return
	 */
	public Integer findSocialUseAuthorityListCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectSocialUseAuthorityListCountByQueryCriteria", query);
	}

	public void deleteSocialUseAuthorityListByReId(String reId) {
		getDao().update("deleteSocialUseAuthorityListByReId", reId);
	}
}
