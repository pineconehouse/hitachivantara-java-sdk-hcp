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

public class NamespacePermission {
	/**
	 * List directory contents.
	 * <p>
	 * Check for directory existence.
	 */
	private boolean canBrowse;
	/**
	 * Retrieve objects and system metadata.
	 * <p>
	 * Check for object existence.
	 * <p>
	 * List annotations.
	 * <p>
	 * Check for and retrieve annotations.
	 * <p>
	 * Read operations also require browse permission.
	 */
	private boolean canRead;
	/**
	 * Store objects.
	 * <p>
	 * Create directories.
	 * <p>
	 * Modify system metadata.
	 * <p>
	 * Add and replace annotations.
	 */
	private boolean canWrite;
	/**
	 * Check for and retrieve ACLs.
	 */
	private boolean canReadAcl;
	/**
	 * Add, replace, and delete ACLs.
	 */
	private boolean canWriteAcl;
	/**
	 * Change object owners.
	 */
	private boolean canChangeOwner;
	/**
	 * Delete objects, empty directories, annotations, and ACLs.
	 */
	private boolean canDelete;
	/**
	 * Delete or purge objects regardless of retention (also requires delete or purge permissions).
	 * <p>
	 * Place objects on hold or release objects from hold (also requires write permission).
	 */
	private boolean canPrivileged;
	/**
	 * Delete objects and their old versions (also requires delete permission).
	 */
	private boolean canPurge;
	/**
	 * Search for objects (also requires browse and read permissions). For information on searching for objects, see HCP Metadata Query API
	 * Reference and Searching Namespaces.
	 */
	private boolean canSearch;

	public NamespacePermission(boolean canBrowse,
			boolean canRead,
			boolean canWrite,
			boolean canReadAcl,
			boolean canWriteAcl,
			boolean canChangeOwner,
			boolean canDelete,
			boolean canPrivileged,
			boolean canPurge,
			boolean canSearch) {
		super();
		this.canBrowse = canBrowse;
		this.canRead = canRead;
		this.canWrite = canWrite;
		this.canReadAcl = canReadAcl;
		this.canWriteAcl = canWriteAcl;
		this.canChangeOwner = canChangeOwner;
		this.canDelete = canDelete;
		this.canPrivileged = canPrivileged;
		this.canPurge = canPurge;
		this.canSearch = canSearch;
	}

	/**
	 * List directory contents.
	 * <p>
	 * Check for directory existence.
	 * 
	 * @return
	 */
	public boolean isCanBrowse() {
		return canBrowse;
	}

	/**
	 * Retrieve objects and system metadata.
	 * <p>
	 * Check for object existence.
	 * <p>
	 * List annotations.
	 * <p>
	 * Check for and retrieve annotations.
	 * <p>
	 * Read operations also require browse permission.
	 * 
	 * @return
	 */
	public boolean isCanRead() {
		return canRead;
	}

	/**
	 * Store objects.
	 * <p>
	 * Create directories.
	 * <p>
	 * Modify system metadata.
	 * <p>
	 * Add and replace annotations.
	 * 
	 * @return
	 */
	public boolean isCanWrite() {
		return canWrite;
	}

	/**
	 * Check for and retrieve ACLs.
	 * 
	 * @return
	 */
	public boolean isCanReadAcl() {
		return canReadAcl;
	}

	/**
	 * Add, replace, and delete ACLs.
	 * 
	 * @return
	 */
	public boolean isCanWriteAcl() {
		return canWriteAcl;
	}

	/**
	 * Change object owners.
	 * 
	 * @return
	 */
	public boolean isCanChangeOwner() {
		return canChangeOwner;
	}

	/**
	 * Delete objects, empty directories, annotations, and ACLs.
	 * 
	 * @return
	 */
	public boolean isCanDelete() {
		return canDelete;
	}

	/**
	 * Delete or purge objects regardless of retention (also requires delete or purge permissions).
	 * <p>
	 * Place objects on hold or release objects from hold (also requires write permission).
	 * 
	 * @return
	 */
	public boolean isCanPrivileged() {
		return canPrivileged;
	}

	/**
	 * Delete objects and their old versions (also requires delete permission).
	 * 
	 * @return
	 */
	public boolean isCanPurge() {
		return canPurge;
	}

	/**
	 * Search for objects (also requires browse and read permissions). For information on searching for objects, see HCP Metadata Query API
	 * Reference and Searching Namespaces.
	 * 
	 * @return
	 */
	public boolean isCanSearch() {
		return canSearch;
	}

}
