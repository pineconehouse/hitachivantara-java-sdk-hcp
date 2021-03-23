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
package com.hitachivantara.hcp.query.api;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.query.model.ObjectQueryResult;
import com.hitachivantara.hcp.query.model.OperationQueryResult;
import com.hitachivantara.hcp.query.model.request.ObjectBasedQueryRequest;
import com.hitachivantara.hcp.query.model.request.OperationBasedQueryRequest;

/**
 * @author sohan
 *
 */
public interface ObjectQuery {
	/**
	 * Object-based queries search for objects currently in the repository based on any combination of system metadata, object paths, custom
	 * metadata that’s well-formed XML, ACLs, and content properties. (For information on content properties, see Content properties.) With
	 * object-based queries, you use a robust query language to construct query criteria.
	 * </p>
	 * In response to an object-based query, HCP returns a set of results, each of which identifies an object and contains metadata for the
	 * object. With object-based queries, you can specify sort criteria to manage the order in which results are returned. You can specify facet
	 * criteria to return summary information about object properties that appear in the result set.
	 * 
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	ObjectQueryResult query(ObjectBasedQueryRequest request) throws InvalidResponseException, HSCException;

	/**
	 * Operation-based queries search for objects based on any combination of create, delete, and disposition operations and, for HCP namespaces
	 * that support versioning, purge and prune operations. Operation-based queries are useful for applications that need to track changes to
	 * namespace content.
	 * </p>
	 * In response to an operation-based query, HCP returns a set of operation records, each of which identifies an object and an operation on
	 * the object and contains additional metadata for the object. For more information on operation records
	 * 
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 * @throws HSCException
	 */
	OperationQueryResult query(OperationBasedQueryRequest request) throws InvalidResponseException, HSCException;
}
