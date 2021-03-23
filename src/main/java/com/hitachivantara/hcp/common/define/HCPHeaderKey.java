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
package com.hitachivantara.hcp.common.define;

import com.amituofo.common.api.StringValueBuilder;
import com.amituofo.common.api.StringValueParser;
import com.amituofo.common.ex.AlgorithmException;
import com.amituofo.common.ex.BuildException;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.core.http.define.HeaderKey;
import com.hitachivantara.core.http.model.Header;
import com.hitachivantara.core.http.model.HttpHeader;
import com.hitachivantara.core.http.model.HttpParameter;
import com.hitachivantara.core.http.util.HttpUtils;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.body.HCPNamespaceBase;
import com.hitachivantara.hcp.standard.define.ObjectType;
import com.hitachivantara.hcp.standard.define.RequestParamKey;
import com.hitachivantara.hcp.standard.model.metadata.Annotation;
import com.hitachivantara.hcp.standard.model.request.HCPRequestParams;
import com.hitachivantara.hcp.standard.model.request.impl.CopyObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CopyPartRequest;
import com.hitachivantara.hcp.standard.util.S3SignerUtils;

public final class HCPHeaderKey<T> extends HeaderKey<T> {
	/**
	 * Specifies user credentials or requests anonymous access. <br>
	 * For valid values for this header, see Authentication.
	 **/
	public final static HCPHeaderKey<Credentials> HCP_AUTHORIZATION = new HCPHeaderKey<Credentials>("Authorization", new StringValueBuilder<Credentials>() {
		public String build(Credentials args) {
			Credentials cred = args;
			String authentication = "HCP " + cred.getAccessKey() + ":" + cred.getSecretKey();

			return authentication;
		}
	});
	/**
	 * You use the HTTP Authorization request header to provide the authentication token for a request. The value of this header is AD followed
	 * by the authentication token, in this format: Authorization: AD authentication-token
	 **/
	public final static HCPHeaderKey<Credentials> AD_AUTHORIZATION = new HCPHeaderKey<Credentials>("Authorization", new StringValueBuilder<Credentials>() {
		public String build(Credentials args) {
			Credentials cred = args;
			String key = "AD " + cred.getAccessKey() + ":" + cred.getSecretKey();

			return key;
		}
	});

	public final static HCPHeaderKey<Object[]> AWS_S3SINGER_AUTHORIZATION = new HCPHeaderKey<Object[]>("Authorization", new StringValueBuilder<Object[]>() {
		public String build(Object[] args) {
			HCPNamespaceBase hcpClient = (HCPNamespaceBase) args[0];
			Method method = (Method) args[1];
			String namespace = (String) args[2];
			String key = (String) args[3];
			HttpHeader header = (HttpHeader) args[4];
			HttpParameter param = (HttpParameter) args[5];

			Credentials credentials = hcpClient.getCredentials();

			if (StringUtils.isEmpty(namespace)) {
				namespace = hcpClient.getNamespace();
			}

			key = URLUtils.catPath("/" + namespace, key);
			key = URLUtils.urlEncode(key, Constants.DEFAULT_URL_ENCODE);

			String canonicalString = S3SignerUtils.makeS3CanonicalString(method, key, header, param);
			byte[] signature = null;
			try {
				signature = S3SignerUtils.calcSignature(canonicalString, credentials.getSecretKey());
			} catch (AlgorithmException e) {
				e.printStackTrace();
				return null;
			}

			String authentication = "AWS " + credentials.getAccessKey() + ":" + S3SignerUtils.BASE64_ENCODER.encode(signature);

			return authentication;
		}
	});
	
//	public final static HCPHeaderKey<Object[]> AWS_S4SINGER_AUTHORIZATION = new HCPHeaderKey<Object[]>("Authorization", new StringValueBuilder<Object[]>() {
//		public String build(Object[] args) {
//			HCPNamespaceBase hcpClient = (HCPNamespaceBase) args[0];
//			Method method = (Method) args[1];
//			String namespace = (String) args[2];
//			String key = (String) args[3];
//			HttpHeader header = (HttpHeader) args[4];
//			HttpParameter param = (HttpParameter) args[5];
//
//			Credentials credentials = hcpClient.getCredentials();
//
//			if (StringUtils.isEmpty(namespace)) {
//				namespace = hcpClient.getNamespace();
//			}
//
//			key = URLUtils.catPath("/" + namespace, key);
//			key = URLUtils.urlEncode(key, Constants.DEFAULT_URL_ENCODE);
//
//			String canonicalString = S4SignerUtils.makeCanonicalString(method, key, header, param);
//			byte[] signature = null;
//			try {
//				signature = S4SignerUtils.calcSignature(canonicalString, credentials.getSecretKey());
//			} catch (AlgorithmException e) {
//				e.printStackTrace();
//				return null;
//			}
//
//			String authentication = "AWS " + credentials.getAccessKey() + ":" + S4SignerUtils.BASE64_ENCODER.encode(signature);
//
//			return authentication;
//		}
//	});

	/**
	 * Specifies the date and time at which the request is being made according to the requester. Normally, this is the current date and time.
	 * The date and time must always be specified using Greenwich Mean Time (GMT).<br>
	 * To specify the date and time, use this format:<br>
	 * DDD, dd MMM yyyy HH:mm:ss (+0000|GMT)<br>
	 * In this format:<br>
	 * DDD is the three-letter abbreviation for the day of the week, with an uppercase first letter (for example, Mon).<br>
	 * dd is the two-digit day of the month.<br>
	 * MMM is the three-letter abbreviation for the month, with an uppercase first letter (for example, Feb).<br>
	 * yyyy is the four-digit year.<br>
	 * HH is the hour on a 24-hour clock.<br>
	 * mm is the number of minutes into the hour.<br>
	 * ss is the number of seconds into the minute.<br>
	 * For example:<br>
	 * Thu, 23 Mar 2017 14:27:05 +0000<br>
	 * All HS3 requests must include either a Date header or an x-amz-date header. If a request includes both headers, HCP uses the date and
	 * time in the x-amz-date header.
	 **/
	public final static HCPHeaderKey<String> DATE = new HCPHeaderKey<String>("Date");
	/**
	 * Specifies the hostname for the request. The host name identifies either a tenant or a namespace.<br>
	 * For a tenant, use this format:<br>
	 * tenant-name.hcp-domain-name <br>
	 * For a namespace, use this format:<br>
	 * namespace-name.tenant-name.hcp-domain-name
	 **/
	public final static HCPHeaderKey<String> HOST = new HCPHeaderKey<String>("Host");
	/**
	 * The system that responded to the request. The value of this header is always HCP followed by the HCP version number (for example, HCP
	 * V7.1.0.16)
	 **/
	public final static HCPHeaderKey<String> SERVER = new HCPHeaderKey<String>("Server");
	/****/
	public final static HCPHeaderKey<String> X_AMZ_DATE = new HCPHeaderKey<String>("x-amz-date");
	/****/
	public final static HCPHeaderKey<String> X_TRANS_ID = new HCPHeaderKey<String>("X-Trans-Id");
	/**
	 * Requesting partial object data
	 * 
	 * To retrieve only part of a single object or version data, specify an HTTP Range request header with the range of bytes of the object data
	 * to retrieve. You specify the Range header in addition to other request elements described above. The first byte of the data is in
	 * position 0 (zero), so a range of 1-5 specifies the second through sixth bytes of the object, not the first through fifth.
	 * 
	 * The Range header has this format:
	 * 
	 * Range: bytes=range
	 **/
	public final static HCPHeaderKey<Long[]> RANGE = new HCPHeaderKey<Long[]>("Range", new StringValueBuilder<Long[]>() {
		public String build(Long[] range) {
			if (range != null && range.length == 2) {
				String v = "bytes=";
				if (range[0] >= 0) {
					v += range[0];
				}
				v += "-";
				if (range[1] > 0) {
					v += range[1];
				}

				return v;
			}

			return "";
		}
	});
	/****/
	public final static HCPHeaderKey<String> LOCATION = new HCPHeaderKey<String>("Location", new StringValueParser<String>() {
		public String parse(String value) {
			// Location=/rest/sdk-test/TestHCP4065477335821456352.txt
			// Skip /rest/
			return value.substring(6);
		}
	});
	/** The version ID of the object. **/
	public final static HCPHeaderKey<String> X_HCP_VERSION_ID = new HCPHeaderKey<String>("X-HCP-VersionId");
	/**  **/
	public final static HCPHeaderKey<String> X_REQUEST_ID = new HCPHeaderKey<String>("X-RequestId");
	/****/
	public final static HCPHeaderKey<Long> X_HCP_INGEST_TIME_MILLISECONDS = new HCPHeaderKey<Long>("X-HCP-IngestTimeMilliseconds", StringValueParser.LONG_TYPE_PARSER);
	/****/
	public final static HCPHeaderKey<Long> X_HCP_VERSION_CREATE_TIME_MILLISECONDS = new HCPHeaderKey<Long>("X-HCP-VersionCreateTimeMilliseconds", StringValueParser.LONG_TYPE_PARSER);
	/** The ETag of the object or version data enclosed in double quotation marks ("). **/
	public final static HCPHeaderKey<String> ETAG = new HCPHeaderKey<String>("ETag", new StringValueParser<String>() {
		public String parse(String value) {
			// ETag="1db925c988f7a07d79ad9ea902d6ea99"
			if (value.charAt(0) == '"') {
				return value.substring(1, value.length() - 1);
			}

			return value;
		}
	});

	/** A true or false value indicating whether the object has an ACL. **/
	public final static HCPHeaderKey<Boolean> X_HCP_ACL = new HCPHeaderKey<Boolean>("X-HCP-ACL", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * The change time for the object or annotation, in milliseconds since January 1, 1970, at 00:00:00 UTC, followed by an integer that’s
	 * unique for the change time
	 **/
	public final static HCPHeaderKey<Long> X_HCP_CHANGE_TIME_MILLISECONDS = new HCPHeaderKey<Long>("X-HCP-ChangeTimeMilliseconds", StringValueParser.LONG_TYPE_PARSER);
	/**
	 * The change time for the object or annotation, in this format: yyyy-MM-ddThh:mm:ssZ In this format, Z represents the offset from UTC and
	 * is specified as: (+|-)hhmm
	 **/
	public final static HCPHeaderKey<String> X_HCP_CHANGE_TIME_STRING = new HCPHeaderKey<String>("X-HCP-ChangeTimeString");
	/** The length of the returned data before HCP compressed it. **/
	public final static HCPHeaderKey<Long> X_HCP_CONTENT_LENGTH = new HCPHeaderKey<Long>("X-HCP-ContentLength", StringValueParser.LONG_TYPE_PARSER);
	/**
	 * The IP address of a node on which object data is stored. You may get better performance retrieving an object if you use this IP address
	 * in the GET request for the object instead of using a hostname in the request URL.
	 * 
	 * This header is returned only if both of these are true:
	 * 
	 * HCP is configured to return the header.
	 * 
	 * HCP has determined that a GET request for the object is likely to have better performance if the request is targeted to the IP address
	 * specified by the header.
	 **/
	public final static HCPHeaderKey<String> X_HCP_CURRENT_STORAGENODE = new HCPHeaderKey<String>("X-HCP-CurrentStorageNode");
	/** A true or false value indicating whether the object has any annotations. **/
	public final static HCPHeaderKey<Boolean> X_HCP_CUSTOM_METADATA = new HCPHeaderKey<Boolean>("X-HCP-Custom-Metadata", StringValueParser.BOOLEAN_TYPE_PARSER);
	/** A true or false value indicating whether the object has any annotations. **/
	public final static HCPHeaderKey<String> X_HCP_METADATA_DIRECTIVE = new HCPHeaderKey<String>("X-HCP-MetadataDirective");
	/**
	 * A comma and space-separated list containing the names and sizes of all object annotations. Each entry in the list consists of the
	 * annotation name, a semicolon (;) and the annotation size in bytes, as in report_data;12908.
	 * 
	 * This header is returned only if X-HCP-Custom-Metadata is true.
	 **/
	public final static HCPHeaderKey<Annotation[]> X_HCP_CUSTOM_METADATA_ANNOTATIONS = new HCPHeaderKey<Annotation[]>(
			"X-HCP-CustomMetadataAnnotations",
			new StringValueParser<Annotation[]>() {

				public Annotation[] parse(String value) {
					Annotation[] metadatas = null;
					// X-HCP-CustomMetadataAnnotations=myannotation3;6251, myannotation;6251
					// Default
					if (StringUtils.isNotEmpty(value)) {
						String[] ms = value.split(",");
						metadatas = new Annotation[ms.length];
						for (int i = 0; i < ms.length; i++) {
							int isp = ms[i].indexOf(';');
							if (isp == -1) {
								String name = ms[i];
								metadatas[i] = new Annotation(name, null);
							} else {
								String name = ms[i].substring(0, isp).trim();
								Long size = Long.parseLong(ms[i].substring(isp + 1));
								metadatas[i] = new Annotation(name, size);
							}
						}
					}

					return metadatas;
				}

			});
	/**
	 * The custom metadata type, one of
	 * 
	 * text/xml if HCP checked for well-formed XM L when the annotation was stored
	 * 
	 * unknown otherwise
	 **/
	public final static HCPHeaderKey<String> X_HCP_CUSTOM_METADATA_CONTENT_TYPE = new HCPHeaderKey<String>("X-HCP-CustomMetadataContentType");
	/**
	 * One of:
	 * 
	 * true - The custom metadata precedes the object data.
	 * 
	 * false - The object data precedes the custom metadata.
	 **/
	public final static HCPHeaderKey<String> X_HCP_CUSTOM_METADATA_FIRST = new HCPHeaderKey<String>("X-HCP-CustomMetadataFirst");
	/**
	 * The cryptographic hash algorithm HCP uses and the cryptographic hash value of the stored annotation, in this format:
	 * 
	 * X-HCP-CustomMetadataHash: hash-algorithm hash-value
	 * 
	 * You can use the returned hash value to verify that the stored annotation is the same as the custom metadata you sent. To do this, compare
	 * this value with a hash value that you generate from the original custom metadata.
	 **/
	public final static HCPHeaderKey<String> X_HCP_CUSTOM_METADATA_HASH = new HCPHeaderKey<String>("X-HCP-CustomMetadataHash");
	/**
	 * The Internet media type of the object, such as text/plain or image/jpg.
	 **/
	public final static HCPHeaderKey<String> X_HCP_DATA_CONTENT_TYPE = new HCPHeaderKey<String>("X-HCP-DataContentType");
	/**
	 * The Active Directory domain that contains the user account identified by the X-HCP-Owner header.
	 * 
	 * This value is an empty string if the X-HCP-Owner header identifies a user account defined in HCP or if the object has no owner.
	 * 
	 * If the X-HCP-Owner header returns a user account ID or nobody, the value of the X-HCP-Domain header is one of several invalid domains
	 * that begin with the percent sign (%). These values have meanings internal to the HCP system.
	 **/
	public final static HCPHeaderKey<String> X_HCP_DOMAIN = new HCPHeaderKey<String>("X-HCP-Domain");
	/** The data protection level of the object or version. **/
	public final static HCPHeaderKey<Integer> X_HCP_DPL = new HCPHeaderKey<Integer>("X-HCP-DPL", StringValueParser.INTEGER_TYPE_PARSER);
	/**
	 * Detailed information about the cause of an error.
	 * 
	 * This header is returned only if a request results in a 400, 403, or 503 error code and HCP has specific information about the cause.
	 **/
	public final static HCPHeaderKey<String> X_HCP_ERRORMESSAGE = new HCPHeaderKey<String>("X-HCP-ErrorMessage");
	/**
	 * The POSIX group ID for the object.
	 * 
	 * For objects added through the NFS protocol, this value is determined by the NFS client.
	 * 
	 * This value is an empty string if either of these are true:
	 * 
	 * The object was added through a protocol other than NFS, and neither the UID nor the GID for the object has been changed.
	 * 
	 * The HCP owner of the object was changed.
	 **/
	public final static HCPHeaderKey<String> X_HCP_GID = new HCPHeaderKey<String>("X-HCP-GID");
	/**
	 * The cryptographic hash algorithm the namespace uses, along with a cryptographic hash value of the stored object or annotation:
	 * 
	 * X-HCP-Hash: hash-algorithmhash-value
	 * 
	 * You can use the returned hash value to verify that the stored data is the same as the data you sent. To do this, compare this value with
	 * a hash value that you generate from the original data.
	 * 
	 * The X-HCP-Hash header is not returned for multipart objects.
	 **/
	public final static HCPHeaderKey<String[]> X_HCP_HASH = new HCPHeaderKey<String[]>("X-HCP-Hash", new StringValueParser<String[]>() {
		public String[] parse(String value) {
			// SHA-256 9D4AB436E68C4A85FD680B070F206A4144F08A8898F67F7E30028DB89BFD6EB0
			return value.split(" ");
		}
	});
	/** A true or false value indicating whether the object is marked for indexing. **/
	public final static HCPHeaderKey<Boolean> X_HCP_INDEX = new HCPHeaderKey<Boolean>("X-HCP-Index", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * The namespace access protocol through which the object was added to the namespace. One of:
	 * 
	 * CIFS_NFS HTTP SMTP WebDAV
	 * 
	 * If HCP cannot determine the protocol through which the object was added, this value is UNKNOWN.
	 **/
	public final static HCPHeaderKey<String> X_HCP_INGEST_PROTOCOL = new HCPHeaderKey<String>("X-HCP-IngestProtocol");
	/** The time when HCP stored the object, in seconds since January 1, 1970, at 00:00:00 UTC. **/
	public final static HCPHeaderKey<Long> X_HCP_INGEST_TIME = new HCPHeaderKey<Long>("X-HCP-IngestTime", StringValueParser.LONG_TYPE_PARSER);
	/**
	 * The user that owns the object. This value can be one of:
	 * 
	 * The username of a user account that’s defined in HCP.
	 * 
	 * The username of an Active Directory user account that HCP recognizes. This can be either the user principal name or the Security
	 * Accounts Manager (SAM) account name for the AD user account.
	 * 
	 * If the object has no owner, an empty string.
	 * 
	 * nobody - The object was added by an authenticated user before the HCP system was upgraded from a release earlier than 5.0 to release
	 * 5.x. This object effectively has no owner.
	 * 
	 * If HCP can no longer identify the object owner by username, a user account ID. For example, you would see a user account ID if the owner
	 * has been deleted.
	 **/
	public final static HCPHeaderKey<String> X_HCP_OWNER = new HCPHeaderKey<String>("X-HCP-Owner");
	/**
	 * A true or false value indicating whether the object from the primary system has been successfully replicated to an outbound system. This
	 * value is called on the primary HCP system.
	 * 
	 * The value is true only if the current version of the object, its system metadata, annotations (if any), and ACL (if any), have been
	 * replicated
	 **/
	public final static HCPHeaderKey<Boolean> X_HCP_REPLICATED = new HCPHeaderKey<Boolean>("X-HCP-Replicated", StringValueParser.BOOLEAN_TYPE_PARSER);
	/** A true or false value indicating whether the object is flagged as a replication collision. **/
	public final static HCPHeaderKey<Boolean> X_HCP_REPLICATION_COLLISION = new HCPHeaderKey<Boolean>("X-HCP-ReplicationCollision", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * The end of the retention period for the object, in seconds since January 1, 1970, at 00:00:00 UTC. This value can also be 0, -1, or -2.
	 **/
	public final static HCPHeaderKey<String> X_HCP_RETENTION = new HCPHeaderKey<String>("X-HCP-Retention");
	/**
	 * The name of the retention class to which the object belongs. This value is an empty string if the object is not in a retention class.
	 **/
	public final static HCPHeaderKey<String> X_HCP_RETENTION_CLASS = new HCPHeaderKey<String>("X-HCP-RetentionClass");
	/** A true or false value indicating whether the object is on hold. **/
	public final static HCPHeaderKey<Boolean> X_HCP_RETENTION_HOLD = new HCPHeaderKey<Boolean>("X-HCP-RetentionHold", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * The end of the retention period for the object, in this format:
	 * 
	 * yyyy-MM-ddThh:mm:ssZ
	 * 
	 * In this format, Z represents the offset from UTC and is specified as:
	 * 
	 * (+|-)hhmm
	 * 
	 * For example, 2015-11-16T14:27:20-0500 represents the start of the 20th second into 2:27 PM, November 16, 2015, EST.
	 * 
	 * The value can also be Deletion Allowed, Deletion Prohibited, or Initial Undefined.
	 * 
	 * For more information on the datetime format, see Specifying a date and time.
	 **/
	public final static HCPHeaderKey<String> X_HCP_RETENTION_STRING = new HCPHeaderKey<String>("X-HCP-RetentionString");
	/**
	 * The domain name of the HCP system responding to the request.
	 * 
	 * If the target HCP system is unable to respond to the request and also participates in replication, this value may be another system in
	 * the replication topology.
	 **/
	public final static HCPHeaderKey<String> X_HCP_SERVICED_BY_SYSTEM = new HCPHeaderKey<String>("X-HCP-ServicedBySystem");
	/** A true or false value indicating whether HCP will shred the object after it is deleted. **/
	public final static HCPHeaderKey<Boolean> X_HCP_SHRED = new HCPHeaderKey<Boolean>("X-HCP-Shred", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * The size of the object, version, or annotation, in bytes. For whole-object data, this value is the size of the object data.
	 **/
	public final static HCPHeaderKey<Long> X_HCP_SIZE = new HCPHeaderKey<Long>("X-HCP-Size", StringValueParser.LONG_TYPE_PARSER);
	/** The version number of the HCP software. **/
	public final static HCPHeaderKey<String> X_HCP_SOFTWARE_VERSION = new HCPHeaderKey<String>("X-HCP-SoftwareVersion");
	/**
	 * The path to the target object or directory as specified when the symbolic link was created.
	 * 
	 * This header is returned only if the URL specifies a symbolic link to an object or directory.
	 **/
	public final static HCPHeaderKey<String> X_HCP_SYMLINK_TARGET = new HCPHeaderKey<String>("X-HCP-SymlinkTarget");
	/**
	 * The time at which HCP sent the response to the request, in seconds since January 1, 1970, at 00:00:00 UTC.
	 **/
	public final static HCPHeaderKey<Long> X_HCP_TIME = new HCPHeaderKey<Long>("X-HCP-Time", StringValueParser.LONG_TYPE_PARSER);
	/**
	 * The entity type. One of:
	 * 
	 * annotation
	 * 
	 * directory
	 * 
	 * object
	 **/
	public final static HCPHeaderKey<ObjectType> X_HCP_TYPE = new HCPHeaderKey<ObjectType>("X-HCP-Type", new StringValueParser<ObjectType>() {
		public ObjectType parse(String value) {
			return ObjectType.valueOf(value);
		}
	});
	/**
	 * The POSIX user ID for the object.
	 * 
	 * For objects added through the NFS protocol, this value is determined by the NFS client.
	 * 
	 * This value is an empty string if either of these are true:
	 * 
	 * The object was added through a protocol other than NFS and neither the UID nor the GID for the object has been changed.
	 * 
	 * The HCP owner of the object was changed.
	 **/
	public final static HCPHeaderKey<String> X_HCP_UID = new HCPHeaderKey<String>("X-HCP-UID");
	/** The version ID of the object. **/
	public final static HCPHeaderKey<String> X_HCP_VERSIONID = new HCPHeaderKey<String>("X-HCP-VersionId");
	/** The version ID of the object. **/
	public final static HCPHeaderKey<String> X_AMZ_VERSION_ID = new HCPHeaderKey<String>("x-amz-version-id");
	/**
	 * X-HCP-CopySource: source-namespace-name.source-tenant-name/source-object-path
	 **/
	public static final HCPHeaderKey<CopyObjectRequest> X_HCP_COPY_SOURCE = new HCPHeaderKey<CopyObjectRequest>("X-HCP-CopySource", new StringValueBuilder<CopyObjectRequest>() {

		public String build(CopyObjectRequest request) throws BuildException {
			String url = request.getSourceNamespace() + "." + request.getSourceEndpoint();
			String key;
			KeyAlgorithm keyAlgorithm = request.getSourceKeyAlgorithm();
			if (keyAlgorithm != null) {
				try {
					key = keyAlgorithm.generate(request.getSourceKey());
				} catch (AlgorithmException e) {
					throw new BuildException(e);
				}
			} else {
				key = request.getSourceKey();
			}

			url = URLUtils.catPath(url, key);

			if (StringUtils.isNotEmpty(request.getSourceVersionId())) {
				HCPRequestParams param = new HCPRequestParams();
				param.setParameter(RequestParamKey.VERSION, request.getSourceVersionId());
				url = HttpUtils.catParams(url, param);
			}

			// if (request.isWithDeletedObject()) {
			// param.enableParameter(RequestParamKey.DELETED);
			// }

			// return URLUtils.encode(url, Constants.DEFAULT_URL_ENCODE);
			return URLUtils.urlEncode(url, Constants.DEFAULT_URL_ENCODE);
		}

	});
	/**
	 * X-HCP-CopySource:
	 * <p>
	 * source namespace and object or object version, in this format:
	 * <p>
	 * /namespace-name/source-object-name [?versionId=source-object-version-id]
	 * <p>
	 * The initial forward slash (/) is required.
	 * <p>
	 * Valid values for source-object-version-id are the IDs of versions of the source object specified in the request.
	 * <p>
	 * The versionId query parameter is not case sensitive.
	 * <p>
	 * If you include the versionId query parameter in the x-amz-copy-source header with an invalid value while versioning is enabled, HCP
	 * returns a 404 (Not Found) status code and does not perform the upload part copy operation.
	 * <p>
	 * If you include the versionId query parameter in the x-amz-copy-source header while versioning is disabled, the parameter is ignored, and
	 * the current version of the specified object is used as the source for the upload part copy operation.
	 **/
	public static final HCPHeaderKey<CopyPartRequest> X_AMZ_COPY_SOURCE = new HCPHeaderKey<CopyPartRequest>("x-amz-copy-source", new StringValueBuilder<CopyPartRequest>() {

		public String build(CopyPartRequest request) throws BuildException {
			String url = "/" + request.getNamespace();
			String key;
			KeyAlgorithm keyAlgorithm = request.getSourceKeyAlgorithm();
			if (keyAlgorithm != null) {
				try {
					key = keyAlgorithm.generate(request.getSourceKey());
				} catch (AlgorithmException e) {
					throw new BuildException(e);
				}
			} else {
				key = request.getSourceKey();
			}

			url = URLUtils.catPath(url, key);

			if (StringUtils.isNotEmpty(request.getSourceVersionId())) {
				HCPRequestParams param = new HCPRequestParams();
				param.setParameter(RequestParamKey.VERSION, request.getSourceVersionId(), true);
				url = HttpUtils.catParams(url, param);
			}

			return URLUtils.encode(url, Constants.DEFAULT_URL_ENCODE);
		}

	});

	/**
	 * X-HCP-CopySource-range: Copying part of the data for an object x-amz-copy-source-range: bytes=1000000-6999999
	 **/
	public static final HCPHeaderKey<CopyPartRequest> X_AMZ_COPY_SOURCE_RANGE = new HCPHeaderKey<CopyPartRequest>("x-amz-copy-source-range", new StringValueBuilder<CopyPartRequest>() {

		public String build(CopyPartRequest request) {
			Long[] range = request.getRange();
			if (range != null && range.length == 2) {
				String v = "bytes=";
				if (range[0] >= 0) {
					v += range[0];
				}
				v += "-";
				if (range[1] > 0) {
					v += range[1];
				}

				return v;
			}

			return "";
		}

	});

	private HCPHeaderKey(String keyname, StringValueBuilder<T> requestValueBuilder) {
		super(keyname, requestValueBuilder);
	}

	private HCPHeaderKey(String keyname, StringValueParser<T> responseHeadValueParser) {
		super(keyname, responseHeadValueParser);
	}

	private HCPHeaderKey(String keyname) {
		super(keyname);
	}

	public T parse(HttpResponse response) {
		Header header = response.getHeader(this.keyname);
		if (header != null) {
			return super.parse(header.getValue());
		}

		return null;
	}
	
	public Header createHeader(T value) throws BuildException {
		return new Header(this.getKeyname(), this.build(value));
	}

}
