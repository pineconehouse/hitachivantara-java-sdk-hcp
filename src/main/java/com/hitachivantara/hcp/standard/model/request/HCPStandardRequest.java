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

public abstract class HCPStandardRequest<T extends HCPStandardRequest<?>> extends HCPRequest {

	private String key = null;
	private String namespace = null;

	public HCPStandardRequest(Method method) {
		super(method);
	}

	public HCPStandardRequest(Method method, String key) {
		super(method);
		this.key = key;
	}

	public HCPStandardRequest(Method method, String namespace, String key) {
		super(method);
		this.key = key;
		this.namespace = namespace;
	}

	/**
	 * Directory and object names are case sensitive. The separator is the forward slash (/).
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T withKey(String key) {
		this.key = key;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withNamespace(String namespace) {
		this.namespace = namespace;
		return (T) this;
	}

	public String getKey() {
		return key;
	}

	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getScope() {
		return namespace;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void validParameter() throws InvalidParameterException {
		ValidUtils.invalidIfEmpty(key, "Parameter key must be specified.");
		ValidUtils.invalidIfContains(key, new String[] { "://" }, "The format of key is invalid.");
		// ValidUtils.exceptionIfContainsChar(key, "`~!", "Key contains invalid char.");

		this.validRequestParameter();
	}
}
