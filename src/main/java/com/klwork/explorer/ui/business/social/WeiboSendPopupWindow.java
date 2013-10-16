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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.model.WeiboForwardSend;
import com.klwork.business.domain.service.SocialMainService;
import com.klwork.business.domain.service.SocialSinaService;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.business.utils.HtmlTranslateImageTool;
import com.klwork.business.utils.ImageRenderer;
import com.klwork.common.utils.FileUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
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
public class WeiboSendPopupWindow extends WeiboPopupWindow {

	private transient SocialUserWeiboService socialUserWeiboService;
	private transient SocialSinaService socialSinaService;
	private transient SocialUserAccountService socialUserAccountService;
	private transient SocialMainService socialMainService;

	protected transient I18nManager i18nManager;

	private AbstractWeiboDisplayPage mainPage;
	private FieldGroup scheduleEventFieldGroup = new FieldGroup();

	private WeiboForwardSend weiboForwardSend = new WeiboForwardSend();
	private BeanItem<WeiboForwardSend> currentBeanItem = new BeanItem<WeiboForwardSend>(
			weiboForwardSend);

	boolean hasOrginWeibo = false;// 是否有原帖
	private TextArea weiboContentTA = new TextArea("");
	private VerticalLayout mainLayout;

	private SocialUserAccount socialUserAccount;
	private OptionGroup accountGroup;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7161608703004851133L;

	public WeiboSendPopupWindow(final SocialUserAccount socialUserAccount) {
		super(socialUserAccount.getType().toString());
		this.socialUserWeiboService = ViewToolManager
				.getBean("socialUserWeiboService");
		this.socialSinaService = ViewToolManager.getBean("socialSinaService");
		this.socialUserAccountService = ViewToolManager
				.getBean("socialUserAccountService");
		this.socialMainService = ViewToolManager.getBean("socialMainService");
		this.i18nManager = ViewToolManager.getI18nManager();
		this.socialUserAccount = socialUserAccount;

		scheduleEventFieldGroup.setBuffered(true);
		if (currentBeanItem != null) {
			scheduleEventFieldGroup.setItemDataSource(currentBeanItem);
		}

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
						/*
						 * Image image = initFaceComponet();
						 * addComponent(image); setExpandRatio(image, 1.2f);
						 * setComponentAlignment(image, Alignment.MIDDLE_LEFT);
						 */
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
				scheduleEventFieldGroup.bind(weiboContentTA, "content");

				/*
				 * Label descriptionField = new Label();
				 * descriptionField.addStyleName("wb_text");
				 * descriptionField.setContentMode(ContentMode.HTML);
				 * descriptionField.setValue(); addComponent(descriptionField);
				 */

				accountGroup = new OptionGroup("同时用其他帐号进行发送");
				accountGroup.setMultiSelect(true);
				accountGroup.setStyleName("horizontal");
				addComponent(accountGroup);
				SocialUserAccountQuery query = new SocialUserAccountQuery();
				query.setOwnUser(socialUserAccount.getOwnUser());

				List<SocialUserAccount> list = socialUserAccountService
						.findSocialUserAccountByQueryCriteria(query, null);
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					SocialUserAccount s = (SocialUserAccount) iterator.next();
					String p = s.getId();
					if (p.equals(socialUserAccount.getId())) {
						continue;
					}
					Item i = accountGroup.addItem(p);
					accountGroup.setItemCaption(p,
							s.queryTypeName() + "_" + s.getName());
				}

				// 按钮
				HorizontalLayout buttonLayout = new HorizontalLayout() {
					{
						setSpacing(true);
						setSizeFull();

						Image image = initFaceComponet();// 表情
						addComponent(image);
						setComponentAlignment(image, Alignment.TOP_LEFT);
						// setMargin(true);
						Button okButton = new Button("发布");
						addComponent(okButton);
						setComponentAlignment(okButton, Alignment.TOP_RIGHT);

						okButton.addClickListener(new ClickListener() {
							public void buttonClick(ClickEvent event) {
								BinderHandler.commit(scheduleEventFieldGroup);
								handlerSendWeibo();
								Notification.show("操作成功",
										Notification.Type.HUMANIZED_MESSAGE);
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

	public void handlerSendWeibo() {
		Collection list = (Collection) accountGroup.getValue();
		List qu = new ArrayList(list);
		qu.add(socialUserAccount.getId());
		if (!isSendAsImage()) {
			socialMainService.sendWeibo(qu, weiboContentTA.getValue(), "0");
		} else {// 以图片的形式发送
			String key = socialUserWeiboService.currentKey();
			StringBuilder fcontent = StringTool.getSplitString(
					weiboContentTA.getValue(), "\n", 16);
			String imageUrlString = currentWeiboToImage(key, fcontent.toString());
			
			//微薄的内容保留50个字
			String contnet = weiboContentTA.getValue().substring(0, 100)
					+ "......";
			socialMainService.sendWeiboAndImage(qu, contnet, imageUrlString,
					"0");
		}
	}

	public String currentWeiboToImage(String key, String content) {
		try {
			short type = ImageRenderer.TYPE_PNG;
			// 产生实际的 html文件
			String htmlFacty = currentFactHtmlByFtl(key, content);
			
			String currentImagPath = HtmlTranslateImageTool
					.currentFilePathByKey("weibo", key, "png");
			FileOutputStream outFile = new FileOutputStream(currentImagPath);
			ImageRenderer r = new ImageRenderer();
			//根据前面产生的html,生成图片
			String fileCurrent = "file:///" + htmlFacty.replace("\\", "/");
			r.renderURL(fileCurrent, outFile, type);
			outFile.close();
			return currentImagPath;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
		return null;
	}

	private String currentFactHtmlByFtl(String key, String content) {
		String pathPrefix = "/com/klwork/weibo";
		Map root = new HashMap();
		root.put("content", content);
		StringBuffer b = FileUtil.getContetByFreemarker(ImageRenderer.class,
				"weiboImageContent.xhtml", pathPrefix, root);
		String fileurl = HtmlTranslateImageTool.currentFilePathByKey("weibo",
				key, "html");
		FileUtil.writeFile(b.toString(), fileurl);
		return fileurl;
	}
}
