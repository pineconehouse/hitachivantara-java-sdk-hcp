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
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.model.metadata.AccessControlList;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;

public class PutACLRequest extends HCPStandardRequest<PutACLRequest> {// implements RCompressed{
	private AccessControlList acl = null;

	public PutACLRequest() {
		super(Method.PUT);
	}

	public PutACLRequest(String key) {
		super(Method.PUT, key);
	}

	public AccessControlList getAcl() {
		return acl;
	}

	public PutACLRequest withAcl(AccessControlList acl) {
		this.acl = acl;
		return this;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfNull(acl, "Parameter acl must be specified.");
	}

	// public boolean isSendingInCompressed() {
	// // TODO Auto-generated method stub
	// return false;
	// }

}
