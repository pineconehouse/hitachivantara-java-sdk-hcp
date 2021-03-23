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
package com.hitachivantara.hcp.standard.model.request;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.common.util.ValidUtils;

public abstract class NamespaceInfoRequest<T extends NamespaceInfoRequest> extends HCPRequest {
	private String namespace;

	public NamespaceInfoRequest(Method method, String namespace) {
		super(method);
		this.namespace = namespace;
	}
	
	public abstract String getResourceIdentifier();

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfEmpty(namespace, "Namespace must be specified.");
	}

	@Override
	public String getScope() {
		return namespace;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public T withNamespace(String namespace) {
		this.namespace = namespace;
		return (T) this;
	}

	public String getNamespace() {
		return namespace;
	}
	
	
}
