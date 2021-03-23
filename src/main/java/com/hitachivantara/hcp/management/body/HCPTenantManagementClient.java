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
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.common.define.HCPRequestHeadValue.CONTENT_TYPE;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.management.api.HCPTenantManagement;
import com.hitachivantara.hcp.management.define.Granularity;
import com.hitachivantara.hcp.management.define.Protocols;
import com.hitachivantara.hcp.management.model.CORSSettings;
import com.hitachivantara.hcp.management.model.ContentStatistics;
import com.hitachivantara.hcp.management.model.DataAccessPermissions;
import com.hitachivantara.hcp.management.model.NamespaceSettings;
import com.hitachivantara.hcp.management.model.ProtocolSettings;
import com.hitachivantara.hcp.management.model.TenantChargebackReport;
import com.hitachivantara.hcp.management.model.UpdatePassword;
import com.hitachivantara.hcp.management.model.UserAccount;
import com.hitachivantara.hcp.management.model.converter.MapiResponseHandler;
import com.hitachivantara.hcp.management.model.request.impl.ChangeDataAccessPermissionRequest;
import com.hitachivantara.hcp.management.model.request.impl.ChangeNamespaceCORSRequest;
import com.hitachivantara.hcp.management.model.request.impl.ChangeNamespaceProtocolRequest;
import com.hitachivantara.hcp.management.model.request.impl.ChangeNamespaceRequest;
import com.hitachivantara.hcp.management.model.request.impl.ChangePasswordRequest;
import com.hitachivantara.hcp.management.model.request.impl.ChangeUserAccountRequest;
import com.hitachivantara.hcp.management.model.request.impl.CheckNamespaceRequest;
import com.hitachivantara.hcp.management.model.request.impl.CheckUserAccountRequest;
import com.hitachivantara.hcp.management.model.request.impl.CreateNamespaceRequest;
import com.hitachivantara.hcp.management.model.request.impl.CreateUserAccountRequest;
import com.hitachivantara.hcp.management.model.request.impl.DeleteNamespaceCORSRequest;
import com.hitachivantara.hcp.management.model.request.impl.DeleteNamespaceRequest;
import com.hitachivantara.hcp.management.model.request.impl.DeleteUserAccountRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetDataAccessPermissionRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetNamespaceCORSRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetNamespaceProtocolRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetNamespaceRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetNamespaceStatisticsRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetTenantChargebackReportRequest;
import com.hitachivantara.hcp.management.model.request.impl.GetUserAccountRequest;
import com.hitachivantara.hcp.management.model.request.impl.ListNamespaceRequest;
import com.hitachivantara.hcp.management.model.request.impl.ListUserAccountsRequest;
import com.hitachivantara.hcp.management.model.request.impl.ResetAllPasswordRequest;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.define.RequestParamKey;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;
import com.hitachivantara.hcp.standard.model.request.HCPRequestHeaders;
import com.hitachivantara.hcp.standard.model.request.HCPRequestParams;

public class HCPTenantManagementClient extends AbstractHCPManagementClient implements HCPTenantManagement {

	public HCPTenantManagementClient(String tenant, String endpoint, Credentials credentials, ClientConfiguration clientConfiguration) {
		super(tenant, endpoint, credentials, clientConfiguration);
	}

	@Override
	public NamespaceSettings getNamespaceSettings(String namespace) throws InvalidResponseException, HSCException {
		GetNamespaceRequest request = new GetNamespaceRequest(tenant, namespace);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		// param.setParameter(RequestParamKey.PRETTYPRINT, null);
		param.enableParameter(RequestParamKey.VERBOSE);

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		NamespaceSettings result = execute(request, MapiResponseHandler.GET_NAMESPACE_SETTING_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public ContentStatistics getNamespaceStatistics(String namespace) throws InvalidResponseException, HSCException {
		GetNamespaceStatisticsRequest request = new GetNamespaceStatisticsRequest(tenant, namespace);

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

	public List<TenantChargebackReport> getTenantChargebackReport(Date start, Date end, Granularity granularity) throws InvalidResponseException, HSCException {
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
	public String[] listNamespaces() throws InvalidResponseException, HSCException {
		ListNamespaceRequest request = new ListNamespaceRequest(tenant);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		String[] result = execute(request, MapiResponseHandler.LIST_NAMESPACES_RESPONSE_HANDLER);
		return result;
	}

	@Override
	public void createNamespace(NamespaceSettings namespaceSetting) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfNull(namespaceSetting, "Namespace Settings must be specified.");

		CreateNamespaceRequest request = new CreateNamespaceRequest(tenant);

		request.withNamespace(namespaceSetting.getName());
		request.withSettings(namespaceSetting);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		// header.setHeader(HeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		execute(request, MapiResponseHandler.CREATE_NAMESPACE_RESPONSE_HANDLER);
	}

	@Override
	public boolean doesNamespaceExist(String namespace) throws InvalidResponseException, HSCException {
		CheckNamespaceRequest request = new CheckNamespaceRequest(tenant, namespace);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		Boolean result = execute(request, MapiResponseHandler.CHECK_NAMESPACE_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public boolean deleteNamespace(String namespace) throws InvalidResponseException, HSCException {
		DeleteNamespaceRequest request = new DeleteNamespaceRequest(tenant, namespace);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		Boolean result = execute(request, MapiResponseHandler.DELETE_NAMESPACE_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public void changeNamespace(String namespace, NamespaceSettings namespaceSetting) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfNull(namespaceSetting, "Namespace Settings must be specified.");

		ChangeNamespaceRequest request = new ChangeNamespaceRequest(tenant);

		request.withNamespace(namespace);

		if (StringUtils.isEmpty(namespaceSetting.getName())) {
			namespaceSetting.setName(namespace);
		}

		request.withSettings(namespaceSetting);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		// header.setHeader(HeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		execute(request, MapiResponseHandler.CHANGE_NAMESPACE_RESPONSE_HANDLER);
	}

	@Override
	public <PT> PT getNamespaceProtocol(String namespace, Protocols<PT> protocol) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfNull(protocol, "Protocol must be specified.");

		GetNamespaceProtocolRequest request = new GetNamespaceProtocolRequest(tenant, namespace);

		request.withProtocol(protocol);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		// HCPRequestParams param = request.parameter();

		// param.setParameter(RequestParamKey.PRETTYPRINT, null);

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		return (PT) execute(request, protocol.retrievingNamespaceProtocolsResponseHandler());
	}

	@Override
	public void changeNamespaceProtocol(String namespace, ProtocolSettings settings) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfNull(settings, "Protocol Settings must be specified.");

		ChangeNamespaceProtocolRequest request = new ChangeNamespaceProtocolRequest(tenant, namespace);

		request.withProtocolSettings(settings);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);

		execute(request, MapiResponseHandler.CHANGE_NAMESPACE_PROTOCOL_RESPONSE_HANDLER);
	}

	@Override
	public DataAccessPermissions getDataAccessPermissions(String username) throws InvalidResponseException, HSCException {
		GetDataAccessPermissionRequest request = new GetDataAccessPermissionRequest(tenant, username);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		DataAccessPermissions result = execute(request, MapiResponseHandler.GET_DATA_ACCESS_PERMISSIONS_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public void changeDataAccessPermissions(String username, DataAccessPermissions permissions) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfNull(permissions, "Data Access Permissions must be specified.");
		ValidUtils.invalidIfZero(permissions.getSize(), "Data Access Permissions must be specified.");

		ChangeDataAccessPermissionRequest request = new ChangeDataAccessPermissionRequest(tenant, username);

		request.withDataAccessPermission(permissions);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		// header.setHeader(HeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		execute(request, MapiResponseHandler.CHANGE_DATA_ACCESS_PERMISSIONS_RESPONSE_HANDLER);
	}

	@Override
	public String[] listUserAccounts(String tenant) throws InvalidResponseException, HSCException {
		ListUserAccountsRequest request = new ListUserAccountsRequest(tenant);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		String[] users = execute(request, MapiResponseHandler.GET_USER_ACCOUNTS_RESPONSE_HANDLER);
		return users;

		// List<UserAccount> results = new ArrayList<UserAccount>();
		// for (String user : users) {
		// UserAccount account = getUserAccount(tenant, user);
		// results.add(account);
		// }

		// return results;
	}

	@Override
	public UserAccount getUserAccount(String username) throws InvalidResponseException, HSCException {
		GetUserAccountRequest request = new GetUserAccountRequest(tenant, username);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		// param.setParameter(RequestParamKey.PRETTYPRINT, null);
		param.enableParameter(RequestParamKey.VERBOSE);

		UserAccount result = execute(request, MapiResponseHandler.GET_USER_ACCOUNT_INFO_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public void createUserAccount(UserAccount userAccount) throws InvalidResponseException, HSCException {
		CreateUserAccountRequest request = new CreateUserAccountRequest(tenant);

		request.withUserAccount(userAccount);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);

		if (userAccount.hasNewPassword()) {
			HCPRequestParams param = request.customParameter();
			param.setParameter(RequestParamKey.PASSWORD, userAccount);
		}

		execute(request, MapiResponseHandler.CREATE_USER_ACCOUNT_RESPONSE_HANDLER);
	}

	@Override
	public void changeUserAccount(String username, UserAccount userAccount) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfNull(userAccount, "User Account must be specified.");

		ChangeUserAccountRequest request = new ChangeUserAccountRequest(tenant, username);

		request.withUserAccount(userAccount);

		if (StringUtils.isEmpty(userAccount.getUsername())) {
			userAccount.setUsername(username);
		}

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);

		if (userAccount.hasNewPassword()) {
			HCPRequestParams param = request.customParameter();
			param.setParameter(RequestParamKey.PASSWORD, userAccount);
		}

		execute(request, MapiResponseHandler.CHANGE_USER_ACCOUNT_RESPONSE_HANDLER);
	}

	@Override
	public boolean deleteUserAccount(String username) throws InvalidResponseException, HSCException {
		DeleteUserAccountRequest request = new DeleteUserAccountRequest(tenant, username);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);

		return execute(request, MapiResponseHandler.DELETE_USER_ACCOUNT_RESPONSE_HANDLER);
	}

	@Override
	public boolean doesUserAccountExist(String username) throws InvalidResponseException, HSCException {
		CheckUserAccountRequest request = new CheckUserAccountRequest(tenant, username);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);

		return execute(request, MapiResponseHandler.CHECK_USER_ACCOUNT_RESPONSE_HANDLER);
	}

	@Override
	public void changePassword(String username, String newPassword) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfEmpty(newPassword, "New Password must be specified.");

		ChangePasswordRequest request = new ChangePasswordRequest(tenant, username);

		UpdatePassword updatePassword = new UpdatePassword(newPassword);
		request.withNewPassword(updatePassword);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		// HCPRequestParams param = request.customParameter();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		// param.setParameter(RequestParamKey.UPDATE_PASSWORD, updatePassword);

		execute(request, MapiResponseHandler.CHANGE_PASSWORD_RESPONSE_HANDLER);
	}

	@Override
	public void resetAllPassword(String newPassword) throws InvalidResponseException, HSCException {
		ValidUtils.invalidIfEmpty(newPassword, "New Password must be specified.");

		ResetAllPasswordRequest request = new ResetAllPasswordRequest(tenant);

		UpdatePassword updatePassword = new UpdatePassword(newPassword);
		request.withNewPassword(updatePassword);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();
		HCPRequestParams param = request.customParameter();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		param.setParameter(RequestParamKey.RESET_PASSWORDS, null);

		execute(request, MapiResponseHandler.CHANGE_PASSWORD_RESPONSE_HANDLER);
	}

	@Override
	public CORSSettings getNamespaceCORSSettings(String namespaceName) throws InvalidResponseException, HSCException {
		GetNamespaceCORSRequest request = new GetNamespaceCORSRequest(tenant, namespaceName);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.ACCEPT, CONTENT_TYPE.APPLICATION_JSON);

		CORSSettings result = execute(request, MapiResponseHandler.GET_NAMESPACE_CORS_SETTING_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public boolean deleteNamespaceCORSSettings(String namespaceName) throws InvalidResponseException, HSCException {
		DeleteNamespaceCORSRequest request = new DeleteNamespaceCORSRequest(tenant, namespaceName);

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);

		Boolean result = execute(request, MapiResponseHandler.DELETE_NAMESPACE_CORS_SETTING_RESPONSE_HANDLER);

		return result;
	}

	@Override
	public void changeNamespaceCORSSettings(String namespaceName, CORSSettings settings) throws InvalidResponseException, HSCException {
		ChangeNamespaceCORSRequest request = new ChangeNamespaceCORSRequest(tenant, namespaceName, settings.getCORSConfiguration());

		// Exec the default request parameter check
		ValidUtils.validateRequest(request);
		
		HCPRequestHeaders header = request.customHeader();

		header.setHeader(HCPHeaderKey.CONTENT_TYPE, CONTENT_TYPE.APPLICATION_XML);
		
		execute(request, MapiResponseHandler.CHANGE_NAMESPACE_CORS_SETTING_RESPONSE_HANDLER);
	}

}
