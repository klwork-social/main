package com.klwork.explorer.ui.business.social;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class WeiboPopupWindow extends PopupWindow {
	public boolean isSendAsImage() {
		return sendAsImage;
	}

	public void setSendAsImage(boolean sendAsImage) {
		this.sendAsImage = sendAsImage;
	}

	//表情窗口
	private Window factWindows;
	private VerticalLayout mainLayout;
	private TextArea webboContentTextArea = new TextArea("");
	private Label inputFontField;
	private List<Map<String, String>> faces = null;
	private String weiboType;
	private int faceWidth = 400;
	private int faceHeight = 280;
	//标示微薄以图片的形式发送，fase为普通发送
	private boolean sendAsImage = false;
	
	private transient AbstractSocialService socialService;
	
	public List<Map<String, String>> initFaces() {
		return DataProvider.getFacesList();
	}
	
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
		webboContentTextArea.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				handlerTextChange(webboContentTextArea.getValue());
			}
			
		});
		//webboContentTextArea.setTextChangeEventMode(TextChangeEventMode.EAGER);
		webboContentTextArea.addTextChangeListener(new TextChangeListener(){
			private static final long serialVersionUID = 2887114440246353008L;

			@Override
			public void textChange(TextChangeEvent event) {
				String s = event.getText();
				handlerTextChange(s);
			}
			
		});
		
		System.out.println(webboContentTextArea.getTextChangeTimeout());
		System.out.println();
	}

	public WeiboPopupWindow(String weiboType) {
		this.weiboType = weiboType;
		this.socialService = AbstractSocialService.querySocialClass(weiboType);
		this.faces = socialService.queryFaces();
		addStyleName(Reindeer.WINDOW_LIGHT);
		setModal(true);
		setHeight("55%");
		setWidth("45%");
		setPositionX(500);
		setPositionY(55);
		
		//center();
		//top();
	}
	
	private void buildNotifications(
			com.vaadin.event.MouseEvents.ClickEvent event) {
		factWindows = new Window("表情");
	
		
		factWindows.setWidth(faceWidth + "px");
		factWindows.setHeight(faceHeight + "px");
		//factWindows.addStyleName("notifications");
		factWindows.setClosable(false);
		factWindows.setResizable(false);
		factWindows.setDraggable(true);
		System.out.println("x---" + event.getClientX() + " relative: " + event.getRelativeX());
		System.out.println("y---" + event.getClientY() + " relative: " + event.getRelativeY() + "w " +  this.getFactWindows().getHeight() + "py " +  this.getPositionY());
		//
		factWindows.setPositionX(event.getClientX() - faceWidth - event.getRelativeX() -12);
		//factWindows.setPositionY(event.getClientY() - faceHeight/2);
		float f = this.getHeight()+ faceHeight/2;
		factWindows.setPositionY((int) (this.getPositionY() + f));
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

	public Label initInputFontField() {
		inputFontField = new Label();
		inputFontField.addStyleName("wb_text");
		inputFontField.setContentMode(ContentMode.HTML);
		inputFontField
				.setValue(getInputTitle(140));
		return inputFontField;
	}

	public String getInputTitle(int count) {
		return "还可输入<span class=\"number\">" + count + " </span>字";
	}
	
	public String getPassInputTitle(int count) {
		return "已经超过<span class=\"number\">" + count + " </span>字, 微薄内容将以图片的形式发送";
	}

	public void handlerTextChange(String s) {
		int totalChineseWords = StringTool.totalChineseWords(s);
		int count = 140 - totalChineseWords;
		if(count >= 0){//没有超过
			inputFontField
			.setValue(getInputTitle(count));
			sendAsImage = false;
		}else {//超过字数
			inputFontField
			.setValue(getPassInputTitle(-count));
			sendAsImage = true;
		}
	}
}
