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
package com.hitachivantara.hcp.management.model.request;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.common.util.ValidUtils;

public abstract class TenantResourceRequest<T extends TenantResourceRequest<T>> extends ManagementRequest {
	private String tenant;

	public TenantResourceRequest(Method method) {
		super(method);
	}

	public TenantResourceRequest(Method method, String tenant) {
		super(method);
		this.tenant = tenant;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfEmpty(tenant, "Tenant must be specified.");
	}

	public T withTenant(String tenant) {
		this.tenant = tenant;
		return (T) this;
	}

	public String getTenant() {
		return tenant;
	}

}
