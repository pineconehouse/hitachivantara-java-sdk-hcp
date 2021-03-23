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
package com.hitachivantara.hcp.management.model;

import java.util.List;

import com.hitachivantara.hcp.management.define.AclsUsage;
import com.hitachivantara.hcp.management.define.HashScheme;
import com.hitachivantara.hcp.management.define.OptimizedFor;
import com.hitachivantara.hcp.management.define.OwnerType;
import com.hitachivantara.hcp.management.define.Permission;
import com.hitachivantara.hcp.management.define.QuotaUnit;

/**
 * The namespace data type describes the namespaces resource.
 * 
 * @author sohan
 *
 */
public class NamespaceSettings {

	private String id;
	private String creationTime;
	private String fullyQualifiedName;
	private String mqeIndexingTimestamp;
	private String replicationTimestamp;

	private String name;
	private String description;
	private double hardQuota;
	private QuotaUnit hardQuotaUnit;
	private Integer softQuota;
	private HashScheme hashScheme;

	private Boolean indexingDefault;
	private Boolean indexingEnabled;
	private Boolean searchEnabled;
	private Boolean enterpriseMode;
	private Boolean serviceRemoteSystemRequests;
	private Boolean customMetadataValidationEnabled;

	private List<String> tags;

	private Boolean appendEnabled;
	private Boolean atimeSynchronizationEnabled;
	private Boolean allowPermissionAndOwnershipChanges;
	private String servicePlan;
	private Boolean customMetadataIndexingEnabled;
	private AclsUsage aclsUsage;
	private String owner;
	private OwnerType ownerType;
	private OptimizedFor optimizedFor;
	private Integer multipartUploadAutoAbortDays;
	private String dpl;
	// Deprecated. Namespace DPL is now a service plan function.
	// private Boolean isDplDynamic;
	private Boolean authUsersAlwaysGrantedAllPermissions;
	private List<Permission> authMinimumPermissions;
	private List<Permission> authAndAnonymousMinimumPermissions;

	private Boolean replicationEnabled;
	private Boolean readFromReplica;
	private Boolean allowErasureCoding;

	private VersioningSettings versioningSettings;

	public NamespaceSettings() {
	}

	public NamespaceSettings(String id,
			String creationTime,
			String fullyQualifiedName,
			String mqeIndexingTimestamp,
			String replicationTimestamp,
			String name,
			String description,
			double hardQuota,
			QuotaUnit hardQuotaUnit,
			Integer softQuota,
			HashScheme hashScheme,
			Boolean indexingDefault,
			Boolean indexingEnabled,
			Boolean searchEnabled,
			Boolean enterpriseMode,
			Boolean serviceRemoteSystemRequests,
			Boolean customMetadataValidationEnabled,
			List<String> tags,
			Boolean appendEnabled,
			Boolean atimeSynchronizationEnabled,
			Boolean allowPermissionAndOwnershipChanges,
			String servicePlan,
			Boolean customMetadataIndexingEnabled,
			AclsUsage aclsUsage,
			String owner,
			OwnerType ownerType,
			OptimizedFor optimizedFor,
			Integer multipartUploadAutoAbortDays,
			String dpl,
			Boolean authUsersAlwaysGrantedAllPermissions,
			List<Permission> authMinimumPermissions,
			List<Permission> authAndAnonymousMinimumPermissions) {
		super();
		this.id = id;
		this.creationTime = creationTime;
		this.fullyQualifiedName = fullyQualifiedName;
		this.mqeIndexingTimestamp = mqeIndexingTimestamp;
		this.replicationTimestamp = replicationTimestamp;

		this.name = name;
		this.description = description;
		this.hardQuota = hardQuota;
		this.hardQuotaUnit = hardQuotaUnit;
		this.softQuota = softQuota;
		this.hashScheme = hashScheme;
		this.indexingDefault = indexingDefault;
		this.indexingEnabled = indexingEnabled;
		this.searchEnabled = searchEnabled;
		this.enterpriseMode = enterpriseMode;
		this.serviceRemoteSystemRequests = serviceRemoteSystemRequests;
		this.customMetadataValidationEnabled = customMetadataValidationEnabled;
		this.tags = tags;
		this.appendEnabled = appendEnabled;
		this.atimeSynchronizationEnabled = atimeSynchronizationEnabled;
		this.allowPermissionAndOwnershipChanges = allowPermissionAndOwnershipChanges;
		this.servicePlan = servicePlan;
		this.customMetadataIndexingEnabled = customMetadataIndexingEnabled;
		this.aclsUsage = aclsUsage;
		this.owner = owner;
		this.ownerType = ownerType;
		this.optimizedFor = optimizedFor;
		this.multipartUploadAutoAbortDays = multipartUploadAutoAbortDays;
		this.dpl = dpl;
		this.authUsersAlwaysGrantedAllPermissions = authUsersAlwaysGrantedAllPermissions;
		this.authMinimumPermissions = authMinimumPermissions;
		this.authAndAnonymousMinimumPermissions = authAndAnonymousMinimumPermissions;
	}

	public String getId() {
		return id;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public String getMqeIndexingTimestamp() {
		return mqeIndexingTimestamp;
	}

	public String getReplicationTimestamp() {
		return replicationTimestamp;
	}

	/**
	 * 
	 * Specifies the name of the namespace. HCP derives the hostname for the namespace from this name. The hostname is used in URLs for access to the
	 * namespace.
	 * 
	 * In English, the name you specify for a namespace must be from one through 63 characters long and can contain only alphanumeric characters
	 * and hyphens (-) but cannot start or end with a hyphen. In other languages, because the derived hostname cannot be more than 63 characters
	 * long, the name you specify may be limited to fewer than 63 characters.
	 * 
	 * Namespace names cannot contain special characters other than hyphens and are not case sensitive. White space is not allowed.
	 * 
	 * Namespace names cannot start with xn-- (that is, the characters x and n followed by two hyphens).
	 * 
	 * Namespace names must be unique for the owning tenant. Different tenants can have namespaces with the same name.
	 * 
	 * You can reuse namespace names that are not currently in use. So, for example, if you delete a namespace, you can give a new namespace the same
	 * name as the one the deleted namespace had.
	 * 
	 * The name of the default namespace is always Default.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getHardQuota() {
		return hardQuota;
	}

	public void setHardQuota(double hardQuota, QuotaUnit hardQuotaUnit) {
		this.hardQuota = hardQuota;
		this.hardQuotaUnit = hardQuotaUnit;
	}

	public QuotaUnit getHardQuotaUnit() {
		return hardQuotaUnit;
	}

	// public void setHardQuotaUnit(SizeUnit hardQuotaUnit) {
	// this.hardQuotaUnit = hardQuotaUnit;
	// }

	public Integer getSoftQuota() {
		return softQuota;
	}

	public void setSoftQuota(Integer softQuota) {
		this.softQuota = softQuota;
	}

	public Boolean getIndexingDefault() {
		return indexingDefault;
	}

	public void setIndexingDefault(Boolean indexingDefault) {
		this.indexingDefault = indexingDefault;
	}

	public Boolean getIndexingEnabled() {
		return indexingEnabled;
	}

	public void setIndexingEnabled(Boolean indexingEnabled) {
		this.indexingEnabled = indexingEnabled;
	}

	public Boolean getSearchEnabled() {
		return searchEnabled;
	}

	public void setSearchEnabled(Boolean searchEnabled) {
		this.searchEnabled = searchEnabled;
	}

	public Boolean getEnterpriseMode() {
		return enterpriseMode;
	}

	public void setEnterpriseMode(Boolean enterpriseMode) {
		this.enterpriseMode = enterpriseMode;
	}

	public Boolean getServiceRemoteSystemRequests() {
		return serviceRemoteSystemRequests;
	}

	public void setServiceRemoteSystemRequests(Boolean serviceRemoteSystemRequests) {
		this.serviceRemoteSystemRequests = serviceRemoteSystemRequests;
	}

	public Boolean getCustomMetadataValidationEnabled() {
		return customMetadataValidationEnabled;
	}

	public void setCustomMetadataValidationEnabled(Boolean customMetadataValidationEnabled) {
		this.customMetadataValidationEnabled = customMetadataValidationEnabled;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Boolean getAppendEnabled() {
		return appendEnabled;
	}

	public void setAppendEnabled(Boolean appendEnabled) {
		this.appendEnabled = appendEnabled;
	}

	public Boolean getAtimeSynchronizationEnabled() {
		return atimeSynchronizationEnabled;
	}

	public void setAtimeSynchronizationEnabled(Boolean atimeSynchronizationEnabled) {
		this.atimeSynchronizationEnabled = atimeSynchronizationEnabled;
	}

	/**
	 * 
	 * @return Specifies whether changes to POSIX UIDs, GIDs, and permissions and to object ownership are allowed for objects under retention in
	 *         the namespace.
	 */
	public Boolean getAllowPermissionAndOwnershipChanges() {
		return allowPermissionAndOwnershipChanges;
	}

	public void setAllowPermissionAndOwnershipChanges(Boolean allowPermissionAndOwnershipChanges) {
		this.allowPermissionAndOwnershipChanges = allowPermissionAndOwnershipChanges;
	}

	public String getServicePlan() {
		return servicePlan;
	}

	public void setServicePlan(String servicePlan) {
		this.servicePlan = servicePlan;
	}

	public Boolean getCustomMetadataIndexingEnabled() {
		return customMetadataIndexingEnabled;
	}

	public void setCustomMetadataIndexingEnabled(Boolean customMetadataIndexingEnabled) {
		this.customMetadataIndexingEnabled = customMetadataIndexingEnabled;
	}

	/**
	 * @return Specifies whether the namespace supports access control lists (ACLs) and, if so, whether HCP honors ACLs in the namespace.
	 */
	public AclsUsage getAclsUsage() {
		return aclsUsage;
	}

	public void setAclsUsage(AclsUsage aclsUsage) {
		this.aclsUsage = aclsUsage;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public OwnerType getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(OwnerType ownerType) {
		this.ownerType = ownerType;
	}

	public OptimizedFor getOptimizedFor() {
		return optimizedFor;
	}

	public void setOptimizedFor(OptimizedFor optimizedFor) {
		this.optimizedFor = optimizedFor;
	}

	public Integer getMultipartUploadAutoAbortDays() {
		return multipartUploadAutoAbortDays;
	}

	public void setMultipartUploadAutoAbortDays(Integer multipartUploadAutoAbortDays) {
		this.multipartUploadAutoAbortDays = multipartUploadAutoAbortDays;
	}

	public String getDpl() {
		return dpl;
	}

	// public void setDpl(String dpl) {
	// this.dpl = dpl;
	// }

	public HashScheme getHashScheme() {
		return hashScheme;
	}

	public void setHashScheme(HashScheme hashScheme) {
		this.hashScheme = hashScheme;
	}

	public Boolean getReplicationEnabled() {
		return replicationEnabled;
	}

	public void setReplicationEnabled(Boolean replicationEnabled) {
		this.replicationEnabled = replicationEnabled;
	}

	public Boolean getReadFromReplica() {
		return readFromReplica;
	}

	public void setReadFromReplica(Boolean readFromReplica) {
		this.readFromReplica = readFromReplica;
	}

	public Boolean getAuthUsersAlwaysGrantedAllPermissions() {
		return authUsersAlwaysGrantedAllPermissions;
	}

	public void setAuthUsersAlwaysGrantedAllPermissions(Boolean authUsersAlwaysGrantedAllPermissions) {
		this.authUsersAlwaysGrantedAllPermissions = authUsersAlwaysGrantedAllPermissions;
	}

	public List<Permission> getAuthMinimumPermissions() {
		return authMinimumPermissions;
	}

	public void setAuthMinimumPermissions(List<Permission> authMinimumPermissions) {
		this.authMinimumPermissions = authMinimumPermissions;
	}

	public List<Permission> getAuthAndAnonymousMinimumPermissions() {
		return authAndAnonymousMinimumPermissions;
	}

	public void setAuthAndAnonymousMinimumPermissions(List<Permission> authAndAnonymousMinimumPermissions) {
		this.authAndAnonymousMinimumPermissions = authAndAnonymousMinimumPermissions;
	}

	// is not returned by a GET request.
	// public VersioningSettings getVersioningSettings() {
	// return versioningSettings;
	// }

	public void setVersioningSettings(VersioningSettings versioningSettings) {
		this.versioningSettings = versioningSettings;
	}

	/**
	 * @return For an HCP namespace, specifies whether HCP is allowed to use erasure coding to implement replication of the objects in the
	 *         namespace.
	 */
	public Boolean getAllowErasureCoding() {
		return allowErasureCoding;
	}

	public void setAllowErasureCoding(Boolean allowErasureCoding) {
		this.allowErasureCoding = allowErasureCoding;
	}

}
