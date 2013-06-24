package com.klwork.business.domain.model;

import java.io.Serializable;
import java.util.Date;

import com.vaadin.ui.components.calendar.event.BasicEvent;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class MyCalendarEvent extends BasicEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	public MyCalendarEvent() {
	}

	/**
	 *  
	 */
	private String id;
	/**
	 *  
	 */
	private String ownUser;
	/**
	 *  
	 */
	private java.util.Date creationDate;
	/**
	 *  
	 */
	private java.util.Date lastUpdate;
	/**
	 *  
	 */
	private java.util.Date startDate;
	/**
	 *  
	 */
	private java.util.Date endDate;
	/**
	 *  
	 */
	private String caption;
	/**
	 *  
	 */
	private String description;
	/**
	 *  
	 */
	private String styleName;
	/**
	 *  
	 */
	private boolean allDay = false;
	/**
	 *  
	 */
	private String relatedTodo;

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
	 * @param ownUser
	 */
	public void setOwnUser(String ownUser) {
		this.ownUser = ownUser;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getOwnUser() {
		return ownUser;
	}

	/**
	 * 
	 * 
	 * @param creationDate
	 */
	public void setCreationDate(java.util.Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public java.util.Date getCreationDate() {
		return creationDate;
	}

	/**
	 * 
	 * 
	 * @param lastUpdate
	 */
	public void setLastUpdate(java.util.Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public java.util.Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * 
	 * 
	 * @param startDate
	 */
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public java.util.Date getStartDate() {
		return startDate;
	}

	/**
	 * 
	 * 
	 * @param endDate
	 */
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public java.util.Date getEndDate() {
		return endDate;
	}

	@Override
	public Date getEnd() {
		return endDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.addon.calendar.event.CalendarEvent#getStart()
	 */
	@Override
	public Date getStart() {
		return startDate;
	}

	@Override
	public void setEnd(Date end) {
		this.endDate = end;
		fireEventChange();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.addon.calendar.event.CalendarEventEditor#setStart(java.util
	 * .Date)
	 */
	@Override
	public void setStart(Date start) {
		this.startDate = start;
		fireEventChange();
	}

	/**
	 * 
	 * 
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
		fireEventChange();
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * 
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
		fireEventChange();
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * 
	 * @param styleName
	 */
	public void setStyleName(String styleName) {
		this.styleName = styleName;
		fireEventChange();
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getStyleName() {
		return styleName;
	}

	/**
	 * 
	 * 
	 * @param allDay
	 */
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
		fireEventChange();
	}

	@Override
	public boolean isAllDay() {
		return allDay;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Boolean getAllDay() {
		return allDay;
	}

	/**
	 * 
	 * 
	 * @param relatedTodo
	 */
	public void setRelatedTodo(String relatedTodo) {
		this.relatedTodo = relatedTodo;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getRelatedTodo() {
		return relatedTodo;
	}
}