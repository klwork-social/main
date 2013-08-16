package com.klwork.explorer.ui.business.social;

import java.util.Date;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.business.utils.SinaSociaTool;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public abstract class AbstractWeiboDisplayPage extends DetailPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	protected transient TaskService taskService;

	protected transient SocialUserWeiboService socialUserWeiboService;

	protected VerticalLayout centralLayout;

	int type = 0;

	SocialUserAccount socialUserAccount;

	public AbstractWeiboDisplayPage(SocialUserAccount socialUserAccount,
			int type) {
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
				socialUserAccount, type);
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
		listTable.setSelectable(false);
	}

	/**
	 * 根据url得到一个图片对象
	 * 
	 * @param picUrl
	 *            外部图片地址
	 * @param defualtUrl
	 *            如果外部的图片地址为空，默认一个本地图片
	 * @param additional
	 *            图片地址是否进行附加，主要用来进行图片的缩放
	 * @return
	 */
	protected Image currentImage(String picUrl, String defualtUrl,
			String additional) {
		Image image = null;
		if (StringTool.judgeBlank(picUrl)) {
			image = new Image(null, new ExternalResource(picUrl + additional));
		} else {
			if (StringTool.judgeBlank(defualtUrl)) {
				image = new Image(null, new ThemeResource(defualtUrl));
			}
		}
		return image;
	}
	
	protected String textTranslate(String text) {
		return text;
	}
	
	/**
	 * 微博操作
	 * @return
	 */
	public HorizontalLayout getWeboButtonLayOut() {
		return new HorizontalLayout() {

			{
				setSizeFull();
				setSpacing(true);
				addStyleName("social");
				Button deletButton = new Button("删除");
				deletButton.addStyleName(Reindeer.BUTTON_LINK);
				deletButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {

					}
				});
				addComponent(deletButton);
				setExpandRatio(deletButton, 1.0f);
				setComponentAlignment(deletButton, Alignment.BOTTOM_RIGHT);

				Button transmitButton = new Button("转发");
				transmitButton.addStyleName(Reindeer.BUTTON_LINK);
				transmitButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {

					}
				});
				addComponent(transmitButton);

				Button commentButton = new Button("评论");
				commentButton.addStyleName(Reindeer.BUTTON_LINK);
				commentButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {

					}
				});
				addComponent(commentButton);
			}
		};
	}
	
	/**
	 * 原帖按钮
	 * @param orginWeibo 
	 * @return
	 */
	public HorizontalLayout getRetweetWeboButtonLayOut(final SocialUserWeibo orginWeibo) {
		return new HorizontalLayout() {
			{
				setSizeFull();
				setHeight("26px");
				setSpacing(true);
				addStyleName("social");

				String repostsCount = orginWeibo.getRepostsCount() + "";
				String commentsCount = orginWeibo.getCommentsCount() + "";
				
				Button transmitButton = new Button("转发(" + repostsCount + ")");
				transmitButton.addStyleName(Reindeer.BUTTON_LINK);
				transmitButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {

					}
				});
				addComponent(transmitButton);
				setExpandRatio(transmitButton, 1.0f);
				setComponentAlignment(transmitButton, Alignment.MIDDLE_RIGHT);

				Button commentButton = new Button("评论(" + commentsCount + ")");
				commentButton.addStyleName(Reindeer.BUTTON_LINK);
				commentButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {

					}
				});
				addComponent(commentButton);
				setComponentAlignment(commentButton, Alignment.MIDDLE_RIGHT);

			}
		};
	}
	
	/**
	 * 显示发帖的日期和来自贴的图片
	 * @param userWeibo
	 * @return
	 */
	public VerticalLayout getFatieAndTime(final SocialUserWeibo userWeibo) {
		return new VerticalLayout() {
			{
				setSizeFull();
				Image image = initOriginalPic(userWeibo);
				if (image != null) {
					addComponent(image);
				}

				// 时间 来自2013-07-18 15:19 来自 投资界
				// setSizeUndefined();
				addComponent(currentComeFromCompent(userWeibo));
			}
		};
	}

	public void initTextLabel(Label descriptionField) {
		descriptionField.addStyleName("wb_text");
		descriptionField.setContentMode(ContentMode.HTML);
		// descriptionField.setWidth("550px");
	}

	public void initOrginTextLable(Label l) {
		// l.setWidth("550px");
		l.setContentMode(ContentMode.HTML);
		// l.addStyleName(ExplorerLayout.STYLE_H4);
	}
	
	/**
	 * 处理用户图像
	 * @param userWeibo
	 * @return
	 */
	public Image initUserProfileImage(final SocialUserWeibo userWeibo) {
		Image image = currentImage(userWeibo.getUserProfileImageUrl(),
				"img/weibo/head_01.jpg", "/50");
		return image;
	}
	
	/**
	 * 发帖人连接
	 * @param userWeibo
	 * @return
	 */
	public Link initUserScreenName(final SocialUserWeibo userWeibo) {
		Link goToMain = new Link(userWeibo.getUserScreenName()
				+ ":", new ExternalResource(getWeiboMainUrl()
				+ userWeibo.getWeiboUid()));
		goToMain.setTargetName("_blank");
		return goToMain;
	}
	
	/**
	 * 处理微博原帖图
	 * @param orginWeibo
	 * @return
	 */
	public Image initRetweetPic(final SocialUserWeibo orginWeibo) {
		// 原帖图
		Image image = currentImage(
				orginWeibo.getThumbnailPic(), null, "");// 小图
		return image;
	}
	
	public String getWeiboMainUrl() {
		return "http://weibo.com/";
	}
	
	/**
	 * 处理微博的图片
	 * @param userWeibo
	 * @return
	 */
	public Image initOriginalPic(final SocialUserWeibo userWeibo) {
		// 一个图片
		String origPic = userWeibo.getOriginalPic();
		Image image = currentImage(origPic, null, "/2000");
		// System.out.println("测试一下" + image.getHeight());
		if (image != null) {
			image.setHeight("120px");
			image.setWidth("200px");
			
		}
		return image;
	}
	
	/**
	 * 原帖的发帖人链接
	 * @param orginWeibo
	 * @return
	 */
	public Link initRetweetedUserScreenName(final SocialUserWeibo orginWeibo) {
		Link link = new Link("@"
				+ orginWeibo.getUserScreenName(),
				new ExternalResource(
						getWeiboMainUrl()
								+ orginWeibo
										.getWeiboId()));
		link.setTargetName("_blank");
		return link;
	}
	
	/**
	 * 来自组件
	 * @param userWeibo
	 * @return
	 */
	public HorizontalLayout currentComeFromCompent(
			final SocialUserWeibo userWeibo) {
		return new HorizontalLayout() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3960592289157559242L;
			{
				setSizeFull();
				setHeight("26px");
				setSpacing(true);
				addStyleName("social");

				// 微博时间
				Date date = userWeibo.getCreateAt();
				if (date != null) {
					String strDate = StringDateUtil.dateToString(date,
							9);
					Link linkDate = new Link(strDate,
							new ExternalResource(getWeiboMainUrl()
									+ userWeibo.getWeiboUid()));
					addComponent(linkDate);
					setComponentAlignment(linkDate,
							Alignment.MIDDLE_LEFT);
					//setExpandRatio(linkDate, 0.2f);
				}
				// 来自
				Label title = new Label("来自");
				title.addStyleName("wb_text");
				//title.addStyleName(ExplorerLayout.STYLE_H3);
				addComponent(title);
				setComponentAlignment(title, Alignment.MIDDLE_LEFT);
				setExpandRatio(title, 0.2f);
				//原url?
				String source = userWeibo.getSource();
				Link link = new Link(source, new ExternalResource(
						getWeiboMainUrl() + source));
				link.setCaption(source);
				addComponent(link);
				setComponentAlignment(link, Alignment.MIDDLE_LEFT);
				setExpandRatio(link, 0.6f);
			}
		};
	}

	public class SocialHeadColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			final SocialUserWeibo userWeibo = BinderHandler.getTableBean(
					source, itemId);
			final GridLayout grid = new GridLayout(3, 1);
			grid.addStyleName(Reindeer.SPLITPANEL_SMALL);
			grid.setMargin(true);
			// grid.setMargin(new MarginInfo(true, false, true, false));
			// 加点空
			grid.setSpacing(true);
			// grid.setSizeFull();
			grid.setSizeUndefined();
			grid.setWidth("800px");
			/*
			 * grid.setWidth("800px"); grid.setHeight("200px");
			 */

			grid.setColumnExpandRatio(0, 0.1f);
			grid.setColumnExpandRatio(1, 0.7f);
			grid.setColumnExpandRatio(2, 0.2f);

			// grid.setSizeUndefined();

			VerticalLayout firstLayout = new VerticalLayout();
			firstLayout.setMargin(true);
			// 头像
			Image image = initUserProfileImage(userWeibo);
			firstLayout.addComponent(image);

			// 粉丝数
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
					Link goToMain = initUserScreenName(userWeibo);
					goToMain.addStyleName("wb_text");
					goToMain.addStyleName("wb_name");
					addComponent(goToMain);

					// 微博内容
					Label descriptionField = new Label();
					initTextLabel(descriptionField);
					descriptionField.setValue(textTranslate(userWeibo.getText()));

					addComponent(descriptionField);

					// 是否有原帖
					if (userWeibo.getRetweetedId() != null) {
						// grid.setHeight("400px");
						final SocialUserWeibo orginWeibo = socialUserWeiboService
								.findSocialUserWeiboById(userWeibo
										.getRetweetedId());
						CustomLayout csslayout = new CustomLayout(
								"forwardWeibo");

						Image image = initRetweetPic(orginWeibo);
						if (image != null) {
							csslayout.addComponent(image, "imgContent");
						}
						// 内容前的@a
						csslayout.addComponent(new HorizontalLayout() {
							/**
							 * 
							 */
							private static final long serialVersionUID = -3960592289157559242L;
							{
								// setSizeUndefined();
								// setMargin(true);
								Link link = initRetweetedUserScreenName(orginWeibo);
								addComponent(link);

								if (orginWeibo.getUserVerified() == 1) {
									Image image = currentImage(null,
											"img/weibo/v.gif", "");
									image.setHeight("10px");
									image.setWidth("11px");
									addComponent(image);
								}
							}
						}, "weiboUrlContent");

						// 原帖内容
						Label l = new Label(
								textTranslate(orginWeibo
										.getText()));
						initOrginTextLable(l);
						csslayout.addComponent(l, "weiboContent");
						
						//下方的评论和转发操作
						csslayout.addComponent(new HorizontalLayout() 
							{

								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;
								{
									setSizeFull();
									setSpacing(true);
									//
									HorizontalLayout currentComeFromCompent = currentComeFromCompent(orginWeibo);
									addComponent(currentComeFromCompent);
									
									//currentComeFromCompent.setHeight("26px");
									//按纽操作
									HorizontalLayout retweetWeboButtonLayOut = getRetweetWeboButtonLayOut(orginWeibo);
									//retweetWeboButtonLayOut.setHeight("26px");
									addComponent(retweetWeboButtonLayOut);
									
								}
	
							}, "forwardOperContent");
						

						//下方的来自组件
						//csslayout.addComponent(currentComeFromCompent(orginWeibo), "comFromContent");

						addComponent(csslayout);
					}// 原帖完

					addComponent(new HorizontalLayout() {
						{
							setSizeFull();
							setSpacing(true);
							// 发帖的日期和来自,贴的图片
							addComponent(getFatieAndTime(userWeibo));

							// 删除操作
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
					setSpacing(true);
					setMargin(true);
					setSizeFull();
					Button addButton = new Button("生成任务");
					addButton.addStyleName(Reindeer.BUTTON_LINK);
					addComponent(addButton);
					setComponentAlignment(addButton, Alignment.BOTTOM_LEFT);
				}
			};
			grid.addComponent(thirdLayout, 2, 0);

			return grid;
		}

	}
}
