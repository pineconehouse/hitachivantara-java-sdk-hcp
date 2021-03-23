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
package com.hitachivantara.hcp.management.model.converter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.kit.value.SimpleConfiguration;
import com.amituofo.common.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.hcp.common.define.HCPResponseKey;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.management.define.AclsUsage;
import com.hitachivantara.hcp.management.define.AuthenticationType;
import com.hitachivantara.hcp.management.define.CaseForcing;
import com.hitachivantara.hcp.management.define.EmailFormat;
import com.hitachivantara.hcp.management.define.HashScheme;
import com.hitachivantara.hcp.management.define.OptimizedFor;
import com.hitachivantara.hcp.management.define.OwnerType;
import com.hitachivantara.hcp.management.define.Permission;
import com.hitachivantara.hcp.management.define.QuotaUnit;
import com.hitachivantara.hcp.management.define.Role;
import com.hitachivantara.hcp.management.model.CORSSettings;
import com.hitachivantara.hcp.management.model.CifsProtocolSettings;
import com.hitachivantara.hcp.management.model.ContentStatistics;
import com.hitachivantara.hcp.management.model.DataAccessPermissions;
import com.hitachivantara.hcp.management.model.HttpProtocolSettings;
import com.hitachivantara.hcp.management.model.IPSettings;
import com.hitachivantara.hcp.management.model.NamespaceSettings;
import com.hitachivantara.hcp.management.model.NfsProtocolSettings;
import com.hitachivantara.hcp.management.model.SmtpProtocolSettings;
import com.hitachivantara.hcp.management.model.TenantSettings;
import com.hitachivantara.hcp.management.model.UserAccount;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.model.NamespacePermissions;
import com.hitachivantara.hcp.standard.model.converter.HCPRestResponseHandler;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;

public class MapiResponseHandler {
	// private static Element getRootElement(HttpResponse response) throws UnsupportedOperationException, IOException, DocumentException {
	// InputStream in = response.getEntity().getContent();
	// StreamUtils.inputStreamToConsole(in, false);
	// SAXReader saxReader = new SAXReader();
	// Document doc = saxReader.read(in);
	// return doc.getRootElement();
	// }

	private static SimpleConfiguration readResponse(HttpResponse response) throws InvalidResponseException {
		// String resultInXML = "";
		// try {
		// resultInXML = StreamUtils.inputStreamToString(response.getEntity().getContent(), true);
		// } catch (UnsupportedOperationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// System.out.println(resultInXML);

		InputStream in = null;
		Map<String, Object> qresult = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			in = response.getEntity().getContent();

			qresult = mapper.readValue(in, new TypeReference<HashMap<String, Object>>() {
			});
			// System.out.println(qresult);

			return new SimpleConfiguration(qresult);
		} catch (Exception e) {
			throw new InvalidResponseException("Error occurred when parsing response.", e);
		} finally {
			HCPRestResponseHandler.close(response);
		}
	}

	private static IPSettings parseIpSettings(SimpleConfiguration config) {
		// "ipSettings" : {
		// "allowIfInBothLists" : false,
		// "allowAddresses" : {
		// "ipAddress" : [ "0.0.0.0/0", "::0/0" ]
		// },
		// "denyAddresses" : {
		// "ipAddress" : [ ]
		// }
		// }
		SimpleConfiguration map = config.getConfig("ipSettings");
		Boolean allowIfInBothLists = map.getBoolean("allowIfInBothLists");
		List<String> allowAddresses = map.getConfig("allowAddresses").getStringList("ipAddress");
		List<String> denyAddresses = map.getConfig("denyAddresses").getStringList("ipAddress");

		return new IPSettings(allowAddresses, denyAddresses, allowIfInBothLists);
	}

	public final static ResponseHandler<Boolean> DEFAULT_OK_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		@Override
		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			HCPRestResponseHandler.close(response);
			return null;
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};
	

	public static final ResponseHandler<Boolean> DEFAULT_CHECK_EXIST_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		@Override
		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			boolean ok = ValidUtils.isResponse(response, HCPResponseKey.OK);
			
			HCPRestResponseHandler.close(response);
			
			return ok;
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.FOUND);
		}

	};

	public static final ResponseHandler<Boolean> DEFAULT_DELETE_RESPONSE_HANDLER = new ResponseHandler<Boolean>() {

		@Override
		public Boolean handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			boolean ok = ValidUtils.isResponse(response, HCPResponseKey.OK);
			
			HCPRestResponseHandler.close(response);
			
			return ok;
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NOT_FOUND);
		}

	};

	
	public final static ResponseHandler<ContentStatistics> GET_NAMESPACE_STATISTICS_RESPONSE_HANDLER = new ResponseHandler<ContentStatistics>() {
		// {
		// "objectCount" : 508664,
		// "shredCount" : 0,
		// "storageCapacityUsed" : 16809959424,
		// "shredSize" : 0,
		// "ingestedVolume" : 14748020205,
		// "customMetadataCount" : 1877,
		// "customMetadataSize" : 1498482
		// }
		//
		// <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		// <statistics>
		// <customMetadataCount>1877</customMetadataCount>
		// <customMetadataSize>1498482</customMetadataSize>
		// <ingestedVolume>14748020205</ingestedVolume>
		// <objectCount>508664</objectCount>
		// <shredCount>0</shredCount>
		// <shredSize>0</shredSize>
		// <storageCapacityUsed>16809959424</storageCapacityUsed>
		// </statistics>
		@Override
		public ContentStatistics handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {

				SimpleConfiguration config = readResponse(response);
				
				Long objectCount = config.getLong("objectCount");
				Long shredCount = config.getLong("shredCount");
				Long storageCapacityUsed = config.getLong("storageCapacityUsed");
				Long shredSize = config.getLong("shredSize");
				Long ingestedVolume = config.getLong("ingestedVolume");
				Long customMetadataCount = config.getLong("customMetadataCount");
				Long customMetadataSize = config.getLong("customMetadataSize");

				Long compressedCount = config.getLong("compressedCount");
				Long compressedSavedSize = config.getLong("compressedSavedSize");
				Long erasureCodedCount = config.getLong("erasureCodedCount");
				Long erasureCodedSize = config.getLong("erasureCodedSize");

				ContentStatistics ns = new ContentStatistics(
						objectCount,
						shredCount,
						storageCapacityUsed,
						shredSize,
						ingestedVolume,
						customMetadataCount,
						customMetadataSize,
						compressedCount,
						compressedSavedSize,
						erasureCodedCount,
						erasureCodedSize);

				return ns;
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<NamespacePermissions> GET_NAMESPACE_PERMISSIONS_RESPONSE_HANDLER = new ResponseHandler<NamespacePermissions>() {

		@Override
		public NamespacePermissions handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
				// Element root = getRootElement(response);

				// Element namespacePermissionsNode = root.element("namespacePermissions");
				// NamespacePermission namespacePermissions = extractPermission(namespacePermissionsNode);
				//
				// Element namespaceEffectivePermissionsNode = root.element("namespaceEffectivePermissions");
				// NamespacePermission namespaceEffectivePermissions = extractPermission(namespaceEffectivePermissionsNode);
				//
				// Element userPermissionsNode = root.element("userPermissions");
				// NamespacePermission userPermissions = extractPermission(userPermissionsNode);
				//
				// Element userEffectivePermissionsNode = root.element("userEffectivePermissions");
				// NamespacePermission userEffectivePermissions = extractPermission(userEffectivePermissionsNode);
				//
				// NamespacePermissions results = new NamespacePermissions(namespacePermissions, namespaceEffectivePermissions, userPermissions,
				// userEffectivePermissions);

				return null;
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			} finally {
				HCPRestResponseHandler.close(response);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};
	
	public final static ResponseHandler<CORSSettings> GET_NAMESPACE_CORS_SETTING_RESPONSE_HANDLER = new ResponseHandler<CORSSettings>() {

		@Override
		public CORSSettings handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			boolean notfound = ValidUtils.isResponse(response, HCPResponseKey.NOT_FOUND);
			if(notfound) {
				return new CORSSettings("");
			}

			try {
				SimpleConfiguration config = readResponse(response);
				String cors = config.getString("cors");

				return new CORSSettings(cors);
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			} finally {
				HCPRestResponseHandler.close(response);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isAnyResponse(response, HCPResponseKey.OK, HCPResponseKey.NOT_FOUND);
		}

	};
	public static final ResponseHandler<Boolean> CHANGE_NAMESPACE_CORS_SETTING_RESPONSE_HANDLER = DEFAULT_OK_RESPONSE_HANDLER;

	public static final ResponseHandler<Boolean> DELETE_NAMESPACE_CORS_SETTING_RESPONSE_HANDLER = DEFAULT_DELETE_RESPONSE_HANDLER;

	public final static ResponseHandler<Boolean> CREATE_NAMESPACE_RESPONSE_HANDLER = DEFAULT_OK_RESPONSE_HANDLER;

	public final static ResponseHandler<Boolean> CHANGE_NAMESPACE_RESPONSE_HANDLER = DEFAULT_OK_RESPONSE_HANDLER;
	
	public final static ResponseHandler<Boolean> CHANGE_NAMESPACE_PROTOCOL_RESPONSE_HANDLER = DEFAULT_OK_RESPONSE_HANDLER;

	public final static ResponseHandler<HttpProtocolSettings> GET_NAMESPACE_HTTP_PROTOCOLS_RESPONSE_HANDLER = new ResponseHandler<HttpProtocolSettings>() {
		// {
		// "ipSettings" : {
		// "allowIfInBothLists" : false,
		// "allowAddresses" : {
		// "ipAddress" : [ "0.0.0.0/0", "::0/0" ]
		// },
		// "denyAddresses" : {
		// "ipAddress" : [ ]
		// }
		// },
		// "httpEnabled" : true,
		// "httpsEnabled" : true
		// }{
		// "ipSettings" : {
		// "allowIfInBothLists" : false,
		// "allowAddresses" : {
		// "ipAddress" : [ "0.0.0.0/0", "::0/0" ]
		// },
		// "denyAddresses" : {
		// "ipAddress" : [ ]
		// }
		// },
		// "httpEnabled" : true,
		// "httpsEnabled" : true,
		// "restEnabled" : true,
		// "restRequiresAuthentication" : true,
		// "httpActiveDirectorySSOEnabled" : false,
		// "hs3Enabled" : true,
		// "hs3RequiresAuthentication" : true,
		// "webdavEnabled" : false,
		// "webdavBasicAuthEnabled" : false,
		// "webdavCustomMetadata" : false,
		// "webdavBasicAuthUsername" : "",
		// "webdavBasicAuthPassword" : "",
		// "hswiftEnabled" : false,
		// "hswiftRequiresAuthentication" : true
		// }

		@Override
		public HttpProtocolSettings handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
				SimpleConfiguration config = readResponse(response);

				Boolean httpEnabled = config.getBoolean("httpEnabled");
				Boolean httpsEnabled = config.getBoolean("httpsEnabled");
				Boolean restEnabled = config.getBoolean("restEnabled");
				Boolean restRequiresAuthentication = config.getBoolean("restRequiresAuthentication");
				Boolean httpActiveDirectorySSOEnabled = config.getBoolean("httpActiveDirectorySSOEnabled");
				Boolean hs3Enabled = config.getBoolean("hs3Enabled");
				Boolean hs3RequiresAuthentication = config.getBoolean("hs3RequiresAuthentication");
				Boolean webdavEnabled = config.getBoolean("webdavEnabled");
				Boolean webdavBasicAuthEnabled = config.getBoolean("webdavBasicAuthEnabled");
				Boolean webdavCustomMetadata = config.getBoolean("webdavCustomMetadata");
				String webdavBasicAuthUsername = config.getString("webdavBasicAuthUsername");
				String webdavBasicAuthPassword = config.getString("webdavBasicAuthPassword");
				Boolean hswiftEnabled = config.getBoolean("hswiftEnabled");
				Boolean hswiftRequiresAuthentication = config.getBoolean("hswiftRequiresAuthentication");

				IPSettings ipSettings = parseIpSettings(config);

				return new HttpProtocolSettings(
						httpEnabled,
						httpsEnabled,
						restEnabled,
						restRequiresAuthentication,
						httpActiveDirectorySSOEnabled,
						hs3Enabled,
						hs3RequiresAuthentication,
						webdavEnabled,
						webdavBasicAuthEnabled,
						webdavCustomMetadata,
						webdavBasicAuthUsername,
						webdavBasicAuthPassword,
						hswiftEnabled,
						hswiftRequiresAuthentication,
						ipSettings);
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<CifsProtocolSettings> GET_NAMESPACE_CIFS_PROTOCOLS_RESPONSE_HANDLER = new ResponseHandler<CifsProtocolSettings>() {
		// {
		// "enabled" : false,
		// "caseSensitive" : true,
		// "caseForcing" : "DISABLED",
		// "ipSettings" : {
		// "allowAddresses" : {
		// "ipAddress" : [ "0.0.0.0/0", "::0/0" ]
		// },
		// "denyAddresses" : {
		// "ipAddress" : [ ]
		// }
		// },
		// "requiresAuthentication" : true
		// }

		@Override
		public CifsProtocolSettings handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
				SimpleConfiguration config = readResponse(response);

				Boolean enabled = config.getBoolean("enabled");
				Boolean caseSensitive = config.getBoolean("caseSensitive");
				CaseForcing caseForcing = CaseForcing.valueOf(config.getString("caseForcing"));
				Boolean requiresAuthentication = config.getBoolean("requiresAuthentication");

				IPSettings ipSettings = parseIpSettings(config);

				return new CifsProtocolSettings(enabled, caseSensitive, caseForcing, requiresAuthentication, ipSettings);
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<NfsProtocolSettings> GET_NAMESPACE_NFS_PROTOCOLS_RESPONSE_HANDLER = new ResponseHandler<NfsProtocolSettings>() {
		// {
		// "enabled" : false,
		// "uid" : 0,
		// "gid" : 0,
		// "ipSettings" : {
		// "allowAddresses" : {
		// "ipAddress" : [ "0.0.0.0/0", "::0/0" ]
		// }
		// }
		// }
		@Override
		public NfsProtocolSettings handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
				SimpleConfiguration config = readResponse(response);

				Boolean enabled = config.getBoolean("enabled");
				Integer uid = config.getInteger("uid");
				Integer gid = config.getInteger("gid");

				IPSettings ipSettings = parseIpSettings(config);

				return new NfsProtocolSettings(enabled, uid, gid, ipSettings);
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<SmtpProtocolSettings> GET_NAMESPACE_SMTP_PROTOCOLS_RESPONSE_HANDLER = new ResponseHandler<SmtpProtocolSettings>() {
		// {
		// "enabled" : false,
		// "emailLocation" : "/email/",
		// "emailFormat" : ".eml",
		// "separateAttachments" : false,
		// "ipSettings" : {
		// "allowAddresses" : {
		// "ipAddress" : [ "0.0.0.0/0", "::0/0" ]
		// },
		// "denyAddresses" : {
		// "ipAddress" : [ ]
		// }

		@Override
		public SmtpProtocolSettings handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
				SimpleConfiguration config = readResponse(response);

				Boolean enabled = config.getBoolean("enabled");
				String emailLocation = config.getString("emailLocation");
				EmailFormat emailFormat = EmailFormat.valueOf(config.getString("emailFormat").substring(1).toLowerCase());
				Boolean separateAttachments = config.getBoolean("separateAttachments");

				IPSettings ipSettings = parseIpSettings(config);

				return new SmtpProtocolSettings(enabled, emailLocation, emailFormat, separateAttachments, ipSettings);
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<NamespaceSettings> GET_NAMESPACE_SETTING_RESPONSE_HANDLER = new ResponseHandler<NamespaceSettings>() {
		// {
		// "name": "songsong1",
		// "fullyQualifiedName": "songsong1.tenant1.hcp-demo.hcpdemo.com",
		// "description": "DDD",
		// "hardQuota": "11.20 GB",
		// "softQuota": 66,
		// "hashScheme": "SHA-512",
		// "indexingDefault": true,
		// "indexingEnabled": true,
		// "searchEnabled": true,
		// "enterpriseMode": true,
		// "serviceRemoteSystemRequests": true,
		// "isDplDynamic": true,
		// "customMetadataValidationEnabled": true,
		// "tags": {
		// "tag": [
		//
		// ]
		// },
		// "appendEnabled": false,
		// "atimeSynchronizationEnabled": false,
		// "allowPermissionAndOwnershipChanges": false,
		// "servicePlan": "Default",
		// "customMetadataIndexingEnabled": true,
		// "mqeIndexingTimestamp": "2019-01-25T09:19:08-0500",
		// "aclsUsage": "ENFORCED",
		// "owner": "user1",
		// "ownerType": "LOCAL",
		// "optimizedFor": "CLOUD",
		// "multipartUploadAutoAbortDays": 6,
		// "creationTime": "2019-01-25T09:19:08-0500",
		// "dpl": "Dynamic",
		// "id": "535116b8-daa9-4c46-8775-6edd9e31c130",
		// "authUsersAlwaysGrantedAllPermissions": true,
		// "authMinimumPermissions": {
		// "permission": [
		//
		// ]
		// },
		// "authAndAnonymousMinimumPermissions": {
		// "permission": [
		//
		// ]
		// }
		// }

		private double getHardQuota(String hardQuota) {
			int s = hardQuota.indexOf(' ');
			return Double.parseDouble(hardQuota.substring(0, s));
		}

		private QuotaUnit getHardQuotaUnit(String hardQuota) {
			int s = hardQuota.indexOf(' ');
			return QuotaUnit.valueOf(hardQuota.substring(s + 1));
		}

		@Override
		public NamespaceSettings handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
				String value = null;
				// StreamUtils.inputStreamToConsole(response.getEntity().getContent(), true);

				SimpleConfiguration config = readResponse(response);

				String id = config.getString("id");
				String creationTime = config.getString("creationTime");
				String fullyQualifiedName = config.getString("fullyQualifiedName");
				String mqeIndexingTimestamp = config.getString("mqeIndexingTimestamp");
				String replicationTimestamp = config.getString("replicationTimestamp");

				String name = config.getString("name");
				String description = config.getString("description");
				String TP1 = config.getString("hardQuota");
				double hardQuota = getHardQuota(TP1);
				QuotaUnit hardQuotaUnit = getHardQuotaUnit(TP1);
				Integer softQuota = config.getInteger("softQuota");
				HashScheme hashScheme = HashScheme.valueOfHashScheme(config.getString("hashScheme"));
				Boolean indexingDefault = config.getBoolean("indexingDefault");
				Boolean indexingEnabled = config.getBoolean("indexingEnabled");
				Boolean searchEnabled = config.getBoolean("searchEnabled");
				Boolean enterpriseMode = config.getBoolean("enterpriseMode");
				Boolean serviceRemoteSystemRequests = config.getBoolean("serviceRemoteSystemRequests");
				Boolean customMetadataValidationEnabled = config.getBoolean("customMetadataValidationEnabled");
				List<String> tags = config.getConfig("tags").getStringList("tag");
				Boolean appendEnabled = config.getBoolean("appendEnabled");
				Boolean atimeSynchronizationEnabled = config.getBoolean("atimeSynchronizationEnabled");
				Boolean allowPermissionAndOwnershipChanges = config.getBoolean("allowPermissionAndOwnershipChanges");
				String servicePlan = config.getString("servicePlan");
				Boolean customMetadataIndexingEnabled = config.getBoolean("customMetadataIndexingEnabled");
				value = config.getString("aclsUsage");
				AclsUsage aclsUsage = null;
				if(StringUtils.isNotEmpty(value)) {
					aclsUsage = AclsUsage.valueOf(value);
				}
				String owner = config.getString("owner");
				value = config.getString("ownerType");
				OwnerType ownerType = null;
				if(StringUtils.isNotEmpty(value)) {
					ownerType = OwnerType.valueOf(value);
				}
				value = config.getString("optimizedFor");
				OptimizedFor optimizedFor = null;
				if(StringUtils.isNotEmpty(value)) {
					optimizedFor = OptimizedFor.valueOf(value);
				}
				
				Integer multipartUploadAutoAbortDays = config.getInteger("multipartUploadAutoAbortDays");
				String dpl = config.getString("dpl");
				// Deprecated. Namespace DPL is now a service plan function.
				// Boolean isDplDynamic = config.getBoolean("isDplDynamic");

				Boolean authUsersAlwaysGrantedAllPermissions = config.getBoolean("authUsersAlwaysGrantedAllPermissions");

				List<String> authMinimumPermissionsStrings = config.getConfig("authMinimumPermissions").getStringList("permission");
				List<Permission> authMinimumPermissions = new ArrayList<Permission>();
				if (authMinimumPermissionsStrings != null && authMinimumPermissionsStrings.size() > 0) {
					for (String permission : authMinimumPermissionsStrings) {
						authMinimumPermissions.add(Permission.valueOf(permission));
					}
				}
				List<String> authAndAnonymousMinimumPermissionsStrings = config.getConfig("authAndAnonymousMinimumPermissions").getStringList("permission");
				List<Permission> authAndAnonymousMinimumPermissions = new ArrayList<Permission>();
				if (authAndAnonymousMinimumPermissionsStrings != null && authAndAnonymousMinimumPermissionsStrings.size() > 0) {
					for (String permission : authAndAnonymousMinimumPermissionsStrings) {
						authAndAnonymousMinimumPermissions.add(Permission.valueOf(permission));
					}
				}

				NamespaceSettings ns = new NamespaceSettings(
						id,
						creationTime,
						fullyQualifiedName,
						mqeIndexingTimestamp,
						replicationTimestamp,
						name,
						description,
						hardQuota,
						hardQuotaUnit,
						softQuota,
						hashScheme,
						indexingDefault,
						indexingEnabled,
						searchEnabled,
						enterpriseMode,
						serviceRemoteSystemRequests,
						customMetadataValidationEnabled,
						tags,
						appendEnabled,
						atimeSynchronizationEnabled,
						allowPermissionAndOwnershipChanges,
						servicePlan,
						customMetadataIndexingEnabled,
						aclsUsage,
						owner,
						ownerType,
						optimizedFor,
						multipartUploadAutoAbortDays,
						dpl,
						authUsersAlwaysGrantedAllPermissions,
						authMinimumPermissions,
						authAndAnonymousMinimumPermissions);

				return ns;
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public static final ResponseHandler<Boolean> CHECK_NAMESPACE_RESPONSE_HANDLER = DEFAULT_CHECK_EXIST_RESPONSE_HANDLER;

	public static final ResponseHandler<Boolean> DELETE_NAMESPACE_RESPONSE_HANDLER = DEFAULT_DELETE_RESPONSE_HANDLER;

	public static final ResponseHandler<DataAccessPermissions> GET_DATA_ACCESS_PERMISSIONS_RESPONSE_HANDLER = new ResponseHandler<DataAccessPermissions>() {
		// {
		// "namespacePermission" : [ {
		// "namespaceName" : "songsong1-new",
		// "permissions" : {
		// "permission" : [ "READ", "BROWSE", "READ_ACL", "WRITE_ACL", "DELETE", "SEARCH", "WRITE" ]
		// }
		// }, {
		// "namespaceName" : "test1",
		// "permissions" : {
		// "permission" : [ "READ", "BROWSE", "READ_ACL", "WRITE_ACL", "DELETE", "CHOWN", "PURGE", "SEARCH", "PRIVILEGED", "WRITE" ]
		// }
		// } ]
		// }
		@Override
		public DataAccessPermissions handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			// try {
			// StreamUtils.inputStreamToConsole(response.getEntity().getContent(), true);
			// } catch (UnsupportedOperationException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			DataAccessPermissions ps = new DataAccessPermissions();

			SimpleConfiguration config1 = readResponse(response);
			List<SimpleConfiguration> configs = config1.getConfigs("namespacePermission");
			for (SimpleConfiguration config : configs) {
				String namespaceName = config.getString("namespaceName");
				List<Permission> permissions = new ArrayList<Permission>();

				List<String> permissionsInStrings = config.getConfig("permissions").getStringList("permission");
				if (permissionsInStrings != null && permissionsInStrings.size() > 0) {
					for (String permissionInStr : permissionsInStrings) {
						permissions.add(Permission.valueOf(permissionInStr));
					}
				}

				ps.setPermissions(namespaceName, permissions);
			}

			return ps;
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};

	public final static ResponseHandler<Boolean> CHANGE_DATA_ACCESS_PERMISSIONS_RESPONSE_HANDLER = DEFAULT_OK_RESPONSE_HANDLER;

	public static final ResponseHandler<String[]> GET_USER_ACCOUNTS_RESPONSE_HANDLER = new ResponseHandler<String[]>() {
//		{"username":["admin","user1"]}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		@Override
		public String[] handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			SimpleConfiguration config = readResponse(response);
			
			List<String> users = config.getStringList("username");
			
			return users.toArray(new String[users.size()]);
		}
	};

	public static final ResponseHandler<UserAccount> GET_USER_ACCOUNT_INFO_RESPONSE_HANDLER = new ResponseHandler<UserAccount>() {
//		{
//			  "roles": {
//			    "role": [
//			      "ADMINISTRATOR",
//			      "MONITOR",
//			      "SECURITY",
//			      "COMPLIANCE"
//			    ]
//			  },
//			  "fullName": "admin",
//			  "description": "Initial Tenant account",
//			  "userID": 500,
//			  "allowNamespaceManagement": true,
//			  "forcePasswordChange": false,
//			  "localAuthentication": true,
//			  "enabled": true,
//			  "username": "admin",
//			  "userGUID": "f1ee9349-ac2d-45d5-b76a-1ca494427c9c"
//			}
		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		@Override
		public UserAccount handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			SimpleConfiguration config = readResponse(response);
			
			String fullName = config.getString("fullName");
			String description = config.getString("description");
			Integer userID = config.getInteger("userID");
			Boolean allowNamespaceManagement = config.getBoolean("allowNamespaceManagement");
			Boolean forcePasswordChange = config.getBoolean("forcePasswordChange");
			Boolean localAuthentication = config.getBoolean("localAuthentication");
			Boolean enabled = config.getBoolean("enabled");
			String username = config.getString("username");
			String userGUID = config.getString("userGUID");
			List<String> rolesInStrs = config.getConfig("roles").getStringList("role");
			Role[] roles = new Role[rolesInStrs.size()];
			for (int i = 0; i < roles.length; i++) {
				roles[i] = Role.valueOf(rolesInStrs.get(i));
			}
			
			UserAccount ua = new UserAccount( fullName,  description, userID,  allowNamespaceManagement,  forcePasswordChange, localAuthentication, enabled,  username, userGUID, roles) ;

			return ua;
		}
	};

	public static final ResponseHandler<Boolean> CREATE_USER_ACCOUNT_RESPONSE_HANDLER = DEFAULT_OK_RESPONSE_HANDLER;

	public static final ResponseHandler<Boolean> DELETE_USER_ACCOUNT_RESPONSE_HANDLER = DEFAULT_DELETE_RESPONSE_HANDLER;

	public static final ResponseHandler<Boolean> CHECK_USER_ACCOUNT_RESPONSE_HANDLER = DEFAULT_CHECK_EXIST_RESPONSE_HANDLER;

	public static final ResponseHandler<String[]> LIST_NAMESPACES_RESPONSE_HANDLER = new ResponseHandler<String[]>() {

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

		@Override
		public String[] handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
//			try {
//				StreamUtils.inputStreamToConsole(response.getEntity().getContent(), true);
//			} catch (Exception e) {
//			}

			SimpleConfiguration config = readResponse(response);
			
			List<String> namespaces = config.getStringList("name");
			
			return namespaces.toArray(new String[namespaces.size()]);
		}
	};

	public static final ResponseHandler<Boolean> CHANGE_PASSWORD_RESPONSE_HANDLER = DEFAULT_OK_RESPONSE_HANDLER;

	public static final ResponseHandler<Boolean> CHANGE_USER_ACCOUNT_RESPONSE_HANDLER = DEFAULT_OK_RESPONSE_HANDLER;

	public static final ResponseHandler<String[]> LIST_TENANTS_RESPONSE_HANDLER = LIST_NAMESPACES_RESPONSE_HANDLER;

	public static final ResponseHandler<TenantSettings> GET_TENANT_SETTING_RESPONSE_HANDLER  = new ResponseHandler<TenantSettings>() {
//		{
//			  "name": "tn9",
//			  "systemVisibleDescription": "",
//			  "softQuota": 85,
//			  "hardQuota": "400.00 GB",
//			  "namespaceQuota": "None",
//			  "authenticationTypes": {
//			    "authenticationType": [
//			      "LOCAL"
//			    ]
//			  },
//			  "tags": {
//			    "tag": [
//			      
//			    ]
//			  },
//			  "managementNetwork": "[hcp_system]",
//			  "dataNetwork": "[hcp_system]",
//			  "fullyQualifiedName": "tn9.hcp8.hdim.lab",
//			  "searchConfigurationEnabled": true,
//			  "replicationConfigurationEnabled": false,
//			  "complianceConfigurationEnabled": true,
//			  "versioningConfigurationEnabled": true,
//			  "servicePlanSelectionEnabled": true,
//			  "creationTime": "2018-02-06T15:37:29+0800",
//			  "id": "4f0e935f-feba-4910-98eb-c52872a66938",
//			  "erasureCodingSelectionEnabled": false
//			}
		private double getHardQuota(String hardQuota) {
			if (hardQuota == null) {
				return 0;
			}
			int s = hardQuota.indexOf(' ');
			if (s > 0) {
				return Double.parseDouble(hardQuota.substring(0, s));
			}
			return -1;
		}

		private QuotaUnit getHardQuotaUnit(String hardQuota) {
			if (hardQuota == null) {
				return QuotaUnit.GB;
			}
			int s = hardQuota.indexOf(' ');
			if (s > 0) {
				return QuotaUnit.valueOf(hardQuota.substring(s + 1));
			}
			return QuotaUnit.GB;
		}

		@Override
		public TenantSettings handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			try {
//				 StreamUtils.inputStreamToConsole(response.getEntity().getContent(), true);
//				{
//					  "name": "Default",
//					  "systemVisibleDescription": "",
//					  "tenantVisibleDescription": "",
//					  "id": "00000000-0000-0000-0000-000000000000",
//					  "managementNetwork": "[hcp_system]",
//					  "dataNetwork": "[hcp_system]",
//					  "syslogLoggingEnabled": false,
//					  "fullyQualifiedName": "Default.hcp-demo.hcpdemo.com",
//					  "creationTime": "2020-01-17T11:46:31+0800",
//					  "snmpLoggingEnabled": false
//				}
//				{
//					  "name": "tenant1",
//					  "systemVisibleDescription": "",
//					  "softQuota": 85,
//					  "hardQuota": "100.00 GB",
//					  "namespaceQuota": "None",
//					  "servicePlan": "Default",
//					  "id": "49f9a563-538e-4e41-bda0-8126180029e2",
//					  "authenticationTypes": {
//					    "authenticationType": [
//					      "LOCAL"
//					    ]
//					  },
//					  "tags": {
//					    "tag": [
//					      
//					    ]
//					  },
//					  "managementNetwork": "[hcp_system]",
//					  "dataNetwork": "[hcp_system]",
//					  "fullyQualifiedName": "tenant1.hcp-demo.hcpdemo.com",
//					  "searchConfigurationEnabled": true,
//					  "replicationConfigurationEnabled": false,
//					  "complianceConfigurationEnabled": false,
//					  "versioningConfigurationEnabled": true,
//					  "erasureCodingSelectionEnabled": false,
//					  "servicePlanSelectionEnabled": false,
//					  "creationTime": "2019-08-27T01:05:45+0800"
//					}
				SimpleConfiguration config = readResponse(response);

				String id = config.getString("id");
				String creationTime = config.getString("creationTime");
				String fullyQualifiedName = config.getString("fullyQualifiedName");

				String name = config.getString("name");
				String systemVisibleDescription = config.getString("systemVisibleDescription");
				String tenantVisibleDescription = config.getString("tenantVisibleDescription");
				Integer softQuota = config.getInteger("softQuota");
				String TP1 = config.getString("hardQuota");
				double hardQuota = getHardQuota(TP1);
				QuotaUnit hardQuotaUnit = getHardQuotaUnit(TP1);
				String namespaceQuota = config.getString("namespaceQuota");
				
				AuthenticationType[] authenticationTypes;
				SimpleConfiguration authenticationTypesNode = config.getConfig("authenticationTypes");
				if (authenticationTypesNode != null) {
					List<String> authenticationTypesStr = authenticationTypesNode.getStringList("authenticationType");
					authenticationTypes = new AuthenticationType[authenticationTypesStr.size()];
					for (int i = 0; i < authenticationTypes.length; i++) {
						authenticationTypes[i] = AuthenticationType.valueOf(authenticationTypesStr.get(i).toUpperCase());
					}
				} else {
					authenticationTypes = new AuthenticationType[0];
				}
				List<String> tags = null;
				SimpleConfiguration tagsNode = config.getConfig("tags");
				if (tagsNode != null) {
					tags = tagsNode.getStringList("tag");
				}
				String managementNetwork = config.getString("managementNetwork");
				String dataNetwork = config.getString("dataNetwork");
				Boolean searchConfigurationEnabled = config.getBoolean("searchConfigurationEnabled");
				Boolean replicationConfigurationEnabled = config.getBoolean("replicationConfigurationEnabled");
				Boolean complianceConfigurationEnabled = config.getBoolean("complianceConfigurationEnabled");
				Boolean versioningConfigurationEnabled = config.getBoolean("versioningConfigurationEnabled");
				Boolean servicePlanSelectionEnabled = config.getBoolean("servicePlanSelectionEnabled");
				String servicePlan = config.getString("servicePlan");
				Boolean erasureCodingSelectionEnabled = config.getBoolean("erasureCodingSelectionEnabled");

				Boolean administrationAllowed = config.getBoolean("administrationAllowed");
				Boolean snmpLoggingEnabled = config.getBoolean("snmpLoggingEnabled");
				Boolean syslogLoggingEnabled = config.getBoolean("syslogLoggingEnabled");
				Integer maxNamespacesPerUser = config.getInteger("maxNamespacesPerUser");

				TenantSettings ts = new TenantSettings(
						id,
						creationTime,
						fullyQualifiedName,
						name,
						systemVisibleDescription,
						tenantVisibleDescription,
						softQuota,
						hardQuota,
						hardQuotaUnit,
						namespaceQuota,
						authenticationTypes,
						tags,
						managementNetwork,
						dataNetwork,
						searchConfigurationEnabled,
						replicationConfigurationEnabled,
						complianceConfigurationEnabled,
						versioningConfigurationEnabled,
						servicePlanSelectionEnabled,
						servicePlan,
						erasureCodingSelectionEnabled,
						administrationAllowed,
						maxNamespacesPerUser,
						snmpLoggingEnabled,
						syslogLoggingEnabled);

				return ts;
			} catch (Exception e) {
				throw new InvalidResponseException(e);
			}
		}

		@Override
		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}

	};
	
	public static final ResponseHandler<Boolean> CHECK_TENANT_RESPONSE_HANDLER = DEFAULT_CHECK_EXIST_RESPONSE_HANDLER;
	public static final ResponseHandler<Boolean> DELETE_TENANT_RESPONSE_HANDLER = DEFAULT_DELETE_RESPONSE_HANDLER;

}
