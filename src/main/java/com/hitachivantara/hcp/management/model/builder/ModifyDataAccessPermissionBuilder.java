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
package com.hitachivantara.hcp.management.model.builder;

import com.amituofo.common.api.Builder;
import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.management.define.Permission;
import com.hitachivantara.hcp.management.model.DataAccessPermission;
import com.hitachivantara.hcp.management.model.DataAccessPermissions;

public class ModifyDataAccessPermissionBuilder implements Builder<DataAccessPermissions, HSCException> {
	DataAccessPermissions daps = new DataAccessPermissions();

	public ModifyDataAccessPermissionBuilder() {
	}

	public ModifyDataAccessPermissionBuilder withPermission(String namespaceName, Permission... permissions) {
		if (permissions == null || permissions.length == 0) {
			daps.setPermissions(namespaceName, null);
		} else {
			for (Permission permission : permissions) {
				daps.addPermission(namespaceName, permission);
			}
		}
		return this;
	}
	
	public ModifyDataAccessPermissionBuilder withoutPermission(String namespaceName) {
		daps.setPermissions(namespaceName, null);
		return this;
	}

	@Override
	public DataAccessPermissions bulid() throws HSCException, InvalidParameterException {
		ValidUtils.invalidIfZero(daps.getSize(), "Need to specifies Data Access Permission.");

		DataAccessPermission[] list = daps.getAllPermissions();
		for (DataAccessPermission dataAccessPermission : list) {
			ValidUtils.invalidIfEmpty(dataAccessPermission.getNamespaceName(), "Namespace Name must be specified.");
//			ValidUtils.exceptionIfEmpty(dataAccessPermission.getPermissions(), "Permission must be specified.");
		}

		return daps;
	}

}
