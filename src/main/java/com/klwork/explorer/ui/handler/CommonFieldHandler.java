package com.klwork.explorer.ui.handler;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.klwork.business.domain.model.DictDef;
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
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
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
		//System.out.println(Locale.getDefault());
		DateField f = new DateField();
		//f.setLocale(Locale.US);
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
		return initComboBoxByData(caption, data, defaultValue, s);
	}
	
	public static ComboBox updateComBoxData(ComboBox comboBox,String caption,
			Map<String, String> data, Object defaultValue) {
		comboBox.removeAllItems();
		return initComboBoxByData(caption, data, defaultValue, comboBox);
	}

	public static ComboBox initComboBoxByData(String caption,
			Map<String, String> data, Object defaultValue, ComboBox s) {
		s.setNullSelectionAllowed(false);
		//s.setImmediate(true);
		Object firstItemId = null;
		if (StringTool.judgeBlank(caption)) {
			s.setCaption(caption);
		}
		int index = 0;
		for (String p : data.keySet()) {
			String title = data.get(p);
			Item i = s.addItem(p);
			s.setItemCaption(p, title);
			if(index == 0 || p.equals(defaultValue)){
				firstItemId = p;
			}
			index++;
		}
		
		// Select first element
	    if (firstItemId != null) {
	      s.select(firstItemId);
	    }
		return s;
	}
	
	/**
	 * 通过数据字典创建下拉框
	 * @param caption
	 * @param data
	 * @param defaultValue
	 * @param type 默认以数据库字典的value作为,如果type为1，则为code
	 * @return
	 */
	public static ComboBox createComBox(String caption,
			List<DictDef> data, Object defaultValue,String type) {
		ComboBox s = new ComboBox();
		s.setNullSelectionAllowed(false);
		//s.setImmediate(true);
		Object firstItemId = null;
		if (StringTool.judgeBlank(caption)) {
			s.setCaption(caption);
		}
		
		for (DictDef def : data) {
			String itemId = def.getValue();
			if("1".equals(type)){
				itemId = def.getCode();
			}
			Item i = s.addItem(itemId);
			s.setItemCaption(itemId, def.getName());
			if(itemId.equals(defaultValue)){
				firstItemId = itemId;
			}
		}
		
		// Select first element
	    if (firstItemId != null) {
	      s.select(firstItemId);
	    }
		return s;
	}
	
	/**
	 * 创建ComBox
	 * @param caption 标题
	 * @param data 数据源
	 * @param defaultValue 默认值
	 * @param isDefault 是否允许默认
	 * @return
	 */
	public static ComboBox createComBox(String caption,
			Map<Object, String> data, Object defaultValue,Boolean isDefault) {
		ComboBox s = new ComboBox();
		s.setNullSelectionAllowed(false);
		//s.setImmediate(true);
		Object firstItemId = null;
		if (StringTool.judgeBlank(caption)) {
			s.setCaption(caption);
		}
		if(data != null){
			for (Object p : data.keySet()) {
				String title = data.get(p);
				Item i = s.addItem(p);
				s.setItemCaption(p, title);
				if(p.equals(defaultValue)){
					firstItemId = p;
				}
			}
		}
		// Select first element
	    if (isDefault == true && firstItemId != null) {
	      s.select(firstItemId);
	    }
		return s;
	}
	
	public static NativeSelect createNativeSelect(String caption ,Map<Object, String> data,Object defaultValue){
		NativeSelect s = new NativeSelect();
		s.setNullSelectionAllowed(false);
		if(StringTool.judgeBlank(caption)){
			s.setCaption(caption);
		}
		Object firstItemId=null;
		if(data != null){
			for(Object o : data.keySet()){
				String title = data.get(o);
				Item i = s.addItem(o);
				s.setItemCaption(o, title);
				if(o.equals(defaultValue)){
					firstItemId = o;
				}
			}
		}
		if(firstItemId != null){
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
	
	public static Label createLable(String caption){
		Label label = new Label(); 
		if (StringTool.judgeBlank(caption)) {
			label.setCaption(caption);
		}
		return label;
		
	}
	
	/**
	 * 
	 * @param list
	 * @param caption
	 * @param mut true为checkbox, false为radio
	 * @return
	 */
	public static OptionGroup createCheckBoxs(List<DictDef> list,String caption,boolean mut) {
		OptionGroup group = new OptionGroup(caption);
		group.setMultiSelect(mut);
		group.setStyleName("horizontal");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DictDef dictDef = (DictDef) iterator.next();
			String p = dictDef.getValue();
			Item i = group.addItem(p);
			group.setItemCaption(p, dictDef.getName());
		}
		return group;
	}
	
	/**
	 * 
	 * @param list
	 * @param caption
	 * @param mut true为checkbox, false为radio
	 * @return
	 */
	public static OptionGroup createCheckBoxs(List<DictDef> list,String caption,boolean mut,String defaultValue) {
		OptionGroup group = new OptionGroup(caption);
		group.setMultiSelect(mut);
		group.setStyleName("horizontal");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DictDef dictDef = (DictDef) iterator.next();
			String p = dictDef.getValue();
			Item i = group.addItem(p);
			group.setItemCaption(p, dictDef.getName());
			if(p.equals(defaultValue)){
				group.select(p);
			}
		}
		return group;
	}
}
