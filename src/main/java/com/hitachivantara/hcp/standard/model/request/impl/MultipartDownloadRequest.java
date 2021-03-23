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

/**
 * Retrieving an object or multiple versions of an object
 * 
 * @author sohan
 *
 */
public class MultipartDownloadRequest extends HCPStandardRequest<MultipartDownloadRequest> {
	public static final int MAXIMUM_MULTIPART_DOWNLOAD_PART_SIZE = 31;
	public static final int MINIMUM_OBJECT_SIZE = 1024 * 1024 * 50; // 50MB
	public static final int DEFAULT_PARTS = 8;

	private GetObjectRequest request = new GetObjectRequest();
	private int parts = DEFAULT_PARTS;
	private long[] partsBeginPosition = new long[DEFAULT_PARTS];
	private long minimumObjectSize = MINIMUM_OBJECT_SIZE;

	private boolean waitForComplete = false;

	public MultipartDownloadRequest() {
		super(Method.GET);
	}

	public MultipartDownloadRequest(String key) {
		super(Method.GET, key);
		request.withKey(key);
	}

	public MultipartDownloadRequest(String key, String versionId) {
		super(Method.GET, key);
		request.withKey(key).withVersionId(versionId);
	}

	/**
	 * By default, the request to retrieve object versions does not include deleted versions (that is,
	 * the marker versions that indicate when an object was deleted). To retrieve a listing that
	 * includes deleted versions
	 * 
	 * @param with
	 * @return
	 */
	public MultipartDownloadRequest withDeletedObject(boolean with) {
		request.withDeletedObject(with);
		return this;
	}

	/**
	 * To force HCP to generate an ETag for an object that does not yet have one, specify a forceEtag
	 * URL query parameter with a value of true
	 * 
	 * @param with
	 * @return
	 */
	public MultipartDownloadRequest withForceGenerateEtag(boolean with) {
		request.withForceGenerateEtag(with);
		return this;
	}

	/**
	 * Choosing not to wait for delayed retrievals HCP may detect that a GET request will take a
	 * significant amount of time to return an object. You can choose to have the request fail in this
	 * situation instead of waiting for HCP to return the object. To do this, use the nowait URL query
	 * parameter.
	 * 
	 * When a GET request fails because the request would take a significant amount of time to return an
	 * object and the nowait parameter is specified, HCP returns an HTTP 503 (Service Unavailable) error
	 * code.
	 * 
	 * @return
	 */
	public MultipartDownloadRequest withNowait(boolean with) {
		request.withNowait(with);
		return this;
	}

	/**
	 * Get a specific old version
	 * 
	 * @param versionId
	 * @return
	 */
	public MultipartDownloadRequest withVersion(String versionId) {
		request.withVersionId(versionId);
		return this;
	}

	public MultipartDownloadRequest withWaitForComplete(boolean with) {
		this.waitForComplete = with;
		return this;
	}

	/**
	 * @param parts
	 * @param partsBeginPosition
	 *            For breakpoint resume
	 * @return
	 */
	public MultipartDownloadRequest withParts(int parts, long[] partsBeginPosition) {
		if (parts <= 0) {
			this.parts = 1;
		} else {
			this.parts = parts;
		}

		if (partsBeginPosition == null) {
			this.partsBeginPosition = new long[this.parts];
		} else {
			this.partsBeginPosition = partsBeginPosition;
		}

		for (int i = 0; i < this.partsBeginPosition.length; i++) {
			if (this.partsBeginPosition[i] < 0) {
				this.partsBeginPosition[i] = 0;
			}
		}

		return this;
	}

	public MultipartDownloadRequest withParts(int parts) {
		return this.withParts(parts, null);
	}

	public MultipartDownloadRequest withMinimumObjectSize(long minimumObjectSize) {
		if (minimumObjectSize < MINIMUM_OBJECT_SIZE) {
			this.minimumObjectSize = MINIMUM_OBJECT_SIZE;
		} else {
			this.minimumObjectSize = minimumObjectSize;
		}

		return this;
	}


	public boolean isWaitForComplete() {
		return waitForComplete;
	}

	public boolean isWithDeletedObject() {
		return request.isWithDeletedObject();
	}

	public String getVersionId() {
		return request.getVersionId();
	}

	public boolean isNowait() {
		return request.isNowait();
	}

	public boolean isForceEtag() {
		return request.isForceEtag();
	}

	public int getParts() {
		return parts;
	}

	public long[] getPartsBeginPosition() {
		return partsBeginPosition;
	}

	public long getMinimumObjectSize() {
		return minimumObjectSize;
	}

	public GetObjectRequest getRequest() {
		return request;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		request.validRequestParameter();
	}

}
