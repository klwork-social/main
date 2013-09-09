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

package com.klwork.explorer.ui.custom;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoggedInUser;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.util.ThemeImageColumnGenerator;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * A popup window that is used to select people. Two possible modes: -
 * multiselect: displays two tables that allow to select users from the left
 * table to the table on the right - non-multiselect: one table where only one
 * user can be chosen from.
 * 
 * {@link SubmitEventListener} can be attached to listen to completion of the
 * selection. The selected user(s) can be retrieved using
 * {@link #getSelectedUserId()} ,{@link #getSelectedUserIds()} and
 * {@link #getSelectedUserRole(String)}.
 * 
 * @author Joram Barrez
 */
public class SelectUsersPopupWindow extends PopupWindow {

	private static final long serialVersionUID = 2288670048774518133L;
	protected String title;
	protected boolean multiSelect = true;
	protected boolean showRoles = true;
	protected Collection<String> ignoredUserIds;

	protected I18nManager i18nManager;

	protected VerticalLayout windowLayout;
	protected TextField searchField;
	protected HorizontalLayout userSelectionLayout;
	protected Table matchingUsersTable;
	protected Button selectUserButton;
	protected Table selectedUsersTable;
	protected Button doneButton;
	
	private int type = 0;//0只查询本组用户，1查询所有用户

	public SelectUsersPopupWindow(String title, boolean multiSelect) {
		this.title = title;
		this.multiSelect = multiSelect;
		this.i18nManager = ViewToolManager.getI18nManager();
	}

	public SelectUsersPopupWindow(String title, boolean multiSelect,
			Collection<String> ignoredUserIds) {
		this(title, multiSelect);
		this.ignoredUserIds = ignoredUserIds;
	}

	public SelectUsersPopupWindow(String title, boolean multiSelect,
			boolean showRoles, Collection<String> ignoredUserIds) {
		this(title, multiSelect);
		this.showRoles = showRoles;
		this.ignoredUserIds = ignoredUserIds;
	}

	@Override
	public void attach() {
		super.attach();
		initUi();
	}

	protected void initUi() {
		// WW_TODO 用户选择弹出对方框
		setCaption(title);
		setModal(true);
		addStyleName(Reindeer.WINDOW_LIGHT);
		windowLayout = new VerticalLayout();
		//每个元素加一个7px的隐藏元素
		windowLayout.setSpacing(true);
		//padding-top: 18px;,margin设置的是内边距
		windowLayout.setMargin(true);
		setContent(windowLayout);
		center();
		if (multiSelect && showRoles) {
			setWidth(820, Unit.PIXELS);
		} else if (multiSelect && !showRoles) {
			setWidth(685, Unit.PIXELS);
		} else {
			setWidth(340, Unit.PIXELS);
		}
		setHeight(350, Unit.PIXELS);
		// 选择我自己按钮/搜索
		initSearchField();
		//
		initUserSelection();
		// done按钮
		initDoneButton();
		
		initEnd();
	}

	protected void initEnd() {
		
	}
	
	protected void initSearchField() {
		HorizontalLayout searchLayout = new HorizontalLayout();
		searchLayout.setSpacing(true);
		windowLayout.addComponent(searchLayout);

		// textfield
		searchField = new TextField();
		searchField.setInputPrompt(i18nManager
				.getMessage(Messages.PEOPLE_SEARCH));
		searchField.setWidth(180, Unit.PIXELS);
		//searchField.focus();
		searchLayout.addComponent(searchField);

		// Logic to change table according to input
		searchField.addTextChangeListener(new TextChangeListener() {
			public void textChange(TextChangeEvent event) {
				searchPeople(event.getText());
			}
		});
		// 选择我自己按钮
		initSelectMyselfButton(searchLayout);
	}

	protected void initSelectMyselfButton(HorizontalLayout searchLayout) {
		final LoggedInUser loggedInUser = LoginHandler.getLoggedInUser();
		if (ignoredUserIds == null
				|| !ignoredUserIds.contains(loggedInUser.getId())) {
			Button meButton = new Button(
					i18nManager.getMessage(Messages.PEOPLE_SELECT_MYSELF));
			meButton.setIcon(Images.USER_16);
			searchLayout.addComponent(meButton);
			searchLayout.setComponentAlignment(meButton, Alignment.MIDDLE_LEFT);

			if (multiSelect) {
				meButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						selectUser(loggedInUser.getId(),
								loggedInUser.getFullName());
					}
				});
			} else {
				meButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						addMatchingUser(loggedInUser.getId(),
								loggedInUser.getFullName());
						matchingUsersTable.select(loggedInUser.getId());
						fireEvent(new SubmitEvent(doneButton,
								SubmitEvent.SUBMITTED));
						close();
					}
				});
			}
		}
	}

	protected void searchPeople(String searchText) {
		if (searchText != null && searchText.length() >= 2) {
			matchingUsersTable.removeAllItems();
//			List<User> results =
//					ViewToolManager.getUserCache().findMatchingUsers(searchText);
			 List<User> results = null;
			if(type == 1){
		      results = queryAllUser(searchText);
			}else {
				results = queryTeamUser(searchText);
			}
			//List<User> results = new ArrayList<User>();
			handleQueryUserList(results);
		}
	}
	
	protected List<User> queryTeamUser(String searchText) {
		TeamService teamService =ViewToolManager.getBean("teamService");
		//查询正式成员组
		Team t = teamService.createTeamByUserAndType(LoginHandler.getLoggedInUser().getId(), EntityDictionary.TEAM_GROUP_TYPE_FORMAL,"default");
		UserQuery u = ProcessEngines.getDefaultProcessEngine()
        .getIdentityService().createUserQuery()
		.memberOfTeam(t.getId());
		if(StringTool.judgeBlank(searchText)){
			u.userFullNameLike("%" + searchText + "%");
		}
		List<User> users = u.list();
		return users;
	}
	
	public List<User> queryAllUser(String searchText) {
		List<User> results;
		results = ProcessEngines.getDefaultProcessEngine()
		          .getIdentityService()
		          .createUserQuery()
		          .userFullNameLike("%" + searchText + "%")
		          .list();
		return results;
	}
	
	
	public void handleQueryUserList(List<User> results) {
		for (User user : results) {
			if (!multiSelect
					|| !selectedUsersTable.containsId(user.getId())) {
				if (ignoredUserIds == null
						|| !ignoredUserIds.contains(user.getId())) {
					addMatchingUser(user.getId(), user.getFirstName() + " "
							+ user.getLastName());
				}
			}
		}
	}

	protected void addMatchingUser(String userId, String name) {
		if (!matchingUsersTable.containsId(userId)) {
			Item item = matchingUsersTable.addItem(userId);
			item.getItemProperty("userName").setValue(name);
		}
	}

	protected void initUserSelection() {
		userSelectionLayout = new HorizontalLayout();
		userSelectionLayout.setSpacing(true);
		windowLayout.addComponent(userSelectionLayout);
		// 生成左边的表格
		initMatchingUsersTable();

		// If multi select: two table to move users from left to the right
		// non-multi select: only one table
		if (multiSelect) {
			// ">" 按钮
			initSelectUserButton();//中间的
			// 右边的已经选择的table
			initSelectedUsersTable();
		}
	}

	protected void initMatchingUsersTable() {
		matchingUsersTable = new Table();
		// 行head隐藏
		matchingUsersTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		matchingUsersTable.setSelectable(true);
		matchingUsersTable.setEditable(false);
		matchingUsersTable.setImmediate(true);
		matchingUsersTable.setNullSelectionAllowed(false);
		matchingUsersTable.setSortEnabled(false);

		if (multiSelect) {// 可以进行多选
			matchingUsersTable.setMultiSelect(true);
		}

		matchingUsersTable.addGeneratedColumn("icon",
				new ThemeImageColumnGenerator(Images.USER_16));
		matchingUsersTable.setColumnWidth("icon", 16);
		// ?
		matchingUsersTable.addContainerProperty("userName", String.class, null);

		matchingUsersTable.setWidth(300, Unit.PIXELS);
		matchingUsersTable.setHeight(200, Unit.PIXELS);
		userSelectionLayout.addComponent(matchingUsersTable);
	}

	protected void initSelectUserButton() {
		selectUserButton = new Button(">");

		selectUserButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1152268040356479685L;

			public void buttonClick(ClickEvent event) {
				for (String selectedItemId : (Set<String>) matchingUsersTable
						.getValue()) {
					// Remove from left table
					Item originalItem = matchingUsersTable
							.getItem(selectedItemId);

					// And put it in right table
					selectUser(selectedItemId, (String) originalItem
							.getItemProperty("userName").getValue());

					// Remove from left table (must be done on the end, or item
					// properties will be inaccessible)
					// 删除左边的东西
					matchingUsersTable.removeItem(selectedItemId);
				}
			}
		});

		userSelectionLayout.addComponent(selectUserButton);
		userSelectionLayout.setComponentAlignment(selectUserButton,
				Alignment.MIDDLE_CENTER);
	}

	protected void initSelectedUsersTable() {
		selectedUsersTable = new Table();
		selectedUsersTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		selectedUsersTable.setEditable(false);
		selectedUsersTable.setSortEnabled(false);

		// Icon column
		selectedUsersTable.addGeneratedColumn("icon",
				new ThemeImageColumnGenerator(Images.USER_ADD));
		selectedUsersTable.setColumnWidth("icon", 16);

		// Name column
		selectedUsersTable.addContainerProperty("userName", String.class, null);

		// Role column
		if (showRoles) {
			selectedUsersTable.addContainerProperty("role", ComboBox.class,
					null);
		}

		// Delete icon column
		selectedUsersTable.addGeneratedColumn("delete",
				new ThemeImageColumnGenerator(Images.DELETE,
						new com.vaadin.event.MouseEvents.ClickListener() {
							public void click(
									com.vaadin.event.MouseEvents.ClickEvent event) {
								Object itemId = ((Embedded) event.getSource())
										.getData();

								// Add to left table (if possible)
								String searchFieldValue = (String) searchField
										.getValue();
								if (searchFieldValue != null
										&& searchFieldValue.length() >= 2) {
									String userName = (String) selectedUsersTable
											.getItem(itemId)
											.getItemProperty("userName")
											.getValue();
									if (matchesSearchField(userName)) {
										Item item = matchingUsersTable
												.addItem(itemId);
										item.getItemProperty("userName")
												.setValue(userName);
									}
								}

								// Delete from right table
								selectedUsersTable.removeItem(itemId);
							}
						}));
		selectedUsersTable.setColumnWidth("icon", 16);

		if (showRoles) {
			selectedUsersTable.setWidth(420, Unit.PIXELS);
		} else {
			selectedUsersTable.setWidth(300, Unit.PIXELS);
		}
		selectedUsersTable.setHeight(200, Unit.PIXELS);
		userSelectionLayout.addComponent(selectedUsersTable);
	}

	protected boolean matchesSearchField(String text) {
		for (String userNameToken : text.split(" ")) {
			if (userNameToken.toLowerCase().startsWith(
					((String) searchField.getValue()).toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	protected void selectUser(String userId, String userName) {
		if (!selectedUsersTable.containsId(userId)) {
			Item item = selectedUsersTable.addItem(userId);
			item.getItemProperty("userName").setValue(userName);

			if (showRoles) {
				ComboBox comboBox = new ComboBox(null, Arrays.asList(
						i18nManager.getMessage(Messages.TASK_ROLE_CONTRIBUTOR),
						i18nManager.getMessage(Messages.TASK_ROLE_IMPLEMENTER),
						i18nManager.getMessage(Messages.TASK_ROLE_MANAGER),
						i18nManager.getMessage(Messages.TASK_ROLE_SPONSOR)));
				comboBox.select(i18nManager
						.getMessage(Messages.TASK_ROLE_CONTRIBUTOR));
				comboBox.setNewItemsAllowed(true);
				item.getItemProperty("role").setValue(comboBox);
			}
		}
	}

	protected void initDoneButton() {
		doneButton = new Button("Done");

		doneButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				// Fire event such that depending UI's can be updated
				fireEvent(new SubmitEvent(doneButton, SubmitEvent.SUBMITTED));

				// close popup window
				close();
			}
		});

		windowLayout.addComponent(doneButton);
		windowLayout.setComponentAlignment(doneButton, Alignment.MIDDLE_RIGHT);
	}

	public String getSelectedUserId() {
		if (multiSelect) {
			throw new RuntimeException(
					"Only use getSelectedUserId in non-multiselect mode");
		}
		return (String) matchingUsersTable.getValue();
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getSelectedUserIds() {
		if (!multiSelect) {
			throw new RuntimeException(
					"Only use getSelectedUserIds in multiselect mode");
		}
		return (Collection<String>) selectedUsersTable.getItemIds();
	}

	public String getSelectedUserRole(String userId) {
		if (!multiSelect) {
			throw new RuntimeException(
					"Only use getSelectedUserIds in multiselect mode");
		}
		return (String) ((ComboBox) selectedUsersTable.getItem(userId)
				.getItemProperty("role").getValue()).getValue();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
