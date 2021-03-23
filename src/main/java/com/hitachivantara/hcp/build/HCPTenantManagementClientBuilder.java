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
package com.hitachivantara.hcp.build;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.management.api.HCPTenantManagement;
import com.hitachivantara.hcp.management.body.HCPTenantManagementClient;

public class HCPTenantManagementClientBuilder extends HCPClientBuilder<HCPTenantManagementClientBuilder, HCPTenantManagement> {
	protected String tenant;
//	protected String domain;

	public HCPTenantManagementClientBuilder() {
		super();
	}

	/**
	 * @param tenant
	 * @return
	 */
	public HCPTenantManagementClientBuilder withTenant(String tenant) {
		this.tenant = tenant;
		return (HCPTenantManagementClientBuilder) this;
	}

	/**
	 * @param domain
	 * @return
	 * @throws HSCException 
	 * @throws InvalidParameterException 
	 */
//	public HCPNamespaceManagementClientBuilder withDomain(String domain) {
//		this.domain = domain;
//		return (HCPNamespaceManagementClientBuilder) this;
//	}

	@Override
	protected HCPTenantManagement newInstance() throws InvalidParameterException, HSCException {
		return new HCPTenantManagementClient(tenant, endpoint, credentials, clientConfiguration);
	}

	@Override
	protected void validate() throws InvalidParameterException {
		// ValidUtils.exceptionWhenAllEmpty("Error", endpoint, domain, tenant);
	}
}
