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

import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.business.query.SocialUserWeiboTableQuery;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * The Class AllWeiboPage.
 */
public class QQWeiboDisplayPage extends DetailPanel {
	// Services
	protected transient TaskService taskService;
	
	protected transient SocialUserWeiboService socialUserWeiboService;

	protected VerticalLayout centralLayout;

	int type = 0;

	SocialUserAccount socialUserAccount;

	public QQWeiboDisplayPage(SocialUserAccount socialUserAccount, int type) {
		super();
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		this.socialUserWeiboService = ViewToolManager
				.getBean("socialUserWeiboService");
		this.socialUserAccount = socialUserAccount;
		this.type = type;
	}

	@Override
	public void attach() {
		super.attach();
		init();
	}

	protected void init() {
		setSizeFull();
		addStyleName(Reindeer.LAYOUT_WHITE);

		// Central panel: all task data
		centralLayout = new VerticalLayout();
		centralLayout.setMargin(true);
		setDetailContainer(centralLayout);

		/*
		 * // 增加一个分隔线 addDetailComponent(CommonFieldHandler.getSpacer());
		 */

		Table listTable = new Table();
		listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		listTable.setWidth(100, Unit.PERCENTAGE);
		listTable.setHeight(100, Unit.PERCENTAGE);
		centralLayout.addComponent(listTable);
		centralLayout.setExpandRatio(listTable, 1);

		LazyLoadingQuery lazyLoadingQuery = new SocialUserWeiboTableQuery(
				socialUserAccount,type);
		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, 10);
		listTable.setContainerDataSource(listContainer);
		if (lazyLoadingQuery.size() < 10) {
			listTable.setPageLength(0);
		} else {
			listTable.setPageLength(10);
		}

		listTable.addGeneratedColumn("", new SocialHeadColumnGenerator());
		TableHandler.setTableNoHead(listTable);
		listTable.setImmediate(false);
	}

	public void noticeNewTask(String processInstanceId) {
		List<Task> loggedInUsersTasks = taskService.createTaskQuery()
				.taskAssignee(LoginHandler.getLoggedInUser().getId())
				.processInstanceId(processInstanceId).list();
		if (loggedInUsersTasks.size() > 0) {
			String message = "一个新任务" + loggedInUsersTasks.get(0).getName()
					+ "在您的收件箱，请注意查收";
			Notification.show(message, Notification.Type.HUMANIZED_MESSAGE);

		}
	}

	protected Image currentImage(String retweetedBmiddlePic, String defualt,String add) {
		Image image = null;
		if (StringTool.judgeBlank(retweetedBmiddlePic)) {
			image = new Image(null, new ExternalResource(retweetedBmiddlePic + add));
		} else {
			if (StringTool.judgeBlank(defualt)) {
				image = new Image(null, new ThemeResource(defualt));
			}
		}
		return image;
	}

	public HorizontalLayout getWeboButtonLayOut() {
		return new HorizontalLayout() {

			{
				setSizeFull();
				setSpacing(true);
				addStyleName("social");
				Button deletButton  = new Button("删除");
				deletButton.addStyleName(Reindeer.BUTTON_LINK);
				deletButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						
					}
				});
				addComponent(deletButton);
				setExpandRatio(deletButton, 1.0f);
				setComponentAlignment(deletButton,
						Alignment.BOTTOM_RIGHT);
				
				Button transmitButton  = new Button("转发");
				transmitButton.addStyleName(Reindeer.BUTTON_LINK);
				transmitButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						
					}
				});
				addComponent(transmitButton);
				
				Button commentButton  = new Button("评论");
				commentButton.addStyleName(Reindeer.BUTTON_LINK);
				commentButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						
					}
				});
				addComponent(commentButton);
			}
		};
	}

	public VerticalLayout getFatieAndTime(final SocialUserWeibo userWeibo) {
		return new VerticalLayout() {
			{
				//一个图片
				String origPic = userWeibo.getOriginalPic();
				Image image = currentImage(
						origPic, null,"/2000");
				//System.out.println("测试一下" + image.getHeight());
				if (image != null) {
					image.setHeight("200px");
					image.setWidth("100px");
					addComponent(image);
				}
				
				// 时间 来自2013-07-18 15:19 来自 投资界
				//setSizeUndefined();
				addComponent(new HorizontalLayout() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -3960592289157559242L;
					{
						setSizeFull();
						setSpacing(true);
						
						//微博时间
						Date date = userWeibo
								.getCreateAt();
						if (date != null) {
							String strDate = StringDateUtil
									.dateToString(date, 9);
							Link linkDate = new Link(
									strDate,
									new ExternalResource(
											"http://weibo.com/"
													+ userWeibo
															.getWeiboUid()));
							addComponent(linkDate);
							setComponentAlignment(linkDate,
									Alignment.MIDDLE_LEFT);
						}
						//来自
						Label title = new Label("来自");
						title.addStyleName(ExplorerLayout.STYLE_H3);
						addComponent(title);
						setComponentAlignment(title,
								Alignment.MIDDLE_LEFT);
						//setExpandRatio(title, 0.1f);
						//
						String source = userWeibo
								.getSource();
						Link link = new Link(
								source,
								new ExternalResource(
										"http://weibo.com/"
												+ source));
						link.setCaption(source);
						addComponent(link);
						
						setComponentAlignment(link,
								Alignment.MIDDLE_LEFT);
						setExpandRatio(link, 0.5f);
					}
				});
			}
		};
	}

	public void initTextLabel(Label descriptionField) {
		descriptionField.addStyleName(ExplorerLayout.STYLE_H3);
		descriptionField.setContentMode(ContentMode.HTML);
		descriptionField.setWidth("550px");
	}

	public void initOrginTextLable(Label l) {
		l.setWidth("550px");
		l.setContentMode(ContentMode.HTML);
		l.addStyleName(ExplorerLayout.STYLE_H4);
	}

	public class SocialHeadColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			final SocialUserWeibo userWeibo = BinderHandler.getTableBean(source,
					itemId);
			GridLayout grid = new GridLayout(3, 1);
			grid.addStyleName(Reindeer.SPLITPANEL_SMALL);
			grid.setMargin(true);
			// grid.setMargin(new MarginInfo(true, false, true, false));
			// 加点空
			grid.setSpacing(true);
			//grid.setSizeFull();
			grid.setColumnExpandRatio(0, 0.1f);
			grid.setColumnExpandRatio(1, 0.7f);
			grid.setColumnExpandRatio(2, 0.2f);
			
			grid.setSizeUndefined();

			VerticalLayout firstLayout = new VerticalLayout();
			firstLayout.setMargin(true);
			//头像
			Image image = currentImage(userWeibo.getUserProfileImageUrl(),
					"img/weibo/head_01.jpg","/50");
			firstLayout.addComponent(image);
			
			//粉丝数
			Label followerLable = new Label(userWeibo.getWeiboUidFollower()
					+ "");
			followerLable.addStyleName(ExplorerLayout.STYLE_H3);
			followerLable.setWidth(100, Unit.PERCENTAGE);
			firstLayout.addComponent(followerLable);
			

			Label title = new Label("粉丝");
			title.addStyleName(ExplorerLayout.STYLE_H3);
			title.setWidth(100, Unit.PERCENTAGE);
			firstLayout.addComponent(title);

			grid.addComponent(firstLayout, 0, 0);

			// 第二档
			VerticalLayout secondLayout = new VerticalLayout() {
				{
					setSpacing(true);
					setMargin(true);
					// 用户连接
					Link goToMain = new Link(userWeibo.getUserScreenName() + ":",
							new ExternalResource("http://weibo.com/"
									+ userWeibo.getWeiboUid() ));
					
					addComponent(goToMain);

					// 微博内容
					/*TextArea descriptionField = CommonFieldHandler
							.createTextArea("");
					//descriptionField.set
					descriptionField.setSizeFull();
					descriptionField.setHeight("80px");
					descriptionField.setValue(userWeibo.getText());*/
					
					Label descriptionField = new Label();
					initTextLabel(descriptionField);
					descriptionField.setValue(userWeibo.getText());
					
					addComponent(descriptionField);
					
					//是否有原帖
					if(userWeibo.getRetweetedId() != null){
						final SocialUserWeibo orginWeibo = socialUserWeiboService.findSocialUserWeiboById(userWeibo.getRetweetedId());
						CustomLayout csslayout = new CustomLayout("forwardWeibo");
						
						
						//原帖图
						Image image = currentImage(
								orginWeibo.getOriginalPic(), null,"/2000");
						if(image != null){
							csslayout.addComponent(image,"imgContent");
						}
						//内容前的@a
						csslayout.addComponent(new HorizontalLayout() {
							/**
							 * 
							 */
							private static final long serialVersionUID = -3960592289157559242L;
							{
								//setSizeUndefined();
								//setMargin(true);
								Link link = new Link(
										"@" + orginWeibo.getUserScreenName(),
										new ExternalResource(
												"http://weibo.com/"
														+ orginWeibo.getWeiboId()));
								link.setTargetName("_blank");
								addComponent(link);
								
								if(orginWeibo.getUserVerified() == 1){
									Image image = currentImage(
											null, "img/weibo/v.gif","");
									image.setHeight("10px");
									image.setWidth("11px");
									addComponent(image);
								}
							}
						},"weiboUrlContent");
						
						//原帖内容
						Label l = new Label(orginWeibo.getText());
						initOrginTextLable(l);
						csslayout.addComponent(l,"weiboContent");
						
						
						
						addComponent(csslayout);
					}//原帖完
					
					addComponent(new HorizontalLayout() {
						{	
							setSizeFull();
							setSpacing(true);
							//发帖的日期和来自,贴的图片
							addComponent(getFatieAndTime(userWeibo));
							
							//删除操作
							HorizontalLayout weboButtonLayOut = getWeboButtonLayOut();
							addComponent(weboButtonLayOut);
							setComponentAlignment(weboButtonLayOut,
									Alignment.BOTTOM_RIGHT);
							
						}
					});
				}
			};
			grid.addComponent(secondLayout, 1, 0);

			// 第三档
			VerticalLayout thirdLayout = new VerticalLayout() {
				{
					Button addButton = new Button("生成任务");
					addButton.addStyleName(Reindeer.BUTTON_LINK);
					addComponent(addButton);
				}
			};
			grid.addComponent(thirdLayout, 2, 0);

			

			return grid;
		}

	}
}
