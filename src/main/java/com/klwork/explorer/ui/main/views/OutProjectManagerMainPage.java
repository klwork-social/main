package com.klwork.explorer.ui.main.views;

import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.outproject.AddInFlowPage;
import com.klwork.explorer.ui.business.outproject.MyPublishFlowPage;
import com.klwork.explorer.ui.business.outproject.NewOutProjectPopupWindow;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class OutProjectManagerMainPage extends AbstractTabViewPage{
	
	public OutProjectManagerMainPage() {
		super();
	}

	@Override
	public void initTabData() {
		 //addTab(new PublicProjectListPage(),i18nManager.getMessage(Messages.MAIN_MENU_PUBLIC_TASK));
        addTab(new AddInFlowPage(),i18nManager.getMessage(Messages.OUTPROJECT_MY_PARTICIPATION));
		addTab(new MyPublishFlowPage(),i18nManager.getMessage(Messages.OUTPROJECT_MY_PUBLISH));
		initAddButton();
	}
	
	protected void initAddButton() {
		Button newCaseButton = new Button();
		newCaseButton.setStyleName("myTabButton");
		newCaseButton.setCaption("启动外部项目");
		newCaseButton.setIcon(Images.TASK_16);
		getMainLayout().addComponent(newCaseButton);

		newCaseButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				NewOutProjectPopupWindow newTaskPopupWindow = new NewOutProjectPopupWindow();
				ViewToolManager.showPopupWindow(newTaskPopupWindow);
			}
		});
	}
}
