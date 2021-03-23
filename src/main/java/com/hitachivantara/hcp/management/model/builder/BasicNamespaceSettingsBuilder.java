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
package com.hitachivantara.hcp.management.model.builder;

import java.util.ArrayList;
import java.util.Arrays;

import com.amituofo.common.api.Builder;
import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.management.define.AclsUsage;
import com.hitachivantara.hcp.management.define.OptimizedFor;
import com.hitachivantara.hcp.management.define.OwnerType;
import com.hitachivantara.hcp.management.define.Permission;
import com.hitachivantara.hcp.management.define.QuotaUnit;
import com.hitachivantara.hcp.management.model.NamespaceSettings;
import com.hitachivantara.hcp.management.model.VersioningSettings;

public class BasicNamespaceSettingsBuilder<T extends BasicNamespaceSettingsBuilder<T>> implements Builder<NamespaceSettings, HSCException> {
	NamespaceSettings ns = new NamespaceSettings();
	VersioningSettings versioningSettings = new VersioningSettings();

	public BasicNamespaceSettingsBuilder() {
	}

	/**
	 * 
	 * Specifies the name of the namespace. HCP derives the hostname for the namespace from this name. The hostname is used in URLs for access
	 * to the namespace.
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
	 * You can reuse namespace names that are not currently in use. So, for example, if you delete a namespace, you can give a new namespace the
	 * same name as the one the deleted namespace had.
	 * 
	 * The name of the default namespace is always Default.
	 * 
	 * @param name
	 * @return
	 */
	public T withName(String name) {
		ns.setName(name);
		return (T) this;
	}

	public T withHardQuota(double hardQuota, QuotaUnit hardQuotaUnit) {
		ns.setHardQuota(hardQuota, hardQuotaUnit);
		return (T) this;
	}

	public T withSoftQuota(int softQuota) {
		ns.setSoftQuota(softQuota);
		return (T) this;
	}
	
	public T withReplicationEnabled(boolean replicationEnabled) {
		ns.setReplicationEnabled(replicationEnabled);
		return (T) this;
	}

	public T withSearchEnabled(boolean searchEnabled) {
		ns.setSearchEnabled(searchEnabled);
		return (T) this;
	}

	public T withOptimizedFor(OptimizedFor optimizedFor) {
		ns.setOptimizedFor(optimizedFor);
		return (T) this;
	}

	public T withDescription(String description) {
		ns.setDescription(description);
		return (T) this;
	}

	public T withServicePlan(String servicePlan) {
		ns.setServicePlan(servicePlan);
		return (T) this;
	}

	public T withOwner(OwnerType type, String owner) {
		ns.setOwnerType(type);
		ns.setOwner(owner);
		return (T) this;
	}

	public T withAclsUsage(AclsUsage aclsUsage) {
		ns.setAclsUsage(aclsUsage);
		return (T) this;
	}

	public T withCustomMetadataIndexingEnabled(boolean customMetadataIndexingEnabled) {
		ns.setCustomMetadataIndexingEnabled(customMetadataIndexingEnabled);
		return (T) this;
	}

	public T withCustomMetadataValidationEnabled(boolean customMetadataValidationEnabled) {
		ns.setCustomMetadataValidationEnabled(customMetadataValidationEnabled);
		return (T) this;
	}

	public T withMultipartUploadAutoAbortDays(int multipartUploadAutoAbortDays) {
		ns.setMultipartUploadAutoAbortDays(multipartUploadAutoAbortDays);
		return (T) this;
	}

	public T withIndexingEnabled(boolean enabled) {
		ns.setIndexingEnabled(enabled);
		return (T) this;
	}

	public T withTags(String... tags) {
		ns.setTags(Arrays.asList(tags));
		return (T) this;
	}

	public T withAuthMinimumPermissions(Permission... permissions) {
		ns.setAuthMinimumPermissions(Arrays.asList(permissions));
		return (T) this;
	}

	public T withAuthAndAnonymousMinimumPermissions(Permission... permissions) {
		ns.setAuthAndAnonymousMinimumPermissions(Arrays.asList(permissions));
		return (T) this;
	}
	
	public T withoutTags() {
		ns.setTags(new ArrayList<String>());
		return (T) this;
	}
	
	public T withoutAuthMinimumPermissions() {
		ns.setAuthMinimumPermissions(new ArrayList<Permission>());
		return (T) this;
	}

	public T withoutAuthAndAnonymousMinimumPermissions() {
		ns.setAuthAndAnonymousMinimumPermissions(new ArrayList<Permission>());
		return (T) this;
	}
	
	@Override
	public NamespaceSettings bulid() throws HSCException, InvalidParameterException {
		ValidUtils.invalidIfEmpty(ns.getName(), "Need to specifies the name of the namespace.");
		return ns;
	}

}
