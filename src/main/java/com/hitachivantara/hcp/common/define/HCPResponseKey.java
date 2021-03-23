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

/**
 * The table below describes the possible return codes for HTTP requests that access a namespace.
 * 
 * @author sohan
 */
public enum HCPResponseKey {
	OK(200,"HCP successfully processed a request"),
	CREATED(201,"HCP successfully added an object, version, annotation, ACL, or directory to the namespace, copied an object, or replaced an annotation or ACL for an object."),
	NO_CONTENT(204,"One of:\r\n" + 
			"For an annotation, the specified object does not have the requested annotation.\r\n" + 
			"For a version, the requested version is a deleted version.\r\n" + 
			"For information about annotations, the object or version does not have any annotations."),
	PARTIAL_CONTENT(206,"HCP successfully retrieved the data in the byte range specified in the request."),
	FOUND(302,"Either the specified resource does not exist, or the specified resource exists, but the user account identified by the Authorization header or hcp-api-auth cookie doesn’t have permission to access the resource."),
	NOT_MODIFIED(304,"One of:\r\n" + 
			"The request specified an If-Modified-Since header, and the object change time is at or before the time specified in the header.\r\n" + 
			"The request specified an If-None-Match header, and the ETag of the requested object or version matches the value in the header.\r\n" + 
			"A request to copy an object specified X-HCP-Copy-Source-If-Modified-Since header, and the source object change time is at or before the time specified in the header.\r\n" + 
			"A request to copy an object specified an X-HCP-Copy-Source-If-None-Match header, and the ETag of the source object matches the value in the header."),
	BAD_REQUEST(400,"The request was not valid. These are some, but not all, of the possible reasons:\r\n" + 
			"The URL in the request is not well-formed.\r\n" + 
			"The request contains an unsupported query parameter or an invalid value for a query parameter.\r\n" + 
			"A GET request has both a type=whole-object query parameter and a Range request header.\r\n" + 
			"A PUT or POST request has a Content-Encoding header that specifies gzip, but the content is not in gzip-compressed format.\r\n" + 
			"The X-HCP-CopySource header in a PUT request to copy an object identifies a symbolic link to an object.\r\n" + 
			"A PUT request has a type=whole-object query parameter but does not have an X-HCP-Size header or the X-HCP-Size header value is greater than the content length.\r\n" + 
			"HCP has custom metadata XML checking enabled, and a PUT request includes an annotation that is not well-formed XML.\r\n" + 
			"For a PUT request that is trying to store an ACL:\r\n" + 
			"	oThe ACL includes invalid entries or values or is not well-formed XM L or JSON.\r\n" + 
			"	oACLs are not enabled for the namespace.\r\n" + 
			"	oThe ACL contains more than one thousand ACEs.\r\n" + 
			"	oThe ACL specifies an AD user or group, but the namespace does not support AD authentication.\r\n" + 
			"The request is trying to change the owner of an object to an AD user, but the namespace does not support AD authentication.\r\n" + 
			"The request is trying to change the retention setting from a retention class to an explicit setting, such as a datetime value.\r\n" + 
			"The request is trying to change the retention setting and the hold setting at the same time.\r\n" + 
			"The specified object has ten annotations and the request is trying to add an annotation.\r\n" + 
			"The request is trying to change the retention setting for an object on hold.\r\n" + 
			"The request is trying to change the shred setting from true to false.\r\n" + 
			"A request other than GET, HEAD, or PUT for an object or version includes a conditional header such as If-Match.\r\n" + 
			"The request includes an If-Match or If-None-Match header and the object does not yet have an ETag.\r\n" + 
			"A request to copy an object includes an X-HCP-CopySource-If-Match or X-HCP-CopySource-If-None-Match header and does not include a ForceEtag=true query parameter, and the source object does not yet have an ETag.\r\n" + 
			"A DELETE request includes a conditional header.\r\n" + 
			"If more information about the error is available, the response headers include the HCP-specific X-HCP-ErrorMessage header."),
	UNAUTHORIZED(401,"The user does not have access to the namespace for which information is being requested."),
	FORBIDDEN(403,"The requested operation was not allowed. These are some, but not all, of the possible reasons:\r\n" + 
			"The Authorization header or hcp-ns-auth cookie specifies invalid credentials.\r\n" + 
			"The namespace requires authentication, and the request does not include an Authorization header or hcp-ns-auth cookie.\r\n" + 
			"The user doesn’t have permission to perform the requested operation.\r\n" + 
			"The namespace does not exist.\r\n" + 
			"The access method (HTTP or HTTPS) is disabled.\r\n" + 
			"For a DELETE request to delete an object, the object is under retention.\r\n" + 
			"For a DELETE request to delete a directory, the directory is not empty.\r\n" + 
			"For a DELETE request to delete an annotation, the object is under retention, and the namespace does not allow deleting annotations for objects under retention.\r\n" + 
			"For a PUT request to add a version of an existing object, the object is under retention.\r\n" + 
			"For a PUT request to add or replace an annotation for an object that is under retention, the namespace does not allow the operation for objects under retention.\r\n" + 
			"If more information about the error is available, the response headers include the HCP-specific X-HCP-ErrorMessage header."),
	NOT_FOUND(404,"One of:\r\n" + 
			"For all methods, HCP could not find the specified object, version, or directory.\r\n" + 
			"Any component of the URL except for the last component in the path is a symbolic link to a directory.\r\n" + 
			"For a GET request for a version or a PUT request to copy a version, the specified version is the current version of a deleted object.\r\n" + 
			"For a GET request to retrieve object or version data and an annotation concurrently, the requested object does not have an annotation with the specified name.\r\n"),
	METHOD_NOT_ALLOWED(405,"Possible reasons include:\r\n" + 
			"For a GET request, the specified object cannot be read because it is in the process of being deleted.\r\n" + 
			"For a PUT request to copy an object or version, the specified object exists but cannot be read."),
	NOT_ACCEPTABLE(406,"One of:\r\n" + 
			"The request has an Accept-Encoding header that does not include gzip or *.\r\n" + 
			"For a GET request for an ACL, the request has a Content-Type header with a value other than application/xml or application/json."),
	CONFLICT(409,"One of:\r\n" + 
			"For a DELETE request, HCP could not delete or purge the specified object or delete the directory, annotation, or ACL because it is currently being written to the namespace.\r\n" + 
			"For a PUT request, HCP could not add the object or directory because it already exists and versioning is not enabled.\r\n" + 
			"For a PUT request to store a version of an existing object, HCP could not add a new version of the object because another version is currently being added.\r\n" + 
			"For a PUT request to store an annotation, the object for which the annotation is being stored was ingested using CIFS or NFS, and the lazy close period for the object has not expired.\r\n" + 
			"For a PUT request that includes a conditional header, HCP is processing another PUT request for the object. \r\n" + 
			"A large number of clients are trying to store custom metadata or ACLs for multiple objects at the same time."),
	GONE(410,"The object exists but the HCP system does not have the object data. Retry the request targeting a different system in the replication topology."),
	PRECONDITION_FAILED(412,"One of:\r\n" + 
			"The request specified an If-Match header, and the ETag of the requested object or version does not match the value in the header.\r\n" + 
			"A PUT request specified an If-None-Match header, and the ETag of the requested object or version matches the value in the header.\r\n" + 
			"The request specified an If-Unmodified-Since header, and the object change time is after the time specified in the header."),
	FILE_TOO_LARGE(413,"One of:\r\n" + 
			"Not enough space is available to store the object. Try the request again after objects or versions are deleted from the namespace or the namespace capacity is increased.\r\n" + 
			"The request is trying to save an object that is larger than two TB. HCP cannot store objects larger than two TB.\r\n" + 
			"The request is trying to store a default annotation that is larger than one GB or any other annotation that is larger than one MB."),
	REQUEST_URI_TOO_LARGE(414,"The portion of the URL following rest is longer than 4,095 bytes."),
	UNSUPPORTED_MEDIA_TYPE(415,"One of:\r\n" + 
			"The request has a Content-Encoding header with a value other than gzip.\r\n" + 
			"For a PUT request for an ACL, the request has a Content-Type header with a value other than application/xml or application/json."),
	REQUESTED_RANGE_NOT_SATISFIABLE(416,"One of:\r\n" + 
			"The specified start position is greater than the size of the requested data.\r\n" + 
			"The size of the specified range is zero."),
	INTERNAL_SERVER_ERROR(500,"An internal error occurred. Try the request again, gradually increasing the delay between each successive attempt. \r\n" + 
			"If this error happens repeatedly, please contact your tenant administrator."),
	SERVICE_UNAVAILABLE(503,"One of:\r\n" + 
			"The request includes a Content-Length header with a value larger than the length of the message body\r\n" + 
			"For a GET request to retrieve object data or an annotation, the request specifies the nowait query parameter, and HCP determined that the request would have taken a significant amount of time to return the data.\r\n" + 
			"For a GET request to retrieve object data, the specified object exists but cannot be read.\r\n" + 
			"For a PUT request to copy an object or version, the specified source object exists but cannot be read.\r\n" + 
			"HCP is temporarily unable to handle the request, probably due to system overload, maintenance, or upgrade.\r\n" + 
			"HCP tried to read the object from another system in the replication topology but could not.\r\n" + 
			"In the last three cases, try the request again, gradually increasing the delay between each successive attempt. If the error persists, please contact your tenant administrator.\r\n" + 
			"If more information about the error is available, the response headers include the HCP-specific X-HCP-ErrorMessage header."),
	UNKNOWN_RESPONSE(-1,"Unknown Response Code");

	private int code = 200;
	private String desc = "";
	private HCPResponseKey(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public int code() {
		return code;
	}

	public static HCPResponseKey valueOf(int code) {
		HCPResponseKey[] values = HCPResponseKey.values();
		for (HCPResponseKey hcpResponseCode : values) {
			if (hcpResponseCode.code == code) {
				return hcpResponseCode;
			}
		}

		//UNKNOWN_RESPONSE.code = code;
		return UNKNOWN_RESPONSE;
	}
}
