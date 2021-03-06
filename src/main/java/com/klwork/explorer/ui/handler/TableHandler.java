package com.klwork.explorer.ui.handler;

import com.klwork.explorer.data.LazyLoadingContainer;
import com.vaadin.ui.Table;

public  class TableHandler {
	
	/**
	 * 选中一个table的元素
	 * @param table
	 * @param index
	 */
	public static void selectElement(Table table, int index) {
		if (table.getContainerDataSource().size() > index) {
			table.select(index);
			table.setCurrentPageFirstItemId(index);
		}
	}
	
	/**
	 * 选择table的下一个元素
	 * @param table
	 */
	public static void refreshSelected(Table table) {
		if(table.getValue() != null){
		    Integer selectedIndex = (Integer) table.getValue();
		    selectElement(table,selectedIndex);
		}
	  }
	
	/**
	 * 选择table的下一个元素
	 * @param table
	 */
	public static void refreshSelectNext(Table table) {
		Integer cFirstItemId = (Integer) table.getCurrentPageFirstItemId();
	    Integer selectedIndex = (Integer) table.getValue();//当前选中的索引
	    table.removeAllItems();
	    
	    // Remove all items
	    table.getContainerDataSource().removeAllItems();
	    
	    // Try to select the next one in the list
	    Integer max = table.getContainerDataSource().size();//查询数据库的总数
	    if (max != 0) {
	      if(cFirstItemId > max) {
	        cFirstItemId = max -1;
	      }
	      if(selectedIndex > max) {
	        selectedIndex = max -1;
	      }
	      table.setCurrentPageFirstItemIndex(cFirstItemId);
	      selectElement(table,selectedIndex);
	    } else {
	      table.refreshRowCache();
	      table.setCurrentPageFirstItemIndex(0);
	    }
	  }
	
	/**
	 * 设置一个table没有表头，不可编辑等一些常用的参数
	 * @param listTable
	 */
	public static void setTableNoHead(Table listTable) {
		//listTable.setEditable(false);
		listTable.setImmediate(true);
		listTable.setSelectable(true);
		listTable.setNullSelectionAllowed(false);
		listTable.setSortEnabled(true);
		listTable.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
		listTable.setSizeFull();
	}
	
	/**
	 * 设置一个table没有表头，不可编辑等一些常用的参数
	 * @param listTable
	 */
	public static void setTableHasHead(Table listTable) {
		//listTable.setEditable(false);
		listTable.setImmediate(true);
		listTable.setSelectable(true);
		listTable.setNullSelectionAllowed(false);
		listTable.setSortEnabled(true);
		listTable.setSizeFull();
	}
	
	/**
	 * table选中的处理
	 * 
	 * @param listTable
	 */
	public static void disposeSelectTable(Table listTable,
			LazyLoadingContainer listContainer,String id) {
		if (id == null) {
			// WW_TODO 没有任务显示第一个
			selectElement(listTable, 0);
		} else {
			// 懒加载容器
			int index = listContainer.getIndexForObjectId(id);
			selectElement(listTable, index);
		}

	}
}
