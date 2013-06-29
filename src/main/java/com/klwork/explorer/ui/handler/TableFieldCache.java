package com.klwork.explorer.ui.handler;

import java.util.HashMap;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

@SuppressWarnings("rawtypes")
public class TableFieldCache {
	private HashMap<Object, HashMap<Object, Field>> fieldCache = new HashMap<Object, HashMap<Object, Field>>();

	public <T> T getPropertyFieldFromCache(Object itemId, Object propertyId) {
		Field tf = null;
		HashMap<Object, Field> itemMap = fieldCache.get(itemId);
		if (itemMap == null) {
			itemMap = new HashMap<Object, Field>();
			fieldCache.put(itemId, itemMap);
		}
		if (itemMap.get(propertyId) != null) {
			tf = itemMap.get(propertyId);
		}
		return (T) tf;
	}
	
	/**
	 * 设置其readOnly属性
	 * @param itemId
	 * @param readOnly
	 */
	public void setFieldReadOnly(Object itemId,boolean readOnly) {
		HashMap<Object, Field> propertyMap = fieldCache.get(itemId);
		if(propertyMap == null)
			return;
		for (Field f : propertyMap.values()) {
			f.setReadOnly(readOnly);
		}
	}
	
	/**
	 * 设置其readOnly属性
	 * @param itemId
	 * @param readOnly
	 */
	public void setFieldFocus(Object itemId) {
		HashMap<Object, Field> propertyMap = fieldCache.get(itemId);
		if(propertyMap == null)
			return;
		for (Field f : propertyMap.values()) {
			f.focus();
		}
	}

	/**
	 * 保存属性的field到cache
	 * 
	 * @param itemId
	 * @param propertyId
	 * @param tf
	 */
	public void savePrppertyFieldToCache(Object itemId, Object propertyId,
			Field<?> tf) {
		if (tf != null) {
			HashMap<Object, Field> propertyMap = fieldCache.get(itemId);// itemId保存多个属性
			if (propertyMap == null) {// 为空的话，创建一个
				propertyMap = new HashMap<Object, Field>();
				fieldCache.put(itemId, propertyMap);
			}
			propertyMap.put(propertyId, tf);// 每个属性一个textfield
			// itemIds.put(tf, itemId);
		}
	}
}
