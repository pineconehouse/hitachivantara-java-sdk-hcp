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
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithRange;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithSourceVersion;

/**
 * Upload a part of a multipart upload by copying all or part of the data for an existing object.
 * @author sohan
 *
 */
public class CopyPartRequest extends HCPStandardRequest<CopyPartRequest> implements ReqWithSourceVersion<CopyPartRequest>, ReqWithRange<CopyPartRequest> {
	private int partNumber;
	private String sourceVersionId = null;
	private Long[] range = null;
	private KeyAlgorithm sourceKeyAlgorithm = KeyAlgorithm.DEFAULT;

	public CopyPartRequest() {
		super(Method.PUT);
	}

	public CopyPartRequest(int partNumber, String sourceKey) {
		super(Method.PUT, sourceKey);
		this.partNumber = partNumber;
	}

	public CopyPartRequest(int partNumber, String sourceNamespace, String sourceKey, String sourceVersionId) {
		super(Method.PUT, sourceNamespace, sourceKey);
		this.partNumber = partNumber;
		this.sourceVersionId = sourceVersionId;
	}

	public CopyPartRequest withSourceNamespace(String sourceNamespace) {
		this.withNamespace(sourceNamespace);
		return this;
	}

	public CopyPartRequest withSourceKey(String sourceKey) {
		this.withKey(sourceKey);
		return this;
	}

	public CopyPartRequest withSourceKeyAlgorithm(KeyAlgorithm sourceKeyAlgorithm) {
		if (sourceKeyAlgorithm != null) {
			this.sourceKeyAlgorithm = sourceKeyAlgorithm;
		} else {
			this.sourceKeyAlgorithm = KeyAlgorithm.DEFAULT;
		}
		return this;
	}

	public CopyPartRequest withSourceVersion(String sourceVersionId) {
		this.sourceVersionId = sourceVersionId;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithRange#withRange(long, long)
	 */
	public CopyPartRequest withRange(long beginPosition, long endPosition) {
		this.range = new Long[2];
		this.range[0] = beginPosition;
		this.range[1] = endPosition;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithRange#withRange(long)
	 */
	@Override
	public CopyPartRequest withRange(long beginPosition) {
		this.range = new Long[2];
		this.range[0] = beginPosition;
		this.range[1] = -1L;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithRange#withLastBytes(long)
	 */
	@Override
	public CopyPartRequest withLastRange(long offsetFromEnd) {
		this.range = new Long[2];
		this.range[0] = -1L;
		this.range[1] = offsetFromEnd;
		return this;
	}

	public int getPartNumber() {
		return partNumber;
	}

	public String getSourceNamespace() {
		return this.getNamespace();
	}

	public String getSourceKey() {
		return this.getKey();
	}

	public KeyAlgorithm getSourceKeyAlgorithm() {
		return sourceKeyAlgorithm;
	}

	public String getSourceVersionId() {
		return sourceVersionId;
	}

	public Long[] getRange() {
		return range;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfTrue(partNumber <= 0, "The partNumber in must be > 0.");

		if (range != null) {
			long beginPosition = range[0];
			long endPosition = range[1];
			ValidUtils.invalidIfZero(endPosition, "End position must be specified like -1 or > 0.");
			ValidUtils.invalidIfTrue(beginPosition >= endPosition, "Range position must be different.");
			ValidUtils.invalidIfTrue(beginPosition < 0 && endPosition < 0, "Start position or End position must be specified.");
		}
	}
}
