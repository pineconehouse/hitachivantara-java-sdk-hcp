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
package com.hitachivantara.hcp.management.body;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.common.define.HCPRequestHeadValue.CONTENT_TYPE;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.management.api.HCPSystemManagement;
import com.hitachivantara.hcp.management.define.Granularity;
import com.hitachivantara.hcp.management.model.ContentStatistics;
import com.hitachivantara.hcp.management.model.TenantChargebackReport;
import com.hitachivantara.hcp.management.model.TenantSettings;
import com.hitachivantara.hcp.management.model.converter.MapiResponseHandler;
import com.hitachivantara.hcp.management.model.request.impl.CheckTenantRequest;
import com.hitachivantara.hcp.management.model.request.impl.DeleteTenantRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetTenantChargebackReportRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetTenantRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetTenantStatisticsRequest;
import com.hitachivantara.hcp.management.model.request.impl.ListTenantsRequest;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.define.RequestParamKey;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;
import com.hitachivantara.hcp.standard.model.request.HCPRequestHeaders;
import com.hitachivantara.hcp.standard.model.request.HCPRequestParams;

public class HCPSystemManagementClient extends AbstractHCPManagementClient implements HCPSystemManagement {

	public HCPSystemManagementClient(String tenant, String endpoint, Credentials credentials, ClientConfiguration clientConfiguration) {
		super(tenant, endpoint, credentials, clientConfiguration);
	}

	@Override
	public ContentStatistics getTenantStatistics(String tenant) throws InvalidResponseException, HSCException {
		GetTenantStatisticsRequest request = new GetTenantStatisticsRequest(tenant);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		param.setParameter(RequestParamKey.PRETTYPRINT, null);

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		ContentStatistics result = execute(request, MapiResponseHandler.GET_NAMESPACE_STATISTICS_RESPONSE_HANDLER);

		return result;
	}

	public List<TenantChargebackReport> getTenantChargebackReport(String tenant, Date start, Date end, Granularity granularity) throws InvalidResponseException, HSCException {
		GetTenantChargebackReportRequest request = new GetTenantChargebackReportRequest(tenant);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		if (start != null) {
			param.setParameter(RequestParamKey.START_DATETIME, start, false);
		}
		if (end != null) {
			param.setParameter(RequestParamKey.END_DATETIME, end, false);
		}
		if (granularity != null) {
			param.setParameter(RequestParamKey.GRANULARITY, granularity);
		}
		param.setParameter(RequestParamKey.PRETTYPRINT, null);

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_XML);

		// TODO
		List<TenantChargebackReport> result = execute(request, new ResponseHandler<List<TenantChargebackReport>>() {

			@Override
			public List<TenantChargebackReport> handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
				try {
					StreamUtils.inputStreamToConsole(response.getEntity().getContent(), true);
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public boolean isValidResponse(HttpResponse response) {
				return true;
			}
		});

		return result;
	}
	
	@Override
	public String[] listTenants() throws InvalidResponseException, HSCException {
		ListTenantsRequest request = new ListTenantsRequest();

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		String[] result = execute(request, MapiResponseHandler.LIST_TENANTS_RESPONSE_HANDLER);
		return result;
	}

	@Override
	public TenantSettings getTenantSettings(String tenant) throws InvalidResponseException, HSCException {
		GetTenantRequest request = new GetTenantRequest(tenant);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		// param.setParameter(RequestParamKey.PRETTYPRINT, null);
		param.enableParameter(RequestParamKey.VERBOSE);

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		TenantSettings result = execute(request, MapiResponseHandler.GET_TENANT_SETTING_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public boolean doesTenantExist(String tenant) throws InvalidResponseException, HSCException {
		CheckTenantRequest request = new CheckTenantRequest(tenant);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		Boolean result = execute(request, MapiResponseHandler.CHECK_TENANT_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public boolean deleteTenant(String tenant) throws InvalidResponseException, HSCException {
		DeleteTenantRequest request = new DeleteTenantRequest(tenant);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		Boolean result = execute(request, MapiResponseHandler.DELETE_TENANT_RESPONSE_HANDLER);

		return result;
	}

}
