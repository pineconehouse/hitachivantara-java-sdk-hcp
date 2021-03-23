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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.io.DigestInputStream;
import com.amituofo.common.kit.io.DigestProgressInputStream;
import com.amituofo.common.kit.io.GZIPCompressedInputStream;
import com.amituofo.common.kit.value.Counter;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.ReflectUtils;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.content.InputStreamEntity;
import com.hitachivantara.core.http.define.HeaderKey;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.common.define.HCPRequestHeadValue;
import com.hitachivantara.hcp.common.define.HashAlgorithm;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.api.MultipartUpload;
import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.api.event.ListObjectHandler;
import com.hitachivantara.hcp.standard.api.event.ObjectCopyingListener;
import com.hitachivantara.hcp.standard.api.event.ObjectDeletingListener;
import com.hitachivantara.hcp.standard.api.event.ObjectFilter;
import com.hitachivantara.hcp.standard.api.event.ObjectMovingListener;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.body.HCPNamespaceBase;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.define.ObjectType;
import com.hitachivantara.hcp.standard.define.RequestParamKey;
import com.hitachivantara.hcp.standard.define.RequestParameterValue;
import com.hitachivantara.hcp.standard.define.SystemMetadataKey;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.NamespaceBasicSetting;
import com.hitachivantara.hcp.standard.model.NamespacePermissions;
import com.hitachivantara.hcp.standard.model.NamespaceStatistics;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.RetentionClasses;
import com.hitachivantara.hcp.standard.model.converter.HCPRestResponseHandler;
import com.hitachivantara.hcp.standard.model.metadata.ACLUserList;
import com.hitachivantara.hcp.standard.model.metadata.AccessControlList;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadata;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummarys;
import com.hitachivantara.hcp.standard.model.metadata.HCPSystemMetadata;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.HCPRequestForm;
import com.hitachivantara.hcp.standard.model.request.HCPRequestHeaders;
import com.hitachivantara.hcp.standard.model.request.HCPRequestParams;
import com.hitachivantara.hcp.standard.model.request.impl.CheckACLRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CheckMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CheckS3MetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CopyDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CopyObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CreateDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteACLRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteS3MetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetACLRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetNamespaceBasicSettingRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetNamespacePermissionsRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetNamespacesStatisticsRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetS3MetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListAccessibleNamespacesRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListRetentionClassesRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListVersionRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MoveDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MoveObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartUploadRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutACLRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutS3MetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutSystemMetadataRequest;
import com.hitachivantara.hcp.standard.util.ACLUtils;
import com.hitachivantara.hcp.standard.util.MetadataUtils;

/**
 * HCP supports a RESTful implementation of the HTTP protocol. To access a namespace through this protocol, you can write applications that
 * use any standard HTTP client library, or you can use a command-line tool, such as cURL, that supports HTTP. You can also use a web
 * browser to access the namespace. The namespace configuration determines whether you need to use HTTP with SSL (HTTPS).
 * 
 * Using the HTTP protocol, you can store, view, retrieve, and delete objects. You can specify certain metadata when you store new objects
 * and change the metadata for existing objects. You can add, replace, and delete custom metadata and ACLs and retrieve information about
 * namespaces.
 * 
 * HCP is compliant with HTTP/1.1, as specified by RFC 2616.
 * 
 * For you to access a namespace through HTTP, this protocol must be enabled in the namespace configuration. If you cannot use the HTTP
 * protocol to access the namespace, contact your tenant administrator.
 * 
 * @author sohan
 *
 */
public class HCPNamespaceImpl extends HCPNamespaceBase {
	public HCPNamespaceImpl(String namespace, String endpoint, Credentials credentials, ClientConfiguration clientConfiguration, KeyAlgorithm keyAlgorithm) {
		super(namespace, endpoint, credentials, clientConfiguration, keyAlgorithm);
	}

	@Override
	public boolean doesNamespacesExist(String namespace) throws InvalidResponseException, HSCException {
		try {
			getNamespacesStatistics(namespace);
		} catch (InvalidResponseException e) {
//			e.printStackTrace();
//			if( e.getStatusCode() == HCPResponseKey.UNAUTHORIZED.code()) {
				return false;
//			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#listAccessibleNamespaces()
	 */
	public List<NamespaceBasicSetting> listAccessibleNamespaces() throws InvalidResponseException, HSCException {
		ListAccessibleNamespacesRequest request = new ListAccessibleNamespacesRequest(this.getNamespace());
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		List<NamespaceBasicSetting> result = execute(request, HCPRestResponseHandler.LIST_ACCESSIBLE_NAMESPACES_RESPONSE_HANDLER);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#getNamespaceSetting()
	 */
	public NamespaceBasicSetting getNamespaceSetting() throws InvalidResponseException, HSCException {
		return getNamespaceSetting(this.getNamespace());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#getNamespaceSetting(java.lang.String)
	 */
	public NamespaceBasicSetting getNamespaceSetting(String namespace) throws InvalidResponseException, HSCException {
		GetNamespaceBasicSettingRequest request = new GetNamespaceBasicSettingRequest(namespace);
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();
		param.enableParameter(RequestParamKey.SINGLE);

		NamespaceBasicSetting result = execute(request, HCPRestResponseHandler.GET_NAMESPACES_RESPONSE_HANDLER);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#getNamespacePermissions()
	 */
	public NamespacePermissions getNamespacePermissions() throws InvalidResponseException, HSCException {
		return getNamespacePermissions(this.getNamespace());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#getNamespacePermissions(java.lang.String)
	 */
	public NamespacePermissions getNamespacePermissions(String namespace) throws InvalidResponseException, HSCException {
		GetNamespacePermissionsRequest request = new GetNamespacePermissionsRequest(namespace);
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		NamespacePermissions result = execute(request, HCPRestResponseHandler.GET_NAMESPACE_PERMISSIONS_RESPONSE_HANDLER);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#listRetentionClasses()
	 */
	@Override
	public List<RetentionClasses> listRetentionClasses() throws InvalidResponseException, HSCException {
		return this.listRetentionClasses(namespace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#listRetentionClasses(java.lang.String)
	 */
	@Override
	public List<RetentionClasses> listRetentionClasses(String namespace) throws InvalidResponseException, HSCException {
		ListRetentionClassesRequest request = new ListRetentionClassesRequest(namespace);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		List<RetentionClasses> result = execute(request, HCPRestResponseHandler.LIST_RETENTION_CLASSES_RESPONSE_HANDLER);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#getNamespacesStatistics()
	 */
	@Override
	public NamespaceStatistics getNamespacesStatistics() throws InvalidResponseException, HSCException {
		return getNamespacesStatistics(this.getNamespace());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.NamespaceInformation#getNamespacesStatistics(java.lang.String)
	 */
	@Override
	public NamespaceStatistics getNamespacesStatistics(String namespace) throws InvalidResponseException, HSCException {
		GetNamespacesStatisticsRequest request = new GetNamespacesStatisticsRequest(namespace);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		NamespaceStatistics result = execute(request, HCPRestResponseHandler.GET_NAMESPACES_STATISTICS_RESPONSE_HANDLER);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectAPI#putObject(com.hitachivantara.hcp.standard.model. request.PutObjectRequest)
	 */
	public PutObjectResult putObject(PutObjectRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		// If has custom METADATA, attach METADATA to this object
		PutMetadataRequest putMetadataRequest = null;
		if (request.getMetadataContent() != null) {
			try {
				putMetadataRequest = new PutMetadataRequest(request.getKey(), request.getMetadataName(), request.getMetadataContent(), request.isSendingMetadataInCompressed())
						.withNamespace(request.getNamespace());
			} catch (IOException e) {
				throw new InvalidParameterException("Failed to get custom metadata content.", e);
			}

			// Exec the default request paramecustomck
			putMetadataRequest.validParameter();
		}

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		InputStream in = null;
		InputStream contentIn = null;
		if (request.isSendingInCompressed()) {
			header.setHeader(HCPHeaderKey.CONTENT_ENCODING, HCPRequestHeadValue.CONTENT_ENCODING.GZIP);
			// header.setHeader(HCPHeaderKey.TRANSFER_ENCODING, RequestHeadValue.TRANSFER_ENCODING.CHUNKED);
		}

		S3CompatibleMetadata keyValueMetadata = request.getS3CompatibleMetadata();
		if (keyValueMetadata != null && keyValueMetadata.size() > 0) {
			int available = 0;
			contentIn = request.getContent();
			try {
				available = contentIn.available();
			} catch (IOException e) {
				throw new HSCException(e);
			}

			// ValidUtils.exceptionIfZero(available, "Available data is 0!");

			InputStream keyValueMetadataIn = MetadataUtils.toInputStream(keyValueMetadata);
			in = new SequenceInputStream(contentIn, keyValueMetadataIn);

			param.setParameter(RequestParamKey.ANNOTATION, keyValueMetadata.getName());
			param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.WHOLE_OBJECT);

			header.setHeader(HCPHeaderKey.X_HCP_SIZE, Long.valueOf(available));
		} else {
			contentIn = in = request.getContent();
		}

		// Creates an entity with a specified content length.
		InputStreamEntity requestEntity = new InputStreamEntity(in, -1);
		request.setEntity(requestEntity);

		PutObjectResult result = execute(request, HCPRestResponseHandler.PUT_OBJECT_RESPONSE_HANDLER);

		// Verify content by md5 or other algorithm
		if (request.isVerifyContent()) {
//			long readLength = 0;
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
//					readLength = ((DigestInputStream) contentIn).getTotalReadLength();
				} else if (contentIn instanceof DigestProgressInputStream) {
					localDigest = ((DigestProgressInputStream) contentIn).getDigest();
//					readLength = ((DigestProgressInputStream) contentIn).getTotalReadLength();
				}
			}
			String remoteDigest = null;
			
			if (request.getContentDigestAlgorithm() == HashAlgorithm.MD5) {
				if (StringUtils.isNotEmpty(result.getETag())) {
					remoteDigest = result.getETag().toLowerCase(Locale.ENGLISH);
				}
			} else {
				remoteDigest = result.getContentHash().toLowerCase(Locale.ENGLISH);
			}

			String localDigestString = DigestUtils.format2Hex(localDigest).toLowerCase(Locale.ENGLISH);
			result.setSourceContentHash(localDigestString);
			if (remoteDigest != null && !localDigestString.equals(remoteDigest)) {
				throw new HSCException("Uploaded object is not the same as local object hash value, transmission failed!");
			}
			
			// bug
//			result.setContentLength(readLength);
		}

		if (contentIn != null) {
			try {
				contentIn.close();
			} catch (IOException e) {
			}
			contentIn = null;
		}

		// If has custom metadata, attach metadata to this object
		if (putMetadataRequest != null) {
			this.putMetadata(putMetadataRequest);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectAPI#putObject(java.lang.String, java.io.File)
	 */
	public PutObjectResult putObject(String key, File file) throws InvalidResponseException, HSCException {
		PutObjectRequest request = new PutObjectRequest(key);

		try {
			request.withContent(file);
		} catch (IOException e) {
			throw new InvalidParameterException(e);
		}

		return this.putObject(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectAPI#putObject(java.lang.String, java.io.InputStream)
	 */
	public PutObjectResult putObject(String key, InputStream in) throws InvalidResponseException, HSCException {
		PutObjectRequest request = new PutObjectRequest(key, in);

		return this.putObject(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectAPI#getObject(java.lang.String)
	 */
	public HCPObject getObject(String key) throws InvalidResponseException, HSCException {
		GetObjectRequest request = new GetObjectRequest(key);

		return this.getObject(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectAPI#getObject(java.lang.String, java.lang.String)
	 */
	public HCPObject getObject(String key, String versionId) throws InvalidResponseException, HSCException {
		GetObjectRequest request = new GetObjectRequest(key, versionId);

		return this.getObject(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectAPI#getObject(com.hitachivantara.hcp.standard.model. request.GetObjectRequest)
	 */
	public HCPObject getObject(GetObjectRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);
		ResponseHandler<HCPObject> responseHandler = null;

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		// Requesting a specific old version
		// To retrieve a specific old version of an object, specify the version URL query parameter with the
		// ID of the version you want. You can omit this parameter to retrieve the current version of an
		// object.
		if (StringUtils.isNotEmpty(request.getVersionId())) {
			param.setParameter(RequestParamKey.VERSION, request.getVersionId());
		}

		// By default, the GET request to retrieve object versions does not include deleted versions (that
		// is, the marker versions that indicate when an object was deleted). To retrieve a listing that
		// includes deleted versions, specify this URL query parameter:
		if (request.isWithDeletedObject()) {
			param.enableParameter(RequestParamKey.DELETED);
		}

		// To force HCP to generate an ETag for an object that does not yet have one, specify a forceEtag
		// URL query parameter with a value of true
		if (request.isForceEtag()) {
			param.enableParameter(RequestParamKey.FORCE_ETAG);
		}

		// Requesting partial object data
		// To retrieve only part of a single object or version data, specify an HTTP Range request header
		// with the range of bytes of the object data to retrieve. You specify the Range header in addition
		// to other request elements described above. The first byte of the data is in position 0 (zero), so
		// a range of 1-5 specifies the second through sixth bytes of the object, not the first through
		// fifth.
		if (request.getRange() != null) {
			header.setHeader(HCPHeaderKey.RANGE, request.getRange());

			responseHandler = HCPRestResponseHandler.GET_PARTIAL_OBJECT_RESPONSE_HANDLER;
		} else {
			responseHandler = HCPRestResponseHandler.GET_OBJECT_RESPONSE_HANDLER;
		}
		
		// To request that HCP return a single object or version in gzip-compressed format, use an Accept-Encoding header containing the value gzip
		// or *. The header can specify additional compression algorithms, but HCP uses only gzip.
		// You can request a single object or version data in compressed format with any of the additional request elements described in the
		// sections below.
		if (request.isGetInCompressed()) {
			header.setHeader(HeaderKey.ACCEPT_ENCODING, HCPRequestHeadValue.CONTENT_ENCODING.GZIP);
		}

		// Choosing not to wait for delayed retrievals
		// HCP may detect that a GET request will take a significant amount of time to return an object. You
		// can choose to have the request fail in this situation instead of waiting for HCP to return the
		// object. To do this, use the nowait URL query parameter.
		//
		// When a GET request fails because the request would take a significant amount of time to return an
		// object and the nowait parameter is specified, HCP returns an HTTP 503 (Service Unavailable) error
		// code.
		if (request.isNowait()) {
			param.enableParameter(RequestParamKey.NOWAIT);
		}
		
		// HCP supports only the gzip algorithm for compressed data transmission.
		if (request.isGetInCompressed()) {
			header.setHeader(HCPHeaderKey.ACCEPT_ENCODING, HCPRequestHeadValue.CONTENT_ENCODING.GZIP);
		}

		// Requesting data in compressed format
		// To request that HCP return an single object or version in gzip-compressed format, use an
		// Accept-Encoding header containing the value gzip or *. The header can specify additional
		// compression algorithms, but HCP uses only gzip.
		//
		// You can request a single object or version data in compressed format with any of the additional
		// request elements described in the sections below.
		// DOTO
		// if (request.isCompressFormat()) {
		// param.putTrue(HCPRequestParamKey.);
		// }

		// Forcing the generation of an ETag
		// To force HCP to generate an ETag for an object that does not yet have one, specify a forceEtag
		// URL query parameter with a value of true.
		// TODO

		HCPObject result = (HCPObject) execute(request, responseHandler);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectAPI#listDirectory(java.lang.String)
	 */
	public HCPObjectEntrys listDirectory(String dir) throws InvalidResponseException, HSCException {
		ListDirectoryRequest request = new ListDirectoryRequest(dir);

		return this.listDirectory(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectAPI#listDirectory(com.hitachivantara.hcp.standard.model .request.ListDirectoryRequest)
	 */
	public HCPObjectEntrys listDirectory(ListDirectoryRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		if (request.isWithDeletedObject()) {
			param.enableParameter(RequestParamKey.DELETED);
		}

		if (request.isWithDeletedDirTime()) {
			param.enableParameter(RequestParamKey.MOST_RECENT_DIR_TIMES);
		}

		String dir = URLUtils.tidyUrlPath(request.getKey());
		request.withKey(dir);

		HCPObjectEntrys hcpObjects = (HCPObjectEntrys) execute(request, HCPRestResponseHandler.LIST_DIR_RESPONSE_HANDLER);

		return hcpObjects;
	}

	public HCPObjectEntrys listVersions(String key) throws InvalidResponseException, HSCException {
		ListVersionRequest request = new ListVersionRequest(key);

		return this.listVersions(request);
	}

	public HCPObjectEntrys listVersions(ListVersionRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.VERSION_LIST, RequestParameterValue.Version.LIST);

		if (request.isWithDeletedObject()) {
			param.enableParameter(RequestParamKey.DELETED);
		}

		HCPObjectEntrys hcpObjects = (HCPObjectEntrys) execute(request, HCPRestResponseHandler.LIST_OBJECT_RESPONSE_HANDLER);

		return hcpObjects;
	}

	public boolean createDirectory(String dir) throws InvalidResponseException, HSCException {
		CreateDirectoryRequest request = new CreateDirectoryRequest(dir);

		return this.createDirectory(request);
	}

	public boolean createDirectory(CreateDirectoryRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		String dir = URLUtils.tidyUrlPath(request.getKey());
		request.withKey(dir);

		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.DIRECTORY);

		return (Boolean) execute(request, HCPRestResponseHandler.CREATE_DIR_RESPONSE_HANDLER);
	}

	public boolean doesDirectoryExist(CheckObjectRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		String dir = URLUtils.tidyUrlPath(request.getKey());
		request.withKey(dir);

//		if (request.isWithDeletedObject()) {
//			HCPRequestParams param = request.customParameter();
//			param.enableParameter(RequestParamKey.DELETED);
//		}

		return (Boolean) execute(request, HCPRestResponseHandler.CHECK_DIR_RESPONSE_HANDLER);
	}

	public boolean doesDirectoryExist(String dir) throws InvalidResponseException, HSCException {
		CheckObjectRequest request = new CheckObjectRequest(dir);

		return this.doesDirectoryExist(request);
	}

	// public boolean deleteDirectory(String dir) throws InvalidResponseException, HSCException {
	// DeleteDirectoryRequest request = new DeleteDirectoryRequest(dir);
	//
	// return this.deleteDirectory(request);
	// }

	public boolean deleteDirectory(final DeleteDirectoryRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		//TODO
		CheckObjectRequest checkRequest = new CheckObjectRequest(request.getKey()).withNamespace(request.getNamespace());//.withDeletedObject(xxx);
		if (!this.doesDirectoryExist(checkRequest)) {
			return false;
		}

		HCPObjectSummary sourceSummary = this.getObjectSummary(checkRequest);

		ValidUtils.invalidIfTrue(sourceSummary.getType() != ObjectType.directory, "The key must be directory.");

		final ObjectDeletingListener deleteListener = request.getObjectDeleteListener();

		final Counter failedC = new Counter();
		if (request.isDeleteContainedObjects()) {
			final ListObjectHandler handler = new ListObjectHandler() {
				NextAction nextAction = null;

				public NextAction foundObject(HCPObjectSummary objectEntry) throws InvalidResponseException, HSCException {
					if (deleteListener != null) {
						nextAction = deleteListener.beforeDeleting(objectEntry);
						if (nextAction == NextAction.stop) {
							return nextAction;
						}
					}

					// if (nextAction != NextAction.Skip) {
					DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(objectEntry.getKey()).withNamespace(request.getNamespace());

					if (!objectEntry.isDirectory()) {
						deleteObjectRequest.withPurge(request.isPurge()).withPrivileged(request.isPrivileged(), request.getReason());
					}

					boolean deleted = deleteObject(deleteObjectRequest);
					if (!deleted) {
						failedC.i++;
					}

					if (deleteListener != null) {
						nextAction = deleteListener.afterDeleting(objectEntry, deleted);
						if (nextAction == NextAction.stop) {
							return nextAction;
						}
					}
					// } else {
					// c.i++;
					// }

					return NextAction.next;
				}
			};

			ListObjectRequest listSubDirRequest = new ListObjectRequest(request.getKey()).withNamespace(request.getNamespace());
			listSubDirRequest.withRecursiveDirectory(true);
			NextAction nextAction = this.listObjects(listSubDirRequest, handler, null);
			if (nextAction == NextAction.stop) {
				return false;
			}
		}

		// 如果当前目录下没有未被删除的对象就删除此目录
		if (failedC.i == 0) {
			DeleteObjectRequest deleteLastDirectory = new DeleteObjectRequest(request.getKey()).withNamespace(request.getNamespace());

			if (deleteListener != null) {
				NextAction nextAction = deleteListener.beforeDeleting(sourceSummary);
				if (nextAction == NextAction.stop) {
					return false;
				}
			}

			boolean deleted = this.deleteObject(deleteLastDirectory);

			if (deleteListener != null) {
				deleteListener.afterDeleting(sourceSummary, deleted);
			}

			return true;
		}

		return false;
	}

	// public boolean deleteDirectory(final DeleteDirectoryRequest request) throws InvalidResponseException, HSCException {
	// // Exec the default request parameter check
	// ValidUtils.validateRequest(request);
	//
	// CheckObjectRequest checkRequest = new CheckObjectRequest(request.getKey()).withNamespace(request.getNamespace());
	// if (!this.doesDirectoryExist(checkRequest)) {
	// return false;
	// }
	//
	// HCPObjectSummary sourceSummary = this.getObjectSummary(checkRequest);
	//
	// ValidUtils.exceptionIfTrue(sourceSummary.getType() != ObjectType.directory, "The key must be directory.");
	//
	// boolean deleted = false;
	// long dirCount = 0;
	// final int maxcount = 10000;
	// HCPObjectEntrys entrys = this.listDirectory(new ListDirectoryRequest().withKey(request.getKey()).withNamespace(request.getNamespace()));
	// ObjectEntryIterator it = entrys.iterator();
	// List<HCPObjectEntry> objects = it.next(maxcount);
	// for (HCPObjectEntry hcpObjectEntry : objects) {
	// if (hcpObjectEntry.isDirectory()) {
	// dirCount++;
	// } else {
	// deleted = this.deleteObject(new
	// DeleteObjectRequest().withKey(request.getKey()).withNamespace(request.getNamespace()).withPurgeDelete(request.isPurge()));
	// if (!deleted) {
	// return false;
	// }
	// }
	// }
	// it.close();
	//
	//// if (dirCount)
	//
	// final ListObjectHandler listener = new ListObjectHandler() {
	// final ObjectDeletingListener deleteListener = request.getObjectDeleteListener();
	// NextAction nextAction;
	//
	// public NextAction foundObject(HCPObjectEntry objectEntry) throws InvalidResponseException, HSCException {
	// if (deleteListener != null) {
	// nextAction = deleteListener.beforeDelete(objectEntry);
	// if (nextAction == NextAction.STOP) {
	// return nextAction;
	// }
	// }
	//
	// DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(objectEntry.getKey()).withNamespace(request.getNamespace());
	//
	// if (!objectEntry.isDirectory()) {
	// deleteObjectRequest.withPurgeDelete(request.isPurge()).withPrivilegedDelete(request.isPrivileged(), request.getReason());
	// }
	//
	// boolean deleted = deleteObject(deleteObjectRequest);
	// if (deleteListener != null) {
	// nextAction = deleteListener.afterDelete(objectEntry, deleted);
	// if (nextAction == NextAction.STOP) {
	// return nextAction;
	// }
	// }
	//
	// return NextAction.CONTINUE;
	// }
	// };
	//
	// ListObjectRequest listSubDirRequest = new ListObjectRequest(request.getKey()).withNamespace(request.getNamespace());
	// listSubDirRequest.withRecursiveDirectory(request.isRecursiveDirectory());
	// NextAction nextAction = this.listObjects(listSubDirRequest, listener, null);
	// if (nextAction == NextAction.STOP) {
	// return false;
	// }
	//
	// DeleteObjectRequest deleteLastDirectory = new DeleteObjectRequest(request.getKey()).withNamespace(request.getNamespace());
	// boolean deleted = this.deleteObject(deleteLastDirectory);
	//
	// return deleted;
	// }

	@Override
	public boolean deleteObject(String key) throws InvalidResponseException, HSCException {
		DeleteObjectRequest request = new DeleteObjectRequest(key);

		return this.deleteObject(request);
	}
	
	@Override
	public boolean deleteObject(String key, String versionId) throws InvalidResponseException, HSCException {
		DeleteObjectRequest request = new DeleteObjectRequest(key).withVersionId(versionId);

		return this.deleteObject(request);
	}

//	public boolean deleteObjects(DeleteObjectsRequest request) throws InvalidResponseException, HSCException {
//		// Exec the default request parameter check
//		ValidUtils.validateRequest(request);
//
//		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
//				+ "<Delete>\r\n"
//				+ " <Object>\r\n"
//				+ " <Key>/loadjdv-991.txt</Key>\r\n"
//				+ " </Object>\r\n"
//				+ " <Object>\r\n"
//				+ " <Key>/loadjdv-992.txt</Key>\r\n"
//				+ " </Object>\r\n"
//				+ "</Delete>";
//		StringEntity requestEntity;
//		try {
//			requestEntity = new StringEntity(s);
//			request.setEntity(requestEntity);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		HCPRequestHeaders header = request.customHeader();
//		HCPRequestParams param = request.customParameter();
//
//		param.setParameter(RequestParamKey.DELETE, null);
//
//		String namespace = StringUtils.notEmptyValue(request.getNamespace(), this.namespace);
//
//		String dt = DateUtils.RFC822_DATE_FORMAT.format(System.currentTimeMillis());
//		header.setHeader(HCPHeaderKey.DATE, dt);
//
//		final Object[] authParams = new Object[6];
//		authParams[0] = this;
//		authParams[1] = request.getMethod();
//		authParams[2] = namespace;
//		authParams[3] = "";
//		authParams[4] = header;
//		authParams[5] = param;
//
//		header.setHeader(HCPHeaderKey.AWS_AUTHORIZATION, authParams);
//
//		HCPHttpRestClient httpClient = new HCPHttpRestClient(this, "/");
//		httpClient.execute(request, HCPRestResponseHandler.DELETE_OBJECTS_RESPONSE_HANDLER);
//		return true;
//	}


	public boolean deleteObject(DeleteObjectRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		if (StringUtils.isNotEmpty(request.getVersionId())) {
			param.setParameter(RequestParamKey.VERSION, request.getVersionId());
		}

		if (request.isPurge()) {
			param.enableParameter(RequestParamKey.PURGE);
		}

		if (request.isPrivileged()) {
			param.enableParameter(RequestParamKey.PRIVILEGED);
			param.setParameter(RequestParamKey.REASON, request.getReason());
		}

		return (Boolean) execute(request, HCPRestResponseHandler.DELETE_OBJECT_RESPONSE_HANDLER);
	}

	public void copyObject(String sourceKey, String targetKey) throws InvalidResponseException, HSCException {
		CopyObjectRequest request = new CopyObjectRequest(sourceKey, targetKey).withCopyingMetadata(true);

		this.copyObject(request);
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.api.ObjectContent#copyObject(com.hitachivantara.hcp.standard.model.request.impl.CopyObjectRequest)
	 */
	public void copyObject(CopyObjectRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		if (StringUtils.isEmpty(request.getSourceNamespace())) {
			request.withSourceNamespace(this.getNamespace());
		}

		if (request.isCopyingMetadata()) {
			header.setHeader(HCPHeaderKey.X_HCP_METADATA_DIRECTIVE, HCPRequestHeadValue.METADATA_DIRECTIVE.ALL);
		}

		// Endpoint will be reset by default endpoint, because of object only can be copying between same tenant.
		request.setSourceEndpoint(this.getEndpoint());

		if (request.isCopyingOldVersion()) {
			ListVersionRequest listVersionRequest = new ListVersionRequest(request.getSourceKey()).withNamespace(request.getSourceNamespace());
			// .withDeletedObject(request.isWithDeletedObject());
			HCPObjectEntrys versionEntry = this.listVersions(listVersionRequest);
			ObjectEntryIterator it = versionEntry.iterator();

			List<HCPObjectEntry> versions = it.next(Integer.MAX_VALUE);
			if (versions != null) {
				final int SIZE = versions.size();
				// for (int i = versions.size() - 1; i > -1; i--) {
				for (int i = 0; i < SIZE; i++) {
					HCPObjectEntry obj = versions.get(i);

					request.withSourceVersion(obj.getVersionId());

					header.setHeader(HCPHeaderKey.X_HCP_COPY_SOURCE, request);

					execute(request, HCPRestResponseHandler.COPY_OBJECT_RESPONSE_HANDLER);
				}
			}
		} else {
			header.setHeader(HCPHeaderKey.X_HCP_COPY_SOURCE, request);

			execute(request, HCPRestResponseHandler.COPY_OBJECT_RESPONSE_HANDLER);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectContent#copyDirectory(java.lang.String, java.lang.String)
	 */
	public void copyDirectory(String sourceDirectory, String targetDirectory) throws InvalidResponseException, HSCException {
		CopyDirectoryRequest request = new CopyDirectoryRequest(sourceDirectory, targetDirectory).withRecursiveDirectory(true).withCopyingMetadata(true)
				.withCopyingOldVersion(false);

		copyDirectory(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.api.ObjectContent#copyDirectory(com.hitachivantara.hcp.standard.model.request.CopyDirectoryRequest)
	 */
	public void copyDirectory(final CopyDirectoryRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		// Get the summary of source folder
		CheckObjectRequest checkRequest = new CheckObjectRequest(request.getSourceKey()).withNamespace(request.getSourceNamespace());
		HCPObjectSummary sourceSummary = this.getObjectSummary(checkRequest);

		// Source key must be an directory
		ValidUtils.invalidIfTrue(sourceSummary.getType() != ObjectType.directory, "Source key must be directory.");

		final ListObjectHandler handler = new ListObjectHandler() {
			final ObjectCopyingListener copyListener = request.getObjectCopyListener();
			final int beginIndex = request.getSourceKey().length();
			NextAction nextAction;

			public NextAction foundObject(HCPObjectSummary objectEntry) throws InvalidResponseException, HSCException {
				if (objectEntry.getType() != ObjectType.directory) {
					if (copyListener != null) {
						nextAction = copyListener.beforeCopying(objectEntry);
						if (nextAction == NextAction.stop) {
							return nextAction;
						}
					}

					String sourceKey = objectEntry.getKey();
					String keypath = sourceKey.substring(beginIndex);
					// Generate target key
					String targetKey = URLUtils.catPath(request.getTargetKey(), keypath);
					final CopyObjectRequest copyRequest = new CopyObjectRequest(sourceKey, targetKey).withSourceNamespace(request.getSourceNamespace())
							.withTargetNamespace(request.getTargetNamespace()).withSourceKeyAlgorithm(request.getSourceKeyAlgorithm())
							.withCopyingMetadata(request.isCopyingMetadata()).withCopyingOldVersion(request.isCopyingOldVersion());

					// copy one object from source to target
					copyObject(copyRequest);

					if (copyListener != null) {
						nextAction = copyListener.afterCopying(objectEntry);
						if (nextAction == NextAction.stop) {
							return nextAction;
						}
					}
				}

				return NextAction.next;
			}
		};

		ListObjectRequest listSubDirRequest = new ListObjectRequest(request.getSourceKey()).withRecursiveDirectory(request.isRecursiveDirectory())
				.withNamespace(request.getSourceNamespace());

		// Traversing source directory and copy all the objects to target folder
		this.listObjects(listSubDirRequest, handler, null);
	}

	public boolean doesObjectExist(String key) throws InvalidResponseException, HSCException {
		CheckObjectRequest request = new CheckObjectRequest(key);

		return this.doesObjectExist(request);
	}

	public boolean doesObjectExist(CheckObjectRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		if (StringUtils.isNotEmpty(request.getVersionId())) {
			param.setParameter(RequestParamKey.VERSION, request.getVersionId());
		}

		if (request.isWithDeletedObject()) {
			param.enableParameter(RequestParamKey.DELETED);
		}

		Boolean exist = (Boolean) execute(request, HCPRestResponseHandler.CHECK_OBJECT_RESPONSE_HANDLER);

		return exist;
	}

	public HCPObjectSummary getObjectSummary(String key) throws InvalidResponseException, HSCException {
		CheckObjectRequest request = new CheckObjectRequest(key);

		return this.getObjectSummary(request);
	}

	public HCPObjectSummary getObjectSummary(CheckObjectRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		if (StringUtils.isNotEmpty(request.getVersionId())) {
			param.setParameter(RequestParamKey.VERSION, request.getVersionId());
		}

		if (request.isWithDeletedObject()) {
			param.enableParameter(RequestParamKey.DELETED);
		}

		HCPObjectSummary objectSummary = (HCPObjectSummary) execute(request, HCPRestResponseHandler.HEAD_OBJECT_RESPONSE_HANDLER);

		return objectSummary;
	}

	public void putMetadata(PutS3MetadataRequest request) throws InvalidResponseException, HSCException {
		InputStream in = MetadataUtils.toInputStream(request.getS3CompatibleMetadata());

		PutMetadataRequest putMetadataRequest = new PutMetadataRequest(request.getKey(), S3CompatibleMetadata.DEFAULT_METADATA_NAME, in).withNamespace(request.getNamespace());

		this.putMetadata(putMetadataRequest);
	}

	public void putMetadata(String key, S3CompatibleMetadata metadata) throws InvalidResponseException, HSCException {
		PutS3MetadataRequest request = new PutS3MetadataRequest(key, metadata);

		this.putMetadata(request);
	}

	public void putMetadata(PutMetadataRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = null;
		HCPRequestParams param = request.customParameter();

		InputStream in = request.getContent();
		try {
			if (request.isSendingInCompressed()) {
				header = request.customHeader();
				header.setHeader(HCPHeaderKey.CONTENT_ENCODING, HCPRequestHeadValue.CONTENT_ENCODING.GZIP);
			}

			param.setParameter(RequestParamKey.ANNOTATION, request.getMetadataName());
			param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.CUSTOM_METADATA);

			// Creates an entity with a specified content length.
			InputStreamEntity requestEntity = new InputStreamEntity(in, -1);
			request.setEntity(requestEntity);

			execute(request, HCPRestResponseHandler.PUT_CUSTOM_METADATA_RESPONSE_HANDLER);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void setSystemMetadata(PutSystemMetadataRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestForm form = request.customForm();
		HCPRequestHeaders header = request.customHeader();

		Map<SystemMetadataKey, Object> map = request.getMetadata().getMetadataMap();
		for (Iterator<SystemMetadataKey> it = map.keySet().iterator(); it.hasNext();) {
			SystemMetadataKey paramKey = it.next();
			Object value = map.get(paramKey);
			if (value != null) {
				form.setParameter(paramKey, value);
			}
		}

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, HCPRequestHeadValue.CONTENT_TYPE.APPLICATION_FORM);

		// The PUT request to store an object and override the default values for its metadata includes
		// these elements:
		execute(request, HCPRestResponseHandler.PUT_SYSTEM_METADATA_RESPONSE_HANDLER);
	}

	public void setSystemMetadata(String key, HCPSystemMetadata metadata) throws InvalidResponseException, HSCException {
		PutSystemMetadataRequest request = new PutSystemMetadataRequest(key).withMetadata(metadata);

		setSystemMetadata(request);
	}

	public HCPMetadata getMetadata(GetMetadataRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.ANNOTATION, request.getMetadataName());
		param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.CUSTOM_METADATA);

		if (StringUtils.isNotEmpty(request.getVersionId())) {
			param.setParameter(RequestParamKey.VERSION, request.getVersionId());
		}

		if (request.isNowait()) {
			param.enableParameter(RequestParamKey.NOWAIT);
		}

		HCPMetadata result = execute(request, HCPRestResponseHandler.GET_CUSTOM_METADATA_RESPONSE_HANDLER);

		return result;
	}

	public HCPMetadata getMetadata(String key, String metadataName) throws InvalidResponseException, HSCException {
		GetMetadataRequest request = new GetMetadataRequest(key, metadataName);

		return this.getMetadata(request);
	}

	public S3CompatibleMetadata getMetadata(String key) throws InvalidResponseException, HSCException {
		return this.getMetadata(new GetS3MetadataRequest(key));
	}

	public S3CompatibleMetadata getMetadata(GetS3MetadataRequest request) throws InvalidResponseException, HSCException {
		GetMetadataRequest getRequest = new GetMetadataRequest(request.getKey(), request.getMetadataName()).withNamespace(request.getNamespace())
				.withVersionId(request.getVersionId());

		HCPMetadata metadata = this.getMetadata(getRequest);

		return HCPRestResponseHandler.toHCPKeyValueMetadata(metadata);
	}

	public HCPMetadataSummarys listMetadatas(ListMetadataRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.CUSTOM_METADATA_INFO);

		if (StringUtils.isNotEmpty(request.getVersionId())) {
			param.setParameter(RequestParamKey.VERSION, request.getVersionId());
		}

		HCPMetadataSummarys result = execute(request, HCPRestResponseHandler.LIST_METADATA_RESPONSE_HANDLER);

		return result;
	}

	public HCPMetadataSummarys listMetadatas(String key, String versionId) throws InvalidResponseException, HSCException {
		ListMetadataRequest request = new ListMetadataRequest(key, versionId);

		return this.listMetadatas(request);
	}

	public HCPMetadataSummarys listMetadatas(String key) throws InvalidResponseException, HSCException {
		return this.listMetadatas(key, null);
	}

	public boolean deleteS3Metadata(String key) throws InvalidResponseException, HSCException {
		DeleteS3MetadataRequest request = new DeleteS3MetadataRequest(key);

		return this.deleteS3Metadata(request);
	}

	public boolean deleteS3Metadata(DeleteS3MetadataRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		// ValidUtils.validateRequest(request);

		DeleteMetadataRequest deleteRequest = new DeleteMetadataRequest(request.getKey(), request.getNamespace()).withNamespace(request.getNamespace())
				.withMetadataName(request.getMetadataName());

		return this.deleteMetadata(deleteRequest);
	}

	public boolean deleteMetadata(String key, String metadataName) throws InvalidResponseException, HSCException {
		DeleteMetadataRequest request = new DeleteMetadataRequest(key, metadataName);

		return this.deleteMetadata(request);
	}

	public boolean deleteMetadata(DeleteMetadataRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		String[] metadataNames = request.getMetadataNames();
		for (String metadataName : metadataNames) {
			param.setParameter(RequestParamKey.ANNOTATION, metadataName);
			param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.CUSTOM_METADATA);

			Boolean result = execute(request, HCPRestResponseHandler.DELETE_CUSTOM_METADATA_RESPONSE_HANDLER);

			if (result == false) {
				return false;
			}
		}

		return true;
	}

	public boolean doesMetadataExist(CheckMetadataRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		String[] metadataNames = request.getMetadataNames();
		for (String metadataName : metadataNames) {
			// An annotation=annotation-name query parameter, where annotation-name is the name of the specific annotation for which you are checking
			// the existence. If you’re checking for the default annotation, you can omit this query parameter.
			param.setParameter(RequestParamKey.ANNOTATION, metadataName);
			// type=custom-metadata URL query parameter.
			param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.CUSTOM_METADATA);

			// To check whether an annotation exists for a specific object version, in addition to specifying the request elements listed above, specify
			// this URL query parameter:
			if (StringUtils.isNotEmpty(request.getVersionId())) {
				param.setParameter(RequestParamKey.VERSION, request.getVersionId());
			}

			Boolean result = execute(request, HCPRestResponseHandler.CHECK_CUSTOM_METADATA_RESPONSE_HANDLER);

			if (result == false) {
				return false;
			}
		}

		return true;
	}

	public boolean doesMetadataExist(String key, String metadataName) throws InvalidResponseException, HSCException {
		CheckMetadataRequest request = new CheckMetadataRequest(key, metadataName);

		return this.doesMetadataExist(request);
	}

	public boolean doesS3MetadataExist(String key) throws InvalidResponseException, HSCException {
		CheckS3MetadataRequest request = new CheckS3MetadataRequest(key);

		return this.doesS3MetadataExist(request);
	}

	public boolean doesS3MetadataExist(CheckS3MetadataRequest request) throws InvalidResponseException, HSCException {
		CheckMetadataRequest checkExistenceRequest = new CheckMetadataRequest(request.getKey(), request.getNamespace()).withNamespace(request.getNamespace())
				.withVersionId(request.getVersionId()).withMetadataName(request.getMetadataName());

		return this.doesMetadataExist(checkExistenceRequest);
	}

	public MultipartUpload getMultipartUpload(MultipartUploadRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		return new HCPMultipartUpload(this, request.getNamespace(), request.getKey(), request.getUploadId());
	}

	public void listObjects(final ListObjectRequest request, final ListObjectHandler handler) throws InvalidResponseException, HSCException {
		// final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, request.getMaximumThreadSize(),
		// Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingDeque<Runnable>());

		listObjects(request, handler, null);
	}

	private NextAction listObjects(ListObjectRequest request, final ListObjectHandler handler, final ThreadPoolExecutor threadPool) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);
		ValidUtils.invalidIfNull(handler, "The parameter listener must be specified.");

		final ObjectFilter filter = request.getObjectFilter();
		ListDirectoryRequest listDirRequest = new ListDirectoryRequest(request.getKey()).withDeletedObject(request.isWithDeletedObject()).withNamespace(request.getNamespace());

		HCPObjectEntrys entrys = this.listDirectory(listDirRequest);
		ObjectEntryIterator it = entrys.iterator();

		final int c = 100;

		List<HCPObjectEntry> subDirList = new ArrayList<HCPObjectEntry>();

		boolean accept = true;
		List<HCPObjectEntry> list;
		while ((list = it.next(c)) != null) {
			for (int i = 0; i < list.size(); i++) {
				final HCPObjectEntry hcpObjectEntry = list.get(i);

				// The directory will be traversed at the end
				if (hcpObjectEntry.isDirectory()) {
					subDirList.add(hcpObjectEntry);
				} else {
					if (filter != null) {
						accept = filter.accept(hcpObjectEntry);
					}
					if (accept) {
						// if (threadPool == null) {
						NextAction nextAction = handler.foundObject(hcpObjectEntry);
						if (nextAction == NextAction.stop) {
							it.abort();
							return nextAction;
						}
						// } else {
						// threadPool.execute(new Runnable() {
						// public void run() {
						// try {
						// listener.foundObject(hcpObjectEntry);
						// } catch (HSCException e) {
						// e.printStackTrace();
						// }
						// }
						// });
						// }
					}
				}
			}
		}

		it.close();

		for (final HCPObjectEntry dirEntry : subDirList) {
			if (request.isRecursiveDirectory()) {
				ListObjectRequest listSubDirRequest = new ListObjectRequest(dirEntry.getKey()).withDeletedObject(request.isWithDeletedObject())
						.withRecursiveDirectory(request.isRecursiveDirectory()).withNamespace(request.getNamespace());

				listSubDirRequest.setFolderTier(request.getFolderTier() + 1);

				NextAction nextAction = listObjects(listSubDirRequest, handler, threadPool);
				if (nextAction == NextAction.stop) {
					return nextAction;
				}
			}

			if (filter != null) {
				accept = filter.accept(dirEntry);
			}

			if (accept) {
				// if (threadPool == null) {
				NextAction nextAction = handler.foundObject(dirEntry);
				if (nextAction == NextAction.stop) {
					return nextAction;
				}
				// } else {
				// threadPool.execute(new Runnable() {
				//
				// public void run() {
				// try {
				// listener.foundObject(dirEntry);
				// } catch (HSCException e) {
				// e.printStackTrace();
				// }
				// }
				// });
				// }
			}
		}

		return NextAction.next;
	}

	public boolean moveObject(final MoveObjectRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		final CopyObjectRequest copyRequest = new CopyObjectRequest(request.getSourceKey(), request.getTargetKey()).withSourceNamespace(request.getSourceNamespace())
				.withTargetNamespace(request.getTargetNamespace()).withSourceKeyAlgorithm(request.getSourceKeyAlgorithm()).withCopyingMetadata(request.isCopyingMetadata())
				.withCopyingOldVersion(request.isCopyingOldVersion());

		copyObject(copyRequest);

		DeleteObjectRequest deleteRequest = new DeleteObjectRequest(request.getSourceKey()).withNamespace(request.getSourceNamespace())
				// The object will be delete with purge option, All the old version will be delete
				.withPurge(true);

		return deleteObject(deleteRequest);
	}

	public void moveDirectory(final MoveDirectoryRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		CheckObjectRequest checkRequest = new CheckObjectRequest(request.getSourceKey()).withNamespace(request.getSourceNamespace());
		HCPObjectSummary sourceSummary = this.getObjectSummary(checkRequest);

		ValidUtils.invalidIfTrue(sourceSummary.getType() != ObjectType.directory, "Source key must be directory.");

		final ListObjectHandler handler = new ListObjectHandler() {
			final ObjectMovingListener movelistener = request.getObjectMoveListener();
			final int beginIndex = request.getSourceKey().length();

			NextAction nextAction;

			public NextAction foundObject(HCPObjectSummary objectEntry) throws InvalidResponseException, HSCException {
				if (objectEntry.getType() != ObjectType.directory) {
					if (movelistener != null) {
						nextAction = movelistener.beforeMoving(objectEntry);
						if (nextAction == NextAction.stop) {
							return nextAction;
						}
					}

					String sourceKey = objectEntry.getKey();
					String keypath = sourceKey.substring(beginIndex);
					String targetKey = URLUtils.catPath(request.getTargetKey(), keypath);

					final MoveObjectRequest moveRequest = new MoveObjectRequest(sourceKey, targetKey).withSourceNamespace(request.getSourceNamespace())
							.withTargetNamespace(request.getTargetNamespace()).withSourceKeyAlgorithm(request.getSourceKeyAlgorithm())
							.withCopyingMetadata(request.isCopyingMetadata()).withCopyingOldVersion(request.isCopyingOldVersion());

					moveObject(moveRequest);

					// final CopyObjectRequest copyRequest = new CopyObjectRequest(sourceKey, targetKey).withCopyingMetadata(request.isCopyingMetadata())
					// .withSourceKeyAlgorithm(request.getSourceKeyAlgorithm()).withSourceNamespace(request.getSourceNamespace());//
					// .withSourceTenant(request.getSourceTenant());
					//
					// copyObject(copyRequest);
					//
					// // The object will be delete with purge option, All the old version will be delete
					// DeleteObjectRequest deleteRequest = new DeleteObjectRequest(sourceKey).withNamespace(request.getSourceNamespace()).withPurgeDelete(true);
					//
					// deleteObject(deleteRequest);

					if (movelistener != null) {
						nextAction = movelistener.afterMoving(objectEntry);
						if (nextAction == NextAction.stop) {
							return nextAction;
						}
					}
				}

				return NextAction.next;
			}
		};

		ListObjectRequest listSubDirRequest = new ListObjectRequest(request.getSourceKey()).withRecursiveDirectory(true).withNamespace(request.getSourceNamespace());

		NextAction nextAction = this.listObjects(listSubDirRequest, handler, null);
		if (nextAction == NextAction.stop) {
			return;
		}

		DeleteDirectoryRequest deleteDirRequest = new DeleteDirectoryRequest(request.getSourceKey()).withNamespace(request.getSourceNamespace()).withPurge(true)
				.withDeleteContainedObjects(true);

		deleteDirectory(deleteDirRequest);
	}

	public void setObjectACL(PutACLRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		// if (request.isSendingInCompressed()) {
		// header = request.header();
		// header.setHeader(HeaderKey.CONTENT_ENCODING, RequestHeadValue.CONTENT_ENCODING.GZIP);
		// }

		param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.ACL);

		InputStream in = ACLUtils.toInputStream(request.getAcl());
		// Creates an entity with a specified content length.
		InputStreamEntity requestEntity = new InputStreamEntity(in, -1);
		request.setEntity(requestEntity);

		execute(request, HCPRestResponseHandler.PUT_ACL_RESPONSE_HANDLER);
	}

	public AccessControlList getObjectACL(GetACLRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.ACL);

		if (StringUtils.isNotEmpty(request.getVersionId())) {
			param.setParameter(RequestParamKey.VERSION, request.getVersionId());
		}

		// Content-Type: application/xml

		AccessControlList result = execute(request, HCPRestResponseHandler.GET_ACL_RESPONSE_HANDLER);

		return result;
	}

	public boolean deleteObjectACL(DeleteACLRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.ACL);

		ACLUserList userList = request.getUserList();
		if (userList != null && userList.getSize() > 0) {
			AccessControlList currentACL = getObjectACL(request.getKey());

			currentACL.remove(userList);

			if (currentACL.hasPermissions()) {
				setObjectACL(request.getKey(), currentACL);
				return true;
			}
		}

		Boolean exist = execute(request, HCPRestResponseHandler.DELETE_ACL_RESPONSE_HANDLER);
		
		return exist;
	}

	public boolean doesObjectACLExist(CheckACLRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.TYPE, RequestParameterValue.Type.ACL);

		if (StringUtils.isNotEmpty(request.getVersionId())) {
			param.setParameter(RequestParamKey.VERSION, request.getVersionId());
		}

		Boolean exist = execute(request, HCPRestResponseHandler.CHECK_ACL_RESPONSE_HANDLER);

		return exist;
	}

	public void setObjectACL(String key, AccessControlList acl) throws InvalidResponseException, HSCException {
		PutACLRequest request = new PutACLRequest(key).withAcl(acl);

		setObjectACL(request);
	}

	public AccessControlList getObjectACL(String key) throws InvalidResponseException, HSCException {
		GetACLRequest request = new GetACLRequest(key);

		return getObjectACL(request);
	}

	public boolean deleteObjectACL(String key) throws InvalidResponseException, HSCException {
		DeleteACLRequest request = new DeleteACLRequest(key);

		return deleteObjectACL(request);
	}

	public boolean doesObjectACLExist(String key) throws InvalidResponseException, HSCException {
		CheckACLRequest request = new CheckACLRequest(key);

		return doesObjectACLExist(request);
	}

	@Override
	public void addObjectACL(String key, AccessControlList acl) throws InvalidResponseException, HSCException {
		AccessControlList currentACL = getObjectACL(key);
		
		currentACL.merge(acl);
		
		setObjectACL(key, acl);
	}
	

//	@Override
//	public void removeObjectACL(String key, AccessControlList acl) throws InvalidResponseException, HSCException {
//		AccessControlList currentACL = getObjectACL(key);
//		
//		currentACL.merge(acl);
//		
//		setObjectACL(key, acl);
//	}
	
	@Override
	public boolean deleteObjectACL(String key, ACLUserList userList) throws InvalidResponseException, HSCException {
		DeleteACLRequest request = new DeleteACLRequest(key).withUserList(userList);

		return deleteObjectACL(request);
	}

	@Override
	public void addObjectACL(PutACLRequest request) throws InvalidResponseException, HSCException {
		AccessControlList currentACL = getObjectACL(new GetACLRequest(request.getKey()).withNamespace(request.getNamespace()));

		if (currentACL != null && currentACL.hasPermissions()) {
			currentACL.merge(request.getAcl());
			request.withAcl(currentACL);
		}

		setObjectACL(request);
	}

}
