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

package com.klwork.explorer.ui.task;

import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.content.AttachmentDetailPopupWindow;
import com.klwork.explorer.ui.content.AttachmentRenderer;
import com.klwork.explorer.ui.content.AttachmentRendererManager;
import com.klwork.explorer.ui.content.CreateAttachmentPopupWindow;
import com.klwork.explorer.ui.content.RelatedContentComponent;
import com.klwork.explorer.ui.custom.ConfirmationDialogPopupWindow;
import com.klwork.explorer.ui.event.ConfirmationEvent;
import com.klwork.explorer.ui.event.ConfirmationEventListener;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.themes.Reindeer;

/**
 * Component for showing related content of a task. Also allows adding, removing
 * and opening related content.
 * 
 * @author Frederik Heremans
 * @author Joram Barrez
 */
public class TaskRelatedContentComponent extends VerticalLayout implements
		RelatedContentComponent {

	private static final long serialVersionUID = 1L;

	protected transient TaskService taskService;
	protected I18nManager i18nManager;
	protected AttachmentRendererManager attachmentRendererManager;

	protected Task task;
	protected VerticalLayout contentLayout;
	protected Table table;
	protected TaskDetailPanel taskDetailPanel;
	protected Label noContentLabel;

	public TaskRelatedContentComponent(Task task,
			TaskDetailPanel taskDetailPanel) {
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		this.i18nManager = ViewToolManager.getI18nManager();
		this.attachmentRendererManager = ViewToolManager
				.getAttachmentRendererManager();

		this.task = task;
		this.taskDetailPanel = taskDetailPanel;

		addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);

		initActions();
		initAttachmentTable();
	}

	public void showAttachmentDetail(Attachment attachment) {
		// Show popup window with detail of attachment rendered in in
		AttachmentDetailPopupWindow popup = new AttachmentDetailPopupWindow(
				attachment);
		ViewToolManager.showPopupWindow(popup);
	}

	protected void initActions() {
		// WW_TODO 关联的内容
		HorizontalLayout actionsContainer = new HorizontalLayout();
		actionsContainer.setSizeFull();

		// Title
		Label processTitle = new Label(
				i18nManager.getMessage(Messages.TASK_RELATED_CONTENT));
		processTitle.addStyleName(ExplorerLayout.STYLE_H3);
		processTitle.setSizeFull();
		actionsContainer.addComponent(processTitle);
		actionsContainer.setComponentAlignment(processTitle,
				Alignment.MIDDLE_LEFT);
		actionsContainer.setExpandRatio(processTitle, 1.0f);

		// Add content button
		Button addRelatedContentButton = new Button();
		addRelatedContentButton.addStyleName(ExplorerLayout.STYLE_ADD);
		addRelatedContentButton
				.addClickListener(new com.vaadin.ui.Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(
							com.vaadin.ui.Button.ClickEvent event) {
						CreateAttachmentPopupWindow popup = new CreateAttachmentPopupWindow();

						if (task.getProcessInstanceId() != null) {
							popup.setProcessInstanceId(task
									.getProcessInstanceId());
						} else {
							popup.setTaskId(task.getId());
						}

						// Add listener to update attachments when added
						popup.addListener(new SubmitEventListener() {

							private static final long serialVersionUID = 1L;

							@Override
							protected void submitted(SubmitEvent event) {
								taskDetailPanel.notifyRelatedContentChanged();
							}

							@Override
							protected void cancelled(SubmitEvent event) {
								// No attachment was added so updating UI isn't
								// needed.
							}
						});

						ViewToolManager.showPopupWindow(popup);
					}
				});

		actionsContainer.addComponent(addRelatedContentButton);
		actionsContainer.setComponentAlignment(processTitle,
				Alignment.MIDDLE_RIGHT);

		addComponent(actionsContainer);
	}

	protected void initAttachmentTable() {
		contentLayout = new VerticalLayout();
		addComponent(contentLayout);

		table = new Table();
		table.setWidth(100, Unit.PERCENTAGE);
		table.addStyleName(ExplorerLayout.STYLE_RELATED_CONTENT_LIST);

		// Invisible by default, only shown when attachments are present
		table.setVisible(false);
		table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

		addContainerProperties();

		// Get the related content for this task
		//WW_TODO 得到这个任务相关的附件
		refreshTaskAttachments();
		contentLayout.addComponent(table);
	}

	protected void addContainerProperties() {
		table.addContainerProperty("type", Embedded.class, null);
		table.setColumnWidth("type", 16);

		table.addContainerProperty("name", Component.class, null);

		table.addContainerProperty("delete", Embedded.class, null);
		table.setColumnWidth("delete", 16);
	}

	public void refreshTaskAttachments() {
		table.removeAllItems();
		if (noContentLabel != null) {
			contentLayout.removeComponent(noContentLabel);
		}

		List<Attachment> attachments = null;
		if (task.getProcessInstanceId() != null) {//流程实体id不为空
			attachments = (taskService.getProcessInstanceAttachments(task
					.getProcessInstanceId()));
		} else {
			attachments = taskService.getTaskAttachments(task.getId());
		}

		if (attachments.size() > 0) {
			addAttachmentsToTable(attachments);
		} else {
			table.setVisible(false);
			noContentLabel = new Label(
					i18nManager.getMessage(Messages.TASK_NO_RELATED_CONTENT));
			noContentLabel.addStyleName(Reindeer.LABEL_SMALL);
			contentLayout.addComponent(noContentLabel);
		}
	}

	@SuppressWarnings("unchecked")
	protected void addAttachmentsToTable(List<Attachment> attachments) {
		for (Attachment attachment : attachments) {
			AttachmentRenderer renderer = attachmentRendererManager
					.getRenderer(attachment);
			Item attachmentItem = table.addItem(attachment.getId());
			attachmentItem.getItemProperty("name").setValue(
					renderer.getOverviewComponent(attachment, this));
			attachmentItem.getItemProperty("type").setValue(
					new Embedded(null, renderer.getImage(attachment)));

			Embedded deleteButton = new Embedded(null, Images.DELETE);
			deleteButton.addStyleName(ExplorerLayout.STYLE_CLICKABLE);
			deleteButton.addClickListener((ClickListener) new DeleteClickedListener(
					attachment));
			attachmentItem.getItemProperty("delete").setValue(deleteButton);
		}

		if (table.getItemIds().size() > 0) {
			table.setVisible(true);
		}
		table.setPageLength(table.size());
	}

	protected void addEmptySpace(ComponentContainer container) {
		Label emptySpace = new Label("&nbsp;", Label.CONTENT_XHTML);
		emptySpace.setSizeUndefined();
		container.addComponent(emptySpace);
	}

	private class DeleteClickedListener extends ConfirmationEventListener
			implements ClickListener {

		private static final long serialVersionUID = 1L;
		private Attachment attachment;

		public DeleteClickedListener(Attachment attachment) {
			this.attachment = attachment;
		}

		public void click(ClickEvent event) {
			ConfirmationDialogPopupWindow confirm = new ConfirmationDialogPopupWindow(
					i18nManager.getMessage(
							Messages.RELATED_CONTENT_CONFIRM_DELETE,
							attachment.getName()));

			confirm.addListener((ConfirmationEventListener) this);
			confirm.showConfirmation();
		}

		@Override
		protected void confirmed(ConfirmationEvent event) {
			taskService.deleteAttachment(attachment.getId());
			taskDetailPanel.notifyRelatedContentChanged();
		}

		@Override
		protected void rejected(ConfirmationEvent event) {
			// Nothing to do here
		}
	}

}
