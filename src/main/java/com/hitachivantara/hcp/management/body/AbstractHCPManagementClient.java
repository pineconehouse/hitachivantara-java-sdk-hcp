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
package com.hitachivantara.hcp.management.body;

import java.io.UnsupportedEncodingException;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Protocol;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.content.StringEntity;
import com.hitachivantara.hcp.common.AbstractHCPClient;
import com.hitachivantara.hcp.common.HCPHttpClient;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.management.model.request.ManagementRequest;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;

public abstract class AbstractHCPManagementClient extends AbstractHCPClient {

	protected String tenant;

	public AbstractHCPManagementClient(String tenant, String endpoint, Credentials credentials, ClientConfiguration clientConfiguration) {
		super(tenant + "." + endpoint, credentials, clientConfiguration);
		this.tenant = tenant;
	}

	@Override
	public void initialize() throws HSCException, InvalidParameterException {
		super.initialize();

		ValidUtils.invalidIfNull(tenant, "Parameter tenant must be specificed.");
	}

	@Override
	protected HCPHttpClient createDefaultClient() {
		// The management API requires the use of SSL security with HTTP (HTTPS).
		clientConfiguration.setProtocol(Protocol.HTTPS);
		
		return new HCPHttpMapiClient(this.getEndpoint(), credentials, clientConfiguration);
	}

	protected <T> T execute(ManagementRequest request, ResponseHandler<T> handler) throws InvalidResponseException, HSCException {
		try {
			String xmlBody = request.buildRequestXMLBody();
			if (xmlBody != null) {
				// System.out.println(xmlBody);
				request.setEntity(new StringEntity(request.buildRequestXMLBody()));
			}
		} catch (UnsupportedEncodingException e) {
			throw new HSCException(e);
		}

		T result = super.execute(request, handler);
		return result;
	}

}
