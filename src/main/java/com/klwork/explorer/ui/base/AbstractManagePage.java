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
package com.klwork.explorer.ui.base;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Superclass for all Explorer pages
 * 
 * @author ww
 * @author Frederik Heremans
 */
public abstract class AbstractManagePage extends BaseCustomComponent {
	
	
	public AbstractManagePage() {//默认进行tab懒加载
		super(true);
	}
	
	public AbstractManagePage(boolean b) {
		super(b);
	}

	private static final long serialVersionUID = 1L;
	
	VerticalLayout mainLayout;
	final HorizontalSplitPanel mainSplit = new HorizontalSplitPanel();
	protected Object leftParameter;
	//用来标识右边的内容是否先加载
	protected boolean firstInitRight = false;
	
	
	public boolean isFirstInitRight() {
		return firstInitRight;
	}

	public void setFirstInitRight(boolean firstInitRight) {
		this.firstInitRight = firstInitRight;
	}

	public VerticalLayout getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(VerticalLayout mainLayout) {
		this.mainLayout = mainLayout;
	}

	public HorizontalSplitPanel getMainSplit() {
		return mainSplit;
	}
	
	

	public Object getLeftParameter() {
		return leftParameter;
	}

	public void setLeftParameter(Object leftParameter) {
		this.leftParameter = leftParameter;
	}
	
	
	@Override
	public void startInit() {
		setSizeFull();
		initMainLayout();
		//头部
		initHead();
		//HorizontalSplitPanel控件
		initSplitLayout();
		//左边的导航
		initLeft();
		//右边的显示页面,默认是不进行触发的
		if(isFirstInitRight()){
			initRight();
		}
	}

	protected void initLeft() {
		getMainSplit().setFirstComponent(initLeftComponent());
	}

	public void initRight() {
		initRight(initRightComponent());
	}
	
	public void initRight(Component c) {
		getMainSplit().setSecondComponent(c);
	}

	/**
	 * 得到左边的显示对象
	 * 
	 * @return
	 */
	protected abstract Component initLeftComponent();

	/**
	 * 得到右边的显示对象
	 * 
	 * @return
	 */
	protected abstract Component initRightComponent();

	/**
	 * 头部内容,默认没有头部
	 */
	protected Component initHeadComponent() {
		return null;
	}

	protected void initHead() {
		Component c = initHeadComponent();
		if(c != null){
			mainLayout.addComponent(c);
		}
	}

	protected void initMainLayout() {
		mainLayout = new VerticalLayout();
		// layout.setMargin(true);
		mainLayout.setSizeFull();
		setCompositionRoot(mainLayout);
		mainLayout.setSpacing(false);
	}

	public void initSplitLayout() {
		mainSplit.setSizeFull();
		mainSplit.setStyleName(Reindeer.SPLITPANEL_SMALL);
		// 设置分割线的位置
		mainSplit.setSplitPosition(20, Unit.PERCENTAGE);
		mainSplit.setLocked(false);
		mainLayout.addComponent(mainSplit);
		mainLayout.setExpandRatio(mainSplit, 1.0f);
	}
}
