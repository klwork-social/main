package com.klwork.explorer.ui.business.social;

import java.util.Date;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.vaadin.alump.scaleimage.ScaleImage;
import org.vaadin.cssinject.CSSInject;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.SocialUserWeiboComment;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.business.query.SocialUserWeiboCommentTableQuery;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public abstract class AbstractCommentDisplayPage extends DetailPanel {
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

	public AbstractCommentDisplayPage(SocialUserAccount socialUserAccount,
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
		// addStyleName("social");
		addStyleName(Reindeer.LAYOUT_WHITE);
		setSpacing(true);
		// Central panel: all task data
		centralLayout = new VerticalLayout();
		centralLayout.setMargin(true);
		setDetailContainer(centralLayout);

		/*
		 * // 增加一个分隔线 addDetailComponent(CommonFieldHandler.getSpacer());
		 */

		Table listTable = new Table();
		listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName("my-no-scroll");
		listTable.setWidth(100, Unit.PERCENTAGE);
		listTable.setHeight(100, Unit.PERCENTAGE);
		centralLayout.addComponent(listTable);
		centralLayout.setExpandRatio(listTable, 1);

		LazyLoadingQuery lazyLoadingQuery = new SocialUserWeiboCommentTableQuery(
				socialUserAccount, weiboType);
		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, 10);
		listTable.setContainerDataSource(listContainer);
		if (lazyLoadingQuery.size() < 10) {
			listTable.setPageLength(listContainer.size());
			listTable.setPageLength(0);
		} else {
			listTable.setPageLength(10);
		}

		listTable
				.addGeneratedColumn("", new SocialCommentHeadColumnGenerator());
		TableHandler.setTableNoHead(listTable);
		listTable.setImmediate(false);
		listTable.setSelectable(false);

		CSSInject css = new CSSInject(UI.getCurrent());
		css.setStyles(".wb_image_css .scale-image{background-repeat: no-repeat; }");
		// css.setStyles(".v-panel-content.v-scrollable {overflow-y:hidden}");
		// css.setStyles(".v-scrollable.v-table-body-wrapper.v-table-body {overflow-y:scroll}");
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
	 * 
	 * @param weiboComment
	 * @return
	 */
	public HorizontalLayout getWeboButtonLayOut(
			final SocialUserWeiboComment weiboComment) {
		return new HorizontalLayout() {
			{
				setSizeFull();
				setHeight("26px");
				setSpacing(true);
				addStyleName("social");

				Button transmitButton = new Button("删除");
				transmitButton.addStyleName(Reindeer.BUTTON_LINK);
				transmitButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						/*
						 * WeiboPopupWindow t = new TransmitPopupWindow(
						 * weiboComment, AbstractCommentDisplayPage.this);
						 * ViewToolManager.showPopupWindow(t);
						 */
					}
				});
				addComponent(transmitButton);
				setExpandRatio(transmitButton, 1.0f);
				setComponentAlignment(transmitButton, Alignment.MIDDLE_RIGHT);

				Button commentButton = new Button("回复");
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

	public void initTextLabel(Label descriptionField) {
		descriptionField.addStyleName("wb_text");
		descriptionField.setContentMode(ContentMode.HTML);
		// descriptionField.setWidth("550px");
	}

	public void initOrginTextLable(Label l) {
		l.addStyleName("wb_text");
		l.setContentMode(ContentMode.HTML);
	}

	/**
	 * 处理用户图像
	 * 
	 * @param weiboComment
	 * @return
	 */
	public Image initUserProfileImage(final SocialUserWeiboComment weiboComment) {
		Image image = currentImage(weiboComment.getUserProfileImageUrl(),
				"img/weibo/head_01.jpg", "/50");
		return image;
	}

	/**
	 * 发帖人连接
	 * 
	 * @param weiboComment
	 * @return
	 */
	public Link initUserScreenName(final SocialUserWeiboComment weiboComment) {
		Link goToMain = new Link(weiboComment.getUserScreenName() + ":",
				new ExternalResource(getWeiboMainUrl()
						+ "n/" + weiboComment.getUserName()));
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
	 * @param weiboComment
	 * @return
	 */
	public HorizontalLayout currentComeFromCompent(
			final SocialUserWeiboComment weiboComment) {
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
				Date date = weiboComment.getCreateAt();
				if (date != null) {
					String strDate = StringDateUtil.dateToString(date, 9);
					Link linkDate = new Link(strDate,
							new ExternalResource(getWeiboMainUrl()
									+ weiboComment.getStatusUserUid()));
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
				String source = weiboComment.getSource();
				source = source.replace("<a", "<a target='blank' ");
				Link link = new Link("ss", new ExternalResource(source));
				Label s = new Label(source, ContentMode.HTML);
				// link.setCaption(source);
				addComponent(s);
				setComponentAlignment(s, Alignment.MIDDLE_LEFT);
				setExpandRatio(s, 0.7f);
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

	/**
	 * 初始化图片cssLayout
	 * 
	 * @param weiboComment
	 * @return
	 */
	public CssLayout initPicLayout(final SocialUserWeiboComment weiboComment) {
		return null;
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

	public class SocialCommentHeadColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			final SocialUserWeiboComment weiboComment = BinderHandler
					.getTableBean(source, itemId);
			final GridLayout grid = new GridLayout(3, 1);
			grid.addStyleName(Reindeer.SPLITPANEL_SMALL);
			grid.setMargin(true);
			grid.setSpacing(true);
			grid.setSizeUndefined();
			grid.setWidth("800px");
			grid.setColumnExpandRatio(0, 0.1f);
			grid.setColumnExpandRatio(1, 0.7f);
			grid.setColumnExpandRatio(2, 0.2f);

			// grid.setSizeUndefined();

			VerticalLayout firstLayout = new VerticalLayout();
			firstLayout.setMargin(true);
			// 头像
			Image image = initUserProfileImage(weiboComment);
			firstLayout.addComponent(image);
			grid.addComponent(firstLayout, 0, 0);

			// 第二档
			VerticalLayout secondLayout = new VerticalLayout() {
				{
					setSpacing(true);
					setMargin(true);

					CustomLayout cusLayout = new CustomLayout("weiboComment");
					// 内容前的@a
					cusLayout.addComponent(new HorizontalLayout() {
						/**
							 * 
							 */
						private static final long serialVersionUID = -3960592289157559242L;
						{
							// setSizeUndefined();
							// setMargin(true);
							Link link = initUserScreenName(weiboComment);
							link.addStyleName("wb_text");
							addComponent(link);

							if (weiboComment.getUserVerified() == 1) {
								Image image = currentImage(null,
										"img/weibo/v.gif", "");
								image.setHeight("10px");
								image.setWidth("11px");
								addComponent(image);
							}
						}
					}, "weiboUrlContent");

					// 发贴内容
					Label l = new Label(textTranslate(weiboComment.getText()));
					initOrginTextLable(l);
					cusLayout.addComponent(l, "weiboContent");

					String statusText = weiboComment.getStatusText();
					if (statusText.length() > 18) {
						statusText = statusText.substring(0, 18);
					}
					String url = "http://api.t.sina.com.cn/"
							+ weiboComment.getStatusUserUid() + "/statuses/"
							+ weiboComment.getStatusWeiboId();
					/*
					 * Link link = new Link(statusText, new ExternalResource(
					 * getWeiboMainUrl() + weiboComment .getStatusWeiboId()));
					 */
					
					Link link = new Link(statusText, new ExternalResource(url));
					//link.addStyleName("wb_text");
					//link.addStyleName("wb_name");
					link.setTargetName("_blank");
					cusLayout.addComponent(link, "relWeiboContent");

					// 下方操作
					cusLayout.addComponent(new HorizontalLayout() {

						/**
								 * 
								 */
						private static final long serialVersionUID = 1L;
						{
							setSizeFull();
							setSpacing(true);
							//
							HorizontalLayout currentComeFromCompent = currentComeFromCompent(weiboComment);
							addComponent(currentComeFromCompent);

							// currentComeFromCompent.setHeight("26px");
							// 按纽操作
							HorizontalLayout retweetWeboButtonLayOut = getWeboButtonLayOut(weiboComment);
							// retweetWeboButtonLayOut.setHeight("26px");
							addComponent(retweetWeboButtonLayOut);

						}

					}, "forwardOperContent");

					// 下方的来自组件
					// csslayout.addComponent(currentComeFromCompent(orginWeibo),
					// "comFromContent");

					addComponent(cusLayout);
				}// 被转发贴完

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
