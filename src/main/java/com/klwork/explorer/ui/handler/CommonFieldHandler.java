package com.klwork.explorer.ui.handler;

import java.util.Map;

import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class CommonFieldHandler {
	public static TextField createTextField(String caption) {
		TextField f = new TextField();
		if (StringTool.judgeBlank(caption)) {
			f.setCaption(caption);
		}
		// TextField f = new TextField(caption);
		f.setNullRepresentation("");
		return f;
	}

	public static CheckBox createCheckBox(String caption) {
		CheckBox cb = new CheckBox(caption);
		cb.setImmediate(true);
		return cb;
	}

	public static TextArea createTextArea(String caption) {
		TextArea f = new TextArea();
		if (StringTool.judgeBlank(caption)) {
			f.setCaption(caption);
		}
		f.setNullRepresentation("");
		return f;
	}

	public static DateField createDateField(String caption,
			boolean useSecondResolution) {
		DateField f = new DateField();
		if (StringTool.judgeBlank(caption)) {
			f.setCaption(caption);
		}
		f.setDateFormat("yyyy-MM-dd HH:mm");
		f.setShowISOWeekNumbers(true);
		if (useSecondResolution) {
			f.setResolution(Resolution.SECOND);
		} else {
			f.setResolution(Resolution.MINUTE);
		}
		return f;
	}

	public static ComboBox createComBox(String caption,
			Map<String, String> data, Object defaultValue) {
		ComboBox s = new ComboBox();
		s.setNullSelectionAllowed(false);
		Object firstItemId = null;
		if (StringTool.judgeBlank(caption)) {
			s.setCaption(caption);
		}
		for (String p : data.keySet()) {
			String title = data.get(p);
			Item i = s.addItem(p);
			s.setItemCaption(p, title);
			if(p.equals(defaultValue)){
				firstItemId = p;
			}
		}
		
		// Select first element
	    if (firstItemId != null) {
	      s.select(firstItemId);
	    }
		return s;
	}
	
	/**
	 * 得到一个分界线
	 * @return
	 */
	public static Component getSpacer() {
		// 增加一个分隔线
		Label spacer = new Label();
		spacer.setWidth(100, Unit.PERCENTAGE);
		spacer.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		return spacer;
	}
}
