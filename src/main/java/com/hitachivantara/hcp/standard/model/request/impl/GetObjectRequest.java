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
import com.hitachivantara.hcp.standard.model.request.content.ReqWithDeletedObject;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithNowait;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithRange;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion;

/**
 * Retrieving an object or multiple versions of an object
 * 
 * @author sohan
 *
 */
public class GetObjectRequest extends HCPStandardRequest<GetObjectRequest>
		implements ReqWithVersion<GetObjectRequest>, ReqWithRange<GetObjectRequest>, ReqWithNowait<GetObjectRequest>, ReqWithDeletedObject<GetObjectRequest> {
	/**
	 * Wait for delayed retrievals
	 */
	private boolean nowait = false;
	/**
	 * Include deleted versions
	 */
	private boolean withDeletedObject = false;
	/**
	 * Forcing the generation of an ETag
	 */
	private boolean forceEtag = false;
	/**
	 * Requesting partial data
	 */
	private Long[] range = null;
	/**
	 * Requesting a specific old version
	 */
	private String versionId = null;
	
	private boolean getInCompressed;

	public GetObjectRequest() {
		super(Method.GET);
	}

	public GetObjectRequest(String key) {
		super(Method.GET, key);
	}

	public GetObjectRequest(String key, String versionId) {
		super(Method.GET, key);
		this.versionId = versionId;
	}

	public GetObjectRequest(String key, long beginOffset, long endOffset) {
		super(Method.GET, key);
		this.range = new Long[2];
		this.range[0] = beginOffset;
		this.range[1] = endOffset;
	}

	/**
	 * By default, the request to retrieve object versions does not include deleted versions (that is, the marker versions that indicate when an
	 * object was deleted). To retrieve a listing that includes deleted versions
	 * 
	 * @param with
	 * @return
	 */
	public GetObjectRequest withDeletedObject(boolean with) {
		this.withDeletedObject = with;
		return this;
	}

	/**
	 * To force HCP to generate an ETag for an object that does not yet have one, specify a forceEtag URL query parameter with a value of true
	 * 
	 * @param with
	 * @return
	 */
	public GetObjectRequest withForceGenerateEtag(boolean with) {
		this.forceEtag = with;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithRange#withRange(long, long)
	 */
	public GetObjectRequest withRange(long beginPosition, long endPosition) {
		this.range = new Long[2];
		this.range[0] = beginPosition;
		this.range[1] = endPosition;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithRange#withRange(long)
	 */
	@Override
	public GetObjectRequest withRange(long beginPosition) {
		this.range = new Long[2];
		this.range[0] = beginPosition;
		this.range[1] = -1L;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithRange#withLastBytes(long)
	 */
	@Override
	public GetObjectRequest withLastRange(long offsetFromEnd) {
		this.range = new Long[2];
		this.range[0] = -1L;
		this.range[1] = offsetFromEnd;
		return this;
	}

	/**
	 * Choosing not to wait for delayed retrievals HCP may detect that a GET request will take a significant amount of time to return an object.
	 * You can choose to have the request fail in this situation instead of waiting for HCP to return the object. To do this, use the nowait URL
	 * query parameter.
	 * 
	 * When a GET request fails because the request would take a significant amount of time to return an object and the nowait parameter is
	 * specified, HCP returns an HTTP 503 (Service Unavailable) error code.
	 * 
	 * @return
	 */
	public GetObjectRequest withNowait(boolean with) {
		this.nowait = with;
		return this;
	}

	/**
	 * Get a specific old version
	 * 
	 * @param versionId
	 * @return
	 */
	public GetObjectRequest withVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}
	
	/**
	 * HCP supports only the gzip algorithm for compressed data transmission.
	 * You can use GZIPInputStream to unzip the content returned by HCP
	 * @param with
	 * @return
	 */
	public GetObjectRequest withGetInCompressed(boolean with) {
		this.getInCompressed = with;
		return this;
	}

	public boolean isWithDeletedObject() {
		return withDeletedObject;
	}

	public Long[] getRange() {
		return range;
	}

	public String getVersionId() {
		return versionId;
	}

	public boolean isNowait() {
		return nowait;
	}

	public boolean isForceEtag() {
		return forceEtag;
	}
	
	public boolean isGetInCompressed() {
		return getInCompressed;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		if (withDeletedObject) {
			ValidUtils.invalidIfEmpty(versionId, "Version id must be specified when retrieving deleted object.");
		}

		if (range != null) {
			long beginPosition = range[0];
			long endPosition = range[1];
			ValidUtils.invalidIfZero(endPosition, "End position must be specified like -1 or > 0.");
			ValidUtils.invalidIfTrue(beginPosition >= endPosition, "Range position must be different.");
			ValidUtils.invalidIfTrue(beginPosition < 0 && endPosition < 0, "Start position or End position must be specified.");
		}
	}

}
