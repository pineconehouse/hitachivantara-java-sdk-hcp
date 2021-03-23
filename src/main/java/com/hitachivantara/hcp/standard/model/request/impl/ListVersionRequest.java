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

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithDeletedObject;

/**
 * @author sohan
 *
 */
public class ListVersionRequest extends HCPStandardRequest<ListVersionRequest> implements ReqWithDeletedObject<ListVersionRequest> {
	private boolean includeDeletedObject = false;

	public ListVersionRequest() {
		super(Method.GET);
	}

	public ListVersionRequest(String key) {
		super(Method.GET, key);
	}

	public ListVersionRequest withDeletedObject(boolean with) {
		this.includeDeletedObject = with;
		return this;
	}

	public boolean isWithDeletedObject() {
		return includeDeletedObject;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}

}
