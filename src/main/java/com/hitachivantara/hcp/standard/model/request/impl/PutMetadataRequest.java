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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.io.GZIPCompressedInputStream;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithCompressed;

public class PutMetadataRequest extends HCPStandardRequest<PutMetadataRequest> implements ReqWithCompressed {
	private InputStream inputStream = null;
	private boolean sendingInCompressed = false;
	private String metadataName;

	public PutMetadataRequest() {
		super(Method.PUT);
	}

	public PutMetadataRequest(String key) {
		super(Method.PUT, key);
	}

	public PutMetadataRequest(String key, String metadataName, File file, boolean sendingInCompressed) throws IOException {
		super(Method.PUT, key);
		this.withMetadataName(metadataName);
		this.withContent(file, sendingInCompressed);
	}

	public PutMetadataRequest(String key, String metadataName, InputStream inputStream, boolean sendingInCompressed) throws IOException {
		super(Method.PUT, key);
		this.withMetadataName(metadataName);
		this.withContent(inputStream, sendingInCompressed);
	}

	public PutMetadataRequest(String key, String metadataName, InputStream inputStream) {
		super(Method.PUT, key);
		this.withMetadataName(metadataName);
		this.withContent(inputStream);
	}

	public PutMetadataRequest(String key, String metadataName, byte[] bytes) {
		super(Method.PUT, key);
		this.withMetadataName(metadataName);
		this.withContent(bytes);
	}

	public PutMetadataRequest withMetadataName(String metadataName) {
		this.metadataName = metadataName;
		return this;
	}

	public PutMetadataRequest withContent(byte[] bytes) {
		this.inputStream = new ByteArrayInputStream(bytes);
		this.sendingInCompressed = false;
		return this;
	}

	public PutMetadataRequest withContent(String content) {
		return this.withContent(content.getBytes());
	}

	public PutMetadataRequest withContent(String content, String charsetName) throws UnsupportedEncodingException {
		return this.withContent(content.getBytes(charsetName));
	}

	public PutMetadataRequest withContent(InputStream inputStream) {
		this.inputStream = inputStream;
		this.sendingInCompressed = false;
		return this;
	}

	/**
	 * @param file
	 * @param sendingInCompressed
	 *            Sending object data in compressed format
	 * @return
	 * @throws IOException
	 */
	public PutMetadataRequest withContent(File file, boolean sendingInCompressed) throws IOException {
		this.withContent(new FileInputStream(file), sendingInCompressed);
		return this;
	}

	private PutMetadataRequest withContent(InputStream inputStream, boolean sendingInCompressed) throws IOException {
		if (sendingInCompressed) {
			// send an annotation in compressed format and have HCP decompress the data before storing it. To do
			// this, in addition to specifying the request elements listed above:
			// Use gzip to compress the content before sending it.
			this.inputStream = new GZIPCompressedInputStream(inputStream);
		} else {
			this.inputStream = inputStream;
			// inputStream.available();
		}
		this.sendingInCompressed = sendingInCompressed;
		return this;
	}

	public InputStream getContent() {
		return inputStream;
	}

	public boolean isSendingInCompressed() {
		return sendingInCompressed;
	}

	public String getMetadataName() {
		return metadataName;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfEmpty(metadataName, "Parameter Metadata Name must be specified.");
		ValidUtils.invalidIfNull(inputStream, "Parameter Metadata Input Stream must be specified.");

		ValidUtils.invalidIfContainsChar(metadataName, " ~`!@#$%^&*()+={}[]|\\:;\"'<>,?/", "Metadata name contains invalid char.");
	}

}
