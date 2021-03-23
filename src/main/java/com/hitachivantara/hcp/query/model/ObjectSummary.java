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
package com.hitachivantara.hcp.query.model;

import com.hitachivantara.hcp.query.define.Operation;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;

public class ObjectSummary extends HCPObjectSummary {
//	private String key;
	private String namespace;
	private String urlName;
	private String objectPath;
//	private String name;// utf8Name
//	private ObjectType type;
//	private Long size;
//	private String versionId;

	private Boolean hasMetadata;
//	private Annotation[] customMetadatas;
//	private String customMetadataContent;

//	private String contentHash;
//	private String hashAlgorithmName;

//	private Long ingestTime;
	private String ingestTimeString;
	private Long accessTime;
	private String accessTimeString;
	private Long updateTime;
	private String updateTimeString;
	private Long changeTimeMilliseconds;
	private String changeTimeString;

//	private String retention;
//	private String retentionClass;
//	private String retentionString;

//	private Boolean hold;
//	private Boolean indexed;
//	private Boolean shred;
//	private Boolean hasAcl;
	private String aclGrant;
//	private Integer dpl;
	private String gid;
	private Operation operation;
	private String permissions;
//	private Boolean replicated;
//	private Boolean replicationCollision;
//	private String owner;
	private String uid;

//	public String getKey() {
//		return key;
//	}
//
//	public void setKey(String key) {
//		this.key = key;
//	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getObjectPath() {
		return objectPath;
	}

	public void setObjectPath(String objectPath) {
		this.objectPath = objectPath;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

//	public ObjectType getType() {
//		return type;
//	}
//
//	public void setType(ObjectType type) {
//		this.type = type;
//	}

//	public Long getSize() {
//		return size;
//	}
//
//	public void setSize(Long size) {
//		this.size = size;
//	}

//	public String getVersionId() {
//		return versionId;
//	}
//
//	public void setVersionId(String versionId) {
//		this.versionId = versionId;
//	}

	public Boolean hasMetadata() {
		return hasMetadata;
	}

	public void setHasMetadata(Boolean hasMetadata) {
		this.hasMetadata = hasMetadata;
	}

//	public Annotation[] getCustomMetadatas() {
//		return customMetadatas;
//	}
//
//	public void setCustomMetadatas(Annotation[] customMetadatas) {
//		this.customMetadatas = customMetadatas;
//	}

//	public String getCustomMetadataContent() {
//		return customMetadataContent;
//	}
//
//	public void setCustomMetadataContent(String customMetadataContent) {
//		this.customMetadataContent = customMetadataContent;
//	}

//	public String getContentHash() {
//		return contentHash;
//	}
//
//	public void setContentHash(String contentHash) {
//		this.contentHash = contentHash;
//	}
//
//	public String getHashAlgorithmName() {
//		return hashAlgorithmName;
//	}
//
//	public void setHashAlgorithmName(String hashAlgorithmName) {
//		this.hashAlgorithmName = hashAlgorithmName;
//	}
//
//	public Long getIngestTime() {
//		return ingestTime;
//	}
//
//	public void setIngestTime(Long ingestTime) {
//		this.ingestTime = ingestTime;
//	}

	public String getIngestTimeString() {
		return ingestTimeString;
	}

	public void setIngestTimeString(String ingestTimeString) {
		this.ingestTimeString = ingestTimeString;
	}

	public Long getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Long accessTime) {
		this.accessTime = accessTime;
	}

	public String getAccessTimeString() {
		return accessTimeString;
	}

	public void setAccessTimeString(String accessTimeString) {
		this.accessTimeString = accessTimeString;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateTimeString() {
		return updateTimeString;
	}

	public void setUpdateTimeString(String updateTimeString) {
		this.updateTimeString = updateTimeString;
	}

	public Long getChangeTimeMilliseconds() {
		return changeTimeMilliseconds;
	}

	public void setChangeTimeMilliseconds(Long changeTimeMilliseconds) {
		this.changeTimeMilliseconds = changeTimeMilliseconds;
	}

	public String getChangeTimeString() {
		return changeTimeString;
	}

	public void setChangeTimeString(String changeTimeString) {
		this.changeTimeString = changeTimeString;
	}

//	public String getRetention() {
//		return retention;
//	}
//
//	public void setRetention(String retention) {
//		this.retention = retention;
//	}
//
//	public String getRetentionClass() {
//		return retentionClass;
//	}
//
//	public void setRetentionClass(String retentionClass) {
//		this.retentionClass = retentionClass;
//	}
//
//	public String getRetentionString() {
//		return retentionString;
//	}
//
//	public void setRetentionString(String retentionString) {
//		this.retentionString = retentionString;
//	}
//
//	public Boolean getHold() {
//		return hold;
//	}
//
//	public void setHold(Boolean hold) {
//		this.hold = hold;
//	}
//
//	public Boolean getIndexed() {
//		return indexed;
//	}
//
//	public void setIndexed(Boolean indexed) {
//		this.indexed = indexed;
//	}
//
//	public Boolean getShred() {
//		return shred;
//	}
//
//	public void setShred(Boolean shred) {
//		this.shred = shred;
//	}
//
//	public Boolean hasAcl() {
//		return hasAcl;
//	}
//
//	public void setHasAcl(Boolean hasAcl) {
//		this.hasAcl = hasAcl;
//	}

	public String getAclGrant() {
		return aclGrant;
	}

	public void setAclGrant(String aclGrant) {
		this.aclGrant = aclGrant;
	}

//	public Integer getDpl() {
//		return dpl;
//	}
//
//	public void setDpl(Integer dataProtectionLevel) {
//		this.dpl = dataProtectionLevel;
//	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

//	public Boolean getReplicated() {
//		return replicated;
//	}
//
//	public void setReplicated(Boolean replicated) {
//		this.replicated = replicated;
//	}
//
//	public Boolean getReplicationCollision() {
//		return replicationCollision;
//	}
//
//	public void setReplicationCollision(Boolean replicationCollision) {
//		this.replicationCollision = replicationCollision;
//	}
//
//	public String getOwner() {
//		return owner;
//	}
//
//	public void setOwner(String owner) {
//		this.owner = owner;
//	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
