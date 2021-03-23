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

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.common.define.HashAlgorithm;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.model.request.DefaultPutStreamRequest;

public class UploadPartRequest extends DefaultPutStreamRequest<UploadPartRequest> {
	private int partNumber;
	private long length;

	public UploadPartRequest() {
		super(null);
	}

	public UploadPartRequest(int partNumber, InputStream in, long length) {
		super(null, in);

		this.partNumber = partNumber;
		this.length = length;
	}

	public int getPartNumber() {
		return partNumber;
	}

	public long getLength() {
		return length;
	}

	@Override
	public UploadPartRequest withVerifyContent(HashAlgorithm algorithm) throws NoSuchAlgorithmException {
		if (algorithm != HashAlgorithm.MD5 && algorithm != HashAlgorithm.NOOP) {
			throw new NoSuchAlgorithmException("Also support MD5 hash algorithm when using multipart put!");
		}
		
		return super.withVerifyContent(algorithm);
	}

	public void validRequestParameter() throws InvalidParameterException {
		// if (this.content != null && !this.getKey().equals(".")) {
		// throw new InvalidParameterException("");
		// }
		super.validRequestParameter();

		ValidUtils.invalidIfTrue(partNumber <= 0, "The partNumber in must be > 0.");
		ValidUtils.invalidIfTrue(length <= 0, "The parameter length must be > 0.");
	}

}
