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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.amituofo.common.api.ProgressListener;
import com.amituofo.common.define.ReadProgressEvent;
import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.io.DigestInputStream;
import com.amituofo.common.kit.io.DigestProgressInputStream;
import com.amituofo.common.kit.io.GZIPCompressedInputStream;
import com.amituofo.common.kit.io.ProgressInputStream;
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.common.define.HashAlgorithm;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithCompressed;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithVerifyContent;

/**
 * Storing an object or version of an object
 * 
 * @author sohan
 *
 */
public abstract class DefaultPutStreamRequest<T extends DefaultPutStreamRequest<?>> extends HCPStandardRequest<T> implements ReqWithCompressed, ReqWithVerifyContent<T> {
	/**
	 * Object input stream
	 */
	private InputStream objectInputStream;
	/**
	 * Sending data in compressed format
	 */
	private boolean sendingInCompressed = false;
	/**
	 * Content will be verified by MD5 by default
	 */
	private HashAlgorithm contentDigestAlgorithm = HashAlgorithm.MD5;
	/**
	 * Original file going to be upload
	 */
	private File file;

	private ProgressListener<ReadProgressEvent, Integer> progressListener = null;

	public DefaultPutStreamRequest() {
		super(Method.PUT);
	}

	public DefaultPutStreamRequest(String key) {
		super(Method.PUT, key);
	}

	public DefaultPutStreamRequest(String key, InputStream objectInputStream) {
		super(Method.PUT, key);
		this.withContent(objectInputStream);
	}

	public DefaultPutStreamRequest(String key, File file) throws IOException {
		super(Method.PUT, key);
		this.withContent(file);
	}

	/**
	 * Verifying the consistency between uploaded objects and local objects by digest
	 * 
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public T withVerifyContent(HashAlgorithm algorithm) throws NoSuchAlgorithmException {
		if (algorithm == null) {
			this.contentDigestAlgorithm = HashAlgorithm.NOOP;
		} else {
			this.contentDigestAlgorithm = algorithm;
		}
		return (T) this;
	}

	public T withVerifyContent(boolean verify) {
		if (verify) {
			this.contentDigestAlgorithm = HashAlgorithm.MD5;
		} else {
			this.contentDigestAlgorithm = HashAlgorithm.NOOP;
		}
		return (T) this;
	}

	public T withContent(File file) throws IOException {
		return (T) this.withContent(file, false);
	}

	/**
	 * @param file
	 * @param sendingInCompressed
	 *            Sending object data in compressed format
	 * @return
	 * @throws IOException
	 */
	public T withContent(File file, boolean sendingInCompressed) throws IOException {
		this.file = file;
		// if (sendingInCompressed) {
		// Open an input stream to the file that will be sent to HCP.
		// This file will be processed by the GZIPCompressedInputStream to
		// produce gzip-compressed content when read by the Apache HTTP client.
		// this.objectInputStream = new GZIPCompressedInputStream(new FileInputStream(file));
		// } else {
		this.objectInputStream = new FileInputStream(file);
		// inputStream.available();
		// }

		this.sendingInCompressed = sendingInCompressed;
		return (T) this;
	}

	public T withContent(byte[] bytes) {
		this.objectInputStream = new ByteArrayInputStream(bytes);
		this.sendingInCompressed = false;
		return (T) this;
	}

	public T withContent(String content) {
		return this.withContent(content.getBytes());
	}

	public T withContent(String content, String charsetName) throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(charsetName)) {
			return this.withContent(content.getBytes());
		}

		return this.withContent(content.getBytes(charsetName));
	}

	public T withContent(InputStream objectInputStream) {
		this.objectInputStream = objectInputStream;
		this.sendingInCompressed = false;
		return (T) this;
	}

	public T withProgressListener(ProgressListener<ReadProgressEvent, Integer> progressListener) {
		this.progressListener = progressListener;
		return (T) this;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfNull(objectInputStream, "Parameter Object Input Stream must be specified.");
	}

	public ProgressListener<ReadProgressEvent, Integer> getProgressListener() {
		return progressListener;
	}

	public boolean isSendingInCompressed() {
		return sendingInCompressed;
	}

	public InputStream getContent() {

		// new GZIPCompressedInputStream(new FileInputStream(file));
		if (contentDigestAlgorithm != HashAlgorithm.NOOP) {
			MessageDigest verifyContentDigest = null;
			try {
				verifyContentDigest = MessageDigest.getInstance(contentDigestAlgorithm.getKeyname());
			} catch (NoSuchAlgorithmException e) {
			}

			InputStream contentIn = null;
			if (progressListener != null) {
				contentIn = new DigestProgressInputStream(objectInputStream, verifyContentDigest, progressListener);
			} else {
				contentIn = new DigestInputStream(objectInputStream, verifyContentDigest);
			}

			if (sendingInCompressed) {
				// Open an input stream to the file that will be sent to HCP.
				// This file will be processed by the GZIPCompressedInputStream to
				// produce gzip-compressed content when read by the Apache HTTP client.
				return new GZIPCompressedInputStream(contentIn);
			} else {
				return contentIn;
			}
		} else {
			if (progressListener != null) {
				return new ProgressInputStream(objectInputStream, progressListener);
			}
			// if (!(objectInputStream instanceof HCPInputStream)) {
			// return new HCPInputStream(objectInputStream);
			// } else {
			// return objectInputStream;
			// }

			return objectInputStream;
		}
	}

	public boolean isVerifyContent() {
		return contentDigestAlgorithm != HashAlgorithm.NOOP;
	}

	public HashAlgorithm getContentDigestAlgorithm() {
		return contentDigestAlgorithm;
	}

	public File getSourceFile() {
		return file;
	}

}
