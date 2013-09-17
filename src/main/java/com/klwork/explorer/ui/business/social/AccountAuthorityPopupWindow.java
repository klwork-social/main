package com.klwork.explorer.ui.business.social;

import java.util.Collection;
import java.util.List;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.ResourcesAssignManager;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.service.ResourcesAssignManagerService;
import com.klwork.business.domain.service.SocialUseAuthorityListService;
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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 账号所属团队授权
 * @author ww
 */
public class AccountAuthorityPopupWindow extends PopupWindow {
	protected transient I18nManager i18nManager;
	public transient SocialUseAuthorityListService socialUseAuthorityListService;
	public transient ResourcesAssignManagerService resourcesAssignManagerService;
	
	private VerticalLayout mainLayout;
	private TextArea webboContentTextArea = new TextArea("");
	private BusinessComponetHelp help = new BusinessComponetHelp();
	public SocialUserAccount sc;
	private ComboBox userGroupComboBox;
	private OptionGroup permitOptionGroup;
	
	public VerticalLayout getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(VerticalLayout mainLayout) {
		this.mainLayout = mainLayout;
	}

	public TextArea getWeiboContentTextArea() {
		return webboContentTextArea;
	}

	public void setWeiboContentTextArea(TextArea weiboContentTA) {
		this.webboContentTextArea = weiboContentTA;
	}

	public AccountAuthorityPopupWindow(SocialUserAccount sc) {
		i18nManager = ViewToolManager.getI18nManager();
		socialUseAuthorityListService =  ViewToolManager
				.getBean("socialUseAuthorityListService");
		resourcesAssignManagerService  =  ViewToolManager
				.getBean("resourcesAssignManagerService");
		this.sc = sc;
		addStyleName(Reindeer.WINDOW_LIGHT);
		setModal(true);
		setHeight("35%");
		setWidth("40%");
		center();
		initMain();

	}

	private void initMain() {
		mainLayout = new VerticalLayout() {
			{
				setSizeFull();
				//setSpacing(true);
				setMargin(new MarginInfo(true, true, true, true));
				ResourcesAssignManager m1 = resourcesAssignManagerService.queryOneByEntityIdAndType(sc.getId(), "1", "1");
				String defaultValue = m1 != null?m1.getTeamId() :"";
				//用户组
				userGroupComboBox = help.getUserOfTeamComboBox("账号所属团队",defaultValue);
				addComponent(userGroupComboBox);
				
				addComponent(CommonFieldHandler.getSpacer());
				
				Label descriptionField = new Label();
				descriptionField.addStyleName("wb_text");
				descriptionField.setContentMode(ContentMode.HTML);
				descriptionField.setValue("团队权限");
				//addComponent(descriptionField);
				
				
				List<DictDef> list = DictDef.queryDictsByType(DictDef
						.dict("user_weibo_authority"));
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
				//setExpandRatio(buttonLayout, 1f);//有滚动条出现，why
			}
		};
		setContent(mainLayout);
	}
	
	private void handleSubmit() {
		String teamId = (String)userGroupComboBox.getValue();
		Collection perimt = (Collection)permitOptionGroup.getValue();
		String entityType = DictDef.dict("user_account_info_type");//1
		String groupType = DictDef.dict("team_type_weibo_permit");//0
		resourcesAssignManagerService.addAccountPermit(sc,teamId, perimt,entityType,groupType);
	}
}
