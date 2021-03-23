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
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.HCPNamespace;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.body.impl.HCPNamespaceImpl;

public class HCPNamespaceClientBuilder extends HCPClientBuilder<HCPNamespaceClientBuilder, HCPNamespace> {
	private String namespace;
	private KeyAlgorithm keyAlgorithm = null;// KeyAlgorithm.DEFAULT;

	public HCPNamespaceClientBuilder withKeyAlgorithm(KeyAlgorithm keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
		return this;
	}

	/**
	 * To query one or more namespaces owned by an HCP tenant. To use this format, you need either a tenant-level user account or, if the tenant
	 * has granted system-level users administrative access to itself, a system-level user account. In either case, the account must be
	 * configured to allow use of the metadata query API.
	 * 
	 * @param endpoint
	 *            To query only the default namespace, use this format: default.hcp-domain-name
	 *            <p>
	 *            To query the entire repository, use this format:admin.hcp-domain-name
	 * @return
	 */
	@Override
	public HCPNamespaceClientBuilder withEndpoint(String endpoint) {
		return super.withEndpoint(endpoint);
	}

	public HCPNamespaceClientBuilder withNamespace(String namespace) {
		this.namespace = namespace;
		return (HCPNamespaceClientBuilder) this;
	}

	public KeyAlgorithm getKeyAlgorithm() {
		return keyAlgorithm;
	}

	public String getNamespace() {
		return namespace;
	}
	
	@Override
	protected HCPNamespace newInstance() throws InvalidResponseException, HSCException {
		return new HCPNamespaceImpl(this.getNamespace(), endpoint, credentials, clientConfiguration, this.getKeyAlgorithm());
	}

	@Override
	protected void validate() throws InvalidParameterException {
		// TODO Auto-generated method stub
		
	}

}
