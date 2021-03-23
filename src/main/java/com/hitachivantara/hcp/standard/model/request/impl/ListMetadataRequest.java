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

public class ListMetadataRequest extends HCPStandardRequest<ListMetadataRequest> implements ReqWithVersion<ListMetadataRequest>, ReqWithDeletedObject<ListMetadataRequest> {
	private boolean withDeletedObject = false;
	private String versionId = null;

	public ListMetadataRequest() {
		super(Method.GET);
	}

	public ListMetadataRequest(String key) {
		super(Method.GET, key);
	}

	public ListMetadataRequest(String key, String versionId) {
		super(Method.GET, key);
		this.versionId = versionId;
	}

	public ListMetadataRequest withDeletedObject(boolean with) {
		this.withDeletedObject = with;
		return this;
	}

	/**
	 * Get a specific old version
	 * 
	 * @param versionId
	 * @return
	 */
	public ListMetadataRequest withVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}

	public boolean isWithDeletedObject() {
		return withDeletedObject;
	}

	public String getVersionId() {
		return versionId;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}
}
