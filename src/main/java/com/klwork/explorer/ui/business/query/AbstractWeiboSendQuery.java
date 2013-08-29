package com.klwork.explorer.ui.business.query;

import com.klwork.business.domain.model.SocialUserWeiboSendQuery;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;

public abstract class AbstractWeiboSendQuery extends SocialUserWeiboSendQuery implements LazyLoadingQuery {
	  protected LazyLoadingContainer lazyLoadingContainer;
	  
	  public void setLazyLoadingContainer(LazyLoadingContainer lazyLoadingContainer) {
	    this.lazyLoadingContainer = lazyLoadingContainer;
	  }
}
