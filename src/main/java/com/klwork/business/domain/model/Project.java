package com.klwork.business.domain.model;

import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class Project implements Serializable {
	private static final long serialVersionUID = 1L;

	public Project() {
	}

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
	private String ownuser;
	/**
	 *  
	 */
	private java.util.Date lastupdate;
	/**
	 *  
	 */
	private String description;

	private String name;

	/**
	 * 
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * 
	 * @param creationdate
	 */
	public void setCreationdate(java.util.Date creationdate) {
		this.creationdate = creationdate;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public java.util.Date getCreationdate() {
		return creationdate;
	}

	/**
	 * 
	 * 
	 * @param ownuser
	 */
	public void setOwnuser(String ownuser) {
		this.ownuser = ownuser;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getOwnuser() {
		return ownuser;
	}

	/**
	 * 
	 * 
	 * @param lastupdate
	 */
	public void setLastupdate(java.util.Date lastupdate) {
		this.lastupdate = lastupdate;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public java.util.Date getLastupdate() {
		return lastupdate;
	}

	/**
	 * 
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}