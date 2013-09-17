package com.klwork.explorer.ui.business.project;

import java.util.Collection;
import java.util.List;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.model.ResourcesAssignManager;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.business.domain.service.ResourcesAssignManagerService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.klwork.explorer.ui.handler.BusinessComponetHelp;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

public class ProjectAuthorityPopupWindow extends PopupWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8902568036218338247L;
	protected transient I18nManager i18nManager;
	public transient ProjectService projectService;
	public transient ResourcesAssignManagerService resourcesAssignManagerService;
	public Project pj;
	private VerticalLayout mainLayout;
	private ComboBox userGroupComboBox;
	private BusinessComponetHelp help = new BusinessComponetHelp();
	private OptionGroup permitOptionGroup;

	public ProjectAuthorityPopupWindow(Project project){
		i18nManager = ViewToolManager.getI18nManager();
		projectService = ViewToolManager.getBean("projectService");
		resourcesAssignManagerService  =  ViewToolManager.getBean("resourcesAssignManagerService");
		this.pj = project;
		addStyleName(Reindeer.WINDOW_LIGHT);
		setModal(true);
		setHeight("35%");
		setWidth("40%");
		center();
		initMain();
	}
	
	@SuppressWarnings("serial")
	private void initMain(){
		mainLayout = new VerticalLayout() {
			{
				setSizeFull();
				setMargin(new MarginInfo(true, true, true, true));
				ResourcesAssignManager m1 = resourcesAssignManagerService.queryOneByEntityIdAndType(pj.getId(), "2", "2");
				String defaultValue = m1 != null?m1.getTeamId() :"";
				//用户组
				userGroupComboBox = help.getUserOfTeamComboBox("计划所属团队",defaultValue);
				addComponent(userGroupComboBox);
				addComponent(CommonFieldHandler.getSpacer());
				Label descriptionField = new Label();
				descriptionField.addStyleName("wb_text");
				descriptionField.setContentMode(ContentMode.HTML);
				descriptionField.setValue("团队权限");
				
				List<DictDef> list = DictDef.queryDictsByType(DictDef
						.dict("project_plan_authority"));
				permitOptionGroup = CommonFieldHandler.createCheckBoxs(list,"团队权限",true);		
				addComponent(permitOptionGroup);
				
				// 按钮
				HorizontalLayout buttonLayout = new HorizontalLayout() {
					{
						setSpacing(true);
						setSizeFull();
						// setMargin(true);
						Button okButton = new Button(
								i18nManager.getMessage(Messages.BUTTON_OK));
						addComponent(okButton);
						setComponentAlignment(okButton, Alignment.TOP_RIGHT);

						okButton.addClickListener(new ClickListener() {
							public void buttonClick(ClickEvent event) {
								handleSubmit();
								close();
							}
						});
						setExpandRatio(okButton, 1.0f);

						Button cancleButton = new Button(
								i18nManager.getMessage(Messages.BUTTON_CANCEL));
						addComponent(cancleButton);
						setComponentAlignment(cancleButton, Alignment.TOP_RIGHT);

						cancleButton.addClickListener(new ClickListener() {
							public void buttonClick(ClickEvent event) {
								close();
							}

						
						});
					}
				};
				addComponent(buttonLayout);
			}
				
		};
		setContent(mainLayout);
	}
	
	private void handleSubmit() {
		String teamId = (String)userGroupComboBox.getValue();
		Collection perimt = (Collection)permitOptionGroup.getValue();
		
		resourcesAssignManagerService.addProjectPlanPermit(pj,teamId, perimt,"2","2");
	}
	
	public VerticalLayout getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(VerticalLayout mainLayout) {
		this.mainLayout = mainLayout;
	}

}
