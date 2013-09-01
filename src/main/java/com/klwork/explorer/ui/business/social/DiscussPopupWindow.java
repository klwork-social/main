/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klwork.explorer.ui.business.social;

import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.WeiboForwardSend;
import com.klwork.business.domain.service.SocialSinaService;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 品论弹出窗口
 * @author ww
 * 
 */
public class DiscussPopupWindow extends WeiboPopupWindow {

	protected transient SocialUserWeiboService socialUserWeiboService;
	private transient AbstractSocialService socialService;
	protected transient I18nManager i18nManager;

	private SocialUserWeibo userWeibo;
	private SocialUserWeibo orginWeibo;
	private AbstractWeiboDisplayPage mainPage;
	private FieldGroup scheduleEventFieldGroup = new FieldGroup();
	
	private WeiboForwardSend weiboForwardSend = new WeiboForwardSend();
	private BeanItem<WeiboForwardSend> 
	currentBeanItem = new BeanItem<WeiboForwardSend>(weiboForwardSend);

	boolean hasOrginWeibo = false;// 是否有原帖
	private TextArea weiboContentTA = new TextArea("");

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7161608703004851133L;

	public DiscussPopupWindow(final SocialUserWeibo userWeibo,
			final AbstractWeiboDisplayPage mainPage) {
		super(mainPage.getSocialType());
		this.socialUserWeiboService = ViewToolManager
				.getBean("socialUserWeiboService");
		this.socialService = AbstractSocialService.querySocialClass(mainPage.getSocialType());
		
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = mainPage;
		
		weiboForwardSend.setWeibId(userWeibo.getWeiboId());
		weiboForwardSend.setUserAccountId(userWeibo.getUserAccountId());
		this.userWeibo = userWeibo;
		if (userWeibo.getRetweetedId() != null) {
			orginWeibo = socialUserWeiboService
					.findSocialUserWeiboById(userWeibo.getRetweetedId());
			hasOrginWeibo = true;
			weiboForwardSend.setWeibId(orginWeibo.getId());
		}
		
		scheduleEventFieldGroup.setBuffered(true);
		if (currentBeanItem != null) {
			scheduleEventFieldGroup.setItemDataSource(currentBeanItem);
		}
		
		
		VerticalLayout mainLayout = initMainLayout(userWeibo);
		setContent(mainLayout);
		
		setMainLayout(mainLayout);
		setWeiboContentTextArea(weiboContentTA);
	}

	public VerticalLayout initMainLayout(final SocialUserWeibo userWeibo) {
		return new VerticalLayout() {
			{
				setSizeFull();
				setSpacing(true);
				setMargin(new MarginInfo(true, true, false, true));
				
				Label descriptionField = new Label();
				descriptionField.addStyleName("wb_text");
				descriptionField.setContentMode(ContentMode.HTML);
				descriptionField.setValue("@" + userWeibo.getUserScreenName()
						+ ":" + mainPage.textTranslate(userWeibo.getText()));
				addComponent(descriptionField);
				
				addComponent(new HorizontalLayout() {// 头像,还可输入
					{
						setWidth("100%");
					
						//
						Label inputFontField = initInputFontField();
						addComponent(inputFontField);
						// setExpandRatio(image, 1.0f);
						setComponentAlignment(inputFontField,
								Alignment.MIDDLE_RIGHT);
						setExpandRatio(inputFontField, 0.2f);
					}
				});

				// 帖子内容
				weiboContentTA = new TextArea("");
				weiboContentTA.setWidth("100%");
				weiboContentTA.setColumns(25);
				weiboContentTA.focus();
				addComponent(weiboContentTA);
				scheduleEventFieldGroup.bind(weiboContentTA, "content");
			
				// 品论给
				addComponent(new HorizontalLayout() {
					{
						setSpacing(true);
						// setSizeFull();
						// setMargin(true);
						CheckBox simuField = CommonFieldHandler
								.createCheckBox("");
						simuField
								.addValueChangeListener(new Property.ValueChangeListener() {

									private static final long serialVersionUID = -7104996493482558021L;

									@Override
									public void valueChange(
											ValueChangeEvent event) {
										Object value = event.getProperty()
												.getValue();
									}

								});
						addComponent(simuField);
						// setExpandRatio(image, 1.0f);
						setComponentAlignment(simuField, Alignment.MIDDLE_LEFT);

						Label commentLable = new Label();
						commentLable.setContentMode(ContentMode.HTML);
						commentLable.setValue("同时转发一条微博");
						
						addComponent(commentLable);
						// setExpandRatio(image, 1.0f);
						setComponentAlignment(commentLable,
								Alignment.MIDDLE_LEFT);
						
						Image image = initFaceComponet();//表情
						addComponent(image);
					}
				});

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
								BinderHandler.commit(scheduleEventFieldGroup);
								int ret = socialService.discussWeibo(weiboForwardSend);
								if(ret == 1){
									Notification.show("操作成功", Notification.Type.HUMANIZED_MESSAGE);
								}
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
				setExpandRatio(buttonLayout, 1f);

			}
		};
	}

}
