package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserWeiboSend;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "socialUserWeiboSendRepository")
public class SocialUserWeiboSendRepository extends
		MbDomainRepositoryImp<SocialUserWeiboSend, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<SocialUserWeiboSend> findSocialUserWeiboSendByQueryCriteria(T query,
			ViewPage page) {
		List<SocialUserWeiboSend> list = getDao().selectList(
				"selectSocialUserWeiboSendByQueryCriteria", query, page);
		if (page != null) {
			int count = findSocialUserWeiboSendCountByQueryCriteria(query);
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
	public Integer findSocialUserWeiboSendCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectSocialUserWeiboSendCountByQueryCriteria", query);
	}
}
