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
package com.hitachivantara.hcp.management.model.builder;

import com.amituofo.common.api.Builder;
import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.management.define.Role;
import com.hitachivantara.hcp.management.model.UserAccount;

public abstract class BasicUserAcccountBuilder<T extends BasicUserAcccountBuilder<T>> implements Builder<UserAccount, HSCException> {
	UserAccount userAccount = new UserAccount();

	public BasicUserAcccountBuilder() {
	}

	public T withUserName(String username, String fullName) {
		userAccount.setUsername(username);
		userAccount.setFullName(fullName);
		return (T) this;
	}

	public T withEnable(boolean enable) {
		userAccount.setEnabled(enable);
		return (T) this;
	}

	public T withDescription(String description) {
		userAccount.setDescription(description);
		return (T) this;
	}

	public T withForcePasswordChange(boolean enable) {
		userAccount.setForcePasswordChange(enable);
		return (T) this;
	}

	public T withAllowNamespaceManagement(boolean enable) {
		userAccount.setAllowNamespaceManagement(enable);
		return (T) this;
	}

	public T withRole(Role... r) {
		userAccount.setRoles(r);
		return (T) this;
	}

	public T withPassword(String password) {
		userAccount.setPassword(password);
		return (T) this;
	}
	
}
