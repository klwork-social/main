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
package com.klwork.business.domain.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.klwork.business.domain.model.Todo;
import com.klwork.business.domain.model.TodoQuery;
import com.klwork.business.domain.service.SocialSinaService;
import com.klwork.business.domain.service.TodoService;
import com.klwork.business.utils.HtmlTranslateImageTool;
import com.klwork.business.utils.SocialConfig;
import com.klwork.common.exception.ApplicationException;
import com.klwork.common.utils.StringTool;
import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;
import com.klwork.explorer.security.LoginHandler;

/**
 * The Class LoginController.
 */
@Controller
@RequestMapping("*")
public class OutRequestController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	TodoService todoService;
	
	@Autowired
	private SocialSinaService socialSinaService;


	/**
	 * 
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "show-todolist-img", method = RequestMethod.GET)
	public void showTodoListImage(HttpServletRequest request,HttpServletResponse response) {
		String proId = request.getParameter("proId");
		 String absolutePath = HtmlTranslateImageTool.currentTodoListImagePath(proId);
		response.setContentType("image/png");
		try {
			OutputStream os = response.getOutputStream();
			readBinFile(absolutePath, os);
		} catch (Exception e) {
			new ApplicationException("读取文件异常!");
		}
	}
	
	
	@RequestMapping(value = "show-todolist", method = RequestMethod.GET)
	public ModelAndView showTodolist(HttpServletRequest request) {
		String proId = request.getParameter("proId");
		TodoQuery query = new TodoQuery();
		query.setProId(proId).setPid("-1").setOrderBy(" pid asc,id asc");
		List<Todo> beanList = initTodoTree(query,null);
		ModelMap map = new ModelMap();
		map.put("todoList", beanList);
		return new ModelAndView("todolist/showTodolist", map);
	}
	
	@RequestMapping(value = "show-weibo-content", method = RequestMethod.GET)
	public ModelAndView showWeiboContent(HttpServletRequest request) {
		String proId = request.getParameter("proId");
		ModelMap map = new ModelMap();
		String s = "";
		map.put("senderName", "klwork");
		return new ModelAndView("weibo/weiboImageContent", map);
	}

	private TodoQuery initQuery(String projectId,String pid) {
		TodoQuery query = new TodoQuery();
		query.setProId(projectId).setPid(pid).setOrderBy(" pid asc,id asc");
		return query;
	}
	
	public List<Todo> initTodoTree(TodoQuery query,Todo parnet) {
		List<Todo> beanList = todoService.findTodoByQueryCriteria(query, null);
		if(parnet != null){//把查询处理的子类放到其中
			parnet.setChilds(beanList);
		}
		for (Iterator iterator = beanList.iterator(); iterator.hasNext();) {
			Todo todo = (Todo) iterator.next();
			boolean isContainer = StringTool.parseBoolean(todo.getIsContainer() + "");
			if (isContainer) {
				initTodoTree(initQuery(query.getProId(),todo.getId()),todo);
			}
		}
		return beanList;
	}


	/**
     * 读取二进制文件的内容，并以流的方式返回到页面
     * @param absolutePath
     * @param os
     * @throws Exception
     */
    private void readBinFile(String absolutePath, OutputStream os) throws Exception{
    	File file = new File(absolutePath);
    	FileInputStream fileIn = null;
    	if (file.exists()) {
   	 	    fileIn = new FileInputStream(file);
    	}
   	 	byte[] buffer = new byte[1024];
   	 	int len;
   	 	if(fileIn != null) {
   	 	while ((len = fileIn.read(buffer)) > 0)
		{
			os.write(buffer, 0, len);
		}
   	 	}
	   	if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error(e.getMessage(),e);
				}
			}
	   	if (fileIn != null) {
			try {
				fileIn.close();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
	    }
}
