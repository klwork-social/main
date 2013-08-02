package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserAccountInfo;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "socialUserAccountInfoRepository")
public class SocialUserAccountInfoRepository extends
		MbDomainRepositoryImp<SocialUserAccountInfo, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<SocialUserAccountInfo> findSocialUserAccountInfoByQueryCriteria(T query,
			ViewPage page) {
		List<SocialUserAccountInfo> list = getDao().selectList(
				"selectSocialUserAccountInfoByQueryCriteria", query, page);
		if (page != null) {
			int count = findSocialUserAccountInfoCountByQueryCriteria(query);
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
	public Integer findSocialUserAccountInfoCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectSocialUserAccountInfoCountByQueryCriteria", query);
	}
}
