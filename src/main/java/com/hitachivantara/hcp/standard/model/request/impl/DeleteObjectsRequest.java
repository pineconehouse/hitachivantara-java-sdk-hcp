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
package com.hitachivantara.hcp.standard.model.request.impl;

import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;

/**
 * @author sohan
 *
 */
public class DeleteObjectsRequest extends HCPStandardRequest<DeleteObjectsRequest> {
	private List<String> keys = new ArrayList<String>();

	public DeleteObjectsRequest() {
		super(Method.POST);
	}

	public DeleteObjectsRequest withKeys(List<String> keys) {
		this.keys.addAll(keys);

		return this;
	}

	public List<String> getKeys() {
		return keys;
	}

	@Override
	public DeleteObjectsRequest withKey(String key) {
		keys.add(key);
		return super.withKey(key);
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfEmpty(keys, "Parameter keys must be specified.");

	}

}
