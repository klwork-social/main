package com.klwork.explorer.ui.business.social;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.klwork.explorer.ui.custom.PopupWindow;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class WeiboPopupWindow extends PopupWindow {
	//表情窗口
	private Window factWindows;
	private VerticalLayout mainLayout;
	private TextArea webboContentTextArea = new TextArea("");
	
	
	
	public Window getFactWindows() {
		return factWindows;
	}

	public void setFactWindows(Window factWindows) {
		this.factWindows = factWindows;
	}

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

	public WeiboPopupWindow() {
		addStyleName(Reindeer.WINDOW_LIGHT);
		setModal(true);
		setHeight("55%");
		setWidth("45%");
		center();
	}
	
	private void buildNotifications(
			com.vaadin.event.MouseEvents.ClickEvent event) {
		factWindows = new Window("表情");
	
		factWindows.setWidth("400px");
		factWindows.setHeight("280px");
		factWindows.addStyleName("notifications");
		factWindows.setClosable(false);
		factWindows.setResizable(false);
		factWindows.setDraggable(false);
		factWindows.setPositionX(event.getClientX() - event.getRelativeX()
				- 12);
		factWindows.setPositionY(event.getClientY() - event.getRelativeY()
				- 12);
		factWindows.setCloseShortcut(KeyCode.ESCAPE, null);
		VerticalLayout l = new VerticalLayout();
		l.setMargin(true);
		l.setSpacing(true);
		l.setSizeFull();
		factWindows.setContent(l);
		
		GridLayout peopleGrid = new GridLayout();
		peopleGrid.setColumns(16);
		peopleGrid.setSizeFull();
		l.addComponent(peopleGrid);
		List<Map<String, String>> faces = DataProvider.getFacesList();
		for (Iterator iterator = faces.iterator(); iterator.hasNext();) {
			Map<String, String> map = (Map<String, String>) iterator.next();
			peopleGrid.addComponent(currentImage(map.get("phrase"),map.get("url")));
		}
	}

	private Image currentImage(final String title, String url) {
		Image image = new Image("", new ExternalResource(url));
		image.setDescription(title);
		image.addClickListener(new com.vaadin.event.MouseEvents.ClickListener() {

			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				if(factWindows != null){
					factWindows.close();
				}
				int c = getWeiboContentTextArea().getCursorPosition();
				StringBuffer oldString = new StringBuffer(getWeiboContentTextArea().getValue());
				if(c > -1){
					oldString.insert(c,title);
				}
				getWeiboContentTextArea().setValue(oldString.toString());
				
			}

		});
		return image;
	}
	
	/**
	 * 表情选择
	 * @return
	 */
	public Image initFaceComponet() {
		Image image = new Image(null, new ThemeResource(
				"img/weibo/smile.gif"));
		
		image.addClickListener(new com.vaadin.event.MouseEvents.ClickListener() {

			@Override
			public void click(
					com.vaadin.event.MouseEvents.ClickEvent event) {
				if (factWindows != null
						&& factWindows.getUI() != null)
					colseNotification();
				else {
					buildNotifications(event);
					getUI().addWindow(factWindows);
					mainLayout
							.addLayoutClickListener(new LayoutClickListener() {
								@Override
								public void layoutClick(
										LayoutClickEvent event) {
									colseNotification();
									mainLayout
											.removeLayoutClickListener(this);
								}
							});
				}
			}

		});
		return image;
	}
	
	public void close() {
		colseNotification();
		super.close();
	}

	public void colseNotification() {
		if(factWindows !=null)
			factWindows.close();
	}
	

}
