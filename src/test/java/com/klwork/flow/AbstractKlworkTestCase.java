package com.klwork.flow;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.impl.util.IoUtil;

public class AbstractKlworkTestCase extends PluggableActivitiTestCase{
	
	protected void createGroup(String groupId, String type) {
		if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
			Group newGroup = identityService.newGroup(groupId);
			newGroup.setName(groupId.substring(0, 1).toUpperCase()
					+ groupId.substring(1));
			newGroup.setType(type);
			identityService.saveGroup(newGroup);
		}
	}
	
	 protected void createUser(String userId, String firstName, String lastName, String password, 
	          String email, String imageResource, List<String> groups, List<String> userInfo) {
	    
	    if (identityService.createUserQuery().userId(userId).count() == 0) {
	      
	      // Following data can already be set by demo setup script
	      
	      User user = identityService.newUser(userId);
	      user.setFirstName(firstName);
	      user.setLastName(lastName);
	      user.setPassword(password);
	      user.setEmail(email);
	      identityService.saveUser(user);
	      
	      if (groups != null) {
	        for (String group : groups) {
	          identityService.createMembership(userId, group);
	        }
	      }
	    }
	    
	    // Following data is not set by demo setup script
	      
	    // image
	    if (imageResource != null) {
	      byte[] pictureBytes = IoUtil.readInputStream(this.getClass().getClassLoader().getResourceAsStream(imageResource), null);
	      Picture picture = new Picture(pictureBytes, "image/jpeg");
	      identityService.setUserPicture(userId, picture);
	    }
	      
	    // user info
	    if (userInfo != null) {
	      for(int i=0; i<userInfo.size(); i+=2) {
	        identityService.setUserInfo(userId, userInfo.get(i), userInfo.get(i+1));
	      }
	    }
	    
	  }

}
