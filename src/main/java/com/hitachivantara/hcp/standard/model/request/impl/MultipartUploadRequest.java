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
import com.hitachivantara.hcp.standard.model.request.content.ReqWithUploadId;

public class MultipartUploadRequest extends HCPStandardRequest<MultipartUploadRequest> implements ReqWithUploadId<MultipartUploadRequest> {
	private String uploadId = null;

//	public MultipartUploadRequest() {
//		super(Method.POST);
//	}

	public MultipartUploadRequest(String key) {
		super(Method.POST, key);
	}

	public MultipartUploadRequest(String key, String uploadId) {
		super(Method.POST, key);
		this.uploadId = uploadId;
	}

	public MultipartUploadRequest withUploadId(String uploadId) {
		this.uploadId = uploadId;
		return this;
	}

	public String getUploadId() {
		return uploadId;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}

}
