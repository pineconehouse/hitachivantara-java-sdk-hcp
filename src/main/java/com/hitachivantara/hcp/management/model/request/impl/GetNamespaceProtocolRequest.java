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
package com.hitachivantara.hcp.management.model.request.impl;

import java.io.UnsupportedEncodingException;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.management.define.Protocols;
import com.hitachivantara.hcp.management.model.request.NamespaceResourceRequest;

public class GetNamespaceProtocolRequest extends NamespaceResourceRequest<GetNamespaceProtocolRequest> {
	protected final static String KEY_PROTOCOLS = "protocols";

	private Protocols protocol = null;

	public GetNamespaceProtocolRequest() {
		super(Method.GET);
	}

	public GetNamespaceProtocolRequest(String tenant, String namespace) {
		super(Method.GET, tenant, namespace);
	}

	public GetNamespaceProtocolRequest withProtocol(Protocols protocol) {
		this.protocol = protocol;
		return this;
	}

	@Override
	public String getResourceIdentifier() {
		return URLUtils.simpleCatPaths(KEY_TENANTS, this.getTenant(), KEY_NAMESPACES, this.getNamespace(), KEY_PROTOCOLS, protocol.name());
	}

	@Override
	public String buildRequestXMLBody() throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		super.validRequestParameter();

		ValidUtils.invalidIfNull(protocol, "Namespace Protocol must be specified.");
	}

}
