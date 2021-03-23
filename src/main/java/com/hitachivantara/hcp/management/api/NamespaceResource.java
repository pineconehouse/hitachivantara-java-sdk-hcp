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
import com.hitachivantara.hcp.management.define.Protocols;
import com.hitachivantara.hcp.management.model.CORSSettings;
import com.hitachivantara.hcp.management.model.ContentStatistics;
import com.hitachivantara.hcp.management.model.NamespaceSettings;
import com.hitachivantara.hcp.management.model.ProtocolSettings;

public interface NamespaceResource {

	/**
	 * Retrieve a list of the namespaces owned by a tenant
	 * 
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	String[] listNamespaces() throws InvalidResponseException, HSCException;

	/**
	 * Retrieve information about a namespace
	 * 
	 * @param namespaceName
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	NamespaceSettings getNamespaceSettings(String namespaceName) throws InvalidResponseException, HSCException;

	/**
	 * Check for the existence of a namespace
	 * 
	 * @param namespaceName
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	boolean doesNamespaceExist(String namespaceName) throws InvalidResponseException, HSCException;

	/**
	 * Delete an HCP namespace
	 * 
	 * @param namespaceName
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	boolean deleteNamespace(String namespaceName) throws InvalidResponseException, HSCException;

	/**
	 * Create an HCP namespace
	 * 
	 * @param settings
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	void createNamespace(NamespaceSettings settings) throws InvalidResponseException, HSCException;

	/**
	 * Modify an HCP namespace
	 * 
	 * @param settings
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	void changeNamespace(String namespaceName, NamespaceSettings settings) throws InvalidResponseException, HSCException;
	//
	// List<TenantChargebackReport> getTenantChargebackReport(Date start, Date end, Granularity granularity) throws
	// InvalidResponseException, HSCException;

	/**
	 * Retrieve information about the content of a namespace
	 * 
	 * @param namespaceName
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	ContentStatistics getNamespaceStatistics(String namespaceName) throws InvalidResponseException, HSCException;

	/**
	 * Retrieve a subset of the namespace access protocol settings for the namespace
	 * 
	 * @param namespaceName
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	<PT> PT getNamespaceProtocol(String namespaceName, Protocols<PT> protocol) throws InvalidResponseException, HSCException;

	/**
	 * Modify a subset of the HTTP namespace access protocol settings for the default namespace
	 * 
	 * @param namespaceName
	 * @param settings
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	void changeNamespaceProtocol(String namespaceName, ProtocolSettings settings) throws InvalidResponseException, HSCException;

	CORSSettings getNamespaceCORSSettings(String namespaceName) throws InvalidResponseException, HSCException;

	void changeNamespaceCORSSettings(String namespaceName, CORSSettings settings) throws InvalidResponseException, HSCException;

	boolean deleteNamespaceCORSSettings(String namespaceName) throws InvalidResponseException, HSCException;

	// <PT> PT getNamespaceReplicationCollisionSettings(String namespaceName) throws InvalidResponseException, HSCException;

	// void changeNamespaceReplicationCollisionSettings(String namespaceName, ReplicationCollisionSettings settings) throws
	// InvalidResponseException, HSCException;

}
