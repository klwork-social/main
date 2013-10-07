package com.klwork.explorer.ui.business.outproject;

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
import org.vaadin.alump.scaleimage.ScaleImage;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.service.OutsourcingProjectService;
import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.process.ProcessDefinitionImageStreamResourceBuilder;
import com.klwork.explorer.ui.task.TaskDetailPanel;
import com.klwork.explorer.ui.task.AbstractTaskPage;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class NewOutProjectPopupWindow extends PopupWindow {

	private static final long serialVersionUID = 3129077881658239761L;

	// Services
	public transient RepositoryService repositoryService;
	protected transient ManagementService managementService;
	public transient RuntimeService runtimeService;
	public transient OutsourcingProjectService outsourcingProjectService;
	protected transient TaskService taskService;
	protected transient ProjectManagerService projectManagerService;
	protected I18nManager i18nManager;
	
	TextField nameField = null;
	private String currentSelectType = "0";
	// Members
	public ProcessDefinition processDefinition;
	protected Deployment deployment;
	protected VerticalLayout mainLayout;
	public NewOutProjectPopupWindow() {
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
		
		this.i18nManager = ViewToolManager.getI18nManager();
		initUI();
	}

	public void initUI() {
		addStyleName(Reindeer.WINDOW_LIGHT);
		setModal(true);
		setHeight("80%");
		setWidth("50%");
		center();
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSizeFull();
		setContent(mainLayout);
		Label processTitle = new Label("启动外部流程");
		processTitle.addStyleName(ExplorerLayout.STYLE_H3);
		mainLayout.addComponent(processTitle);
		
		initForm();
		
		//initNotice();
		initButtons();
	}

	private void initForm() {
		FormLayout formLayout  = new FormLayout();
		nameField = new TextField("需求简称");
		nameField.setValue("");
		nameField.focus();
		nameField.setRequired(true);
		nameField.setRequiredError("需求的名称不能为空");
		formLayout.addComponent(nameField);
		
		List<DictDef> list = DictDef.queryDictsByType(DictDef
				.dict("flow_type"));
		OptionGroup g = CommonFieldHandler.createCheckBoxs(list, "流程分类", false,
				"0");
		g.setImmediate(true);
	
		
		final ScaleImage tileImage = new ScaleImage();
		tileImage.setStyleName("tile-image");
		tileImage.setSource(new ThemeResource("img/project/flow_type_" + 0 + ".png"));
		tileImage.setWidth("400px");
		tileImage.setHeight("400px");
		
		formLayout.addComponent(g);
		formLayout.addComponent(tileImage);
		
		g.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Object value = event.getProperty().getValue();
				//当期的选中记录下来
				currentSelectType = value.toString();
				initImageContent(value.toString(),tileImage);
			}

			
		});
		
		
		mainLayout.addComponent(formLayout);
	}
	
	private void initImageContent(String value, ScaleImage tileImage) {
		tileImage.setSource(new ThemeResource("img/project/flow_type_" + value + ".png"));
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
				Task task = doStartProcessInstance();
				 Component detailComponent = new TaskDetailPanel(task, null);
				 mainLayout.removeAllComponents();
				 mainLayout.addComponent(detailComponent);
				//close();
			}
		});

		Button calButton = new Button("取消");
		buttonLayout.addComponent(calButton);
		buttonLayout.setComponentAlignment(calButton, Alignment.BOTTOM_CENTER);
		calButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		mainLayout.addComponent(buttonLayout);
		mainLayout.setExpandRatio(buttonLayout, 1.0f);
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

	private Task doStartProcessInstance() {
		DictDef def = DictDef.dictValue(DictDef.dict("flow_type"), currentSelectType);
		String sign = def.getCode().substring("flow_type_".length() ) + "-flow";
		//WW_TODO 开始启动关联的一个流程
		ProcessInstance processInstance = projectManagerService.startProcessInstance(sign);
		
		List<Task> loggedInUsersTasks = taskService.createTaskQuery()
				.taskAssignee(LoginHandler.getLoggedInUser().getId())
				.processInstanceId(processInstance.getId()).list();
		if (loggedInUsersTasks.size() > 0) {
			Task task = loggedInUsersTasks.get(0);
			task.setName("发布需求_" + nameField.getValue());
			taskService.saveTask(task);
			String message = "一个新任务" + task.getName()
					+ "在您的收件箱，请注意查收";
			//Notification.show(message, Notification.Type.HUMANIZED_MESSAGE);
			return task;
		}
		return null;
	}
}
