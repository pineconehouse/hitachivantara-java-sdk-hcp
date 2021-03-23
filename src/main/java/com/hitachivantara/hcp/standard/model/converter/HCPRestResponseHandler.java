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
package com.hitachivantara.hcp.standard.model.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.amituofo.common.api.IOCloseListener;
import com.amituofo.common.util.StreamUtils;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.util.EntityUtils;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.common.define.HCPResponseKey;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.SimpleXMLReader;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.common.util.XMLUtils;
import com.hitachivantara.hcp.management.define.RetentionMode;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.body.impl.HCPObjectEntryIterator;
import com.hitachivantara.hcp.standard.define.ACLDefines;
import com.hitachivantara.hcp.standard.define.ACLDefines.ACLPermission;
import com.hitachivantara.hcp.standard.define.ObjectType;
import com.hitachivantara.hcp.standard.define.RequestParamKey;
import com.hitachivantara.hcp.standard.io.HCPObjectInputStream;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.HCPStandardResult;
import com.hitachivantara.hcp.standard.model.Initiator;
import com.hitachivantara.hcp.standard.model.NamespaceBasicSetting;
import com.hitachivantara.hcp.standard.model.NamespacePermission;
import com.hitachivantara.hcp.standard.model.NamespacePermissions;
import com.hitachivantara.hcp.standard.model.NamespaceStatistics;
import com.hitachivantara.hcp.standard.model.Owner;
import com.hitachivantara.hcp.standard.model.PartSummary;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.RetentionClasses;
import com.hitachivantara.hcp.standard.model.UploadPartList;
import com.hitachivantara.hcp.standard.model.UploadPartResult;
import com.hitachivantara.hcp.standard.model.metadata.AccessControlList;
import com.hitachivantara.hcp.standard.model.metadata.Annotation;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadata;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummary;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummarys;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;

public class HCPRestResponseHandler {

	public final static ResponseHandler<NamespacePermissions> GET_NAMESPACE_PERMISSIONS_RESPONSE_HANDLER = new ResponseHandler<NamespacePermissions>() {

		@Override
		public NamespacePermissions handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
				Element root = getRootElement(response);

				Element namespacePermissionsNode = root.element("namespacePermissions");
				NamespacePermission namespacePermissions = extractPermission(namespacePermissionsNode);

				Element namespaceEffectivePermissionsNode = root.element("namespaceEffectivePermissions");
				NamespacePermission namespaceEffectivePermissions = extractPermission(namespaceEffectivePermissionsNode);

				Element userPermissionsNode = root.element("userPermissions");
				NamespacePermission userPermissions = extractPermission(userPermissionsNode);

				Element userEffectivePermissionsNode = root.element("userEffectivePermissions");
				NamespacePermission userEffectivePermissions = extractPermission(userEffectivePermissionsNode);

				NamespacePermissions results = new NamespacePermissions(namespacePermissions, namespaceEffectivePermissions, userPermissions, userEffectivePermissions);

				return results;
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			} finally {
				close(response);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		private NamespacePermission extractPermission(Element namespacePermissionsNode) {
			boolean browse = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("browse"));
			boolean read = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("read"));
			boolean write = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("write"));
			boolean readAcl = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("readAcl"));
			boolean writeAcl = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("writeAcl"));
			boolean changeOwner = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("changeOwner"));
			boolean delete = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("delete"));
			boolean privileged = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("privileged"));
			boolean purge = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("purge"));
			boolean search = Boolean.parseBoolean(namespacePermissionsNode.attributeValue("search"));

			NamespacePermission permission = new NamespacePermission(browse, read, write, readAcl, writeAcl, changeOwner, delete, privileged, purge, search);
			return permission;
		}

	};
	

//	public static final ResponseHandler<Boolean> HEAD_NAMESPACES_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {
//
//		public boolean isValidResponse(HttpResponse response) {
//			return ValidUtils.isResponse(response, HCPResponseKey.OK);
//		}
//
//		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
//			close(response);
//
//			return Boolean.TRUE;
//		}
//
//	};

	public final static ResponseHandler<NamespaceBasicSetting> GET_NAMESPACES_RESPONSE_HANDLER = new ResponseHandler<NamespaceBasicSetting>() {

		@Override
		public NamespaceBasicSetting handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			List<NamespaceBasicSetting> results = LIST_ACCESSIBLE_NAMESPACES_RESPONSE_HANDLER.handleValidResponse(request, response);
			if (results.size() == 1) {
				return results.get(0);
			} else {
				throw new InvalidResponseException("Unable to get namespace settings.");
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<List<NamespaceBasicSetting>> LIST_ACCESSIBLE_NAMESPACES_RESPONSE_HANDLER = new ResponseHandler<List<NamespaceBasicSetting>>() {

		@Override
		public List<NamespaceBasicSetting> handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			List<NamespaceBasicSetting> results = new ArrayList<NamespaceBasicSetting>();

			try {
//				 InputStream in = response.getEntity().getContent();
//				 StreamUtils.inputStreamToConsole(in, true);

				Element root = getRootElement(response);
				List<Node> namespaceNodes = root.selectNodes("namespace");
				for (int i = 0; i < namespaceNodes.size(); i++) {
					Element namespaceEl = (Element) namespaceNodes.get(i);

					String name = namespaceEl.attributeValue("name");
					String nameIDNA = namespaceEl.attributeValue("nameIDNA");
					boolean versioningEnabled = Boolean.parseBoolean(namespaceEl.attributeValue("versioningEnabled"));
					boolean searchEnabled = Boolean.parseBoolean(namespaceEl.attributeValue("searchEnabled"));
					RetentionMode retentionMode = RetentionMode.valueOf(namespaceEl.attributeValue("retentionMode").toLowerCase());
					boolean defaultShredValue = Boolean.parseBoolean(namespaceEl.attributeValue("defaultShredValue"));
					boolean defaultIndexValue = Boolean.parseBoolean(namespaceEl.attributeValue("defaultIndexValue"));
					String defaultRetentionValue = namespaceEl.attributeValue("defaultRetentionValue");
					String hashScheme = namespaceEl.attributeValue("hashScheme");
					String dpl = namespaceEl.attributeValue("dpl");
					List<Node> descriptionNodes = namespaceEl.selectNodes("description");

					String description = (descriptionNodes != null && descriptionNodes.size() > 0 ? descriptionNodes.get(0).getText().trim() : "");

					NamespaceBasicSetting ns = new NamespaceBasicSetting(
							name,
							nameIDNA,
							versioningEnabled,
							searchEnabled,
							retentionMode,
							defaultShredValue,
							defaultIndexValue,
							defaultRetentionValue,
							hashScheme,
							dpl,
							description);
					results.add(ns);
				}
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			} finally {
				close(response);
			}

			return results;
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<List<RetentionClasses>> LIST_RETENTION_CLASSES_RESPONSE_HANDLER = new ResponseHandler<List<RetentionClasses>>() {

		@Override
		public List<RetentionClasses> handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			List<RetentionClasses> results = new ArrayList<RetentionClasses>();
			try {
				InputStream in = response.getEntity().getContent();
				SAXReader saxReader = new SAXReader();
				Document doc = saxReader.read(in);
				List<Node> namespaceNodes = doc.getRootElement().selectNodes("retentionClass");
				for (int i = 0; i < namespaceNodes.size(); i++) {
					Element namespaceEl = (Element) namespaceNodes.get(i);

					String name = namespaceEl.attributeValue("name");
					String value = namespaceEl.attributeValue("value");
					boolean autoDelete = Boolean.parseBoolean(namespaceEl.attributeValue("autoDelete"));
					String description = namespaceEl.selectSingleNode("description").getText().trim();

					RetentionClasses ns = new RetentionClasses(name, value, autoDelete, description);
					results.add(ns);
				}
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			} finally {
				close(response);
			}

			return results;
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<NamespaceStatistics> GET_NAMESPACES_STATISTICS_RESPONSE_HANDLER = new ResponseHandler<NamespaceStatistics>() {

		@Override
		public NamespaceStatistics handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
				Element root = getRootElement(response);

				String namespaceName = root.attributeValue("namespaceName");
				long totalCapacityBytes = Long.parseLong(root.attributeValue("totalCapacityBytes"));
				long usedCapacityBytes = Long.parseLong(root.attributeValue("usedCapacityBytes"));
				double softQuotaPercent = Double.parseDouble(root.attributeValue("softQuotaPercent"));
				long objectCount = Long.parseLong(root.attributeValue("objectCount"));
				long shredObjectCount = Long.parseLong(root.attributeValue("shredObjectCount"));
				long shredObjectBytes = Long.parseLong(root.attributeValue("shredObjectBytes"));
				long customMetadataObjectCount = Long.parseLong(root.attributeValue("customMetadataObjectCount"));
				long customMetadataObjectBytes = Long.parseLong(root.attributeValue("customMetadataObjectBytes"));

				NamespaceStatistics result = new NamespaceStatistics(
						namespaceName,
						totalCapacityBytes,
						usedCapacityBytes,
						softQuotaPercent,
						objectCount,
						shredObjectCount,
						shredObjectBytes,
						customMetadataObjectCount,
						customMetadataObjectBytes);

				return result;
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			} finally {
				close(response);
			}

		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<PutObjectResult> PUT_OBJECT_RESPONSE_HANDLER = new ResponseHandler<PutObjectResult>() {

		public boolean isValidResponse(HttpResponse response) {
			// HCP successfully stored the object. If versioning is enabled and
			// an object with the same name
			// already exists, HCP created a new version of the object.
			return ValidUtils.isAnyResponse(response, HCPResponseKey.CREATED, HCPResponseKey.NOT_MODIFIED);
		}

		public PutObjectResult handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			// -Header:--------------------------------------------------------------------------
			// Date=Fri, 20 Apr 2018 01:49:21 GMT
			// Server=HCP V8.0.0.10-
			// X-HCP-ServicedBySystem=hcp8.hdim.lab
			// Location=/rest/sdk-test/TestHCP3047663538126820589.txt
			// X-HCP-VersionId=97548093539073
			// X-HCP-IngestTimeMilliseconds=1524188961548
			// X-HCP-Hash=SHA-256
			// 0974E8AAA6974E4BCA5585F9CEE2802F8ECC300134D539B47621B0DE943D2921
			// ETag="f3855d2bb4261d891c9fdd41caed8e30"
			// X-RequestId=5F8E1FF3F36B5C6B
			// X-HCP-Time=1524188961
			// Content-Length=0
			
//			hcp8.2
			// 61187975HttpResponseProxy{HTTP/1.1 201 Created [Date: Wed,
			// 18 Mar 2020 08:12:47 GMT,
			// Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-eval' 'unsafe-inline'; connect-src 'self'; img-src 'self';
			// style-src 'self' 'unsafe-inline'; object-src 'self'; frame-ancestors 'self';,
			// Cache-Control: no-cache,
			// no-store,
			// must-revalidate,
			// X-Download-Options: noopen,
			// Strict-Transport-Security: max-age=31536000; includeSubDomains,
			// X-Frame-Options: SAMEORIGIN,
			// Pragma: no-cache,
			// Vary: Origin,
			// Access-Control-Request-Headers,
			// Access-Control-Request-Method,
			// X-XSS-Protection: 1; mode=block,
			// Expires: Thu,
			// 01 Jan 1970 00:00:00 GMT,
			// X-DNS-Prefetch-Control: off,
			// X-Content-Type-Options: nosniff,
			// X-HCP-ServicedBySystem: hcp-demo.hcpdemo.com,
			// Location: /rest/backup/lib/amtf-datafile.jar,
			// X-HCP-VersionId: 101409226728257,
			// X-HCP-VersionCreateTimeMilliseconds: 1584519167629,
			// X-HCP-Hash: MD5 3BF226CD3F09F4D472668ABBE52E1D1D,
			// ETag: "3bf226cd3f09f4d472668abbe52e1d1d",
			// X-RequestId: D37CFDBDC82B662F,
			// X-HCP-Time: 1584519167,
			// Content-Length: 0] [Content-Length: 0,
			// Chunked: false]}
			
			String location = HCPHeaderKey.LOCATION.parse(response);
			String versionId = HCPHeaderKey.X_HCP_VERSION_ID.parse(response);
			String[] contentHash = HCPHeaderKey.X_HCP_HASH.parse(response);
			String eTag = HCPHeaderKey.ETAG.parse(response);
			Long ingestTimeMilliseconds = HCPHeaderKey.X_HCP_INGEST_TIME_MILLISECONDS.parse(response);
			if (ingestTimeMilliseconds == null) {
				ingestTimeMilliseconds = HCPHeaderKey.X_HCP_VERSION_CREATE_TIME_MILLISECONDS.parse(response);
			}

			PutObjectResult result = new PutObjectResult(location, versionId, contentHash[0], contentHash[1], eTag, ingestTimeMilliseconds);

			close(response);

			return result;
		}
	};

	public static final ResponseHandler<Boolean> CREATE_DIR_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.CREATED, HCPResponseKey.CONFLICT);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			// HCP could not create the directory in the namespace because a
			// directory, object, or symbolic link
			// with the specified name already exists.
			if (ValidUtils.isResponse(response, HCPResponseKey.CONFLICT)) {

				close(response);

				return Boolean.FALSE;
			}

			close(response);

			// HCP successfully created the directory.
			return Boolean.TRUE;
		}

	};

	public static final ResponseHandler<Boolean> DELETE_OBJECT_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NOT_FOUND);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			if (ValidUtils.isResponse(response, HCPResponseKey.NOT_FOUND)) {

				close(response);

				// The specified object does not have the requested annotation.
				return Boolean.FALSE;
			}

			close(response);

			// The specified object has the requested annotation.
			return Boolean.TRUE;
		}

	};

	public static final ResponseHandler<HCPObject> GET_OBJECT_RESPONSE_HANDLER = new ResponseHandler<HCPObject>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public HCPObject handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			// -Body:----------------------------------------------------------------------------<br>
			// 2017-12-27<br>
			// Mon Apr 02 16:07:43 CST 2018<br>
			// -Resp:----------------------------------------------------------------------------<br>
			// Code : 200 OK<br>
			// -Header:--------------------------------------------------------------------------<br>
			// Date=Mon, 02 Apr 2018 08:34:48 GMT<br>
			// Server=HCP V8.0.0.10-<br>
			// X-RequestId=5F8E1FF3F18B0281<br>
			// X-HCP-ServicedBySystem=hcp8.hdim.lab<br>
			// X-HCP-Time=1522658088<br>
			// X-HCP-SoftwareVersion=8.0.0.10<br>
			// ETag="4c77ca9ccd1aab26210483d7070b341e"<br>
			// Cache-Control=no-cache,no-store<br>
			// Pragma=no-cache<br>
			// Expires=Thu, 01 Jan 1970 00:00:00 GMT<br>
			// Content-Type=text/plain<br>
			// X-HCP-ContentLength=42<br>
			// X-HCP-Type=object<br>
			// X-HCP-Size=42<br>
			// X-HCP-Hash=SHA-256
			// 5D45EA970F0782E780A22D74321F6D6C839FACF6525E0E41F561B927AA0A4BA6<br>
			// X-HCP-VersionId=97450013608321<br>
			// X-HCP-IngestTime=1522656462<br>
			// X-HCP-IngestTimeMilliseconds=1522656462630<br>
			// X-HCP-RetentionClass=<br>
			// X-HCP-RetentionString=Deletion Allowed<br>
			// X-HCP-Retention=0<br>
			// X-HCP-IngestProtocol=HTTP<br>
			// X-HCP-RetentionHold=false<br>
			// X-HCP-Shred=false<br>
			// X-HCP-DPL=1<br>
			// X-HCP-Index=true<br>
			// X-HCP-Custom-Metadata=false<br>
			// X-HCP-ACL=false<br>
			// X-HCP-Owner=admin<br>
			// X-HCP-Domain=<br>
			// X-HCP-UID=<br>
			// X-HCP-GID=<br>
			// X-HCP-CustomMetadataAnnotations=<br>
			// X-HCP-Replicated=false<br>
			// X-HCP-ReplicationCollision=false<br>
			// X-HCP-ChangeTimeMilliseconds=1522656462729.00<br>
			// X-HCP-ChangeTimeString=2018-04-02T16:07:42+0800<br>
			// Last-Modified=Mon, 02 Apr 2018 08:07:42 GMT<br>
			// ----------------------------------------------------------------------------------<br>

			// if (ValidUtils.isResponse(response, ResponseKey.NOT_FOUND)) {
			// HCPObject hcpObj = new HCPObject();
			//
			// hcpObj.setKey(key);
			// hcpObj.setName(URLUtils.getRequestTargetName(key));
			//
			// return hcpObj;
			// }

			// if (ValidUtils.isResponse(response, ResponseKey.OK)) {
			HCPObjectInputStream content = getCloseHandleableContent(response);

			HCPObject hcpObj = new HCPObject();

			hcpObj.setKey(((HCPStandardRequest) request).getKey());
			hcpObj.setName(URLUtils.getRequestTargetName(((HCPStandardRequest) request).getKey()));

			hcpObj.setContent(content);

			injectCommonProperties(hcpObj, response);

			injectObjectSummary(hcpObj, response);

			return hcpObj;
		}

	};

	public static final ResponseHandler<HCPObject> GET_PARTIAL_OBJECT_RESPONSE_HANDLER = new ResponseHandler<HCPObject>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.PARTIAL_CONTENT);
		}

		public HCPObject handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			return GET_OBJECT_RESPONSE_HANDLER.handleValidResponse(request, response);
		}

	};

	public static final ResponseHandler<HCPObjectEntrys> LIST_OBJECT_RESPONSE_HANDLER = new ResponseHandler<HCPObjectEntrys>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public HCPObjectEntrys handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			HCPObjectEntrys hcpObjects = null;
			try {
				InputStream content = getCloseHandleableContent(response);
				hcpObjects = new HCPObjectEntrys(
						((HCPStandardRequest) request).getKey(),
						new HCPObjectEntryIterator(((HCPStandardRequest) request).getKey(), ObjectType.object, content));
			} catch (Exception e) {
				throw new InvalidResponseException(e);
//			} finally {
//				close(response);
			}

			return hcpObjects;
		}
	};

	public static final ResponseHandler<HCPObjectEntrys> LIST_DIR_RESPONSE_HANDLER = new ResponseHandler<HCPObjectEntrys>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public HCPObjectEntrys handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			String dir = (String) ((HCPStandardRequest) request).getKey();
			HCPObjectEntrys hcpObjects = null;
			try {
				InputStream content = getCloseHandleableContent(response);
				hcpObjects = new HCPObjectEntrys(dir, new HCPObjectEntryIterator(dir, ObjectType.directory, content));
			} catch (Exception e) {
				throw new InvalidResponseException(e);
				// } finally {
				// close(response);
			}

			return hcpObjects;
		}
	};

	public static final ResponseHandler<HCPObjectSummary> HEAD_OBJECT_RESPONSE_HANDLER = new ResponseHandler<HCPObjectSummary>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public HCPObjectSummary handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			HCPObjectSummary summary = new HCPObjectSummary();

			summary.setKey(((HCPStandardRequest) request).getKey());
			summary.setName(URLUtils.getRequestTargetName(((HCPStandardRequest) request).getKey()));

			injectCommonProperties(summary, response);

			injectObjectSummary(summary, response);

			if (summary.isDirectory() && StringUtils.isEmpty(summary.getName())) {
				String n = URLUtils.getLastRequestPath(summary.getKey());
				summary.setName(n);
			}

			close(response);

			return summary;
		}

	};

	public static final ResponseHandler<Boolean> CHECK_OBJECT_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			// 204 No Content The requested version is a deleted version.
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NOT_FOUND, HCPResponseKey.NO_CONTENT, HCPResponseKey.NOT_MODIFIED);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			if (ValidUtils.isResponse(response, HCPResponseKey.OK)) {

				close(response);

				return Boolean.TRUE;
			} else {

				close(response);

				return Boolean.FALSE;
			}
		}

	};

	public static final ResponseHandler<Boolean> PUT_CUSTOM_METADATA_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			// HCP successfully stored the object. If versioning is enabled and
			// an object with the same name
			// already exists, HCP created a new version of the object.
			return ValidUtils.isResponse(response, HCPResponseKey.CREATED);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			close(response);
			return Boolean.TRUE;
		}

	};

	public static final ResponseHandler<HCPMetadata> GET_CUSTOM_METADATA_RESPONSE_HANDLER = new ResponseHandler<HCPMetadata>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public HCPMetadata handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			// -Header:--------------------------------------------------------------------------
			// Date=Wed, 25 Apr 2018 06:34:50 GMT
			// Server=HCP V8.0.0.10-
			// X-RequestId=5F8E1FF3F401065F
			// X-HCP-ServicedBySystem=hcp8.hdim.lab
			// X-HCP-Time=1524638090
			// X-HCP-SoftwareVersion=8.0.0.10
			// ETag="814aede1bd9d4b85a13a54b49ccbebfe"
			// Cache-Control=no-cache,no-store
			// Pragma=no-cache
			// Expires=Thu, 01 Jan 1970 00:00:00 GMT
			// X-HCP-ContentLength=6251
			// X-HCP-Type=annotation
			// X-HCP-Size=6251
			// X-HCP-ChangeTimeMilliseconds=1524638069000.00
			// X-HCP-ChangeTimeString=2018-04-25T14:34:29+0800
			// X-HCP-Hash=SHA-256
			// 178A6FAC6E65E22AE13B111EAF2041C0B5270E4BF5E21D0C520E574110C4A497
			// Content-Type=text/xml
			// ----------------------------------------------------------------------------------

			String etag = HCPHeaderKey.ETAG.parse(response);
			// String cache-Control
			// String Pragma
			// String Expires
			Long size = HCPHeaderKey.X_HCP_SIZE.parse(response);
			String[] contentHash = HCPHeaderKey.X_HCP_HASH.parse(response);
			Long changeTimeMilliseconds = HCPHeaderKey.X_HCP_CHANGE_TIME_MILLISECONDS.parse(response);
			// String ChangeTimeString

			InputStream content = getCloseHandleableContent(response);

			String metadataName = request.getRequestParameter().getParameter(RequestParamKey.ANNOTATION);
			HCPMetadata metadata = new HCPMetadata(metadataName, content);

			injectCommonProperties(metadata, response);

			metadata.setETag(etag);
			// cache-Control
			// Pragma
			// Expires
			metadata.setSize(size);
			metadata.setHashAlgorithmName(contentHash != null ? contentHash[0] : null);
			metadata.setContentHash(contentHash != null ? contentHash[1] : null);
			metadata.setChangeTime(changeTimeMilliseconds);

			return metadata;
		}

	};

	public static final ResponseHandler<HCPMetadataSummarys> LIST_METADATA_RESPONSE_HANDLER = new ResponseHandler<HCPMetadataSummarys>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NO_CONTENT);
		}

		public HCPMetadataSummarys handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			if (ValidUtils.isResponse(response, HCPResponseKey.NO_CONTENT)) {
				close(response);

				HCPMetadataSummarys metadatas = new HCPMetadataSummarys(new ArrayList<HCPMetadataSummary>());
				return metadatas;
			} else {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				InputStream in = null;
				try {
					in = response.getEntity().getContent();
					MetadataListHandler handler = new MetadataListHandler();
					SAXParser parser = factory.newSAXParser();
					parser.parse(in, handler);
					List<HCPMetadataSummary> list = handler.getMetadatas();
					handler = null;
					
					HCPMetadataSummarys metadatas = new HCPMetadataSummarys(list);
					
					injectCommonProperties(metadatas, response);
					
					return metadatas;
				} catch (Exception e) {
					throw new InvalidResponseException(e);
				} finally {
					close(response);
					factory = null;
				}
			}
		}

	};

	public static final ResponseHandler<Boolean> DEFAULT_DELETE_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NO_CONTENT);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			if (ValidUtils.isResponse(response, HCPResponseKey.NO_CONTENT)) {
				close(response);

				// The specified object does not have the requested annotation.
				return Boolean.FALSE;
			}

			close(response);

			// The specified object has the requested annotation.
			return Boolean.TRUE;
		}

	};

	public static final ResponseHandler<Boolean> DELETE_CUSTOM_METADATA_RESPONSE_HANDLER = DEFAULT_DELETE_RESPONSE_HANDLER;

	public static final ResponseHandler<Boolean> CHECK_CUSTOM_METADATA_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NOT_FOUND, HCPResponseKey.NO_CONTENT);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			if (ValidUtils.isResponse(response, HCPResponseKey.OK)) {
				close(response);
				// The specified object has the requested annotation.
				return Boolean.TRUE;
			} else {
				close(response);
				// The specified object does not have the requested annotation.
				return Boolean.FALSE;
			}
		}

	};

	public static final ResponseHandler<Boolean> CHECK_DIR_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NOT_FOUND);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			if (ValidUtils.isResponse(response, HCPResponseKey.NOT_FOUND)) {
				close(response);
				// HCP could not find a directory
				return Boolean.FALSE;
			}

			close(response);
			// HCP found a directory or object at the specified URL.
			return Boolean.TRUE;
		}

	};

	public static final ResponseHandler<Boolean> PUT_SYSTEM_METADATA_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			close(response);
			return Boolean.TRUE;
		}

	};

	public static final ResponseHandler<Boolean> COPY_OBJECT_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.CREATED, HCPResponseKey.OK, HCPResponseKey.NOT_MODIFIED);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			close(response);
			return Boolean.TRUE;
		}

	};

	public static final ResponseHandler<String> MULTIUP_INIT_RESPONSE_HANDLER = new ResponseHandler<String>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public String handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			// <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
			// <InitiateMultipartUploadResult xmlns="http://www.hcp.com/doc/">
			// <Namespace>namespace-name</Namespace>
			// <Key>object-name</Key>
			// <UploadId>upload-id</UploadId>
			// </InitiateMultipartUploadResult>
			String responseXml = null;
			try {
				responseXml = StreamUtils.inputStreamToString(response.getEntity().getContent(), true);
				String temp = responseXml.toLowerCase();
				int is = temp.indexOf("<uploadid>");
				int ie = temp.indexOf("</uploadid>");
				if (is != -1 && ie != -1) {
					return responseXml.substring(is + 10, ie);
				}
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			} finally {
				close(response);
			}

			throw new InvalidResponseException("Invalid response content. " + responseXml);
		}

	};

	public static final ResponseHandler<UploadPartResult> MULTIUP_UPLOAD_RESPONSE_HANDLER = new ResponseHandler<UploadPartResult>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public UploadPartResult handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			UploadPartResult result = new UploadPartResult();

			injectCommonProperties(result, response);

			String etag = HCPHeaderKey.ETAG.parse(response);
			String partNumber = request.getRequestParameter().getParameter(RequestParamKey.PART_NUMBER);
			String versionId = HCPHeaderKey.X_AMZ_VERSION_ID.parse(response);

			result.setKey(((HCPStandardRequest) request).getKey());
			result.setVersionId(versionId);
			result.setETag(etag);
			result.setPartNumber(Integer.valueOf(partNumber));
			// multipart upload, the value of this header is always 0 (zero).
			// result.setSize(result.getContentLength());
			close(response);

			return result;
		}

	};

	public static final ResponseHandler<UploadPartResult> MULTIUP_COPY_RESPONSE_HANDLER = MULTIUP_UPLOAD_RESPONSE_HANDLER;

	public static final ResponseHandler<String> MULTIUP_COMPLETE_RESPONSE_HANDLER = new ResponseHandler<String>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public String handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			String resultInXML = "";
			try {
				// InputStream is = response.getEntity().getContent();
				// DocumentBuilderFactory docfac = DocumentBuilderFactory.newInstance();
				// Document doc = docfac.newDocumentBuilder().parse(is);

				resultInXML = StreamUtils.inputStreamToString(response.getEntity().getContent(), true);

			} catch (Exception e) {
				close(response);
				throw new InvalidResponseException("Error when parse response.", e);
			}

			if (resultInXML.contains("<Error>")) {
				// <Error>
				// <Code>InvalidPartOrder</Code>
				// <Message>The list of parts was not in ascending order. Parts must be ordered by part number.</Message>
				// <RequestId>1533015297164</RequestId>
				// <HostId>aGNwOC5oZGltLmxhYjo3NQ==</HostId>
				// <UploadId>98112977754497</UploadId>
				// </Error>
				int iMsg = resultInXML.indexOf("<Message>");
				if (iMsg != -1) {
					String msg = resultInXML.substring(iMsg + 9, resultInXML.indexOf("</Message>", iMsg + 10));
					close(response);
					throw new InvalidResponseException(msg);
				}
			} else {
				// <?xml version='1.0' encoding='UTF-8'?>
				// <CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc
				// /2006-03-01/">
				// <Location>object-url</Location>
				// <Namespace>namespace-name</Namespace>
				// <Key>object-name</Key>
				// <ETag>&quot;etag&quot;</ETag>
				// </CompleteMultipartUploadResult>

			}

			close(response);

			return null;
		}

	};

	public static final ResponseHandler<Object> MULTIUP_DELETE_RESPONSE_HANDLER = new ResponseHandler<Object>() {

		public boolean isValidResponse(HttpResponse response) {
			// 204 No Content
			// HCP successfully aborted the multipart upload.
			return ValidUtils.isResponse(response, HCPResponseKey.NO_CONTENT);
		}

		public Object handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			close(response);
			return null;
		}

	};

	public static final ResponseHandler<UploadPartList> MULTIUP_LIST_RESPONSE_HANDLER = new ResponseHandler<UploadPartList>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public UploadPartList handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			// injectCommonProperties(result, response);
			// <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
			// <ListPartsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
			// <Namespace>cloud</Namespace>
			// <Key>sdk-test/multpartObj_key9uc</Key>
			// <UploadId>98113077233217</UploadId>
			// <Initiator>
			// <ID>4f0e935f-feba-48e4-98eb-c52872a66938</ID>
			// <DisplayName>admin</DisplayName>
			// </Initiator>
			// <Owner>
			// <ID>4f0e935f-feba-48e4-98eb-c52872a66938</ID>
			// <DisplayName>admin</DisplayName>
			// </Owner>
			// <StorageClass>STANDARD</StorageClass>
			// <PartNumberMarker>0</PartNumberMarker>
			// <NextPartNumberMarker>0</NextPartNumberMarker>
			// <MaxParts>1000</MaxParts>
			// <IsTruncated>false</IsTruncated>
			// <Part>
			// <PartNumber>1</PartNumber>
			// <LastModified>2018-07-31T06:00:40.523Z</LastModified>
			// <ETag>"8af9b0ec89a6e7a531608de704dee60a"</ETag>
			// <Size>8388608</Size>
			// </Part>
			// <Part>
			// <PartNumber>2</PartNumber>
			// <LastModified>2018-07-31T06:00:50.164Z</LastModified>
			// <ETag>"10380dfafe00e09817d0c64c6c4d5308"</ETag>
			// <Size>8388608</Size>
			// </Part>
			// </ListPartsResult>

			String xmlContent = null;
			try {
				xmlContent = StreamUtils.inputStreamToString(response.getEntity().getContent(), true);
			} catch (Exception e) {
				close(response);
				throw new InvalidResponseException("Error when parse response.", e);
			}

			SimpleXMLReader reader = new SimpleXMLReader(xmlContent);

			String namespace = reader.trackSingleNodeValue("Namespace");
			String key = reader.trackSingleNodeValue("Key");
			String uploadId = reader.trackSingleNodeValue("UploadId");
			String storageClass = reader.trackSingleNodeValue("StorageClass");
			Integer partNumberMarker = StringUtils.toInteger(reader.trackSingleNodeValue("PartNumberMarker"));
			Integer nextPartNumberMarker = StringUtils.toInteger(reader.trackSingleNodeValue("NextPartNumberMarker"));
			Integer maxParts = StringUtils.toInteger(reader.trackSingleNodeValue("MaxParts"));
			Boolean truncated = StringUtils.toBoolean(reader.trackSingleNodeValue("IsTruncated"));

			String InitiatorNode = reader.trackSingleNodeValue("Initiator");
			Initiator initiator = new Initiator(XMLUtils.trackSingleNodeValue(InitiatorNode, "DisplayName"), XMLUtils.trackSingleNodeValue(InitiatorNode, "ID"));

			String OwnerNode = reader.trackSingleNodeValue("Owner");
			Owner owner = new Owner(XMLUtils.trackSingleNodeValue(OwnerNode, "DisplayName"), XMLUtils.trackSingleNodeValue(OwnerNode, "ID"));

			UploadPartList result = new UploadPartList(namespace, key, uploadId, initiator, owner, storageClass, partNumberMarker, nextPartNumberMarker, maxParts, truncated);

			List<String> nodes = reader.trackNodeValue(0, "Part");
			for (String node2 : nodes) {
				SimpleXMLReader node2reader = new SimpleXMLReader(node2);
				Integer partNumber = StringUtils.toInteger(node2reader.trackSingleNodeValue("PartNumber"));
				String eTag = node2reader.trackSingleNodeValue("ETag");
				eTag = HCPHeaderKey.ETAG.parse(eTag);
				Long size = StringUtils.toLong(node2reader.trackSingleNodeValue("Size"));
				String lastModified = node2reader.trackSingleNodeValue("LastModified");

				PartSummary summary = new PartSummary(partNumber, size, eTag, lastModified);
				result.add(summary);
			}

			close(response);

			return result;
		}

	};

	public static final ResponseHandler<Object> DELETE_OBJECTS_RESPONSE_HANDLER = new ResponseHandler<Object>() {

		public boolean isValidResponse(HttpResponse response) {
			// HCP successfully stored the ACL.
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NO_CONTENT);
		}

		public Object handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			close(response);
			return null;
		}

	};

	public static final ResponseHandler<Object> PUT_ACL_RESPONSE_HANDLER = new ResponseHandler<Object>() {

		public boolean isValidResponse(HttpResponse response) {
			// HCP successfully stored the ACL.
			return ValidUtils.isResponse(response, HCPResponseKey.CREATED);
		}

		public Object handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			close(response);
			return null;
		}

	};

	public static final ResponseHandler<Boolean> CHECK_ACL_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		public boolean isValidResponse(HttpResponse response) {
			// HCP successfully stored the ACL.
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			Boolean exist = HCPHeaderKey.X_HCP_ACL.parse(response);

			close(response);

			return exist;
		}

	};

	public static final ResponseHandler<Boolean> DELETE_ACL_RESPONSE_HANDLER = DEFAULT_DELETE_RESPONSE_HANDLER;

	public static final ResponseHandler<AccessControlList> GET_ACL_RESPONSE_HANDLER = new ResponseHandler<AccessControlList>() {

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		public AccessControlList handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			String xmlContent = null;
			try {
				xmlContent = StreamUtils.inputStreamToString(response.getEntity().getContent(), true);
			} catch (Exception e) {
				close(response);
				throw new InvalidResponseException("Error when parse response.", e);
			}

			AccessControlList acl = new AccessControlList();

			SimpleXMLReader reader = new SimpleXMLReader(xmlContent);

			List<String> grants = reader.trackNodeValue(0, "grant");
			for (String grant : grants) {
				SimpleXMLReader grantNodeReader = new SimpleXMLReader(grant);
				String nameInStr = grantNodeReader.trackSingleNodeValue("name");
				String typeInStr = grantNodeReader.trackSingleNodeValue("type");
				String domainInStr = grantNodeReader.trackSingleNodeValue("domain");
				List<String> permissionsInStr = grantNodeReader.trackNodeValue(0, "permission");

				ACLPermission[] permissions = new ACLPermission[permissionsInStr.size()];
				for (int i = 0; i < permissions.length; i++) {
					permissions[i] = ACLPermission.valueOf(permissionsInStr.get(i).toUpperCase());
				}
				acl.grantPermissions(ACLDefines.Type.valueOf(typeInStr.toUpperCase()), nameInStr, domainInStr, permissions);
			}

			close(response);

			return acl;
		}

	};

	// public static PutObjectResult toPutObjectResult(HttpResponse response) {
	// // -Header:--------------------------------------------------------------------------
	// // Date=Fri, 20 Apr 2018 01:49:21 GMT
	// // Server=HCP V8.0.0.10-
	// // X-HCP-ServicedBySystem=hcp8.hdim.lab
	// // Location=/rest/sdk-test/TestHCP3047663538126820589.txt
	// // X-HCP-VersionId=97548093539073
	// // X-HCP-IngestTimeMilliseconds=1524188961548
	// // X-HCP-Hash=SHA-256 0974E8AAA6974E4BCA5585F9CEE2802F8ECC300134D539B47621B0DE943D2921
	// // ETag="f3855d2bb4261d891c9fdd41caed8e30"
	// // X-RequestId=5F8E1FF3F36B5C6B
	// // X-HCP-Time=1524188961
	// // Content-Length=0
	//
	// String location = (String) HCPHeaderKey.LOCATION.parse(response);
	// String versionId = (String) HCPHeaderKey.X_HCP_VERSION_ID.parse(response);
	// String[] contentHash = (String[]) HCPHeaderKey.X_HCP_HASH.parse(response);
	// String eTag = (String) HCPHeaderKey.ETAG.parse(response);
	// Long ingestTimeMilliseconds = (Long) HCPHeaderKey.X_HCP_INGEST_TIME_MILLISECONDS.parse(response);
	//
	// PutObjectResult result = new PutObjectResult(location, versionId, contentHash[0], contentHash[1],
	// eTag, ingestTimeMilliseconds);
	// return result;
	// }

	public static HCPObjectSummary injectObjectSummary(HCPObjectSummary hcpObj, HttpResponse response) {
		// Date=Tue, 22 May 2018 07:36:24 GMT
		// Server=HCP V8.0.0.10-
		// X-RequestId=65C06EC0EB496310
		// X-HCP-ServicedBySystem=hcp8.hdim.lab
		// X-HCP-Time=1526974584
		// X-HCP-SoftwareVersion=8.0.0.10
		// ETag="814aede1bd9d4b85a13a54b49ccbebfe"
		// Cache-Control=no-cache,no-store
		// Pragma=no-cache
		// Expires=Thu, 01 Jan 1970 00:00:00 GMT
		// Content-Type=text/plain
		// Content-Length=42
		// X-HCP-Type=object
		// X-HCP-Size=42
		// X-HCP-Hash=SHA-256 373AB23A0178AD90E64E5C25FAF26EC8A83E27136FB7E0688793711A73DCC8D8
		// X-HCP-VersionId=97548415849473
		// X-HCP-IngestTime=1524193997
		// X-HCP-IngestTimeMilliseconds=1524193997648
		// X-HCP-RetentionClass=
		// X-HCP-RetentionString=Deletion Allowed
		// X-HCP-Retention=0
		// X-HCP-IngestProtocol=HTTP
		// X-HCP-RetentionHold=false
		// X-HCP-Shred=false
		// X-HCP-DPL=1
		// X-HCP-Index=true
		// X-HCP-Custom-Metadata=true
		// X-HCP-ACL=false
		// X-HCP-Owner=admin
		// X-HCP-Domain=
		// X-HCP-UID=
		// X-HCP-GID=
		// X-HCP-CustomMetadataAnnotations=myannotation3;6251, myannotation;6251
		// X-HCP-Replicated=false
		// X-HCP-ReplicationCollision=false
		// X-HCP-ChangeTimeMilliseconds=1524638069447.00
		// X-HCP-ChangeTimeString=2018-04-25T14:34:29+0800
		// Last-Modified=Wed, 25 Apr 2018 06:34:29 GMT

		String etag = HCPHeaderKey.ETAG.parse(response);
		// String cache-Control
		// String Pragma
		// String Expires
		ObjectType objectType = HCPHeaderKey.X_HCP_TYPE.parse(response);
		Long size = HCPHeaderKey.X_HCP_SIZE.parse(response);
		String[] contentHash = HCPHeaderKey.X_HCP_HASH.parse(response);
		String versionId = HCPHeaderKey.X_HCP_VERSION_ID.parse(response);
		Long ingestTimeMilliseconds = HCPHeaderKey.X_HCP_INGEST_TIME_MILLISECONDS.parse(response);
		if (ingestTimeMilliseconds == null) {
			ingestTimeMilliseconds = HCPHeaderKey.X_HCP_INGEST_TIME.parse(response);
		}
		String retentionClass = HCPHeaderKey.X_HCP_RETENTION_CLASS.parse(response);
		String retentionString = HCPHeaderKey.X_HCP_RETENTION_STRING.parse(response);
		String retention = HCPHeaderKey.X_HCP_RETENTION.parse(response);
		String ingestProtocol = HCPHeaderKey.X_HCP_INGEST_PROTOCOL.parse(response);
		Boolean retentionHold = HCPHeaderKey.X_HCP_RETENTION_HOLD.parse(response);
		Boolean shred = HCPHeaderKey.X_HCP_SHRED.parse(response);
		Integer dpl = HCPHeaderKey.X_HCP_DPL.parse(response);
		Boolean index = HCPHeaderKey.X_HCP_INDEX.parse(response);
		Boolean hasCustomMetadata = HCPHeaderKey.X_HCP_CUSTOM_METADATA.parse(response);
		Boolean acl = HCPHeaderKey.X_HCP_ACL.parse(response);
		String owner = HCPHeaderKey.X_HCP_OWNER.parse(response);
		String domain = HCPHeaderKey.X_HCP_DOMAIN.parse(response);
		String uid = HCPHeaderKey.X_HCP_UID.parse(response);
		String gid = HCPHeaderKey.X_HCP_GID.parse(response);
		Annotation[] customMetadataAnnotations = HCPHeaderKey.X_HCP_CUSTOM_METADATA_ANNOTATIONS.parse(response);
		Boolean replicated = HCPHeaderKey.X_HCP_REPLICATED.parse(response);
		Boolean replicationCollision = HCPHeaderKey.X_HCP_REPLICATION_COLLISION.parse(response);
		Long changeTimeMilliseconds = HCPHeaderKey.X_HCP_CHANGE_TIME_MILLISECONDS.parse(response);
		// String ChangeTimeString

		hcpObj.setETag(etag);
		// cache-Control
		// Pragma
		// Expires
		hcpObj.setType(objectType);
		hcpObj.setSize(size);
		hcpObj.setHashAlgorithmName(contentHash != null ? contentHash[0] : null);
		hcpObj.setContentHash(contentHash != null ? contentHash[1] : null);
		hcpObj.setVersionId(versionId);
		// ingestTime
		hcpObj.setIngestTime(ingestTimeMilliseconds);
		hcpObj.setRetentionClass(retentionClass);
		hcpObj.setRetentionString(retentionString);
		hcpObj.setRetention(retention);
		hcpObj.setIngestProtocol(ingestProtocol);
		hcpObj.setHold(retentionHold);
		hcpObj.setShred(shred);
		hcpObj.setDpl(dpl);
		hcpObj.setIndexed(index);
		hcpObj.setHasMetadata(hasCustomMetadata);
		hcpObj.setHasAcl(acl);
		hcpObj.setOwner(owner);
		hcpObj.setDomain(domain);
		hcpObj.setPosixUserID(uid);
		hcpObj.setPosixGroupIdentifier(gid);
		hcpObj.setCustomMetadatas(customMetadataAnnotations);
		hcpObj.setReplicated(replicated);
		hcpObj.setReplicationCollision(replicationCollision);
		hcpObj.setChangeTime(changeTimeMilliseconds);
		// ChangeTimeString

		return hcpObj;
	}

	public static void injectCommonProperties(HCPStandardResult result, HttpResponse response) {
		String date = HCPHeaderKey.DATE.parse(response);
		String server = HCPHeaderKey.SERVER.parse(response);
		String requestId = HCPHeaderKey.X_REQUEST_ID.parse(response);
		String servicedBySystem = HCPHeaderKey.X_HCP_SERVICED_BY_SYSTEM.parse(response);
		Long hcpTime = HCPHeaderKey.X_HCP_TIME.parse(response);
		String hcpVersion = HCPHeaderKey.X_HCP_SOFTWARE_VERSION.parse(response);
		String contentType = HCPHeaderKey.CONTENT_TYPE.parse(response);
		Long contentLength = HCPHeaderKey.X_HCP_CONTENT_LENGTH.parse(response);
		if (contentLength == null) {
			contentLength = HCPHeaderKey.CONTENT_LENGTH.parse(response);
		}

		result.setDate(date);
		result.setServer(server);
		result.setRequestId(requestId);
		result.setServicedBySystem(servicedBySystem);
		result.setHcpTime(hcpTime);
		result.setHcpVersion(hcpVersion);
		result.setContentType(contentType);
		result.setContentLength(contentLength);
	}

	public static S3CompatibleMetadata toHCPKeyValueMetadata(HCPMetadata metadata) throws InvalidResponseException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			DefaultMetadataHandler handler = new DefaultMetadataHandler();
			SAXParser parser = factory.newSAXParser();
			parser.parse(metadata.getContent(), handler);
			S3CompatibleMetadata kvm = handler.getMetadata();
			handler = null;

			kvm.setDate(metadata.getDate());
			kvm.setServer(metadata.getServer());
			kvm.setRequestId(metadata.getRequestId());
			kvm.setServicedBySystem(metadata.getServicedBySystem());
			kvm.setHcpTime(metadata.getHcpTime());
			kvm.setHcpVersion(metadata.getHcpVersion());
			kvm.setContentType(metadata.getContentType());
			kvm.setContentLength(metadata.getContentLength());
			kvm.setResponseCode(metadata.getResponseCode());

			kvm.setETag(metadata.getETag());
			kvm.setHashAlgorithmName(metadata.getHashAlgorithmName());
			kvm.setContentHash(metadata.getContentHash());
			kvm.setSize(metadata.getSize());
			kvm.setChangeTime(metadata.getChangeTime());

			return kvm;
		} catch (Exception e) {
			throw new InvalidResponseException(e);
		}
	}
	
	public static HCPObjectInputStream getCloseHandleableContent(final HttpResponse response) throws InvalidResponseException {
		HCPObjectInputStream content;
		try {
			content = new HCPObjectInputStream(response.getEntity().getContent(), new IOCloseListener() {

				@Override
				public void closed() throws IOException  {
					response.close();
				}
			});

			return content;
		} catch (Exception e) {

			close(response);

			throw new InvalidResponseException(e);
		}
	}
	
	private static Element getRootElement(HttpResponse response) throws UnsupportedOperationException, IOException, DocumentException {
		InputStream in = response.getEntity().getContent();
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(in);
		return doc.getRootElement();
	}
	
	public static void close(HttpResponse response) throws InvalidResponseException {
		try {
			EntityUtils.consume(response.getEntity());
			response.close();
		} catch (IOException e) {
			throw new InvalidResponseException(e);
		}
	}

}
