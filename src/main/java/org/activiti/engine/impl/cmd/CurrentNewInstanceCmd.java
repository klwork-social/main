package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class CurrentNewInstanceCmd implements Command<Object>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -310879105454816888L;
	protected String processInstanceId;
	protected String activitId;
	protected Map<String, String> formProperties;
	protected final String LOOP_COUNTER = "loopCounter";
	protected final String NUMBER_OF_INSTANCES = "nrOfInstances";
	protected final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
	protected final String NUMBER_OF_COMPLETED_INSTANCES = "nrOfCompletedInstances";

	public CurrentNewInstanceCmd(String processInstanceId, String activitId,
			Map<String, String> formProperties) {
		this.processInstanceId = processInstanceId;
		this.activitId = activitId;
		this.formProperties = formProperties;
	}

	@Override
	public Object execute(CommandContext commandContext) {
		List<ExecutionEntity> allExecutions = commandContext
				.getExecutionEntityManager()
				.findChildExecutionsByProcessInstanceId(processInstanceId);

		for (ExecutionEntity tExecution : allExecutions) {
			if (activitId.equals(tExecution.getActivityId())
					&& !tExecution.isActive()) {// 主任务已经关闭
				System.out.println("任务详情：id" + tExecution.getId()
						+ "--- parentId:" + tExecution.getParentId() + " "
						+ tExecution.isActive() + " "
						+ tExecution.getSuperExecutionId());
				ProcessDefinitionEntity deployedProcessDefinition = Context
						.getProcessEngineConfiguration()
						.getDeploymentManager()
						.findDeployedProcessDefinitionById(
								tExecution.getProcessDefinitionId());
				// 从流程定义中，查询出此活动
				PvmActivity activity = deployedProcessDefinition
						.findActivity(tExecution.getActivityId());
				// 实体重新计算
				handlerInsNumber(tExecution);
				// 一个子类生成
				ActivityExecution subExecution = tExecution.createExecution();
				subExecution.setActive(true);
				subExecution.setConcurrent(true);
				subExecution.setScope(false);

				// In case of an embedded subprocess, and extra child execution
				// is required
				// Otherwise, all child executions would end up under the same
				// parent,
				// without any differentation to which embedded subprocess they
				// belong
				if (true) {// SubProcessActivityBehavior
					ActivityExecution extraScopedExecution = subExecution
							.createExecution();
					extraScopedExecution.setActive(true);
					extraScopedExecution.setConcurrent(false);// 非并发
					extraScopedExecution.setScope(true);
					subExecution = extraScopedExecution;
				}
				subExecution.setVariableLocal(LOOP_COUNTER, new Integer(1));
				// 放到流程变量中
				if (formProperties != null) {
					subExecution.setVariablesLocal(formProperties);
				}
				// 后续再监听中
				// concurrentExecution.setVariable("claimUserId", userId);
				subExecution.executeActivity(activity);//可能执行任务的开始
			}
		}
		return null;
	}

	private void handlerInsNumber(ActivityExecution concurrentExecution) {
		//总数加1
		int nrOfInstances = getLoopVariable(concurrentExecution,
				NUMBER_OF_INSTANCES) + 1;
		int nrOfActiveInstances = getLoopVariable(concurrentExecution,
				NUMBER_OF_ACTIVE_INSTANCES) + 1;

		setLoopVariable(concurrentExecution, NUMBER_OF_INSTANCES, nrOfInstances);
		setLoopVariable(concurrentExecution, NUMBER_OF_ACTIVE_INSTANCES,
				nrOfActiveInstances);
	}

	protected void setLoopVariable(ActivityExecution execution,
			String variableName, Object value) {
		execution.setVariableLocal(variableName, value);
	}

	protected Integer getLoopVariable(ActivityExecution execution,
			String variableName) {
		Object value = execution.getVariableLocal(variableName);
		ActivityExecution parent = execution.getParent();
		while (value == null && parent != null) {
			value = parent.getVariableLocal(variableName);
			parent = parent.getParent();
		}
		return (Integer) value;
	}

}
