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

import com.amituofo.common.api.Builder;
import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.hcp.common.HCPClient;
import com.hitachivantara.hcp.common.auth.Credentials;

public abstract class HCPClientBuilder<BUILDER, CLI> implements Builder<CLI, HSCException> {
	protected String endpoint;

	protected Credentials credentials = null;
	protected ClientConfiguration clientConfiguration = null;

	protected HCPClientBuilder() {
	}

	protected abstract CLI newInstance() throws HSCException;

	protected void validate() throws InvalidParameterException {
		
	}

	public CLI bulid() throws HSCException, InvalidParameterException {
		this.validate();
		CLI client = this.newInstance();
		((HCPClient)client).initialize();
		return client;
	}

	/**
	 * @param endpoint
	 * @return
	 */
	public BUILDER withEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return (BUILDER) this;
	}

	public BUILDER withCredentials(Credentials credentials) {
		this.credentials = credentials;
		return (BUILDER) this;
	}

	public BUILDER withClientConfiguration(ClientConfiguration clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
		return (BUILDER) this;
	}

	public static HCPQueryClientBuilder queryClient() {
		return new HCPQueryClientBuilder();
	}

	public static HCPTenantManagementClientBuilder tenantManagementClient() {
		return new HCPTenantManagementClientBuilder();
	}

	public static HCPSystemManagementClientBuilder systemManagementClient() {
		return new HCPSystemManagementClientBuilder();
	}

	public static HCPNamespaceClientBuilder defaultHCPClient() {
		return new HCPNamespaceClientBuilder();
	}

	// Open later
	// public static HCPStandardClientBuilder s3CompatibleClient() {
	// return new HCPStandardClientBuilder() {
	//
	// @Override
	// protected HCPStandardClient newInstance() {
	// return new S3CompatibleClient(this.getNamespace(), endpoint, credentials, clientConfiguration, this.getKeyAlgorithm());
	// }
	// };
	// }

}
