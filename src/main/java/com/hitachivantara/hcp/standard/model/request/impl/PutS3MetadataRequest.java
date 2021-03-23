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
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;

public class PutS3MetadataRequest extends HCPStandardRequest<PutS3MetadataRequest> {
	private S3CompatibleMetadata s3CompatibleMetadata = null;

	public PutS3MetadataRequest() {
		super(Method.PUT);
	}

	public PutS3MetadataRequest(String key) {
		super(Method.PUT, key);
	}

	public PutS3MetadataRequest(String key, S3CompatibleMetadata s3CompatibleMetadata) {
		super(Method.PUT, key);
		this.withS3CompatibleMetadata(s3CompatibleMetadata);
	}

	public S3CompatibleMetadata getS3CompatibleMetadata() {
		return s3CompatibleMetadata;
	}

	public PutS3MetadataRequest withS3CompatibleMetadata(S3CompatibleMetadata s3CompatibleMetadata) {
		this.s3CompatibleMetadata = s3CompatibleMetadata;
		return this;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfNull(s3CompatibleMetadata, "Parameter s3CompatibleMetadata must be specified.");
	}

}
