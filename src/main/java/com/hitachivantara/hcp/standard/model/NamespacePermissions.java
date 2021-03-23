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
package com.hitachivantara.hcp.standard.model;

public class NamespacePermissions {
	/**
	 * The permissions specified for the namespace.
	 */
	private NamespacePermission namespacePermissions;
	/**
	 * The permissions that are in effect for the namespace. This is the logical AND of the permissions specified at the system, tenant, and
	 * namespace levels.
	 */
	private NamespacePermission namespaceEffectivePermissions;
	/**
	 * Your user permissions.
	 */
	private NamespacePermission userPermissions;
	/**
	 * The permissions that result from the combination of the namespace permissions and your user permissions. This is the logical AND of the
	 * permissions specified for the system, tenant, and namespace and your user permissions.
	 */
	private NamespacePermission userEffectivePermissions;

	public NamespacePermissions(NamespacePermission namespacePermissions,
			NamespacePermission namespaceEffectivePermissions,
			NamespacePermission userPermissions,
			NamespacePermission userEffectivePermissions) {
		super();
		this.namespacePermissions = namespacePermissions;
		this.namespaceEffectivePermissions = namespaceEffectivePermissions;
		this.userPermissions = userPermissions;
		this.userEffectivePermissions = userEffectivePermissions;
	}

	/**
	 * The permissions specified for the namespace.
	 * @return
	 */
	public NamespacePermission getNamespacePermissions() {
		return namespacePermissions;
	}

	/**
	 * The permissions that are in effect for the namespace. This is the logical AND of the permissions specified at the system, tenant, and
	 * namespace levels.
	 * @return
	 */
	public NamespacePermission getNamespaceEffectivePermissions() {
		return namespaceEffectivePermissions;
	}

	/**
	 * Your user permissions.
	 * @return
	 */
	public NamespacePermission getUserPermissions() {
		return userPermissions;
	}

	/**
	 * The permissions that result from the combination of the namespace permissions and your user permissions. This is the logical AND of the
	 * permissions specified for the system, tenant, and namespace and your user permissions.
	 * @return
	 */
	public NamespacePermission getUserEffectivePermissions() {
		return userEffectivePermissions;
	}

}
