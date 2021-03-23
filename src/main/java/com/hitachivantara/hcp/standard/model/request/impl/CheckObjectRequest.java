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
import com.hitachivantara.hcp.standard.model.request.content.ReqWithDeletedObject;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion;

/**
 * check whether an object, a version of an object, or a range of versions of an object exists in a namespace. 
 * 
 * @author sohan
 *
 */
public class CheckObjectRequest extends HCPStandardRequest<CheckObjectRequest> implements ReqWithVersion<CheckObjectRequest>, ReqWithDeletedObject<CheckObjectRequest> {
	private boolean withDeletedObject = false;
	private String versionId = null;

	public CheckObjectRequest() {
		super(Method.HEAD);
	}

	public CheckObjectRequest(String key) {
		super(Method.HEAD, key);
	}

	public CheckObjectRequest(String key, String versionId) {
		super(Method.HEAD, key);
		this.versionId = versionId;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithDeletedObject#withDeletedObject(boolean)
	 */
	public CheckObjectRequest withDeletedObject(boolean with) {
		this.withDeletedObject = with;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion#withVersionId(java.lang.String)
	 */
	public CheckObjectRequest withVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithDeletedObject#isWithDeletedObject()
	 */
	public boolean isWithDeletedObject() {
		return withDeletedObject;
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
