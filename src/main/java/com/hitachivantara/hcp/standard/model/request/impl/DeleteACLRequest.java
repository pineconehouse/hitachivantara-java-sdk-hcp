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
package com.hitachivantara.hcp.standard.model.request.impl;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.standard.model.metadata.ACLUserList;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;

public class DeleteACLRequest extends HCPStandardRequest<DeleteACLRequest> {
	private ACLUserList userList;
	
	public DeleteACLRequest() {
		super(Method.DELETE);
	}

	public DeleteACLRequest(String key) {
		super(Method.DELETE, key);
	}
	
	public DeleteACLRequest withUserList(ACLUserList userList) {
		this.userList = userList;
		return this;
	}
	
	public ACLUserList getUserList() {
		return userList;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}
}
