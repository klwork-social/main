package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class UserDataStatistic implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public UserDataStatistic() {
 	}
 

	/**
	 *  
	 */
	private String userId;
	/**
	 *  
	 */
	private Integer todoTaskTotal = 0;
	/**
	 *  
	 */
	private Integer overdueTodoTaskTotal = 0;
	/**
	 *  
	 */
	private Integer myTaskTotal = 0;
	/**
	 *  
	 */
	private Integer overdueMyTaskTotal = 0;
	/**
	 *  
	 */
	private Integer teamTaskTotal = 0;
	/**
	 *  
	 */
	private Integer overdueTeamTaskTotal = 0;
	/**
	 *  
	 */
	private Integer involvedTaskTotal = 0;
	/**
	 *  
	 */
	private Integer overdueInvolvedTaskTotal = 0;
	/**
	 *  
	 */
	private Boolean dirty;
	/**
	 *  
	 */
	private java.util.Date lastUpdate;

	/**
	 * 
	 *
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserId(){
		return userId;
	}
	/**
	 * 
	 *
	 * @param todoTaskTotal
	 */
	public void setTodoTaskTotal(Integer todoTaskTotal){
		this.todoTaskTotal = todoTaskTotal;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getTodoTaskTotal(){
		return todoTaskTotal;
	}
	/**
	 * 
	 *
	 * @param overdueTodoTaskTotal
	 */
	public void setOverdueTodoTaskTotal(Integer overdueTodoTaskTotal){
		this.overdueTodoTaskTotal = overdueTodoTaskTotal;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getOverdueTodoTaskTotal(){
		return overdueTodoTaskTotal;
	}
	/**
	 * 
	 *
	 * @param myTaskTotal
	 */
	public void setMyTaskTotal(Integer myTaskTotal){
		this.myTaskTotal = myTaskTotal;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getMyTaskTotal(){
		return myTaskTotal;
	}
	/**
	 * 
	 *
	 * @param overdueMyTaskTotal
	 */
	public void setOverdueMyTaskTotal(Integer overdueMyTaskTotal){
		this.overdueMyTaskTotal = overdueMyTaskTotal;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getOverdueMyTaskTotal(){
		return overdueMyTaskTotal;
	}
	/**
	 * 
	 *
	 * @param teamTaskTotal
	 */
	public void setTeamTaskTotal(Integer teamTaskTotal){
		this.teamTaskTotal = teamTaskTotal;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getTeamTaskTotal(){
		return teamTaskTotal;
	}
	/**
	 * 
	 *
	 * @param overdueTeamTaskTotal
	 */
	public void setOverdueTeamTaskTotal(Integer overdueTeamTaskTotal){
		this.overdueTeamTaskTotal = overdueTeamTaskTotal;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getOverdueTeamTaskTotal(){
		return overdueTeamTaskTotal;
	}
	/**
	 * 
	 *
	 * @param involvedTaskTotal
	 */
	public void setInvolvedTaskTotal(Integer involvedTaskTotal){
		this.involvedTaskTotal = involvedTaskTotal;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getInvolvedTaskTotal(){
		return involvedTaskTotal;
	}
	/**
	 * 
	 *
	 * @param overdueInvolvedTaskTotal
	 */
	public void setOverdueInvolvedTaskTotal(Integer overdueInvolvedTaskTotal){
		this.overdueInvolvedTaskTotal = overdueInvolvedTaskTotal;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getOverdueInvolvedTaskTotal(){
		return overdueInvolvedTaskTotal;
	}
	/**
	 * 
	 *
	 * @param dirty
	 */
	public void setDirty(Boolean dirty){
		this.dirty = dirty;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Boolean getDirty(){
		return dirty;
	}
	/**
	 * 
	 *
	 * @param lastUpdate
	 */
	public void setLastUpdate(java.util.Date lastUpdate){
		this.lastUpdate = lastUpdate;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getLastUpdate(){
		return lastUpdate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((involvedTaskTotal == null) ? 0 : involvedTaskTotal
						.hashCode());
		result = prime * result
				+ ((myTaskTotal == null) ? 0 : myTaskTotal.hashCode());
		result = prime
				* result
				+ ((overdueInvolvedTaskTotal == null) ? 0
						: overdueInvolvedTaskTotal.hashCode());
		result = prime
				* result
				+ ((overdueMyTaskTotal == null) ? 0 : overdueMyTaskTotal
						.hashCode());
		result = prime
				* result
				+ ((overdueTeamTaskTotal == null) ? 0 : overdueTeamTaskTotal
						.hashCode());
		result = prime
				* result
				+ ((overdueTodoTaskTotal == null) ? 0 : overdueTodoTaskTotal
						.hashCode());
		result = prime * result
				+ ((teamTaskTotal == null) ? 0 : teamTaskTotal.hashCode());
		result = prime * result
				+ ((todoTaskTotal == null) ? 0 : todoTaskTotal.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDataStatistic other = (UserDataStatistic) obj;
		if (involvedTaskTotal == null) {
			if (other.involvedTaskTotal != null)
				return false;
		} else if (!involvedTaskTotal.equals(other.involvedTaskTotal))
			return false;
		if (myTaskTotal == null) {
			if (other.myTaskTotal != null)
				return false;
		} else if (!myTaskTotal.equals(other.myTaskTotal))
			return false;
		if (overdueInvolvedTaskTotal == null) {
			if (other.overdueInvolvedTaskTotal != null)
				return false;
		} else if (!overdueInvolvedTaskTotal
				.equals(other.overdueInvolvedTaskTotal))
			return false;
		if (overdueMyTaskTotal == null) {
			if (other.overdueMyTaskTotal != null)
				return false;
		} else if (!overdueMyTaskTotal.equals(other.overdueMyTaskTotal))
			return false;
		if (overdueTeamTaskTotal == null) {
			if (other.overdueTeamTaskTotal != null)
				return false;
		} else if (!overdueTeamTaskTotal.equals(other.overdueTeamTaskTotal))
			return false;
		if (overdueTodoTaskTotal == null) {
			if (other.overdueTodoTaskTotal != null)
				return false;
		} else if (!overdueTodoTaskTotal.equals(other.overdueTodoTaskTotal))
			return false;
		if (teamTaskTotal == null) {
			if (other.teamTaskTotal != null)
				return false;
		} else if (!teamTaskTotal.equals(other.teamTaskTotal))
			return false;
		if (todoTaskTotal == null) {
			if (other.todoTaskTotal != null)
				return false;
		} else if (!todoTaskTotal.equals(other.todoTaskTotal))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UserDataStatistic [userId=" + userId + ", todoTaskTotal="
				+ todoTaskTotal + ", overdueTodoTaskTotal="
				+ overdueTodoTaskTotal + ", myTaskTotal=" + myTaskTotal
				+ ", overdueMyTaskTotal=" + overdueMyTaskTotal
				+ ", teamTaskTotal=" + teamTaskTotal
				+ ", overdueTeamTaskTotal=" + overdueTeamTaskTotal
				+ ", involvedTaskTotal=" + involvedTaskTotal
				+ ", overdueInvolvedTaskTotal=" + overdueInvolvedTaskTotal
				+ ", dirty=" + dirty + "]";
	}
	
	
}