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
package com.hitachivantara.hcp.management.model;

import com.amituofo.common.util.StringUtils;
import com.hitachivantara.hcp.management.define.Role;

public class UserAccount {
	private String fullName;
	private String description;
	private Boolean allowNamespaceManagement;
	private Boolean forcePasswordChange;
	private Boolean enabled;
	private String username;
	private Role[] roles;
	private Integer userID;
	private Boolean localAuthentication;
	private String userGUID;

	private String password;

	public UserAccount() {
	}

	public UserAccount(String fullName,
			String description,
			Integer userID,
			Boolean allowNamespaceManagement,
			Boolean forcePasswordChange,
			Boolean localAuthentication,
			Boolean enabled,
			String username,
			String userGUID,
			Role[] roles) {
		super();
		this.fullName = fullName;
		this.description = description;
		this.userID = userID;
		this.allowNamespaceManagement = allowNamespaceManagement;
		this.forcePasswordChange = forcePasswordChange;
		this.localAuthentication = localAuthentication;
		this.enabled = enabled;
		this.username = username;
		this.userGUID = userGUID;
		this.roles = roles;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAllowNamespaceManagement() {
		return allowNamespaceManagement;
	}

	public void setAllowNamespaceManagement(Boolean allowNamespaceManagement) {
		this.allowNamespaceManagement = allowNamespaceManagement;
	}

	public Boolean getForcePasswordChange() {
		return forcePasswordChange;
	}

	public void setForcePasswordChange(Boolean forcePasswordChange) {
		this.forcePasswordChange = forcePasswordChange;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
	}

	public boolean hasRole(Role role) {
		if (roles != null) {
			for (Role r : roles) {
				if (role == r) {
					return true;
				}
			}
		}

		return false;
	}

	public Boolean getLocalAuthentication() {
		return localAuthentication;
	}

	public void setLocalAuthentication(Boolean localAuthentication) {
		this.localAuthentication = localAuthentication;
	}

	public Integer getUserID() {
		return userID;
	}

	public String getUserGUID() {
		return userGUID;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean hasNewPassword() {
		return StringUtils.isNotEmpty(password);
	}

}
