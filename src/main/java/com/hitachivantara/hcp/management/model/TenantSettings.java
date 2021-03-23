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

import com.hitachivantara.hcp.management.define.AuthenticationType;
import com.hitachivantara.hcp.management.define.QuotaUnit;

public class TenantSettings {
	private 	String id;
	private 	String creationTime;
	private 	String fullyQualifiedName;
	private 	String name;
	private 	String systemVisibleDescription;
	private 	String tenantVisibleDescription;
	private 	Integer softQuota;
	private 	double hardQuota;
	private 	QuotaUnit hardQuotaUnit;
	private 	String namespaceQuota;
	private 	AuthenticationType[] authenticationTypes;
	private 	List<String> tags;
	private 	String managementNetwork;
	private 	String dataNetwork;
	private 	Boolean searchConfigurationEnabled;
	private 	Boolean replicationConfigurationEnabled;
	private 	Boolean complianceConfigurationEnabled;
	private 	Boolean versioningConfigurationEnabled;
	private 	Boolean servicePlanSelectionEnabled;
	private 	String servicePlan;
	private 	Boolean erasureCodingSelectionEnabled;
	private 	Boolean administrationAllowed;
	private 	Integer maxNamespacesPerUser;
	private 	Boolean snmpLoggingEnabled;
	private 	Boolean syslogLoggingEnabled;
	
	public TenantSettings() {
		// TODO Auto-generated constructor stub
	}

	public TenantSettings(String id,
			String creationTime,
			String fullyQualifiedName,
			String name,
			String systemVisibleDescription,
			String tenantVisibleDescription,
			Integer softQuota,
			double hardQuota,
			QuotaUnit hardQuotaUnit,
			String namespaceQuota,
			AuthenticationType[] authenticationTypes,
			List<String> tags,
			String managementNetwork,
			String dataNetwork,
			Boolean searchConfigurationEnabled,
			Boolean replicationConfigurationEnabled,
			Boolean complianceConfigurationEnabled,
			Boolean versioningConfigurationEnabled,
			Boolean servicePlanSelectionEnabled,
			String servicePlan,
			Boolean erasureCodingSelectionEnabled,
			Boolean administrationAllowed,
			Integer maxNamespacesPerUser,
			Boolean snmpLoggingEnabled,
			Boolean syslogLoggingEnabled) {
		super();
		this.id = id;
		this.creationTime = creationTime;
		this.fullyQualifiedName = fullyQualifiedName;
		this.name = name;
		this.systemVisibleDescription = systemVisibleDescription;
		this.tenantVisibleDescription = tenantVisibleDescription;
		this.softQuota = softQuota;
		this.hardQuota = hardQuota;
		this.hardQuotaUnit = hardQuotaUnit;
		this.namespaceQuota = namespaceQuota;
		this.authenticationTypes = authenticationTypes;
		this.tags = tags;
		this.managementNetwork = managementNetwork;
		this.dataNetwork = dataNetwork;
		this.searchConfigurationEnabled = searchConfigurationEnabled;
		this.replicationConfigurationEnabled = replicationConfigurationEnabled;
		this.complianceConfigurationEnabled = complianceConfigurationEnabled;
		this.versioningConfigurationEnabled = versioningConfigurationEnabled;
		this.servicePlanSelectionEnabled = servicePlanSelectionEnabled;
		this.servicePlan = servicePlan;
		this.erasureCodingSelectionEnabled = erasureCodingSelectionEnabled;
		this.administrationAllowed = administrationAllowed;
		this.maxNamespacesPerUser = maxNamespacesPerUser;
		this.snmpLoggingEnabled = snmpLoggingEnabled;
		this.syslogLoggingEnabled = syslogLoggingEnabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystemVisibleDescription() {
		return systemVisibleDescription;
	}

	public void setSystemVisibleDescription(String systemVisibleDescription) {
		this.systemVisibleDescription = systemVisibleDescription;
	}

	public Integer getSoftQuota() {
		return softQuota;
	}

	public void setSoftQuota(Integer softQuota) {
		this.softQuota = softQuota;
	}

	public double getHardQuota() {
		return hardQuota;
	}

	public void setHardQuota(double hardQuota) {
		this.hardQuota = hardQuota;
	}

	public QuotaUnit getHardQuotaUnit() {
		return hardQuotaUnit;
	}

	public void setHardQuotaUnit(QuotaUnit hardQuotaUnit) {
		this.hardQuotaUnit = hardQuotaUnit;
	}

	public String getNamespaceQuota() {
		return namespaceQuota;
	}

	public void setNamespaceQuota(String namespaceQuota) {
		this.namespaceQuota = namespaceQuota;
	}

	public AuthenticationType[] getAuthenticationTypes() {
		return authenticationTypes;
	}

	public void setAuthenticationTypes(AuthenticationType[] authenticationTypes) {
		this.authenticationTypes = authenticationTypes;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getManagementNetwork() {
		return managementNetwork;
	}

	public void setManagementNetwork(String managementNetwork) {
		this.managementNetwork = managementNetwork;
	}

	public String getDataNetwork() {
		return dataNetwork;
	}

	public void setDataNetwork(String dataNetwork) {
		this.dataNetwork = dataNetwork;
	}

	public Boolean getSearchConfigurationEnabled() {
		return searchConfigurationEnabled;
	}

	public void setSearchConfigurationEnabled(Boolean searchConfigurationEnabled) {
		this.searchConfigurationEnabled = searchConfigurationEnabled;
	}

	public Boolean getReplicationConfigurationEnabled() {
		return replicationConfigurationEnabled;
	}

	public void setReplicationConfigurationEnabled(Boolean replicationConfigurationEnabled) {
		this.replicationConfigurationEnabled = replicationConfigurationEnabled;
	}

	public Boolean getComplianceConfigurationEnabled() {
		return complianceConfigurationEnabled;
	}

	public void setComplianceConfigurationEnabled(Boolean complianceConfigurationEnabled) {
		this.complianceConfigurationEnabled = complianceConfigurationEnabled;
	}

	public Boolean getVersioningConfigurationEnabled() {
		return versioningConfigurationEnabled;
	}

	public void setVersioningConfigurationEnabled(Boolean versioningConfigurationEnabled) {
		this.versioningConfigurationEnabled = versioningConfigurationEnabled;
	}

	public Boolean getServicePlanSelectionEnabled() {
		return servicePlanSelectionEnabled;
	}

	public void setServicePlanSelectionEnabled(Boolean servicePlanSelectionEnabled) {
		this.servicePlanSelectionEnabled = servicePlanSelectionEnabled;
	}

	public String getServicePlan() {
		return servicePlan;
	}

	public void setServicePlan(String servicePlan) {
		this.servicePlan = servicePlan;
	}

	public Boolean getErasureCodingSelectionEnabled() {
		return erasureCodingSelectionEnabled;
	}

	public void setErasureCodingSelectionEnabled(Boolean erasureCodingSelectionEnabled) {
		this.erasureCodingSelectionEnabled = erasureCodingSelectionEnabled;
	}

	public Boolean getAdministrationAllowed() {
		return administrationAllowed;
	}

	public void setAdministrationAllowed(Boolean administrationAllowed) {
		this.administrationAllowed = administrationAllowed;
	}

	public Integer getMaxNamespacesPerUser() {
		return maxNamespacesPerUser;
	}

	public void setMaxNamespacesPerUser(Integer maxNamespacesPerUser) {
		this.maxNamespacesPerUser = maxNamespacesPerUser;
	}

	public Boolean getSnmpLoggingEnabled() {
		return snmpLoggingEnabled;
	}

	public void setSnmpLoggingEnabled(Boolean snmpLoggingEnabled) {
		this.snmpLoggingEnabled = snmpLoggingEnabled;
	}

	public Boolean getSyslogLoggingEnabled() {
		return syslogLoggingEnabled;
	}

	public void setSyslogLoggingEnabled(Boolean syslogLoggingEnabled) {
		this.syslogLoggingEnabled = syslogLoggingEnabled;
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

	public String getTenantVisibleDescription() {
		return tenantVisibleDescription;
	}


}
