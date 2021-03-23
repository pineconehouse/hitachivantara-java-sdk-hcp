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

import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.standard.model.request.DefaultMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion;

/**
 * Request for check whether an object or specific version of an object has a specific meta data.
 * @author sohan
 *
 */
public class CheckMetadataRequest extends DefaultMetadataRequest<CheckMetadataRequest> implements ReqWithVersion<CheckMetadataRequest>  {
	private String versionId = null;

	public CheckMetadataRequest() {
		super(Method.HEAD);
	}

	public CheckMetadataRequest(String key) {
		super(Method.HEAD, key);
	}

	public CheckMetadataRequest(String key, String metadataName) {
		super(Method.HEAD, key, metadataName);
	}

	/**
	 * Get a specific old version
	 * 
	 * @param versionId
	 * @return
	 */
	public CheckMetadataRequest withVersionId(String versionId) {
		this.versionId = versionId;
		return (CheckMetadataRequest) this;
	}

	public String getVersionId() {
		return versionId;
	}
}
