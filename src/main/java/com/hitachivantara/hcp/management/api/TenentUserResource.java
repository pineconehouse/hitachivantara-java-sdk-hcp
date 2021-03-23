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
package com.hitachivantara.hcp.management.api;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.management.model.DataAccessPermissions;
import com.hitachivantara.hcp.management.model.UserAccount;

public interface TenentUserResource {

	/**
	 * Retrieve the list of permissions in the data access permission mask for a namespace
	 * 
	 * @param userName
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	DataAccessPermissions getDataAccessPermissions(String userName) throws InvalidResponseException, HSCException;

	/**
	 * Modify the list of permissions in the data access permission mask for a namespace
	 * 
	 * @param userName
	 * @param permissions
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	void changeDataAccessPermissions(String userName, DataAccessPermissions permissions) throws InvalidResponseException, HSCException;

	/**
	 * Retrieve information about a user account
	 * 
	 * @param userName
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	UserAccount getUserAccount(String userName) throws InvalidResponseException, HSCException;

	/**
	 * Retrieve a list of the user accounts defined for a tenant
	 * 
	 * @param tenant
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	String[] listUserAccounts(String tenant) throws InvalidResponseException, HSCException;

	/**
	 * Check for the existence of a user account
	 * 
	 * @param username
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	boolean doesUserAccountExist(String username) throws InvalidResponseException, HSCException;

	/**
	 * Delete a user account
	 * 
	 * @param username
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	boolean deleteUserAccount(String username) throws InvalidResponseException, HSCException;

	/**
	 * 
	 * Modify a user account. <b>A user with only the administrator role can modify only the allow-NamespaceManagement property. A user with
	 * only the security role cannot modify that property.</b>
	 * 
	 * @param username
	 * @param userAccount
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	void changeUserAccount(String username, UserAccount userAccount) throws InvalidResponseException, HSCException;

	/**
	 * Create a user account for a tenant
	 * 
	 * @param userAccount
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	void createUserAccount(UserAccount userAccount) throws InvalidResponseException, HSCException;

	/**
	 * Change the password for a locally authenticated tenant-level user account
	 * 
	 * @param username
	 * @param newPassword
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	void changePassword(String username, String newPassword) throws InvalidResponseException, HSCException;

	/**
	 * Reset the passwords of all locally authenticated user accounts with the security role
	 * 
	 * @param newPassword
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	void resetAllPassword(String newPassword) throws InvalidResponseException, HSCException;

}
