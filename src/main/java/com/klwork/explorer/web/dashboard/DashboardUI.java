/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.klwork.explorer.web.dashboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.MyDefaultErrorHandler;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoggedInUser;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.security.ShiroSecurityNavigator;
import com.klwork.explorer.ui.main.views.DashboardView;
import com.klwork.explorer.ui.main.views.PushDataResult;
import com.klwork.explorer.ui.main.views.PushUpdateInterface;
import com.klwork.explorer.ui.main.views.TasksView;
import com.klwork.explorer.ui.main.views.TodoListView;
import com.klwork.explorer.ui.user.ChangePasswordPopupWindow;
import com.klwork.explorer.ui.user.ProfilePopupWindow;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.Transferable;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Component
@Scope("prototype")
@Theme("dashboard")
@Title("KLWORK Dashboard")
@PreserveOnRefresh
@Push
public class DashboardUI extends UI implements ErrorHandler{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
    private static final long serialVersionUID = 1L;
    @Autowired
    protected I18nManager i18nManager;
    
    CssLayout rootLayout = new CssLayout();
    private LoggedInUser loggedInUser;

    CssLayout menuLayout = new CssLayout();
    CssLayout contentLayout = new CssLayout();
    
    //此UI当前打开的视图
    private View currentView; 
   HashMap<String, String> routes = new HashMap<String, String>() {
        {
            put(DashboardView.NAME, "dashboardView");
            put(TasksView.NAME, "tasksView");
        }
    };

    HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();

    private ShiroSecurityNavigator nav;

    private HelpManager helpManager;

    @Override
    protected void init(VaadinRequest request) {
    	loggedInUser = LoginHandler.querySecurityUser();
    	 VaadinSession.getCurrent().setErrorHandler(this);
        helpManager = new HelpManager(this);
        
        setLocale(Locale.CHINESE);

        setContent(rootLayout);
        rootLayout.addStyleName("root");
        rootLayout.setSizeFull();

        // Unfortunate to use an actual widget here, but since CSS generated
        // elements can't be transitioned yet, we must
        Label bg = new Label();
        bg.setSizeUndefined();
        bg.addStyleName("login-bg");
        rootLayout.addComponent(bg);

       // buildLoginView(false);
        buildMainView();
        //进行push线程的初始化
        new PushThread().start();
    }

    private void buildMainView() {
    	final LoggedInUser user = LoginHandler.getLoggedInUser();
        nav = new ShiroSecurityNavigator(this, contentLayout);

        helpManager.closeAll();
        removeStyleName("login");
        //root.removeComponent(loginLayout);

        rootLayout.addComponent(new HorizontalLayout() {
            {
                setSizeFull();
                addStyleName("main-view");
                addComponent(new VerticalLayout() {
                    // Sidebar
                    {
                        addStyleName("sidebar");//侧边难
                        setWidth(null);
                        setHeight("100%");

                        // Branding element
                        addComponent(new CssLayout() {
                            {
                                addStyleName("branding");//商标
                                Label logo = new Label(
                                        "<span>KLWORK</span>",
                                        ContentMode.HTML);
                                logo.setSizeUndefined();
                                addComponent(logo);
                                // addComponent(new Image(null, new
                                // ThemeResource(
                                // "img/branding.png")));
                            }
                        });

                        // Main menu
                        addComponent(menuLayout);
                        setExpandRatio(menuLayout, 1);

                        // User menu
                        addComponent(new VerticalLayout() {
                            {
                                setSizeUndefined();
                                addStyleName("user");
                                initProfileButton(user,this);
                            }
                        });
                    }
                });
                // Content
                addComponent(contentLayout);
                contentLayout.setSizeFull();
                contentLayout.addStyleName("view-content");
                setExpandRatio(contentLayout, 1);
            }

        });

        menuLayout.removeAllComponents();
      
        
        initViewButton();
        
    
        menuLayout.addStyleName("menu");
        menuLayout.setHeight("100%");

        viewNameToMenuButton.get("/dashboard").setHtmlContentAllowed(true);
        viewNameToMenuButton.get("/dashboard").setCaption(
                "Dashboard<span class=\"badge\">2</span>");

        String f = queryViewName();
        if (f == null || f.equals("") || f.equals("/")) {
            nav.navigateTo("/dashboard");
            menuLayout.getComponent(0).addStyleName("selected");
            helpManager.showHelpFor(DashboardView.class);
        } else {
            nav.navigateTo(f);
            //helpManager.showHelpFor(routes.get(f));
            viewNameToMenuButton.get(f).addStyleName("selected");
        }

        nav.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                helpManager.closeAll();
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                View newView = event.getNewView();
                helpManager.showHelpFor(newView);
                if (autoCreateReport && newView instanceof TodoListView) {
                    //((ReportsView) newView).autoCreate(2, items, transactions);
                }
                autoCreateReport = false;
            }
        });

    }
    
    /**
     * 得到视图的名称
     * @return
     */
	public String queryViewName() {
		String f = Page.getCurrent().getUriFragment();
        if (f != null && f.startsWith("!")) {
            f = f.substring(1);
        }
		return f;
	}

	public void initViewButton() {
		String view = "dashboard";
		//每个按钮可以有一个图片
        Button b = createViewButton(view,i18nManager.getMessage(Messages.MAIN_MENU_PUBLIC_TASK));
        menuLayout.addComponent(b);
        viewNameToMenuButton.put("/" + view, b);
        
        view = "tasks";
        b = createViewButton(view,i18nManager.getMessage(Messages.MAIN_MENU_TASKS));
        menuLayout.addComponent(b);
        viewNameToMenuButton.put("/" + view, b);
        
        
        view = "todolist";
        b = createViewButton(view,i18nManager.getMessage(Messages.MAIN_MENU_TODOLIST));
        menuLayout.addComponent(b);
        viewNameToMenuButton.put("/" + view, b);
        
        view = "team";
        b = createViewButton(view,i18nManager.getMessage(Messages.TEAM_GROUP_TABLE_TITLE));
        menuLayout.addComponent(b);
        viewNameToMenuButton.put("/" + view, b);
        
        view = "outProject";
        b = createViewButton(view,i18nManager.getMessage(Messages.MAIN_MENU_MY_PROJECT));
        menuLayout.addComponent(b);
        viewNameToMenuButton.put("/" + view, b);
        
        view = "social";
        b = createViewButton(view,i18nManager.getMessage(Messages.SOCIAL_MAIN_MENU));
        menuLayout.addComponent(b);
        viewNameToMenuButton.put("/" + view, b);
        
        view = "newPublicProject";
        b = createViewButton(view,"公共项目");
        menuLayout.addComponent(b);
        viewNameToMenuButton.put("/" + view, b);
        
	}

	public Button createViewButton(final String view,String caption) {
		Button b = new NativeButton(caption);
		b.addStyleName("icon-" + view);
		b.addClickListener(new ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
		        clearMenuSelection();
		        event.getButton().addStyleName("selected");
		       
		        if (nav.getState().equals("/" + view))
		            nav.navigateTo(nav.getState().substring(1));
		        else 
		        	nav.navigateTo(view);
		    }
		});
		return b;
	}

    private Transferable items;

    private void clearMenuSelection() {
        for (Iterator<com.vaadin.ui.Component> it = menuLayout.getComponentIterator(); it.hasNext();) {
        	com.vaadin.ui.Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                // Wow, this is ugly (even uglier than the rest of the code)
                ((DragAndDropWrapper) next).iterator().next()
                        .removeStyleName("selected");
            }
        }
    }

   public void updateReportsButtonBadge(String badgeCount) {
        viewNameToMenuButton.get("/reports").setHtmlContentAllowed(true);
        viewNameToMenuButton.get("/reports").setCaption(
                "Reports<span class=\"badge\">" + badgeCount + "</span>");
    }

    public void clearDashboardButtonBadge() {
        viewNameToMenuButton.get("/dashboard").setCaption("Dashboard");
    }

    boolean autoCreateReport = false;
    Table transactions;

    public void openReports(Table t) {
        transactions = t;
        autoCreateReport = true;
        nav.navigateTo("/reports");
        clearMenuSelection();
        viewNameToMenuButton.get("/reports").addStyleName("selected");
    }

    public HelpManager getHelpManager() {
        return helpManager;
    }
    @Override
    public void error(com.vaadin.server.ErrorEvent event)
    {
        // connector event
        if (event.getThrowable().getCause() instanceof IllegalArgumentException)
        {
            IllegalArgumentException exception = (IllegalArgumentException) event.getThrowable().getCause();
            Notification.show(exception.getMessage(), Notification.Type.ERROR_MESSAGE);

            // Cleanup view. Now Vaadin ignores errors and always shows the view.  :-(
            // since beta10
            //setContent(null);
            return;
        }

        // Error on page load. Now it doesn't work. User sees standard error page.
        if (event.getThrowable() instanceof IllegalArgumentException)
        {
            IllegalArgumentException exception = (IllegalArgumentException) event.getThrowable();

            Label label = new Label(exception.getMessage());
            label.setWidth(-1, Unit.PERCENTAGE);

            Link goToMain = new Link("Go to main", new ExternalResource("/"));

            VerticalLayout layout = new VerticalLayout();
            layout.addComponent(label);
            layout.addComponent(goToMain);
            layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
            layout.setComponentAlignment(goToMain, Alignment.MIDDLE_CENTER);

            VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.setSizeFull();
            mainLayout.addComponent(layout);
            mainLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

            setContent(mainLayout);
            Notification.show(exception.getMessage(), Notification.Type.ERROR_MESSAGE);
            return;
        }
        MyDefaultErrorHandler.doDefault(event);
    }

	public void initProfileButton(final LoggedInUser user,VerticalLayout layout) {
		Image profilePic = new Image(
		        null,
		        new ThemeResource("img/profile-pic.png"));
		profilePic.setWidth("34px");
		layout.addComponent(profilePic);
		Label userName = new Label(user.getFirstName()
		        + " "
		        + user.getLastName());
		userName.setSizeUndefined();
		layout.addComponent(userName);

		Command cmd = new Command() {
		    @Override
		    public void menuSelected(
		            MenuItem selectedItem) {
		        Notification
		                .show("Not implemented in this demo");
		    }
		};
		MenuBar settings = new MenuBar();
		MenuItem settingsMenu = settings.addItem("",
		        null);
		settingsMenu.setStyleName("icon-cog");
		
		
		settingsMenu.addItem(i18nManager.getMessage(Messages.PROFILE_SHOW),
				new Command() {
					public void menuSelected(MenuItem selectedItem) {
						ViewToolManager
								.showPopupWindow(new ProfilePopupWindow(
										user.getId()));
					}
				});
		//settingsMenu.addItem("Settings", cmd);
		settingsMenu.addItem("账户管理", new Command() {

			public void menuSelected(MenuItem selectedItem) {
				ViewToolManager.showPopupWindow(new ProfilePopupWindow(user
						.getId()));
			}
		});

		// Change password
		settingsMenu.addItem(i18nManager.getMessage(Messages.PASSWORD_CHANGE),
				new Command() {
					public void menuSelected(MenuItem selectedItem) {
						ViewToolManager
								.showPopupWindow(new ChangePasswordPopupWindow());
					}
				});
		settingsMenu.addSeparator();
		// 邀请好友
		settingsMenu.addItem(i18nManager.getMessage(Messages.EXPAND_MENU_INVITE), new Command() {
					public void menuSelected(MenuItem selectedItem) {
						// xx.close();
						// ViewToolManager.logout();
					}
				});
		
		//我的日程
		settingsMenu.addItem(i18nManager.getMessage(Messages.EXPAND_MENU_MY_SCHEDULE), new Command() {
			public void menuSelected(MenuItem selectedItem) {
				// nav.navigateTo("/dashboard");
			}
		});
				
		layout.addComponent(settings);

		Button exit = new NativeButton("Exit");
		exit.addStyleName("icon-cancel");
		exit.setDescription("Sign Out");
		layout.addComponent(exit);
		exit.addClickListener(new ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
		    	ViewToolManager.logout();
		    }
		});
	}
	
	class PushThread extends Thread {
		int count = 0;
		public PushThread() {
		}
		
		@Override
		public void run() {
			try {
				// Update the data for a while
				while (true) {
					Thread.sleep(10000);
					 final View cView = getCurrentView();
					 PushDataResult r = null;
					 if( cView !=null && cView instanceof PushUpdateInterface){
						 PushUpdateInterface in = ((PushUpdateInterface)cView);
						 logger.debug("queryFactViewName:" + queryViewName() + "-----" + in.getViewName());
						 if(!checkQueryView(in)){//不是当前视图
							 logger.debug("不是当前视图");
								 continue;
						 }
						  r = in.getPushData();
						  //数据是最新的，不需要更新
						  if(!r.isNeedUpdate()){
								 continue;
						   }
					 }else {
						 continue;
					 }
					 
					//WW_TODO push提醒
					UI.getCurrent().access(new Runnable() {
						@Override
						public void run() {
							 if( cView !=null && cView instanceof PushUpdateInterface){
								 ((PushUpdateInterface)cView).reflashUIByPush();
							 }
						}
					});
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public boolean checkQueryView(PushUpdateInterface in) {
			if(queryViewName().indexOf(in.getViewName()) == 0){
				return true;
			}
			return false;
		}
	}

	public View getCurrentView() {
		return currentView;
	}

	public void setCurrentView(View currentView) {
		this.currentView = currentView;
	}

	public LoggedInUser getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(LoggedInUser loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	
	
}
