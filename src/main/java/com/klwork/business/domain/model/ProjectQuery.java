package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class ProjectQuery extends QueryParameter {

	/**
	 *  
	 */
	private String id;
	/**
	 *  
	 */
	private java.util.Date creationdate;

	/**
	 *  
	 */
	private java.util.Date lastupdate;
	/**
	 *  
	 */
	private String description;
	/**
	 *  
	 */
	private String ownuser;

	private String name;

	public String getOwnuser() {
		return ownuser;
	}

	public void setOwnuser(String ownuser) {
		this.ownuser = ownuser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public java.util.Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(java.util.Date creationdate) {
		this.creationdate = creationdate;
	}

	public java.util.Date getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(java.util.Date lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
