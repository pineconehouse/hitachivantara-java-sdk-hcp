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
import com.hitachivantara.hcp.management.model.ContentStatistics;
import com.hitachivantara.hcp.management.model.TenantSettings;

public interface TenantResource {

	/**
	 * Retrieve a list of the tenants defined in an HCP system
	 * 
	 * @return list of the tenants
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	String[] listTenants() throws InvalidResponseException, HSCException;

	/**
	 * Retrieve information about a Tenant
	 * 
	 * @param tenant
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	TenantSettings getTenantSettings(String tenant) throws InvalidResponseException, HSCException;

	/**
	 * Check for the existence of a tenant
	 * 
	 * @param tenant
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	boolean doesTenantExist(String tenant) throws InvalidResponseException, HSCException;

	/**
	 * Delete an HCP tenant
	 * 
	 * @param tenant
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	boolean deleteTenant(String tenant) throws InvalidResponseException, HSCException;

	/**
	 * Retrieve statistics about the content of the namespaces owned by a tenant
	 * 
	 * @param tenant
	 * @return {@link #ContentStatistics} Statistics resource
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	ContentStatistics getTenantStatistics(String tenant) throws InvalidResponseException, HSCException;

}
