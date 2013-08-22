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
package com.klwork.explorer.ui.business.social;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.utils.SinaSociaTool;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Link;

/**
 * The Class AllWeiboPage.
 */
public class SinaWeiboDisplayPage extends AbstractWeiboDisplayPage {

	public SinaWeiboDisplayPage(SocialUserAccount socialUserAccount, int type) {
		super(socialUserAccount,type);
	}
	
	
	/**
	 * 发帖人连接
	 * @param userWeibo
	 * @return
	 */
	@Override
	public Link initUserScreenName(final SocialUserWeibo userWeibo) {
		Link goToMain = new Link(userWeibo.getUserScreenName()
				+ ":", new ExternalResource(getWeiboMainUrl()
				+ userWeibo.getWeiboUid()));
		goToMain.setTargetName("_blank");
		return goToMain;
	}
	
	/**
	 * 原帖的发帖人链接
	 * @param orginWeibo
	 * @return
	 */
	@Override
	public Link initRetweetedUserScreenName(final SocialUserWeibo orginWeibo) {
		Link link = new Link("@"
				+ orginWeibo.getUserScreenName(),
				new ExternalResource(
						getWeiboMainUrl()
								+ orginWeibo
										.getWeiboId()));
		link.setTargetName("_blank");
		return link;
	}
	
	/**
	 * 处理用户图像
	 * @param userWeibo
	 * @return
	 */
	@Override
	public Image initUserProfileImage(final SocialUserWeibo userWeibo) {
		Image image = currentImage(userWeibo.getUserProfileImageUrl(),
				"img/weibo/head_01.jpg", "/50");
		return image;
	}
	
	/**
	 * 原帖图
	 */
	@Override
	public Image initRetweetPic(final SocialUserWeibo orginWeibo) {
		// 原帖图
		Image image = currentImage(
				orginWeibo.getThumbnailPic(), null, "");// 小图
		return image;
	}
	
	@Override
	protected String textTranslate(String text) {
		return SinaSociaTool.textTranslate(text);
	}
	
	@Override
	public String getWeiboMainUrl() {
		return "http://weibo.com/";
	}
	
	@Override
	public Image initOriginalPic(final SocialUserWeibo userWeibo) {
		// 一个图片
		String origPic = userWeibo.getOriginalPic();
		Image image = currentImage(origPic, null,"");
		// System.out.println("测试一下" + image.getHeight());
		if (image != null) {
			image.setHeight("200px");
			image.setWidth("200px");
			
		}
		return image;
	}


	@Override
	public String getSocialType() {
		return "0";
	}
}
