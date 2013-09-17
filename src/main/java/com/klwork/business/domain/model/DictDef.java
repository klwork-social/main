package com.klwork.business.domain.model;


import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.klwork.business.domain.repository.DictDefRepository;
import com.klwork.common.exception.ApplicationException;
import com.klwork.common.utils.StringTool;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class DictDef implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public DictDef() {
 	}
 	
 	

	public DictDef(String id,String code, String type, String name, String value) {
		super();
		this.id = id;
		this.code = code;
		this.type = type;
		this.name = name;
		this.value = value;
	}



	/**
	 *  
	 */
	private String id;
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
	 */
	private String value;

	/**
	 * 
	 *
	 * @param id
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getId(){
		return id;
	}
	/**
	 * 
	 *
	 * @param code
	 */
	public void setCode(String code){
		this.code = code;
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
	public void setType(String type){
		this.type = type;
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
	public void setName(String name){
		this.name = name;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getName(){
		return name;
	}
	/**
	 * 
	 *
	 * @param comment
	 */
	public void setValue(String value){
		this.value = value;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getValue(){
		return value;
	}
	
	public static Map<String, DictDef> queryAllDefMaps() {
		return DictDefRepository.getDefMaps();
	}
	
	/**
	 * 从代码得到数据字典
	 * @param code
	 * @return
	 */
	public static DictDef queryDictDefByCode(String code){
		DictDef def  = DictDef.queryAllDefMaps().get(code);
		return def;
	}
	
	/**
	 * 得到一种类型的所有数据字典
	 * @param code 为
	 * @return
	 */
	public static DictDef dictValue(String code,String value){
		List<DictDef> list =  DictDefRepository.getDefTypeMap().get(code);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DictDef dictDef = (DictDef) iterator.next();
			if(dictDef.getValue().equals(value)){
				return dictDef;
			}
			
		}
		return null;
	}
	
	/**
	 * 得到一种类型的所有数据字典
	 * @param code
	 * @return
	 */
	public static List<DictDef> queryDictsByType(String code){
		return DictDefRepository.getDefTypeMap().get(code);
	}
	
	/**
	 * 从代码得到数据值
	 * @param code
	 * @return value
	 */
	public static String dict(String code){
		DictDef def  = queryDictDefByCode(code);
		if(def != null){
			return def.getValue();
		}
		return "";
	}
	
	/**
	 * 从代码得到数据值
	 * @param code
	 * @return value
	 */
	public static int dictInt(String code){
		String dict = dict(code);
		if(dict == null || "".equals(dict)){
			throw new ApplicationException("数据字典不存在");
		}
		return StringTool.parseInt(dict);
	}
}