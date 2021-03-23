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

import com.hitachivantara.hcp.standard.define.ObjectType;
import com.hitachivantara.hcp.standard.model.metadata.Annotation;

public class HCPObjectSummary extends HCPStandardResult {
	private String key;
	private String name;

	private String etag;
	// String cache-Control
	// String Pragma
	// String Expires
	private ObjectType type;
	private Long size;
	private String hashAlgorithmName;
	private String contentHash;
	private String versionId;
	private Long ingestTime;
	// Long ingestTimeMilliseconds;
	private String retentionClass;
	private String retentionString;
	private String retention;
	private String ingestProtocol;
	private Boolean hold;
	private Boolean shred;
	private Integer dpl;// dataProtectionLevel;
	private Boolean indexed;
	private Boolean hasMetadata;
	private Boolean hasAcl;
	private String owner;
	private String domain;
	private String posixUserID;
	private String posixGroupIdentifier;
	private Annotation[] customMetadatas; // TODO
	private Boolean replicated;
	private Boolean replicationCollision;
	private Long changeTime;
	// String ChangeTimeString

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isDirectory() {
		return type == ObjectType.directory;
	}

	public Boolean isObject() {
		return type == ObjectType.object;
	}

	public Boolean isSymlink() {
		return type == ObjectType.symlink;
	}

	public Boolean isAnnotation() {
		return type == ObjectType.annotation;
	}

	public String getETag() {
		return etag;
	}

	public void setETag(String etag) {
		this.etag = etag;
	}

	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
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

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public Long getIngestTime() {
		return ingestTime;
	}

	public void setIngestTime(Long ingestTime) {
		this.ingestTime = ingestTime;
	}

	public String getRetentionClass() {
		return retentionClass;
	}

	public void setRetentionClass(String retentionClass) {
		this.retentionClass = retentionClass;
	}

	public String getRetentionString() {
		return retentionString;
	}

	public void setRetentionString(String retentionString) {
		this.retentionString = retentionString;
	}

	public String getRetention() {
		return retention;
	}

	public void setRetention(String retention) {
		this.retention = retention;
	}

	public String getIngestProtocol() {
		return ingestProtocol;
	}

	public void setIngestProtocol(String ingestProtocol) {
		this.ingestProtocol = ingestProtocol;
	}

	public Boolean isHold() {
		return hold;
	}

	public void setHold(Boolean holdEnabled) {
		this.hold = holdEnabled;
	}

	public Boolean isShred() {
		return shred;
	}

	public void setShred(Boolean shredEnabled) {
		this.shred = shredEnabled;
	}

	public Integer getDpl() {
		return dpl;
	}

	public void setDpl(Integer dataProtectionLevel) {
		this.dpl = dataProtectionLevel;
	}

	public Boolean isIndexed() {
		return indexed;
	}

	public void setIndexed(Boolean indexEnabled) {
		this.indexed = indexEnabled;
	}

	public Boolean hasMetadata() {
		return hasMetadata;
	}

	public void setHasMetadata(Boolean hasMetadata) {
		this.hasMetadata = hasMetadata;
	}

	public Boolean hasAcl() {
		return hasAcl;
	}

	public void setHasAcl(Boolean aclEnabled) {
		this.hasAcl = aclEnabled;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPosixUserID() {
		return posixUserID;
	}

	public void setPosixUserID(String posixUserID) {
		this.posixUserID = posixUserID;
	}

	public String getPosixGroupIdentifier() {
		return posixGroupIdentifier;
	}

	public void setPosixGroupIdentifier(String posixGroupIdentifier) {
		this.posixGroupIdentifier = posixGroupIdentifier;
	}

//	public Annotation[] getMetadata() {
//		return customMetadatas;
//	}
//
//	public void setMetadata(Annotation[] customMetadatas) {
//		this.customMetadatas = customMetadatas;
//	}
	
	public Annotation[] getCustomMetadatas() {
		return customMetadatas;
	}
	
	public void setCustomMetadatas(Annotation[] customMetadatas) {
		this.customMetadatas = customMetadatas;
	}

	public Boolean isReplicated() {
		return replicated;
	}

	public void setReplicated(Boolean replicated) {
		this.replicated = replicated;
	}

	public Boolean isReplicationCollision() {
		return replicationCollision;
	}

	public void setReplicationCollision(Boolean replicationCollision) {
		this.replicationCollision = replicationCollision;
	}

	public Long getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Long changeTime) {
		this.changeTime = changeTime;
	}

}
