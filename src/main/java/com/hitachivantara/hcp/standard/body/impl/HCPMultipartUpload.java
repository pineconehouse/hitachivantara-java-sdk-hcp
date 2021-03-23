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
package com.hitachivantara.hcp.standard.body.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.io.DigestInputStream;
import com.amituofo.common.kit.io.DigestProgressInputStream;
import com.amituofo.common.kit.io.GZIPCompressedInputStream;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.ReflectUtils;
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.core.http.content.InputStreamEntity;
import com.hitachivantara.hcp.common.define.Constants;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.common.define.HCPRequestHeadValue.CONTENT_TYPE;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.api.MultipartUpload;
import com.hitachivantara.hcp.standard.body.HCPHttpRestClient;
import com.hitachivantara.hcp.standard.body.HCPNamespaceBase;
import com.hitachivantara.hcp.standard.define.RequestParamKey;
import com.hitachivantara.hcp.standard.model.PartETag;
import com.hitachivantara.hcp.standard.model.UploadPartList;
import com.hitachivantara.hcp.standard.model.UploadPartResult;
import com.hitachivantara.hcp.standard.model.converter.HCPRestResponseHandler;
import com.hitachivantara.hcp.standard.model.request.DefaultMultipartRequest;
import com.hitachivantara.hcp.standard.model.request.HCPRequestHeaders;
import com.hitachivantara.hcp.standard.model.request.HCPRequestParams;
import com.hitachivantara.hcp.standard.model.request.impl.CopyPartRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListUploadedPartsRequest;
import com.hitachivantara.hcp.standard.model.request.impl.UploadPartRequest;
import com.hitachivantara.hcp.standard.util.S3SignerUtils;

public class HCPMultipartUpload implements MultipartUpload {
	private final HCPHttpRestClient httpS3Client;
	private final HCPNamespaceBase hcpClient;

	private final DefaultMultipartRequest stdRequest = new DefaultMultipartRequest();

	public HCPMultipartUpload(HCPNamespaceBase hcpClient, String namespace, String key, String uploadId) {
		this.hcpClient = hcpClient;
		this.httpS3Client = new HCPHttpRestClient(hcpClient, "/");
		this.stdRequest.withKey(key).withUploadId(uploadId).withNamespace(namespace);
	}

	public String getNamespace() {
		return stdRequest.getNamespace();
	}

	public String getUploadId() {
		return stdRequest.getUploadId();
	}

	public String getKey() {
		return stdRequest.getKey();
	}

	public String initiate() throws InvalidResponseException, HSCException {
		stdRequest.validParameter();

		HCPRequestHeaders header = stdRequest.customHeader();
		HCPRequestParams param = stdRequest.customParameter();
		param.clear();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_OCTET_STREAM);

		param.setParameter(RequestParamKey.UPLOADS, null);

		S3SignerUtils.generateS3Signer(hcpClient, Method.POST, stdRequest.getNamespace(), stdRequest.getKey(), header, param);

		String uploadId = httpS3Client.execute(stdRequest, HCPRestResponseHandler.MULTIUP_INIT_RESPONSE_HANDLER);
		stdRequest.withUploadId(uploadId);

		return uploadId;
	}

	public UploadPartResult uploadPart(int partNumber, InputStream in, long length) throws InvalidResponseException, HSCException {
		UploadPartRequest request = new UploadPartRequest(partNumber, in, length);

		return uploadPart(request);
	}

	public UploadPartResult uploadPart(int partNumber, File file, long offset, long length) throws InvalidResponseException, HSCException {
		InputStream in = null;
		try {
			in = new FileInputStream(file);

			UploadPartRequest request = new UploadPartRequest(partNumber, in, length);

			return uploadPart(request);
		} catch (FileNotFoundException e) {
			throw new InvalidParameterException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public UploadPartResult copyPart(CopyPartRequest request) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfEmpty(stdRequest.getUploadId(), "The parameter uploadId must be specified.");
		// Perform the default request parameter check
		ValidUtils.validateRequest(request);

		String key = StringUtils.notEmptyValue(request.getKey(), stdRequest.getKey());
		String namespace = StringUtils.notEmptyValue(request.getNamespace(), stdRequest.getNamespace());
		request.withKey(key).withNamespace(namespace);

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.PART_NUMBER, request.getPartNumber());
		param.setParameter(RequestParamKey.UPLOAD_ID, stdRequest.getUploadId());

		if (StringUtils.isEmpty(request.getNamespace())) {
			if (StringUtils.isEmpty(namespace)) {
				request.withNamespace(hcpClient.getNamespace());
			} else {
				request.withNamespace(namespace);
			}
		}
		header.setHeader(HCPHeaderKey.X_AMZ_COPY_SOURCE, request);
		header.setHeader(HCPHeaderKey.X_AMZ_COPY_SOURCE_RANGE, request);
		// TODO
		// x-amz-copy-source-if-match
		// x-amz-copy-source-if-modified-since
		// x-amz-copy-source-if-none-match
		// x-amz-copy-source-if-unmodified-since
		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_OCTET_STREAM);

		S3SignerUtils.generateS3Signer(hcpClient, Method.PUT, namespace, key, header, param);

		UploadPartResult result = httpS3Client.execute(request, HCPRestResponseHandler.MULTIUP_COPY_RESPONSE_HANDLER);

		return result;
	}

	public UploadPartResult uploadPart(UploadPartRequest request) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfEmpty(stdRequest.getUploadId(), "The parameter uploadId must be specified.");
		String specificKey = request.getKey();
		// Perform the default request parameter check
		ValidUtils.validateRequest(request.withKey(Constants.DUMMY_KEY));

		String key = StringUtils.notEmptyValue(specificKey, stdRequest.getKey());
		String namespace = StringUtils.notEmptyValue(request.getNamespace(), stdRequest.getNamespace());
		request.withKey(key).withNamespace(namespace);

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_OCTET_STREAM);

		param.setParameter(RequestParamKey.PART_NUMBER, request.getPartNumber());
		param.setParameter(RequestParamKey.UPLOAD_ID, stdRequest.getUploadId());

		// Creates an entity with a specified content length.
		InputStream contentIn = request.getContent();
		InputStreamEntity requestEntity = new InputStreamEntity(contentIn, request.getLength());
		request.setEntity(requestEntity);

		S3SignerUtils.generateS3Signer(hcpClient, Method.PUT, namespace, key, header, param);

		UploadPartResult result = httpS3Client.execute(request, HCPRestResponseHandler.MULTIUP_UPLOAD_RESPONSE_HANDLER);

		// Specifies the size, in bytes
		result.setSize(request.getLength());

		// Verify content by md5 or other algorithm
		if (request.isVerifyContent()) {
			// System.out.println(request.getKey() + " VerifyContent");
			byte[] localDigest = null;
			if (request.isSendingInCompressed()) {
				Object ino = ReflectUtils.getFieldValue(((GZIPCompressedInputStream) contentIn), "orginalInputStream");
				if (ino instanceof DigestInputStream) {
					localDigest = ((DigestInputStream) ino).getDigest();
				} else if (ino instanceof DigestProgressInputStream) {
					localDigest = ((DigestProgressInputStream) ino).getDigest();
				}
			} else {
				if (contentIn instanceof DigestInputStream) {
					localDigest = ((DigestInputStream) contentIn).getDigest();
				} else if (contentIn instanceof DigestProgressInputStream) {
					localDigest = ((DigestProgressInputStream) contentIn).getDigest();
				}
			}
			String remoteDigest = null;

			remoteDigest = result.getETag().toLowerCase(Locale.ENGLISH);
			
//			System.out.println(""+request.getPartNumber()+" "+DigestUtils.format2Hex(localDigest).toLowerCase(Locale.ENGLISH)+" "+remoteDigest);

			if (remoteDigest != null && !DigestUtils.format2Hex(localDigest).toLowerCase(Locale.ENGLISH).equals(remoteDigest)) {
				throw new HSCException("Uploaded part is not the same as local part hash value, transmission failed!");
			}
		}

		return result;
	}

	public void complete(List<PartETag> partETags) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfEmpty(stdRequest.getUploadId(), "The parameter uploadId must be specified.");
		ValidUtils.invalidIfEmpty(partETags, "The parameter partETags must be specified.");

		HCPRequestHeaders header = stdRequest.customHeader();
		HCPRequestParams param = stdRequest.customParameter();
		param.clear();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.TEXT_XML);

		param.setParameter(RequestParamKey.UPLOAD_ID, stdRequest.getUploadId());

		S3SignerUtils.generateS3Signer(hcpClient, Method.POST, stdRequest.getNamespace(), stdRequest.getKey(), header, param);

		// Creates an entity with a specified content length.
		InputStreamEntity requestEntity = null;
		try {

			// The list of parts must be in ascending order. Parts must be ordered by part number
			Collections.sort(partETags, new Comparator<PartETag>() {
				public int compare(PartETag o1, PartETag o2) {
					return o1.getPartNumber() > o2.getPartNumber() ? 1 : -1;
				}
			});

			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
			xml.append("<CompleteMultipartUpload>\n");
			for (PartETag partETag : partETags) {
				xml.append("  <Part>\n");
				xml.append("    <PartNumber>" + partETag.getPartNumber() + "</PartNumber>\n");
				xml.append("    <ETag>" + partETag.getETag() + "</ETag>\n");
				xml.append("  </Part>\n");
			}
			xml.append("</CompleteMultipartUpload>\n");

			InputStream in = new ByteArrayInputStream(xml.toString().getBytes("utf-8"));
			requestEntity = new InputStreamEntity(in, in.available());
			stdRequest.setEntity(requestEntity);
		} catch (Exception e) {
			throw new HSCException(e);
		}

		httpS3Client.execute(stdRequest, HCPRestResponseHandler.MULTIUP_COMPLETE_RESPONSE_HANDLER);
	}

	public void abort() throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfEmpty(stdRequest.getUploadId(), "The parameter uploadId must be specified.");

		DeleteObjectRequest request = new DeleteObjectRequest(stdRequest.getKey()).withNamespace(stdRequest.getNamespace());

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.UPLOAD_ID, stdRequest.getUploadId());

		S3SignerUtils.generateS3Signer(hcpClient, Method.DELETE, stdRequest.getNamespace(), stdRequest.getKey(), header, param);

		httpS3Client.execute(request, HCPRestResponseHandler.MULTIUP_DELETE_RESPONSE_HANDLER);

		// Clear upload Id
		stdRequest.withUploadId(null);
	}

	public UploadPartList listParts() throws InvalidResponseException, HSCException {
		return listParts(null);
	}

	public UploadPartList listParts(ListUploadedPartsRequest request) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfEmpty(stdRequest.getUploadId(), "The parameter uploadId must be specified.");
		// Perform the default request parameter check
		// NO NEED VALIDATION
		// ValidUtils.validateRequest(request);

		if (request == null) {
			request = new ListUploadedPartsRequest(stdRequest.getKey());
		}

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.UPLOAD_ID, stdRequest.getUploadId());

		String key = StringUtils.notEmptyValue(request.getKey(), stdRequest.getKey());
		String namespace = StringUtils.notEmptyValue(request.getNamespace(), stdRequest.getNamespace());
		request.withKey(key).withNamespace(namespace);

		// Specifies the maximum number of parts to be included in the returned part listing. Valid values
		// are integers in the range zero through one thousand. If you specify an integer greater than one
		// thousand, HCP returns a 400 (Invalid Argument) status code and does not return a part listing.
		if (request.getMaxParts() > 0) {
			param.setParameter(RequestParamKey.MAX_PARTS, request.getMaxParts());
		}

		// Starts the returned part listing with the part with the next higher number than a specified
		// value. Valid values are integers in the range zero through ten thousand.
		if (request.getPartNumberMarker() > 0) {
			param.setParameter(RequestParamKey.PART_NUMBER_MARKER, request.getPartNumberMarker());
		}

		S3SignerUtils.generateS3Signer(hcpClient, Method.GET, namespace, key, header, param);

		return httpS3Client.execute(request, HCPRestResponseHandler.MULTIUP_LIST_RESPONSE_HANDLER);
	}

//	private void updateAuthHeader(Method method, String namespace, String key, HCPRequestHeaders header, HCPRequestParams param) {
//
//		String dt = DateUtils.RFC822_DATE_FORMAT.format(System.currentTimeMillis());
//		try {
//			header.setHeader(HCPHeaderKey.DATE, dt);
//
//			final Object[] authParams = new Object[6];
//			authParams[0] = hcpClient;
//			authParams[1] = method;
//			authParams[2] = namespace;
//			authParams[3] = key;
//			authParams[4] = header;
//			authParams[5] = param;
//
//			header.setHeader(HCPHeaderKey.AWS_AUTHORIZATION, authParams);
//		} catch (BuildException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
