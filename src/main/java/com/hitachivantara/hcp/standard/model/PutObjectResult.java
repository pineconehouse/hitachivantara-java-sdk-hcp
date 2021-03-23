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
package com.hitachivantara.hcp.standard.model;

/**
 * @author sohan
 *
 *         -Resp:----------------------------------------------------------------------------<br>
 *         Code : 201 Created<br>
 *         -Header:--------------------------------------------------------------------------<br>
 *         Date=Mon, 02 Apr 2018 08:07:42 GMT<br>
 *         Server=HCP V8.0.0.10-<br>
 *         X-HCP-ServicedBySystem=hcp8.hdim.lab<br>
 *         Location=/rest/00002/Test1-2.log<br>
 *         X-HCP-VersionId=97450013608321<br>
 *         X-HCP-IngestTimeMilliseconds=1522656462630<br>
 *         X-HCP-Hash=SHA-256 5D45EA970F0782E780A22D74321F6D6C839FACF6525E0E41F561B927AA0A4BA6<br>
 *         ETag="4c77ca9ccd1aab26210483d7070b341e"<br>
 *         X-RequestId=5F8E1FF3F18A9A37<br>
 *         X-HCP-Time=1522656462<br>
 *         Content-Length=0<br>
 *         ----------------------------------------------------------------------------------<br>
 * 
 */
public class PutObjectResult extends HCPStandardResult {
	/**
	 * The version ID of the new, uploaded object. This field will only be<br>
	 * present if object versioning has been enabled for the namespace to which the<br>
	 * object was uploaded.
	 */
	private String versionId;
	private String hashAlgorithmName;
	private String contentHash;
	/** The ETag value of the new object */
	private String eTag;
	private String location;
	private Long ingestTime;

	private String sourceContentHash;

	public PutObjectResult(String location, String versionId, String hashAlgorithmName, String contentHashCode, String eTag, Long ingestTimeMilliseconds) {
		super();
		this.versionId = versionId;
		this.hashAlgorithmName = hashAlgorithmName;
		this.contentHash = contentHashCode;
		this.eTag = eTag;
		this.location = location;
		this.ingestTime = ingestTimeMilliseconds;
	}

	public String getVersionId() {
		return versionId;
	}

	public String getETag() {
		return eTag;
	}

	public String getLocation() {
		return location;
	}

	public Long getIngestTime() {
		return ingestTime;
	}

	public String getHashAlgorithmName() {
		return hashAlgorithmName;
	}

	public String getContentHash() {
		return contentHash;
	}

	public String getSourceContentHash() {
		return sourceContentHash;
	}

	public void setSourceContentHash(String sourceContentHash) {
		this.sourceContentHash = sourceContentHash;
	}

}
