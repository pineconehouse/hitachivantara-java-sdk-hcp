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
package com.hitachivantara.hcp.standard.model.metadata;

import com.hitachivantara.hcp.standard.model.HCPStandardResult;

public class HCPMetadataSummary extends HCPStandardResult {
	private String name;
	private Long size;

	private String etag;
	private String hashAlgorithmName;
	private String contentHash;
	private String contentType;
	private Long changeTime;

	public HCPMetadataSummary(String metadataName) {
		this.name = metadataName;
	}

	public String getName() {
		return name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getETag() {
		return etag;
	}

	public void setETag(String etag) {
		this.etag = etag;
	}

	public String getHashAlgorithmName() {
		return hashAlgorithmName;
	}

	public void setHashAlgorithmName(String hashAlgorithmName) {
		this.hashAlgorithmName = hashAlgorithmName;
	}

	public String getContentHash() {
		return contentHash;
	}

	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Long changeTime) {
		this.changeTime = changeTime;
	}

}
