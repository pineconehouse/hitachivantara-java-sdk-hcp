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
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion;

/**
 * Request for check whether an object or version of an object has an ACL
 * 
 * @author sohan
 *
 */
public class CheckACLRequest extends HCPStandardRequest<CheckACLRequest> implements ReqWithVersion<CheckACLRequest> {
	private String versionId = null;

	public CheckACLRequest() {
		super(Method.HEAD);
	}

	/**
	 * @param key
	 */
	public CheckACLRequest(String key) {
		super(Method.HEAD, key);
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion#withVersionId(java.lang.String)
	 */
	public CheckACLRequest withVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion#getVersionId()
	 */
	public String getVersionId() {
		return versionId;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}
}
