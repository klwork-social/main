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

import java.util.Map;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.NoteSendEntity;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.service.SocialEvernoteService;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 保存到日记本窗口弹出窗口
 * @author ww
 * 
 */
public class SaveToNotePopupWindow extends WeiboPopupWindow {

	protected transient SocialUserWeiboService socialUserWeiboService;
	private transient AbstractSocialService socialService;
	private transient  SocialUserAccountService socialUserAccountService;
	//笔记本
	private transient SocialEvernoteService socialEvernoteService;
	
	protected transient I18nManager i18nManager;

	private SocialUserAccount socialUserAccount;
	private SocialUserWeibo userWeibo;
	private SocialUserWeibo orginWeibo;
	private AbstractWeiboDisplayPage mainPage;
	private FieldGroup scheduleEventFieldGroup = new FieldGroup();
	
	private NoteSendEntity noteEntity = new NoteSendEntity();
	private BeanItem<NoteSendEntity> currentBeanItem = new BeanItem<NoteSendEntity>(noteEntity);

	boolean hasOrginWeibo = false;// 是否有原帖
	private TextArea weiboContentTA = new TextArea("");

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7161608703004851133L;

	public SaveToNotePopupWindow(final SocialUserWeibo userWeibo,
			SocialUserAccount socialUserAccount, final AbstractWeiboDisplayPage mainPage) {
		super(mainPage.getSocialType());
		this.socialUserAccount = socialUserAccount;
		this.socialUserWeiboService = ViewToolManager
				.getBean("socialUserWeiboService");
		this.socialService = AbstractSocialService.querySocialClass(mainPage.getSocialType());
		this.socialUserAccountService = ViewToolManager
				.getBean("socialUserAccountService");
		//笔记本
		this.socialEvernoteService = ViewToolManager
				.getBean("socialEvernoteService");
		
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = mainPage;
		
		noteEntity.setWeibId(userWeibo.getWeiboId());
		
		this.userWeibo = userWeibo;
		if (userWeibo.getRetweetedId() != null) {
			orginWeibo = socialUserWeiboService
					.findSocialUserWeiboById(userWeibo.getRetweetedId());
			hasOrginWeibo = true;
			noteEntity.setWeibId(orginWeibo.getId());
		}
		
		scheduleEventFieldGroup.setBuffered(true);
		if (currentBeanItem != null) {
			scheduleEventFieldGroup.setItemDataSource(currentBeanItem);
		}
		
		setHeight("65%");
		setWidth("50%");
		
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
				descriptionField.setValue("笔记内容");
				addComponent(descriptionField);
				// 帖子内容
				weiboContentTA = new TextArea("");
				weiboContentTA.setWidth("100%");
				weiboContentTA.setColumns(25);
				weiboContentTA.focus();
				addComponent(weiboContentTA);
				scheduleEventFieldGroup.bind(weiboContentTA, "content");
				
				if (hasOrginWeibo) {// 如果是原帖
					weiboContentTA.setValue("//@"
							+ userWeibo.getUserScreenName() + ": "
							+ userWeibo.getText());
				}else {
					weiboContentTA.setValue(
							userWeibo.getText());
							//+ mainPage.textTranslate(userWeibo.getText()));
				}
			
				//笔记本,
				addComponent(new VerticalLayout() {
					{
						setSpacing(true);
						
						TextField titleField = CommonFieldHandler.createTextField("标题");
						scheduleEventFieldGroup.bind(titleField, "title");
						
						//标题默认为内容的前十
						if(userWeibo.getText() != null && userWeibo.getText().length() > 10){
							titleField.setValue(userWeibo.getText().substring(0,10));
						}
						addComponent(titleField);
						// setSizeFull();
						// setMargin(true);
						SocialUserAccount noteAccount = socialUserAccountService.findSocialUserByType(
								socialUserAccount.getOwnUser(),
								DictDef.dictInt("evernote"));
						if(noteAccount != null){
							noteEntity.setUserAccountId(noteAccount.getId());
						}
						
						Map<String, String> map = socialEvernoteService.queryNotebook(noteAccount);
						ComboBox noteMap = CommonFieldHandler.createComBox("请选择笔记本", map, "");
						addComponent(noteMap);
						setComponentAlignment(noteMap, Alignment.MIDDLE_LEFT);
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
								//把微薄内容保存到第三方笔记本
								int ret = socialEvernoteService.saveWeiboToNotes(noteEntity);
								if(ret == 1){
									Notification.show("操作成功", Notification.Type.HUMANIZED_MESSAGE);
								}else {
									Notification.show("操作未成功!", Notification.Type.HUMANIZED_MESSAGE);
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
	
	@Override
	public void handlerTextChange(String s) {
		
	}
}
