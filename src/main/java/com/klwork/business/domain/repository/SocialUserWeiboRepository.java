package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "socialUserWeiboRepository")
public class SocialUserWeiboRepository extends
		MbDomainRepositoryImp<SocialUserWeibo, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<SocialUserWeibo> findSocialUserWeiboByQueryCriteria(T query,
			ViewPage page) {
		List<SocialUserWeibo> list = getDao().selectList(
				"selectSocialUserWeiboByQueryCriteria", query, page);
		if (page != null) {
			int count = findSocialUserWeiboCountByQueryCriteria(query);
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
	public Integer findSocialUserWeiboCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectSocialUserWeiboCountByQueryCriteria", query);
	}

	public <T extends QueryParameter> SocialUserWeibo queryLastWeibo(T query) {
		return (SocialUserWeibo)getDao().selectOne(
				"selectLastSocialUserWeibo", query);
	}
}
