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
package com.hitachivantara.hcp.management.model.request.impl;

import java.util.List;

import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.management.model.DataAccessPermission;
import com.hitachivantara.hcp.management.model.DataAccessPermissions;
import com.hitachivantara.hcp.management.model.request.TenantUserAccountResourceRequest;
import com.hitachivantara.hcp.management.util.BodyBuildUtils;

public class ChangeDataAccessPermissionRequest extends TenantUserAccountResourceRequest<ChangeDataAccessPermissionRequest> {
	protected final static String KEY_DATA_ACCESS_PERMISSIONS = "dataAccessPermissions";

	private DataAccessPermissions permissions;

	public ChangeDataAccessPermissionRequest() {
		super(Method.POST);
	}

	public ChangeDataAccessPermissionRequest(String tenant, String userName) {
		super(Method.POST, tenant, userName);
	}

	public void withDataAccessPermission(DataAccessPermissions permissions) {
		this.permissions = permissions;
	}

	public void withDataAccessPermission(List<DataAccessPermission> permissions) {
		permissions.addAll(permissions);
	}

	@Override
	public String getResourceIdentifier() {
		return URLUtils.simpleCatPaths(KEY_TENANTS, this.getTenant(), KEY_USER_ACCOUNTS, this.getUsername(), KEY_DATA_ACCESS_PERMISSIONS);
	}

	@Override
	public String buildRequestXMLBody() {
		return BodyBuildUtils.build(permissions);
	}

}
