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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.DefaultPutStreamRequest;

/**
 * Storing an object or version of an object
 * 
 * @author sohan
 *
 */
public class PutObjectRequest extends DefaultPutStreamRequest<PutObjectRequest> {
	/**
	 * Storing object data and simple metadata together
	 */
	private S3CompatibleMetadata metadata = null;
	/**
	 * Storing object data and custom metadata together
	 */
	private PutMetadataRequest putMetadataRequest = null;

	public PutObjectRequest() {
		super();
	}

	public PutObjectRequest(String key) {
		super(key);
	}

	public PutObjectRequest(String key, InputStream objectInputStream) {
		super(key, objectInputStream);
	}

	public PutObjectRequest(String key, File file) throws IOException {
		super(key, file);
	}

	public PutObjectRequest withMetadata(S3CompatibleMetadata metadata) {
		this.metadata = metadata;
		return this;
	}

	// public PutObjectRequest withMetadata(String metadataName, InputStream metadataContent, boolean sendingMetadataInCompressed) throws
	// IOException {
	// putMetadataRequest = new PutMetadataRequest(this.getKey(), metadataName, metadataContent, sendingMetadataInCompressed);
	// return this;
	// }

	public PutObjectRequest withMetadata(String metadataName, File metadataContent, boolean sendingMetadataInCompressed) throws IOException {
		putMetadataRequest = new PutMetadataRequest(this.getKey(), metadataName, metadataContent, sendingMetadataInCompressed);
		return this;
	}

	public PutObjectRequest withMetadata(String metadataName, byte[] metadataContent) throws IOException {
		putMetadataRequest = new PutMetadataRequest(this.getKey(), metadataName, metadataContent);
		return this;
	}

	public PutObjectRequest withMetadata(String metadataName, String metadataContent, String charsetName) throws IOException {
		putMetadataRequest = new PutMetadataRequest(this.getKey(), metadataName, metadataContent.getBytes(charsetName));
		return this;
	}

	public PutObjectRequest withMetadata(String metadataName, InputStream metadataContent) {
		putMetadataRequest = new PutMetadataRequest(this.getKey(), metadataName, metadataContent);
		return this;
	}

	public PutObjectRequest withMetadata(String metadataName, File file) throws IOException {
		putMetadataRequest = new PutMetadataRequest(this.getKey(), metadataName, file, false);
		return this;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		super.validRequestParameter();
		if (putMetadataRequest != null) {
			putMetadataRequest.validParameter();
		}
	}

	public S3CompatibleMetadata getS3CompatibleMetadata() {
		return metadata;
	}

	public InputStream getMetadataContent() {
		if (putMetadataRequest != null) {
			return putMetadataRequest.getContent();
		}
		return null;
	}

	public boolean isSendingMetadataInCompressed() {
		if (putMetadataRequest != null) {
			return putMetadataRequest.isSendingInCompressed();
		}
		return false;
	}

	public String getMetadataName() {
		if (putMetadataRequest != null) {
			return putMetadataRequest.getMetadataName();
		}
		return null;
	}

}
