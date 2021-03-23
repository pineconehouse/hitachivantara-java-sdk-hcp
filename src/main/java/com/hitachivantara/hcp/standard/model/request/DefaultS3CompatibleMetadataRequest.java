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
package com.hitachivantara.hcp.standard.model.request;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion;

public class DefaultS3CompatibleMetadataRequest<T extends DefaultS3CompatibleMetadataRequest<?>> extends HCPStandardRequest<T> implements ReqWithVersion<T> {
	private String versionId = null;

	public DefaultS3CompatibleMetadataRequest() {
		super(Method.HEAD);
	}

	public DefaultS3CompatibleMetadataRequest(String key) {
		super(Method.HEAD, key);
	}

	/**
	 * Get a specific old version
	 * 
	 * @param versionId
	 * @return
	 */
	public T withVersionId(String versionId) {
		this.versionId = versionId;
		return (T) this;
	}

	public String getVersionId() {
		return versionId;
	}

	public String getMetadataName() {
		return S3CompatibleMetadata.DEFAULT_METADATA_NAME;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}

}
