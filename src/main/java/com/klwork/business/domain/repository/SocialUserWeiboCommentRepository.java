package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserWeiboComment;
import com.klwork.business.domain.model.SocialUserWeiboCommentQuery;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "socialUserWeiboCommentRepository")
public class SocialUserWeiboCommentRepository extends
		MbDomainRepositoryImp<SocialUserWeiboComment, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<SocialUserWeiboComment> findSocialUserWeiboCommentByQueryCriteria(T query,
			ViewPage page) {
		List<SocialUserWeiboComment> list = getDao().selectList(
				"selectSocialUserWeiboCommentByQueryCriteria", query, page);
		if (page != null) {
			int count = findSocialUserWeiboCommentCountByQueryCriteria(query);
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
	public Integer findSocialUserWeiboCommentCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectSocialUserWeiboCommentCountByQueryCriteria", query);
	}

	public SocialUserWeiboComment queryLastComment(
			SocialUserWeiboCommentQuery query) {
		return (SocialUserWeiboComment)getDao().selectOne(
				"selectLastSocialUserWeiboComment", query);
	}
}
