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

package com.klwork.explorer.ui.business.project;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.model.WeiboForwardSend;
import com.klwork.business.domain.service.SocialMainService;
import com.klwork.business.domain.service.SocialSinaService;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.business.utils.HtmlTranslateImageTool;
import com.klwork.business.utils.ImageRenderer;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.business.social.AbstractWeiboDisplayPage;
import com.klwork.explorer.ui.business.social.WeiboPopupWindow;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ww
 * 
 */
public class TodoSharePopupWindow extends WeiboPopupWindow {

	private transient SocialUserWeiboService socialUserWeiboService;
	private transient SocialSinaService socialSinaService;
	private transient SocialUserAccountService socialUserAccountService;
	private transient SocialMainService socialMainService;
	
	protected transient I18nManager i18nManager;

	private AbstractWeiboDisplayPage mainPage;

	private WeiboForwardSend weiboForwardSend = new WeiboForwardSend();
	private BeanItem<WeiboForwardSend> currentBeanItem = new BeanItem<WeiboForwardSend>(
			weiboForwardSend);

	boolean hasOrginWeibo = false;// 是否有原帖
	private TextArea weiboContentTA = new TextArea("");
	private VerticalLayout mainLayout;
	private String projectId = null;
	
	//private SocialUserAccount socialUserAccount;
	private OptionGroup accountGroup;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7161608703004851133L;
	

	public TodoSharePopupWindow(String projectId) {
		super("0");
		this.socialUserWeiboService = ViewToolManager
				.getBean("socialUserWeiboService");
		this.socialSinaService = ViewToolManager.getBean("socialSinaService");
		this.socialUserAccountService = ViewToolManager.getBean("socialUserAccountService");
		this.socialMainService = ViewToolManager.getBean("socialMainService");
		this.i18nManager = ViewToolManager.getI18nManager();
		
		
		this.projectId = projectId;//项目id
		
		
		initTodoListImage(projectId);
		mainLayout = new VerticalLayout() {
			{
				setSizeFull();
				setSpacing(true);
				setMargin(new MarginInfo(true, true, false, true));
				
				addComponent(new HorizontalLayout() {// 头像,还可输入
					{
						// setSizeFull();
						// setSpacing(true);
						setWidth("100%");
						// setSpacing(true);
						// setMargin(true);
						/*Image image = initFaceComponet();
						addComponent(image);
						setExpandRatio(image, 1.2f);
						setComponentAlignment(image, Alignment.MIDDLE_LEFT);*/
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
				
				weiboContentTA.setWidth("100%");
				weiboContentTA.setColumns(25);
				weiboContentTA.focus();
				addComponent(weiboContentTA);
				
				
				/*Label descriptionField = new Label();
				descriptionField.addStyleName("wb_text");
				descriptionField.setContentMode(ContentMode.HTML);
				descriptionField.setValue();
				addComponent(descriptionField);*/
			
				accountGroup = new OptionGroup("选择微博帐号进行发送");
				accountGroup.setMultiSelect(true);
				accountGroup.setStyleName("horizontal");
				addComponent(accountGroup);
				SocialUserAccountQuery query = new SocialUserAccountQuery();
				query.setOwnUser(LoginHandler.getLoggedInUser().getId());
				
				List<SocialUserAccount> list =  socialUserAccountService.findSocialUserAccountByQueryCriteria(query , null);
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					SocialUserAccount s = (SocialUserAccount) iterator
							.next();
					String p = s.getId();
					Item i = accountGroup.addItem(p);
					accountGroup.setItemCaption(p, s.queryTypeName() + "_" + s.getName());
				}
		
				
				// 按钮
				HorizontalLayout buttonLayout = new HorizontalLayout() {
					{
						setSpacing(true);
						setSizeFull();
						
						Image image = initFaceComponet();//表情
						addComponent(image);
						setComponentAlignment(image, Alignment.TOP_LEFT);
						// setMargin(true);
						Button okButton = new Button("发布");
						addComponent(okButton);
						setComponentAlignment(okButton, Alignment.TOP_RIGHT);

						okButton.addClickListener(new ClickListener() {
							public void buttonClick(ClickEvent event) {
								handlerSendWeibo();
								Notification.show("操作成功", Notification.Type.HUMANIZED_MESSAGE);
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
								// handleFormSubmit();
								close();
							}
						});
					}
				};
				addComponent(buttonLayout);
				setExpandRatio(buttonLayout, 1f);

			}
		};
		setContent(mainLayout);
		setMainLayout(mainLayout);
		setWeiboContentTextArea(weiboContentTA);
	}
	
	public void initTodoListImage(String proId){
		  try {
	            short type = ImageRenderer.TYPE_PNG;
	            String filePant = HtmlTranslateImageTool.currentTodoListImagePath(proId);
	            File f = new File(filePant);
	            if(!f.exists()){//如果已经存在，不进行文件的生成,或者文件没有进行更新，则不进行更新
					FileOutputStream os = new FileOutputStream(filePant);
		            ImageRenderer r = new ImageRenderer();
		            //todolist的展示url
		            String urlstring = HtmlTranslateImageTool.getTodoListHtmlPath(proId);
					r.renderURL(urlstring, os, type);
		            os.close();
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	            System.err.println("Error: " + e.getMessage());
	        }
	}

	
	public void handlerSendWeibo() {
		Collection list = (Collection)accountGroup.getValue();
		List webAccountList = new ArrayList(list);
		String htmlUrlString = HtmlTranslateImageTool.getTodoListHtmlPath(projectId);
		String imageUrlString = HtmlTranslateImageTool.currentTodoListImagePath(projectId);
		String content = weiboContentTA.getValue() + " >>"+ htmlUrlString;
		socialMainService
				.sendWeiboAndImage(webAccountList,content,imageUrlString,"0");
	}

}
