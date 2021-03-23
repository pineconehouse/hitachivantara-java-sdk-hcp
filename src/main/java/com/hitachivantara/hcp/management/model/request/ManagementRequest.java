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

import java.io.UnsupportedEncodingException;

import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;

public abstract class ManagementRequest extends HCPRequest {
	protected final static String KEY_TENANTS = "tenants";
	protected final static String KEY_NAMESPACES = "namespaces";
	protected final static String KEY_USER_ACCOUNTS = "userAccounts";
	
	public ManagementRequest(Method method) {
		super(method);
	}

	public abstract String getResourceIdentifier();
	
	public abstract String buildRequestXMLBody() throws UnsupportedEncodingException;
	
	@Override
	public String getScope() {
		return null;
	}

}
