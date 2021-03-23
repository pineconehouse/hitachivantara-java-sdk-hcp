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
package com.hitachivantara.test.hcp.cases.query;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.query.define.Facet;
import com.hitachivantara.hcp.query.define.ObjectProperty;
import com.hitachivantara.hcp.query.define.Order;
import com.hitachivantara.hcp.query.model.ObjectQueryResult;
import com.hitachivantara.hcp.query.model.ObjectSummary;
import com.hitachivantara.hcp.query.model.OperationQueryResult;
import com.hitachivantara.hcp.query.model.QueryResult;
import com.hitachivantara.hcp.query.model.request.ObjectBasedQueryRequest;
import com.hitachivantara.hcp.query.model.request.OperationBasedQueryRequest;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestQuery extends TestHCPBase {
	
	@Test
	public void testObjectBasedQuery() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// ClientConfiguration myClientConfig = new ClientConfiguration();
		// myClientConfig.setProtocol(Protocol.HTTP);
		// hcpQuery = HCPClientBuilder.queryClient().withEndpoint(namespaceName+"."+endpoint).withNamespace(null).withCredentials(new
		// DefaultHCPCredentials(user, password))
		// .withClientConfiguration(myClientConfig).bulid();
		// PREPARE TEST DATA ----------------------------------------------------------------------

		ObjectBasedQueryRequest request = new ObjectBasedQueryRequest();
		request.setQuery("+(objectPath:abc.txt)");
		request.addSort(ObjectProperty.ingestTime, Order.asc);
		request.addSort(ObjectProperty.utf8Name);
		request.setResults(100);
//		requestBody.setOffset(0);

		request.addProperty(ObjectProperty.accessTime);
		request.addProperty(ObjectProperty.accessTimeString);
		request.addProperty(ObjectProperty.acl);
		request.addProperty(ObjectProperty.aclGrant);
		request.addProperty(ObjectProperty.changeTimeMilliseconds);
		request.addProperty(ObjectProperty.changeTimeString);
		request.addProperty(ObjectProperty.customMetadata);
		request.addProperty(ObjectProperty.customMetadataAnnotation);
		// requestBody.addProperty(ObjectProperty.customMetadataContent);
		request.addProperty(ObjectProperty.dpl);
		request.addProperty(ObjectProperty.gid);
		request.addProperty(ObjectProperty.hash);
		request.addProperty(ObjectProperty.hashScheme);
		request.addProperty(ObjectProperty.hold);
		request.addProperty(ObjectProperty.index);
		request.addProperty(ObjectProperty.ingestTime);
		request.addProperty(ObjectProperty.ingestTimeString);
		request.addProperty(ObjectProperty.namespace);
		request.addProperty(ObjectProperty.objectPath);
		request.addProperty(ObjectProperty.operation);
		request.addProperty(ObjectProperty.owner);
		request.addProperty(ObjectProperty.permissions);
		request.addProperty(ObjectProperty.replicated);
		request.addProperty(ObjectProperty.replicationCollision);
		request.addProperty(ObjectProperty.retention);
		request.addProperty(ObjectProperty.retentionClass);
		request.addProperty(ObjectProperty.retentionString);
		request.addProperty(ObjectProperty.shred);
		request.addProperty(ObjectProperty.size);
		request.addProperty(ObjectProperty.type);
		request.addProperty(ObjectProperty.uid);
		request.addProperty(ObjectProperty.updateTime);
		request.addProperty(ObjectProperty.updateTimeString);
		request.addProperty(ObjectProperty.urlName);
		request.addProperty(ObjectProperty.utf8Name);
		request.addProperty(ObjectProperty.version);

		request.addFacet(Facet.namespace);
		request.addFacet(Facet.hold);
		request.addFacet(Facet.retention);
		request.addFacet(Facet.retentionClass);

		ObjectQueryResult result = null;
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		
		result = hcpQuery.query(request);

		handleResult(result);

		while (result.isIncomplete()) {
			System.out.println(request.getRequestBody().build());

			//			long s = System.currentTimeMillis();
			result = hcpQuery.query(request.withNextPage());

//			long e = System.currentTimeMillis();
//			System.out.println(e-s);

			handleResult(result);
		}

		result = hcpQuery.query(request.withPrevPage());

		handleResult(result);

		// System.out.println(result1);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// assertTrue(contains(ps.get(0), Permission.READ));
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testObjectBasedQueryPage() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		ObjectBasedQueryRequest request = new ObjectBasedQueryRequest();
		request.setQuery("+(objectPath:abc.txt)");
		request.addSort(ObjectProperty.ingestTime, Order.asc);
		request.addSort(ObjectProperty.utf8Name);
		request.setResults(2);

		ObjectQueryResult result = null;
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		
		result = hcpQuery.query(request);
		handleResult(result);

		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}
		
		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}
		
		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}
		
		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}

		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);

		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
		
		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}

		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
		
		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
		
		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
		
		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testOperationBasedQuery() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// ClientConfiguration myClientConfig = new ClientConfiguration();
		// myClientConfig.setProtocol(Protocol.HTTP);
		// hcpQuery = HCPClientBuilder.queryClient().withEndpoint(namespaceName+"."+endpoint).withNamespace(null).withCredentials(new
		// DefaultHCPCredentials(user, password))
		// .withClientConfiguration(myClientConfig).bulid();
		// PREPARE TEST DATA ----------------------------------------------------------------------

		OperationBasedQueryRequest request = new OperationBasedQueryRequest();
		
		request.setResults(100);

		request.addProperty(ObjectProperty.accessTime);
		request.addProperty(ObjectProperty.accessTimeString);
		request.addProperty(ObjectProperty.acl);
		request.addProperty(ObjectProperty.aclGrant);
		request.addProperty(ObjectProperty.changeTimeMilliseconds);
		request.addProperty(ObjectProperty.changeTimeString);
		request.addProperty(ObjectProperty.customMetadata);
		request.addProperty(ObjectProperty.customMetadataAnnotation);
		// requestBody.addProperty(ObjectProperty.customMetadataContent);
		request.addProperty(ObjectProperty.dpl);
		request.addProperty(ObjectProperty.gid);
		request.addProperty(ObjectProperty.hash);
		request.addProperty(ObjectProperty.hashScheme);
		request.addProperty(ObjectProperty.hold);
		request.addProperty(ObjectProperty.index);
		request.addProperty(ObjectProperty.ingestTime);
		request.addProperty(ObjectProperty.ingestTimeString);
		request.addProperty(ObjectProperty.namespace);
		request.addProperty(ObjectProperty.objectPath);
		request.addProperty(ObjectProperty.operation);
		request.addProperty(ObjectProperty.owner);
		request.addProperty(ObjectProperty.permissions);
		request.addProperty(ObjectProperty.replicated);
		request.addProperty(ObjectProperty.replicationCollision);
		request.addProperty(ObjectProperty.retention);
		request.addProperty(ObjectProperty.retentionClass);
		request.addProperty(ObjectProperty.retentionString);
		request.addProperty(ObjectProperty.shred);
		request.addProperty(ObjectProperty.size);
		request.addProperty(ObjectProperty.type);
		request.addProperty(ObjectProperty.uid);
		request.addProperty(ObjectProperty.updateTime);
		request.addProperty(ObjectProperty.updateTimeString);
		request.addProperty(ObjectProperty.urlName);
		request.addProperty(ObjectProperty.utf8Name);
		request.addProperty(ObjectProperty.version);
		
		OperationQueryResult result = null;
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		result = hcpQuery.query(request);

		handleResult(result);

		while (result.isIncomplete()) {
			// XXX
			System.out.println(request.getRequestBody().build());

//			long s = System.currentTimeMillis();
			result = hcpQuery.query(request.withNextPage());

//			long e = System.currentTimeMillis();
//			System.out.println(e-s);

			handleResult(result);
		}

		result = hcpQuery.query(request.withPrevPage());

		handleResult(result);

		// System.out.println(result1);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// assertTrue(contains(ps.get(0), Permission.READ));
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testOperationBasedQueryPage() throws InvalidResponseException, HSCException, IOException {
		OperationBasedQueryRequest request = new OperationBasedQueryRequest();
		
		request.setResults(2);

		OperationQueryResult result = null;
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		result = hcpQuery.query(request);
		handleResult(result);

		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}
		
		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}
		
		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}
		
		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}

		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);

		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);

		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
		
		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}

		if (result.isIncomplete()) {
			result = hcpQuery.query(request.withNextPage());
			handleResult(result);
		}

		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
		
		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
		
		result = hcpQuery.query(request.withPrevPage());
		handleResult(result);
	}

	long i = 0;

	private void handleResult(QueryResult result) {
		List<ObjectSummary> res = result.getResults();
		for (ObjectSummary objectSummary : res) {
			print.appendRecord(++i, objectSummary.getKey(), objectSummary.getVersionId(), objectSummary.getUrlName());
		}
		print.printout();
	}
}