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
package com.hitachivantara.hcp.query.model.request;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.query.model.RequestBody;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;

public class QueryRequest<SUBCLASS, T extends RequestBody> extends HCPRequest {

	private T requestBody;

	public QueryRequest() {
		this(null);
	}

	public QueryRequest(T requestBody) {
		super(Method.POST);
		this.requestBody = requestBody;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfNull(requestBody, "Parameter requestBody must be specified.");

		requestBody.validParameter();
	}

	public T getRequestBody() {
		return requestBody;
	}

	/**
	 * @param requestBody
	 * @return
	 */
	public SUBCLASS withRequestBody(T requestBody) {
		this.requestBody = requestBody;
		return (SUBCLASS)this;
	}


	public SUBCLASS withNextPage() throws InvalidParameterException {
		validRequestParameter();

		requestBody.nextOffset();

		return (SUBCLASS)this;
	}

	public SUBCLASS withPrevPage() throws InvalidParameterException {
		validRequestParameter();

		requestBody.prevOffset();

		return (SUBCLASS)this;
	}

	public SUBCLASS resetOffset() {
		requestBody.resetOffset();

		return (SUBCLASS)this;
	}

	@Override
	public String getScope() {
		return null;
	}

	// public QueryRequest withDefaultTenant() {
	// return this.withTenant("default");
	// }
	//
	// public QueryRequest withAdminTenant() {
	// return this.withTenant("admin");
	// }
}
