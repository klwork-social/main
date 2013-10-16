package com.klwork.explorer.ui.business.social;

import java.util.Date;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.vaadin.alump.scaleimage.ScaleImage;
import org.vaadin.cssinject.CSSInject;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.business.query.SocialUserWeiboTableQuery;
import com.klwork.explorer.ui.custom.ConfirmationDialogPopupWindow;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.event.ConfirmationEvent;
import com.klwork.explorer.ui.event.ConfirmationEventListener;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
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

	protected AbstractSocialService socialService;

	protected VerticalLayout centralLayout;

	// 见数据类型为6的数据字典
	int weiboType = 0;

	SocialUserAccount socialUserAccount;
	
	
	public AbstractWeiboDisplayPage(SocialUserAccount socialUserAccount,
			int type) {
		super(true);
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		this.socialUserWeiboService = ViewToolManager
				.getBean("socialUserWeiboService");
		this.socialService = AbstractSocialService
				.querySocialClass(getSocialType());
		this.socialUserAccount = socialUserAccount;
		this.weiboType = type;
	}

	abstract public String getSocialType();


	@Override
	protected void initUI() {
		setSizeFull();
		//addStyleName("social");
		addStyleName(Reindeer.LAYOUT_WHITE);
		setSpacing(true);
		// Central panel: all task data
		centralLayout = new VerticalLayout();
		centralLayout.setMargin(true);
		setDetailContainer(centralLayout);
		 getMainPanel().setScrollTop(15);
		 
		/*Button scrollButton = new Button("scroll");
		scrollButton.addClickListener(new Button.ClickListener() {
		    public void buttonClick(ClickEvent event) {
		        // Print the current page
		        JavaScript.getCurrent().execute("VSA_initScrollbars();");
		    }
		});
		centralLayout.addComponent(scrollButton);*/

		/*
		 * // 增加一个分隔线 addDetailComponent(CommonFieldHandler.getSpacer());
		 */

		Table listTable = new Table();
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName("my-no-scroll");
		listTable.setWidth(100, Unit.PERCENTAGE);
		listTable.setHeight(100, Unit.PERCENTAGE);
		centralLayout.addComponent(listTable);
		centralLayout.setExpandRatio(listTable, 1);

		LazyLoadingQuery lazyLoadingQuery = new SocialUserWeiboTableQuery(
				socialUserAccount, weiboType);
		int batchSize = 20;
		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, batchSize);
		listTable.setContainerDataSource(listContainer);
		if (lazyLoadingQuery.size() < batchSize) {
			listTable.setPageLength(0);
		} else {
			listTable.setPageLength(batchSize);
		}

		listTable.addGeneratedColumn("", new SocialHeadColumnGenerator());
		TableHandler.setTableNoHead(listTable);
		listTable.setImmediate(false);
		listTable.setSelectable(false);
		//CSSInject css = new CSSInject(UI.getCurrent());
		//JavaScript.getCurrent().execute(script)
		//css.setStyles(".wb_image_css .scale-image{background-repeat: no-repeat; }");
		//css.setStyles(".v-panel-content.v-scrollable {overflow-y:hidden}");
		//css.setStyles(".v-scrollable.v-table-body-wrapper.v-table-body {overflow-y:scroll}");
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
	
	/**
	 * 微博文字转化
	 * @param text
	 * @return
	 */
	protected abstract String textTranslate(String text);

	/**
	 * 微博操作
	 * 
	 * @param orginWeibo
	 *            被转发贴
	 * @param userWeibo
	 *            当期贴
	 * @return
	 */
	public HorizontalLayout getWeboButtonLayOut(final SocialUserWeibo userWeibo) {
		return new HorizontalLayout() {

			{
				setSizeFull();
				setSpacing(true);
				addStyleName("social");
				boolean hasDeleteButton = false;
				if (weiboType == DictDef.dictInt("weibo_user_timeline")) {// 删除操作
					Button deletButton = initDeleteButton(userWeibo);
					addComponent(deletButton);
					setExpandRatio(deletButton, 1.0f);
					setComponentAlignment(deletButton, Alignment.BOTTOM_RIGHT);
					hasDeleteButton = true;
				}

				String repostsCount = userWeibo.getRepostsCount() + "";
				String commentsCount = userWeibo.getCommentsCount() + "";

				Button transmitButton = new Button("转发(" + repostsCount + ")");
				transmitButton.addStyleName(Reindeer.BUTTON_LINK);
				transmitButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						// JavaScript.getCurrent().execute("alert('Hello')");
						WeiboPopupWindow t = new TransmitPopupWindow(
								userWeibo, AbstractWeiboDisplayPage.this);
						ViewToolManager.showPopupWindow(t);
					}
				});
				addComponent(transmitButton);
				setComponentAlignment(transmitButton, Alignment.BOTTOM_RIGHT);
				if (!hasDeleteButton) {
					setExpandRatio(transmitButton, 1f);
				}

				Button commentButton = new Button("评论(" + commentsCount + ")");
				commentButton.addStyleName(Reindeer.BUTTON_LINK);
				commentButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						DiscussPopupWindow t = new DiscussPopupWindow(
								userWeibo, AbstractWeiboDisplayPage.this);
						ViewToolManager.showPopupWindow(t);
					}
				});
				addComponent(commentButton);
				setComponentAlignment(commentButton, Alignment.BOTTOM_RIGHT);
			}
		};
	}

	/**
	 * 被转发贴按钮
	 * 
	 * @param orginWeibo
	 * @return
	 */
	public HorizontalLayout getRetweetWeboButtonLayOut(
			final SocialUserWeibo orginWeibo) {
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
						WeiboPopupWindow t = new TransmitPopupWindow(
								orginWeibo, AbstractWeiboDisplayPage.this);
						ViewToolManager.showPopupWindow(t);
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
	 * 
	 * @param userWeibo
	 * @return
	 */
	public VerticalLayout getFatieAndTime(final SocialUserWeibo userWeibo) {
		return new VerticalLayout() {
			{
				setSizeFull();

				// 增加一个图片的容器
				CssLayout picLayout = initPicLayout(userWeibo);
				if (picLayout != null) {
					addComponent(picLayout);
				}
				/*
				 * Image image = initOriginalPic(userWeibo); if (image != null)
				 * { addComponent(image); }
				 */

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
	 * 
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
	 * 
	 * @param userWeibo
	 * @return
	 */
	public Link initUserScreenName(final SocialUserWeibo userWeibo) {
		Link goToMain = new Link(userWeibo.getUserScreenName() + ":",
				new ExternalResource(getWeiboMainUrl()
						+ userWeibo.getWeiboUid()));
		goToMain.setTargetName("_blank");
		return goToMain;
	}

	/**
	 * 处理微博被转发贴图
	 * 
	 * @param orginWeibo
	 * @return
	 */
	public Image initRetweetPic(final SocialUserWeibo orginWeibo) {
		// 被转发贴图
		Image image = currentImage(orginWeibo.getThumbnailPic(), null, "");// 小图
		return image;
	}

	public String getWeiboMainUrl() {
		return "http://weibo.com/";
	}

	/**
	 * 处理微博的图片
	 * 
	 * @param userWeibo
	 * @return
	 */
	abstract public Image initOriginalPic(final SocialUserWeibo userWeibo);

	/**
	 * 被转发贴的发帖人链接
	 * 
	 * @param orginWeibo
	 * @return
	 */
	public Link initRetweetedUserScreenName(final SocialUserWeibo orginWeibo) {
		Link link = new Link("@" + orginWeibo.getUserScreenName(),
				new ExternalResource(getWeiboMainUrl()
						+ orginWeibo.getWeiboId()));
		link.setTargetName("_blank");
		return link;
	}

	/**
	 * 来自组件
	 * 
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
					String strDate = StringDateUtil.dateToString(date, 9);
					Link linkDate = new Link(strDate, new ExternalResource(
							getWeiboMainUrl() + userWeibo.getWeiboUid()));
					addComponent(linkDate);
					setComponentAlignment(linkDate, Alignment.MIDDLE_LEFT);
					// setExpandRatio(linkDate, 0.2f);
				}
				// 来自
				Label title = new Label("来自");
				title.addStyleName("wb_text");
				// title.addStyleName(ExplorerLayout.STYLE_H3);
				addComponent(title);
				setComponentAlignment(title, Alignment.MIDDLE_LEFT);
				setExpandRatio(title, 0.2f);
				// 原url?
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

	public Button initDeleteButton(final SocialUserWeibo userWeibo) {
		Button deletButton = new Button("删除");
		deletButton.addStyleName(Reindeer.BUTTON_LINK);
		deletButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				ConfirmationDialogPopupWindow confirmationPopup = new ConfirmationDialogPopupWindow(
						"您确定要删除本条微博?");
				confirmationPopup.addListener(new ConfirmationEventListener() {
					protected void rejected(ConfirmationEvent event) {
					}

					protected void confirmed(ConfirmationEvent event) {
						socialService.deleteWeibo(userWeibo);
					}

				});
				ViewToolManager.showPopupWindow(confirmationPopup);
			}
		});
		return deletButton;
	}

	public CssLayout initOriginalPicLayout2(final SocialUserWeibo userWeibo) {
		String origPic = userWeibo.getThumbnailPic();
		if (!StringTool.judgeBlank(origPic)) {
			return null;
		}

		final CssLayout tile = new CssLayout();
		tile.setStyleName("wb_image_css");
		tile.setWidth(120, Unit.PIXELS);
		tile.setHeight(120, Unit.PIXELS);

		final ScaleImage tileImage = new ScaleImage();
		tile.addLayoutClickListener(new LayoutClickListener() {
			private boolean big = true;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				int size = 120;
				if (big) {
					size = 500;
				}

				tile.setWidth(size, Unit.PIXELS);
				tile.setHeight(size, Unit.PIXELS);
				// tileImage.setWidth(size, Unit.PIXELS);
				// tileImage.setHeight(size, Unit.PIXELS);
				if (big) {
					tileImage.setSource(new ExternalResource(userWeibo
							.getOriginalPic() + ""));

				} else {
					tileImage.setSource(new ExternalResource(userWeibo
							.getThumbnailPic() + ""));
				}
				big = !big;
				// tileImage.setSource(new
				// ExternalResource(userWeibo.getOriginalPic() + ""));
			}
		});

		if (StringTool.judgeBlank(origPic)) {
			tileImage.setSource(new ExternalResource(origPic + ""));
			tileImage.setStyleName("tile-image");
			tile.addComponent(tileImage);
		}
		return tile;
	}

	/**
	 * 初始化图片cssLayout
	 * 
	 * @param retweetWeibo
	 * @return
	 */
	public CssLayout initPicLayout(final SocialUserWeibo retweetWeibo) {
		String smallPic = retweetWeibo.getThumbnailPic();
		String bigPic = retweetWeibo.getOriginalPic();
		if (!StringTool.judgeBlank(smallPic)) {
			return null;
		}
		int bigWidth = 100;
		int bigHeight = 100;
		int smallWidth = 100;
		int smallHeight = 100;
		String smallPicSize = retweetWeibo.getThumbnailPicSize();
		String bigPicSize = retweetWeibo.getOriginalPicSize();
		final CssLayout cssLayout = new CssLayout();
		cssLayout.setStyleName("wb_image_css");
		// tile.setWidth(100, Unit.PIXELS);
		// tile.setHeight(100, Unit.PIXELS);
		if (StringTool.judgeBlank(smallPicSize)) {// 初始化是显示小图
			String width = smallPicSize.split(",")[0];

			String height = smallPicSize.split(",")[1];
			smallWidth = Integer.parseInt(width);
			if (smallWidth < 100) {
				smallWidth = 100;
			}
			smallHeight = Integer.parseInt(height);
			if (smallHeight < 100) {
				smallHeight = 100;
			}
		}

		if (StringTool.judgeBlank(bigPicSize)) {//
			String width = bigPicSize.split(",")[0];
			String height = bigPicSize.split(",")[1];
			bigWidth = Integer.parseInt(width);
			bigHeight = Integer.parseInt(height);
			if (bigWidth > 600) {
				bigWidth = 600;
			}
			if (bigHeight > 800) {
				bigHeight = 800;
			}
		}

		cssLayout.setWidth(smallWidth, Unit.PIXELS);//默认设置为最小的
		cssLayout.setHeight(smallHeight, Unit.PIXELS);

		ScaleImage tileImage = new ScaleImage();
		tileImage.setStyleName("tile-image");
		tileImage.setSource(new ExternalResource(smallPic + ""));
		cssLayout.addComponent(tileImage);
		cssLayout.addLayoutClickListener(new MyLayoutClickListener(cssLayout,
				smallPic, smallWidth, smallHeight, bigPic, bigWidth, bigHeight,
				tileImage));

		return cssLayout;
	}

	private final class MyLayoutClickListener implements LayoutClickListener {
		private final CssLayout cssLayout;
		private final String smallPic;
		private final String bigPic;
		private final ScaleImage tileImage;
		private boolean big = true;
		private int smallWidth, smallHeight, bigHeight, bigWidth;

		public MyLayoutClickListener(CssLayout cssLayout, String smallPic,
				int smallWidth, int smallHeight, String bigPic, int bigWidth,
				int bigHeight, ScaleImage tileImage) {
			this.cssLayout = cssLayout;
			this.smallPic = smallPic;
			this.bigPic = bigPic;
			this.tileImage = tileImage;
			this.smallHeight = smallHeight;
			this.smallWidth = smallWidth;
			this.bigHeight = bigHeight;
			this.bigWidth = bigWidth;
		}

		@Override
		public void layoutClick(LayoutClickEvent event) {
			if (big) {
				cssLayout.setWidth(bigWidth, Unit.PIXELS);
				cssLayout.setHeight(bigHeight, Unit.PIXELS);
				tileImage.setSource(new ExternalResource(bigPic + ""));
			} else {
				cssLayout.setWidth(smallWidth, Unit.PIXELS);
				cssLayout.setHeight(smallHeight, Unit.PIXELS);
				tileImage.setSource(new ExternalResource(smallPic + ""));
			}
			big = !big;
		}
	}

	public class SocialHeadColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			// 当前贴
			final SocialUserWeibo userWeibo = BinderHandler.getTableBean(
					source, itemId);
			// 被转发贴对象
			// final SocialUserWeibo orginWeibo;
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
					descriptionField
							.setValue(textTranslate(userWeibo.getText()));

					addComponent(descriptionField);

					// 是否有被转发贴
					if (userWeibo.getRetweetedId() != null) {
						// grid.setHeight("400px");
						final SocialUserWeibo orginWeibo = socialUserWeiboService
								.findSocialUserWeiboById(userWeibo
										.getRetweetedId());
						CustomLayout csslayout = new CustomLayout(
								"forwardWeibo");

						// Image image = initRetweetPic(orginWeibo);
						CssLayout imageCssLayout = initPicLayout(orginWeibo);
						if (imageCssLayout != null) {
							csslayout
									.addComponent(imageCssLayout, "imgContent");
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

						// 被转发贴内容
						Label l = new Label(textTranslate(orginWeibo.getText()));
						initOrginTextLable(l);
						csslayout.addComponent(l, "weiboContent");

						// 下方的评论和转发操作
						csslayout.addComponent(new HorizontalLayout() {

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

								// currentComeFromCompent.setHeight("26px");
								// 按纽操作
								HorizontalLayout retweetWeboButtonLayOut = getRetweetWeboButtonLayOut(orginWeibo);
								// retweetWeboButtonLayOut.setHeight("26px");
								addComponent(retweetWeboButtonLayOut);

							}

						}, "forwardOperContent");

						// 下方的来自组件
						// csslayout.addComponent(currentComeFromCompent(orginWeibo),
						// "comFromContent");

						addComponent(csslayout);
					}// 被转发贴完

					addComponent(new HorizontalLayout() {
						{
							setSizeFull();
							setSpacing(true);
							// 发帖的日期和来自,贴的图片
							addComponent(getFatieAndTime(userWeibo));

							// 删除/转发/评论操作
							HorizontalLayout weboButtonLayOut = getWeboButtonLayOut(userWeibo);
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
					MenuBar profileMenu = new MenuBar();
					profileMenu.addStyleName(ExplorerLayout.STYLE_HEADER_PROFILE_BOX);
					MenuItem rootItem = profileMenu.addItem("操作",Images.BULLET_2, null);
					rootItem.setDescription("展开操作项");
					rootItem.setStyleName(ExplorerLayout.STYLE_HEADER_PROFILE_MENU);
					
					rootItem.addItem("生成任务", new Command() {
						public void menuSelected(MenuItem selectedItem) {
						}
					});
					
					rootItem.addItem("保存到笔记本", new Command() {
						public void menuSelected(MenuItem selectedItem) {
							WeiboPopupWindow t = new SaveToNotePopupWindow(
									userWeibo, socialUserAccount, AbstractWeiboDisplayPage.this);
							ViewToolManager.showPopupWindow(t);
						}
					});
					addComponent(profileMenu);
					setComponentAlignment(profileMenu, Alignment.TOP_CENTER);
					
					/*Button addButton = new Button("生成任务");
					addButton.addStyleName(Reindeer.BUTTON_LINK);
					addComponent(addButton);
					setComponentAlignment(addButton, Alignment.BOTTOM_LEFT);
					setExpandRatio(addButton, 1);
					
					Button weiboSaveToNote = new Button("保存到笔记本");
					weiboSaveToNote.addStyleName(Reindeer.BUTTON_LINK);
					addComponent(weiboSaveToNote);
					
					weiboSaveToNote.addClickListener(new ClickListener() {
						public void buttonClick(ClickEvent event) {
							WeiboPopupWindow t = new SaveToNotePopupWindow(
									userWeibo, socialUserAccount, AbstractWeiboDisplayPage.this);
							ViewToolManager.showPopupWindow(t);
						}
					});
					
					setComponentAlignment(weiboSaveToNote, Alignment.BOTTOM_LEFT);*/
				}
			};
			grid.addComponent(thirdLayout, 2, 0);

			return grid;
		}

	}
}
