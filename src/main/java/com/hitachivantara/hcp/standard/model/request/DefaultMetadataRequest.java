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
import com.hitachivantara.hcp.standard.model.request.content.ReqWithMetadataNames;

public abstract class DefaultMetadataRequest<T extends DefaultMetadataRequest<?>> extends HCPStandardRequest<T> implements ReqWithMetadataNames<T> {
	private String[] metadataNames = null;

	public DefaultMetadataRequest(Method method) {
		super(method);
	}

	public DefaultMetadataRequest(Method method, String key) {
		super(method, key);
	}

	public DefaultMetadataRequest(Method method, String key, String metadataName) {
		super(method, key);
		this.metadataNames = new String[] { metadataName };
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithMetadataNames#withMetadataName(java.lang.String)
	 */
	public T withMetadataName(String metadataName) {
		this.metadataNames = new String[] { metadataName };
		return (T) this;
	}

	public T withMetadataNames(String... metadataNames) {
		this.metadataNames = metadataNames;
		return (T) this;
	}
	
	public String[] getMetadataNames() {
		return metadataNames;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfNull(metadataNames, "Parameter metadata name must be specified.");
		for (String metadataName : metadataNames) {
			ValidUtils.invalidIfEmpty(metadataName, "Parameter metadata name must be specified.");
		}
	}

}
