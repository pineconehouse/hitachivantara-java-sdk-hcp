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
package com.hitachivantara.hcp.query.body;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.content.StringEntity;
import com.hitachivantara.hcp.common.AbstractHCPClient;
import com.hitachivantara.hcp.common.HCPHttpClient;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.common.define.HCPRequestHeadValue.CONTENT_TYPE;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.query.api.HCPQuery;
import com.hitachivantara.hcp.query.model.ObjectQueryResult;
import com.hitachivantara.hcp.query.model.ObjectSummary;
import com.hitachivantara.hcp.query.model.OperationQueryResult;
import com.hitachivantara.hcp.query.model.converter.QueryResponseHandler;
import com.hitachivantara.hcp.query.model.request.ObjectBasedQueryRequest;
import com.hitachivantara.hcp.query.model.request.OperationBasedQueryRequest;
import com.hitachivantara.hcp.standard.model.request.HCPRequestHeaders;

public class HCPQueryClient extends AbstractHCPClient implements HCPQuery {

	public HCPQueryClient(String endpoint, Credentials credentials, ClientConfiguration clientConfiguration) {
		super(endpoint, credentials, clientConfiguration);
	}

	@Override
	protected HCPHttpClient createDefaultClient() {
		return new HCPHttpQueryClient(this.getEndpoint(), credentials, clientConfiguration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.query.api.ObjectQuery#query(com.hitachivantara.hcp.query.model.request.ObjectBasedQueryRequest)
	 */
	public ObjectQueryResult query(ObjectBasedQueryRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		// HCPRequestParams param = request.parameter();

		// param.setParameter(RequestParamKey.PRETTYPRINT, null);

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		try {
			request.setEntity(new StringEntity(request.getRequestBody().build()));
		} catch (UnsupportedEncodingException e) {
			throw new HSCException(e);
		}

		ObjectQueryResult result = execute(request, QueryResponseHandler.OBJECT_QUERY_RESPONSE_HANDLER);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.query.api.ObjectQuery#query(com.hitachivantara.hcp.query.model.request.OperationBasedQueryRequest)
	 */
	public OperationQueryResult query(OperationBasedQueryRequest request) throws InvalidResponseException, HSCException {
		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		// HCPRequestParams param = request.parameter();

		// param.setParameter(RequestParamKey.PRETTYPRINT, null);

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		try {
			request.setEntity(new StringEntity(request.getRequestBody().build()));
		} catch (UnsupportedEncodingException e) {
			throw new HSCException(e);
		}

		OperationQueryResult result = execute(request, QueryResponseHandler.OPERATION_QUERY_RESPONSE_HANDLER);

		if (result.isIncomplete()) {
			List<ObjectSummary> results = result.getResults();
			ObjectSummary lastOne = results.get(results.size() - 1);
			request.getRequestBody().setLastResult(result.getStatus().getResults(), lastOne.getUrlName(), lastOne.getChangeTimeMilliseconds(), lastOne.getVersionId());
			// } else {
			// request.getRequestBody().setLastResult(null, null, null);
		}

		return result;
	}
}
