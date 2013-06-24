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

package com.klwork.explorer.ui.content;

import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Frederik Heremans
 */
public class CreateAttachmentPopupWindow extends PopupWindow {

	private static final long serialVersionUID = 1L;

	protected String taskId;
	protected String processInstanceId;

	protected I18nManager i18nManager;
	protected AttachmentRendererManager attachmentRendererManager;
	protected transient TaskService taskService;

	protected HorizontalLayout layout;
	protected GridLayout detailLayout;
	protected AttachmentEditorComponent currentEditor;
	protected Table attachmentTypes;
	protected Button okButton;
	
	//如果指定了包含，则只显示此类文件
	protected List includeTypes = null;

	public CreateAttachmentPopupWindow(List types) {
		this.includeTypes = types;
		this.i18nManager = ViewToolManager.getI18nManager();
		this.attachmentRendererManager = ViewToolManager
				.getAttachmentRendererManager();
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();

		setCaption(i18nManager.getMessage(Messages.RELATED_CONTENT_ADD));
		setWidth(700, Unit.PIXELS);
		setHeight(430, Unit.PIXELS);
		center();
		setModal(true);
		addStyleName(Reindeer.WINDOW_LIGHT);

		layout = new HorizontalLayout();
		layout.setSpacing(false);
		layout.setMargin(true);
		layout.setSizeFull();
		setContent(layout);

		initTable();
		// 右边的显示的附件上传详细
		detailLayout = new GridLayout(1, 2);
		detailLayout.setSizeFull();
		detailLayout.setMargin(true);
		detailLayout.setSpacing(true);
		detailLayout
				.addStyleName(ExplorerLayout.STYLE_RELATED_CONTENT_CREATE_DETAIL);

		layout.addComponent(detailLayout);
		layout.setExpandRatio(detailLayout, 1.0f);

		detailLayout.setRowExpandRatio(0, 1.0f);
		detailLayout.setColumnExpandRatio(0, 1.0f);
		// 创建按钮
		initActions();
	}
	
	public CreateAttachmentPopupWindow() {
		this(null);
	}

	@Override
	public void attach() {
		super.attach();
		if (attachmentTypes.size() > 0) {// 选择第一个
			attachmentTypes.select(attachmentTypes.firstItemId());
		}
	}

	protected void initActions() {
		okButton = new Button(
				i18nManager.getMessage(Messages.RELATED_CONTENT_CREATE));
		detailLayout.addComponent(okButton, 0, 1);
		okButton.setEnabled(false);
		okButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				// WW_TODO 保存上传的附件
				saveAttachment();
			}
		});
		detailLayout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
	}

	protected void initTable() {
		attachmentTypes = new Table();
		attachmentTypes.setSizeUndefined();
		attachmentTypes.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		attachmentTypes.setSelectable(true);
		attachmentTypes.setImmediate(true);
		attachmentTypes.setNullSelectionAllowed(false);
		attachmentTypes.setWidth(200, Unit.PIXELS);
		attachmentTypes.setHeight(100, Unit.PERCENTAGE);

		attachmentTypes.setCellStyleGenerator(new CellStyleGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getStyle(Table source, Object itemId,
					Object propertyId) {
				if ("name".equals(propertyId)) {
					return ExplorerLayout.STYLE_RELATED_CONTENT_CREATE_LIST_LAST_COLUMN;
				}
				return null;
			}
		});

		attachmentTypes
				.addStyleName(ExplorerLayout.STYLE_RELATED_CONTENT_CREATE_LIST);

		attachmentTypes.addContainerProperty("type", Embedded.class, null);
		attachmentTypes.setColumnWidth("type", 16);
		attachmentTypes.addContainerProperty("name", String.class, null);

		// Add all possible attachment types
		for (AttachmentEditor editor : attachmentRendererManager
				.getAttachmentEditors()) {// 支持的类型，文件盒url
			
			if(includeTypes != null && !includeTypes.contains(editor.getName())){
				continue;
			}
				String name = editor.getTitle(i18nManager);
				Embedded image = null;
	
				Resource resource = editor.getImage();
				if (resource != null) {
					image = new Embedded(null, resource);
				}
				Item item = attachmentTypes.addItem(editor.getName());
				item.getItemProperty("type").setValue(image);
				item.getItemProperty("name").setValue(name);
			
		}

		// Add listener to show editor component
		attachmentTypes.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				String type = (String) event.getProperty().getValue();
				selectType(type);
			}
		});

		layout.addComponent(attachmentTypes);
	}

	protected void selectType(String type) {
		if (type != null) {
			setCurrentEditor(attachmentRendererManager.getEditor(type));
		} else {
			setCurrentEditor(null);
		}
	}

	protected void setCurrentEditor(AttachmentEditor editor) {
		AttachmentEditorComponent component = editor.getEditor(null, taskId,
				processInstanceId);
		this.currentEditor = component;
		detailLayout.removeComponent(detailLayout.getComponent(0, 0));

		if (currentEditor != null) {
			currentEditor.setSizeFull();
			detailLayout.addComponent(currentEditor, 0, 0);
			okButton.setEnabled(true);
		} else {
			okButton.setEnabled(false);
		}
	}

	protected void saveAttachment() {
		try {
			// Creation and persistence of attachment is done in editor
			//跟踪代码里面，会进行附件的持久化
			Attachment attachment = currentEditor.getAttachment();

			fireEvent(new SubmitEvent(this, SubmitEvent.SUBMITTED, attachment));

			// Finally, close window
			close();
		} catch (InvalidValueException ive) {
			// Validation error, Editor UI will handle this.
		}
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
}
