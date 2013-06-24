package com.klwork.business.domain.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.OutsourcingProjectQuery;
import com.klwork.business.domain.model.ProjectParticipant;
import com.klwork.business.domain.model.ProjectParticipantQuery;
import com.klwork.business.domain.repository.ProjectParticipantRepository;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class ProjectParticipantService {
	@Autowired
	private ProjectParticipantRepository rep;
	
	@Autowired
	OutsourcingProjectService outsourcingProjectService;

	public void createProjectParticipant(ProjectParticipant projectParticipant) {
		projectParticipant.setId(rep.getNextId());
		Date lastUpdate = StringDateUtil.now();
		projectParticipant.setLastUpdate(lastUpdate);
		rep.insert(projectParticipant);
	}
	
	
	
	/*public void deleteProjectParticipant(ProjectParticipant projectParticipant) {
	}*/

	public int updateProjectParticipant(ProjectParticipant projectParticipant) {
		Date lastUpdate = StringDateUtil.now();
		projectParticipant.setLastUpdate(lastUpdate);
		return rep.update(projectParticipant);
	}

	public List<ProjectParticipant> findProjectParticipantByQueryCriteria(
			ProjectParticipantQuery query, ViewPage<ProjectParticipant> page) {
		return rep.findProjectParticipantByQueryCriteria(query, page);
	}

	public ProjectParticipant findProjectParticipantById(String id) {
		return rep.find(id);
	}

	public int count(ProjectParticipantQuery query) {
		return rep.findProjectParticipantCountByQueryCriteria(query);
	}

	public boolean checkExistParticipant(String outPrgId, String userId,
			String participantsType) {
		ProjectParticipantQuery query = new ProjectParticipantQuery();
		query.setOutPrgId(outPrgId).setUserId(userId).setParticipantsType(participantsType);
		ProjectParticipant r = findOneByQuery(query);
		if (r == null)
			return false;
		return true;
	}

	public ProjectParticipant findOneByQuery(ProjectParticipantQuery query) {
		List<ProjectParticipant> list = rep
				.findProjectParticipantByQueryCriteria(query, null);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 为一个项目增加指定的参与人
	 * @param project
	 * @param userId
	 * @param participantsTypeUser
	 */
	public void addProjectParticipantByParam(OutsourcingProject project,
			String userId, String participantsTypeUser,String taskId) {
		//if (!checkExistParticipant(project.getId(), userId, participantsTypeUser)) {//不存在就新增
			ProjectParticipant p = new ProjectParticipant();
			p.setOutPrgId(project.getId());
			p.setUserId(userId);
			p.setParticipantsType(participantsTypeUser);
			p.setProcInstId(project.getProcInstId());
			p.setAssessedTaskId(taskId);
			createProjectParticipant(p);
		//}
	}

	public void addProjectParticipantByParam(String outsourcingProjectId,
			String userId, String participantsTypeScorer,String taskId) {
		OutsourcingProject p = outsourcingProjectService.findOutsourcingProjectById(outsourcingProjectId);
		this.addProjectParticipantByParam(p, userId, participantsTypeScorer,taskId);
	}


	public int count(OutsourcingProjectQuery q) {
		return rep.findProjectParticipantCountByQueryCriteria(q);
	}
}