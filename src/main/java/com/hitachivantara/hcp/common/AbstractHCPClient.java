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
package com.hitachivantara.hcp.common;

import java.io.IOException;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;

public abstract class AbstractHCPClient implements HCPClient {
	protected String endpoint;

	protected Credentials credentials = null;
	protected ClientConfiguration clientConfiguration = null;
	
	private HCPHttpClient client;

	public AbstractHCPClient(String endpoint, Credentials credentials, ClientConfiguration clientConfiguration) {
		super();
		this.endpoint = endpoint;
		this.credentials = credentials;
		this.clientConfiguration = clientConfiguration;
	}

	@Override
	public void close() throws IOException {
		client.close();
	}

	public void initialize() throws HSCException, InvalidParameterException {
		ValidUtils.invalidIfNull(this.getEndpoint(), "Parameter endpoint must be specificed.");
		ValidUtils.invalidIfContains(this.getEndpoint(),
				new String[] { "http", ":", "/", "\\", " " },
				"Format of parameter endpoint is invalid. Example: \"tenant1.hcp1.hcpdemo.com\"");
		ValidUtils.invalidIfNull(this.getCredentials(), "Parameter credentials must be specificed.");

		this.client = createDefaultClient();
}
	
	protected abstract HCPHttpClient createDefaultClient() throws HSCException, InvalidParameterException;

	protected <T> T execute(HCPRequest request, ResponseHandler<T> handler) throws InvalidResponseException {
		T result = (T) client.execute(request, handler);
		return result;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public ClientConfiguration getClientConfiguration() {
		return clientConfiguration;
	}
}
