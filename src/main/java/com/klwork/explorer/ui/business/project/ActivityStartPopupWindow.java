package com.klwork.explorer.ui.business.project;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.service.OutsourcingProjectService;
import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.process.ProcessDefinitionImageStreamResourceBuilder;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ActivityStartPopupWindow extends PopupWindow {

	private static final long serialVersionUID = 3129077881658239761L;
	// protected static final Logger LOGGER =
	// LoggerFactory.getLogger(ActivityStartPopupWindow.class);

	// Services
	public transient RepositoryService repositoryService;
	protected transient ManagementService managementService;
	public transient RuntimeService runtimeService;
	public transient OutsourcingProjectService outsourcingProjectService;
	protected transient TaskService taskService;
	protected transient ProjectManagerService projectManagerService;
	protected I18nManager i18nManager;

	// Members
	public ProcessDefinition processDefinition;
	protected Deployment deployment;
	protected VerticalLayout mainLayout;
	public String todoId;
	public ActivityStartPopupWindow(String todoId) {
		this.runtimeService = ProcessEngines.getDefaultProcessEngine()
				.getRuntimeService();
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		this.repositoryService = ProcessEngines.getDefaultProcessEngine()
				.getRepositoryService();
		this.managementService = ProcessEngines.getDefaultProcessEngine()
				.getManagementService();
		
		this.outsourcingProjectService = ViewToolManager.getBean("outsourcingProjectService");
		projectManagerService = ViewToolManager
				.getBean("projectManagerService");
		
		this.todoId = todoId;
		this.i18nManager = ViewToolManager.getI18nManager();
		String processDefinitionId = "vacationRequest:1:36";
		this.processDefinition = repositoryService
				.getProcessDefinition(processDefinitionId);
		if (processDefinition != null) {
			deployment = repositoryService.createDeploymentQuery()
					.deploymentId(processDefinition.getDeploymentId())
					.singleResult();
		}
		addStyleName(Reindeer.WINDOW_LIGHT);
		setModal(true);
		setHeight("80%");
		setWidth("50%");
		center();
		// addComponent(new ProfilePanel(userId));
		// setContent(new ProfilePanel(userId));
		mainLayout = new VerticalLayout();
		setContent(mainLayout);
		Label processTitle = new Label(
				i18nManager.getMessage(Messages.PROCESS_HEADER_DIAGRAM));
		processTitle.addStyleName(ExplorerLayout.STYLE_H3);
		mainLayout.addComponent(processTitle);
		
		//initImage();

		initNotice();
		initButtons();
	}

	private void initNotice() {
		Label modelerDescriptionLabel = new Label("任务关联外部流程，您是否需要立即启动");
		modelerDescriptionLabel.addStyleName(Reindeer.LABEL_SMALL);
		modelerDescriptionLabel.addStyleName(ExplorerLayout.STYLE_CLICKABLE);
		mainLayout.addComponent(modelerDescriptionLabel);
	}

	private void initButtons() {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(100, Unit.PERCENTAGE);

		Button createButton = new Button("启动流程");
		buttonLayout.addComponent(createButton);
		buttonLayout.setComponentAlignment(createButton, Alignment.BOTTOM_LEFT);
		createButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				doStartProcessInstance();
				close();
			}
		});

		Button calButton = new Button("稍后");
		buttonLayout.addComponent(calButton);
		buttonLayout.setComponentAlignment(calButton, Alignment.BOTTOM_CENTER);
		calButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		mainLayout.addComponent(buttonLayout);
	}

	protected void initImage() {
		boolean didDrawImage = false;
		boolean jsDia = true;
		if (jsDia) {
			try {

				final InputStream definitionStream = repositoryService
						.getResourceAsStream(
								processDefinition.getDeploymentId(),
								processDefinition.getResourceName());
				XMLInputFactory xif = XMLInputFactory.newInstance();
				XMLStreamReader xtr = xif
						.createXMLStreamReader(definitionStream);
				BpmnModel bpmnModel = new BpmnXMLConverter()
						.convertToBpmnModel(xtr);

				if (bpmnModel.getFlowLocationMap().size() > 0) {

					int maxX = 0;
					int maxY = 0;
					for (String key : bpmnModel.getLocationMap().keySet()) {
						GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(key);
						double elementX = graphicInfo.getX()
								+ graphicInfo.getWidth();
						if (maxX < elementX) {
							maxX = (int) elementX;
						}
						double elementY = graphicInfo.getY()
								+ graphicInfo.getHeight();
						if (maxY < elementY) {
							maxY = (int) elementY;
						}
					}

					Panel imagePanel = new Panel(); // using panel for
													// scrollbars
					imagePanel.addStyleName(Reindeer.PANEL_LIGHT);
					imagePanel.setWidth(100, UNITS_PERCENTAGE);
					imagePanel.setHeight(100, UNITS_PERCENTAGE);

					URL url = new URL(Page.getCurrent().getLocation()
							.toString().replace("/ui", "")
							+ "diagram-viewer/index.html?processDefinitionId="
							+ processDefinition.getId());
					Embedded browserPanel = new Embedded("",
							new ExternalResource(url));
					browserPanel.setType(Embedded.TYPE_BROWSER);
					browserPanel.setWidth(maxX + 350 + "px");
					browserPanel.setHeight(maxY + 220 + "px");

					HorizontalLayout panelLayout = new HorizontalLayout();
					panelLayout.setSizeUndefined();
					imagePanel.setContent(panelLayout);
					panelLayout.addComponent(browserPanel);

					mainLayout.addComponent(imagePanel);

					didDrawImage = true;
				}

			} catch (Exception e) {
				// LOGGER.error("Error loading process diagram component", e);
			}
		}

		if (didDrawImage == false) {

			StreamResource diagram = null;

			// Try generating process-image stream
			if (processDefinition.getDiagramResourceName() != null) {
				diagram = new ProcessDefinitionImageStreamResourceBuilder()
						.buildStreamResource(processDefinition,
								repositoryService);
			}

			if (diagram != null) {

				Embedded embedded = new Embedded(null, diagram);
				embedded.setType(Embedded.TYPE_IMAGE);
				embedded.setSizeUndefined();

				Panel imagePanel = new Panel(); // using panel for scrollbars
				imagePanel.addStyleName(Reindeer.PANEL_LIGHT);
				imagePanel.setWidth(100, UNITS_PERCENTAGE);
				imagePanel.setHeight(100, UNITS_PERCENTAGE);
				HorizontalLayout panelLayout = new HorizontalLayout();
				panelLayout.setSizeUndefined();
				imagePanel.setContent(panelLayout);
				panelLayout.addComponent(embedded);
				mainLayout.addComponent(imagePanel);

				didDrawImage = true;
			}
		}

		if (didDrawImage == false) {
			Label noImageAvailable = new Label(
					i18nManager.getMessage(Messages.PROCESS_NO_DIAGRAM));
			mainLayout.addComponent(noImageAvailable);
		}
		
	}

	private void doStartProcessInstance() {
		//WW_TODO 开始启动关联的一个流程
		ProcessInstance processInstance = projectManagerService.startProcessInstanceRelateTodo(todoId, "klwork-crowdsourcing-act");
		
		List<Task> loggedInUsersTasks = taskService.createTaskQuery()
				.taskAssignee(LoginHandler.getLoggedInUser().getId())
				.processInstanceId(processInstance.getId()).list();
		if (loggedInUsersTasks.size() > 0) {
			String message = "一个新任务" + loggedInUsersTasks.get(0).getName()
					+ "在您的收件箱，请注意查收";
			Notification.show(message, Notification.Type.HUMANIZED_MESSAGE);
			
		}
	}
}
