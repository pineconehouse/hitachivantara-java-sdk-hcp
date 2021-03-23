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
package com.hitachivantara.hcp.standard.model.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amituofo.common.util.StringUtils;
import com.hitachivantara.hcp.standard.define.ACLDefines;
import com.hitachivantara.hcp.standard.define.ACLDefines.ACLPermission;
import com.hitachivantara.hcp.standard.define.ACLDefines.Type;

public class AccessControlList {
	private Map<String, PermissionGrant> acl = new HashMap<String, PermissionGrant>();

	public AccessControlList() {
	}

	public void merge(AccessControlList acl2) {
		if (acl2 != null) {
			acl.putAll(acl2.acl);
		}
	}

	public void remove(ACLUserList removeList) {
		List<String> users = removeList.getUserKeys();
		for (String user : users) {
			acl.remove(user);
		}
	}

	public void grantPermissionsToUser(String userName, ACLPermission... permissions) {
		grantPermissions(ACLDefines.Type.USER, userName, null, permissions);
	}

	public void grantPermissionsToUser(String userName, String domain, ACLPermission... permissions) {
		grantPermissions(ACLDefines.Type.USER, userName, domain, permissions);
	}

	public void grantPermissionsToGroup(String groupName, String domain, ACLPermission... permissions) {
		grantPermissions(ACLDefines.Type.GROUP, groupName, domain, permissions);
	}

	public void grantPermissionsToAllUsers(String domain, ACLPermission... permissions) {
		grantPermissions(ACLDefines.Type.GROUP, ACLDefines.Name.ALL_USERS, domain, permissions);
	}

	public void grantPermissionsToAuthenticatedUsers(String domain, ACLPermission... permissions) {
		grantPermissions(ACLDefines.Type.GROUP, ACLDefines.Name.AUTHENTICATED, domain, permissions);
	}

	public void grantPermissionsToAllUsers(ACLPermission... permissions) {
		grantPermissions(ACLDefines.Type.GROUP, ACLDefines.Name.ALL_USERS, null, permissions);
	}

	public void grantPermissionsToAuthenticatedUsers(ACLPermission... permissions) {
		grantPermissions(ACLDefines.Type.GROUP, ACLDefines.Name.AUTHENTICATED, null, permissions);
	}

	public void grantPermissions(ACLDefines.Type type, String userName, String domain, ACLPermission... tobeGrantPermissions) {
		// aclGrant.put(genKey(type, userName, domain), new PermissionGrant(type, userName, domain, tobeGrantPermissions));

		if (tobeGrantPermissions == null || tobeGrantPermissions.length == 0) {
			return;
		}

		PermissionGrant pg = acl.get(genKey(type, userName, domain));

		if (pg == null) {
			acl.put(genKey(type, userName, domain), new PermissionGrant(type, userName, domain, tobeGrantPermissions));
			return;
		}

		ACLPermission[] newpermissions = null;
		ACLPermission[] currentPermissions = pg.getPermissions();
		List<ACLPermission> newpermissionList = new ArrayList<ACLPermission>();
		for (ACLPermission currentPermission : currentPermissions) {
			newpermissionList.add(currentPermission);
		}

		for (ACLPermission tobeGrantPermission : tobeGrantPermissions) {
			if (!newpermissionList.contains(tobeGrantPermission)) {
				newpermissionList.add(tobeGrantPermission);
			}
		}

		newpermissions = newpermissionList.toArray(new ACLPermission[newpermissionList.size()]);

		pg.setPermissions(newpermissions);
	}

	public void removePermissionsFromUser(String userName, ACLPermission... permissions) {
		removePermissions(ACLDefines.Type.USER, userName, null, permissions);
	}

	public void removePermissionsFromUser(String userName, String domain, ACLPermission... permissions) {
		removePermissions(ACLDefines.Type.USER, userName, domain, permissions);
	}

	public void removePermissionsFromGroup(String groupName, String domain, ACLPermission... permissions) {
		removePermissions(ACLDefines.Type.GROUP, groupName, domain, permissions);
	}

	public void removePermissionsFromAllUsers(String domain, ACLPermission... permissions) {
		removePermissions(ACLDefines.Type.GROUP, ACLDefines.Name.ALL_USERS, domain, permissions);
	}

	public void removePermissionsFromAuthenticatedUsers(String domain, ACLPermission... permissions) {
		removePermissions(ACLDefines.Type.GROUP, ACLDefines.Name.AUTHENTICATED, domain, permissions);
	}

	public void removePermissionsFromAllUsers(ACLPermission... permissions) {
		removePermissions(ACLDefines.Type.GROUP, ACLDefines.Name.ALL_USERS, null, permissions);
	}

	public void removePermissionsFromAuthenticatedUsers(ACLPermission... permissions) {
		removePermissions(ACLDefines.Type.GROUP, ACLDefines.Name.AUTHENTICATED, null, permissions);
	}

	public void removePermissions(ACLDefines.Type type, String userName, String domain, ACLPermission... tobeRemovePermissions) {
		PermissionGrant pg = acl.get(genKey(type, userName, domain));
		if (pg != null) {
			ACLPermission[] newpermissions = null;
			if (tobeRemovePermissions == null) {
				newpermissions = null;
			} else {
				List<ACLPermission> newpermissionList = new ArrayList<ACLPermission>();
				ACLPermission[] currentPermissions = pg.getPermissions();
				for (ACLPermission currentPermission : currentPermissions) {
					if (!hasPermissions(tobeRemovePermissions, currentPermission)) {
						newpermissionList.add(currentPermission);
					}
				}

				newpermissions = newpermissionList.toArray(new ACLPermission[newpermissionList.size()]);
			}

			if (newpermissions == null || newpermissions.length == 0) {
				acl.remove(genKey(type, userName, domain));
			} else {
				pg.setPermissions(newpermissions);
			}
		}
	}

	private String genKey(Type type, String userName, String domain) {
		return type.name() + StringUtils.nullToString(userName) + StringUtils.nullToString(domain);
	}

	public Collection<PermissionGrant> getAllPermissions() {
		return acl.values();
	}

	public boolean hasPermissions() {
		return acl.size() != 0;
	}

	public static boolean hasPermissions(ACLPermission[] ps, ACLPermission p) {
		if (ps == null || p == null) {
			return false;
		}

		for (ACLPermission aclPermission : ps) {
			if (aclPermission == p) {
				return true;
			}
		}

		return false;
	}

	public ACLPermission[] getPermission(String userOrGroupName) {
		Collection<PermissionGrant> c = getAllPermissions();
		for (Iterator<PermissionGrant> it = c.iterator(); it.hasNext();) {
			PermissionGrant permissionGrant = (PermissionGrant) it.next();
			if (userOrGroupName.equalsIgnoreCase(permissionGrant.getUserName())) {
				return permissionGrant.getPermissions();
			}

		}

		return null;
	}

}
