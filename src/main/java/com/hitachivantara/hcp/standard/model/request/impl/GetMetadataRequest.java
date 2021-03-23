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
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithNowait;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion;

public class GetMetadataRequest extends HCPStandardRequest<GetMetadataRequest> implements ReqWithVersion<GetMetadataRequest>, ReqWithNowait<GetMetadataRequest> {
	private boolean nowait = false;
	private String metadataName = null;
	private String versionId = null;

	public GetMetadataRequest() {
		super(Method.GET);
	}

	public GetMetadataRequest(String key, String metadataName) {
		super(Method.GET, key);
		this.metadataName = metadataName;
	}

	/**
	 * Get a specific old version
	 * 
	 * @param versionId
	 * @return
	 */
	public GetMetadataRequest withVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}

	/**
	 * Choosing not to wait for delayed retrievals HCP may detect that a GET request will take a significant amount of time to return the
	 * annotation. You can choose to have the request fail in this situation instead of waiting for HCP to return the annotation. To do this, in
	 * addition to specifying the request elements listed above, use the nowait URL query parameter.
	 * 
	 * @return
	 */
	public GetMetadataRequest withNowait(boolean with) {
		this.nowait = with;
		return this;
	}

	@Override
	public void validParameter() throws InvalidParameterException {
		super.validParameter();
		ValidUtils.invalidIfEmpty(metadataName, "The Annotation name must be specified.");
	}

	public String getMetadataName() {
		return metadataName;
	}

	public String getVersionId() {
		return versionId;
	}

	public boolean isNowait() {
		return nowait;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}

}
