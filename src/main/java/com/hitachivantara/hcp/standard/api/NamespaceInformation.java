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
package com.hitachivantara.hcp.standard.api;

import java.util.List;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.NamespaceBasicSetting;
import com.hitachivantara.hcp.standard.model.NamespacePermissions;
import com.hitachivantara.hcp.standard.model.NamespaceStatistics;
import com.hitachivantara.hcp.standard.model.RetentionClasses;

/**
 * Retrieve information about the namespaces you can access.
 * 
 * @author sohan
 *
 */
public interface NamespaceInformation {
	/**
	 * Use the method to list the permissions for current namespace.
	 * 
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	NamespacePermissions getNamespacePermissions() throws InvalidResponseException, HSCException;

	/**
	 * Use the method to list the permissions for a namespace.
	 * 
	 * @param namespace
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	NamespacePermissions getNamespacePermissions(String namespace) throws InvalidResponseException, HSCException;

	/**
	 * Use the method to retrieve the settings for an individual namespace. The response contains information about the namespace only if you
	 * have any permissions for the namespace.
	 * 
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	NamespaceBasicSetting getNamespaceSetting() throws InvalidResponseException, HSCException;

	/**
	 * Use the method to retrieve the settings for an individual namespace. The response contains information about the namespace only if you
	 * have any permissions for the namespace.
	 * 
	 * @param namespace
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	NamespaceBasicSetting getNamespaceSetting(String namespace) throws InvalidResponseException, HSCException;

	/**
	 * Use the method to list the namespaces you can access. The list contains the namespaces owned by the specified tenant and for which you
	 * have any permissions
	 * 
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	List<NamespaceBasicSetting> listAccessibleNamespaces() throws InvalidResponseException, HSCException;

	/**
	 * Use the method to list the retention classes defined for a namespace.
	 * 
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	List<RetentionClasses> listRetentionClasses() throws InvalidResponseException, HSCException;

	/**
	 * Use the method to list the retention classes defined for a namespace.
	 * 
	 * @param namespace
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	List<RetentionClasses> listRetentionClasses(String namespace) throws InvalidResponseException, HSCException;

	/**
	 * Use the method to list statistics for a namespace. The values returned include information such as the total and used capacity of the
	 * namespace and the number of objects with annotations. For a description of the statistics returned
	 * 
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	NamespaceStatistics getNamespacesStatistics() throws InvalidResponseException, HSCException;

	/**
	 * Use the method to list statistics for a namespace. The values returned include information such as the total and used capacity of the
	 * namespace and the number of objects with annotations. For a description of the statistics returned
	 * 
	 * @param namespace
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	NamespaceStatistics getNamespacesStatistics(String namespace) throws InvalidResponseException, HSCException;

	boolean doesNamespacesExist(String namespace) throws InvalidResponseException, HSCException;

}
