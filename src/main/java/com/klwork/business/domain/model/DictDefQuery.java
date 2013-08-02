package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class DictDefQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public DictDefQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String code;
	
	/**
	 *  
	 */
	private String type;
	
	/**
	 *  
	 */
	private String name;
	

	/**
	 * 
	 *
	 * @param code
	 */
	public DictDefQuery setCode(String code){
		this.code = code;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getCode(){
		return code;
	}
	
	/**
	 * 
	 *
	 * @param type
	 */
	public DictDefQuery setType(String type){
		this.type = type;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getType(){
		return type;
	}
	
	/**
	 * 
	 *
	 * @param name
	 */
	public DictDefQuery setName(String name){
		this.name = name;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getName(){
		return name;
	}
	

}