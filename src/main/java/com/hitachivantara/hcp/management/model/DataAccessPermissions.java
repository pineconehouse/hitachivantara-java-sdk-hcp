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
package com.hitachivantara.hcp.management.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hitachivantara.hcp.management.define.Permission;

public class DataAccessPermissions {
	private Map<String, List<Permission>> permissionsMap = new HashMap<String, List<Permission>>();

	public DataAccessPermissions() {
	}

	public DataAccessPermissions(String namespaceName, List<Permission> permissions) {
		super();
		permissionsMap.put(namespaceName, permissions);
	}

	public DataAccessPermission[] getAllPermissions() {
		DataAccessPermission[] list = new DataAccessPermission[permissionsMap.size()];

		int i = 0;
		for (Iterator<String> it = permissionsMap.keySet().iterator(); it.hasNext();) {
			String namespaceName = (String) it.next();

			list[i] = new DataAccessPermission(namespaceName, permissionsMap.get(namespaceName));
			i++;
		}

		return list;
	}

	public List<Permission> getPermissions(String namespaceName) {
		return permissionsMap.get(namespaceName);
	}

	public void setPermissions(String namespaceName, List<Permission> permissions) {
		permissionsMap.put(namespaceName, permissions);
	}

	public void addPermission(String namespaceName, Permission permission) {
		List<Permission> ps = permissionsMap.get(namespaceName);
		if (ps == null) {
			ps = new ArrayList<Permission>();
			permissionsMap.put(namespaceName, ps);
		}

		if (!ps.contains(permission)) {
			ps.add(permission);
		}
	}

	public void removePermission(String namespaceName, Permission permission) {
		List<Permission> ps = permissionsMap.get(namespaceName);
		if (ps == null) {
			ps = new ArrayList<Permission>();
			permissionsMap.put(namespaceName, ps);
		} else {
			ps.remove(permission);
		}
	}

	public boolean hasPermission(String namespaceName, Permission permission) {
		List<Permission> ps = permissionsMap.get(namespaceName);
		if (ps != null) {
			for (Permission p : ps) {
				if (permission == p) {
					return true;
				}
			}
		}

		return false;
	}
	
	public int getSize() {
		return permissionsMap.size();
	}

}
