/*                                                                             
 * Copyright (C) 2019 Rison Han                                     
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");           
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
package com.hitachivantara.hcp.standard.model.metadata;

import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.util.StringUtils;
import com.hitachivantara.hcp.standard.define.ACLDefines;
import com.hitachivantara.hcp.standard.define.ACLDefines.ACLPermission;
import com.hitachivantara.hcp.standard.define.ACLDefines.Type;

public class ACLUserList {
	private List<String> users = new ArrayList<String>();

	public ACLUserList() {
	}
	
	public void removeUser(String userName) {
		userKey(ACLDefines.Type.USER, userName, null);
	}

	public void removeUser(String userName, String domain) {
		userKey(ACLDefines.Type.USER, userName, domain);
	}

	public void removeGroup(String groupName, String domain) {
		userKey(ACLDefines.Type.GROUP, groupName, domain);
	}

	public void removeAllUsers(String domain) {
		userKey(ACLDefines.Type.GROUP, ACLDefines.Name.ALL_USERS, domain);
	}

	public void removeAuthenticatedUsers(String domain) {
		userKey(ACLDefines.Type.GROUP, ACLDefines.Name.AUTHENTICATED, domain);
	}

	public void removeAllUsers(ACLPermission... permissions) {
		userKey(ACLDefines.Type.GROUP, ACLDefines.Name.ALL_USERS, null);
	}

	public void removeAuthenticatedUsers(ACLPermission... permissions) {
		userKey(ACLDefines.Type.GROUP, ACLDefines.Name.AUTHENTICATED, null);
	}

	public void userKey(ACLDefines.Type type, String userName, String domain) {
		users.add(genKey(type, userName, domain));
	}

	private String genKey(Type type, String userName, String domain) {
		return type.name() + StringUtils.nullToString(userName) + StringUtils.nullToString(domain);
	}

	public List<String> getUserKeys() {
		return users;
	}
	
	public int getSize() {
		return users.size();
	}

}
