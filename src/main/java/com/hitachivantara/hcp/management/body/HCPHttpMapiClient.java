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

import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.hcp.common.HCPHttpClient;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.management.model.request.ManagementRequest;

public class HCPHttpMapiClient extends HCPHttpClient<ManagementRequest> {

	public HCPHttpMapiClient(String endpoint, Credentials credentials, ClientConfiguration configuration) {
		super(null, endpoint, 9090, "/mapi", credentials, configuration);
	}

	@Override
	protected String generateServicePoint(ManagementRequest request) throws Exception {
		return request.getResourceIdentifier();
	}

}
