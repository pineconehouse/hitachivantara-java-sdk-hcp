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

public class VersioningSettings {

	/**
	 * Specifies whether versioning is enabled for the namespace. Valid values are:
	 * 
	 * •true — Versioning is enabled.
	 * 
	 * •false — Versioning is disabled.
	 * 
	 * The default is false.
	 * 
	 * This property is required on a PUT request to create a namespace and on a POST request to modify namespace defaults if the request
	 * includes the versioningSettings property.
	 * 
	 * You cannot enable versioning for a namespace while the CIFS, NFS, WebDAV, or SMTP protocol or appendable objects are enabled.
	 */
	private Boolean enabled;

	/**
	 * Specifies whether HCP should keep records of deletion
	 * 
	 * operations (delete, purge, prune, disposition) that occur in the namespace if the namespace has ever had versioning enabled. Valid values
	 * are:
	 * 
	 * •true — Keep records of deletion operations.
	 * 
	 * •false — Do not keep records of deletion operations.
	 * 
	 * The default is true.
	 * 
	 * The amount of time for which HCP keeps deletion records is determined by the system configuration.
	 * 
	 * This property is not valid on a POST request to modify namespace defaults and is not returned by any GET request for namespace defaults.
	 */
	private Boolean keepDeletionRecords;

	/**
	 * Specifies whether version pruning is enabled for the namespace. Valid values are:
	 * 
	 * •true— Version pruning is enabled for the namespace.
	 * 
	 * •false— Version pruning is disabled for the namespace.
	 * 
	 * The default is false.
	 * 
	 * This property is required on a PUT request to create a namespace and on a POST request to modify namespace defaults if the enabled
	 * property is set to true.
	 * 
	 * You cannot include both this property and the pruneOnPrimary property in the same request.
	 */
	private Boolean prune;

	/**
	 * Specifies the number of days old versions of objects must remain in the namespace before they are pruned. Valid values are integers in
	 * the range zero through 36,500( that is,100 years). A value of zero means prune immediately.
	 * 
	 * The default is zero.
	 * 
	 * This property is required on a PUT request to create a namespace and on a POST request to modify namespace defaults if the prune property
	 * is set to true.
	 * 
	 * You cannot include both this property and the daysOnPrimary property in the same request.
	 */
	private Integer pruneDays;

	public VersioningSettings() {
	}

	public VersioningSettings(Boolean enabled, Boolean keepDeletionRecords, Boolean prune, Integer pruneDays) {
		super();
		this.enabled = enabled;
		this.keepDeletionRecords = keepDeletionRecords;
		this.prune = prune;
		this.pruneDays = pruneDays;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Boolean getKeepDeletionRecords() {
		return keepDeletionRecords;
	}

	public Boolean getPrune() {
		return prune;
	}

	public Integer getPruneDays() {
		return pruneDays;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setKeepDeletionRecords(Boolean keepDeletionRecords) {
		this.keepDeletionRecords = keepDeletionRecords;
	}

	public void setPrune(Boolean prune) {
		this.prune = prune;
	}

	public void setPruneDays(Integer pruneDays) {
		this.pruneDays = pruneDays;
	}

}
