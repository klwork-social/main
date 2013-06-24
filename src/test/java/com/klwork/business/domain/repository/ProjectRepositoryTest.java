package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.test.base.BaseTxWebTests;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.repository.ProjectRepository;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class ProjectRepositoryTest extends BaseTxWebTests {
	@Autowired
	private ProjectRepository rep;

	@Test
	public void testCrud() {
		Project entity = new Project();
		entity.setDescription("好项目");
		entity.setId("111111");
		rep.insert(entity );
	}

}
