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

import com.hitachivantara.hcp.standard.define.ACLDefines;
import com.hitachivantara.hcp.standard.define.ACLDefines.ACLPermission;
import com.hitachivantara.hcp.standard.define.ACLDefines.Type;

public class PermissionGrant {
	private ACLDefines.Type type;
	private String userName;
	private String domain;
	private ACLPermission[] permissions = new ACLPermission[0];

	public PermissionGrant() {
	}

	public PermissionGrant(Type type, String userName, String domain, ACLPermission[] permissions) {
		super();
		this.type = type;
		this.userName = userName;
		this.domain = domain;
		this.permissions = (permissions == null ? new ACLPermission[0] : permissions);
	}

	public ACLDefines.Type getType() {
		return type;
	}

	public void setType(ACLDefines.Type type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public ACLPermission[] getPermissions() {
		return permissions;
	}

	public void setPermissions(ACLPermission[] permissions) {
		this.permissions = (permissions == null ? new ACLPermission[0] : permissions);
	}

	public boolean hasPermission(ACLPermission permission) {
		if (permissions.length > 0)
			for (ACLPermission aclPermission : permissions) {
				if (permission == aclPermission) {
					return true;
				}
			}

		return false;
	}

}
