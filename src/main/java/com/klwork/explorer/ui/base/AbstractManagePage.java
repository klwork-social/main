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
public abstract class AbstractManagePage extends CustomComponent {

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
	public void attach() {
		super.attach();
		initUi();
	}

	/**
	 * Override this method (and call super()) when you want to influence the
	 * UI.
	 */
	protected void initUi() {
		setSizeFull();
		initMainLayout();
		initHead();
		initSplitLayout();
		initLeft();
		if(isFirstInitRight()){
			initRight();
		}
	}

	protected void initLeft() {
		getMainSplit().setFirstComponent(initLeftComponent());
	}

	public void initRight() {
		getMainSplit().setSecondComponent(initRightComponent());
	}

	/**
	 * 得到左边的空间
	 * 
	 * @return
	 */
	protected abstract Component initLeftComponent();

	/**
	 * 右边的内容
	 * 
	 * @return
	 */
	protected abstract Component initRightComponent();

	/**
	 * 头部内容
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
