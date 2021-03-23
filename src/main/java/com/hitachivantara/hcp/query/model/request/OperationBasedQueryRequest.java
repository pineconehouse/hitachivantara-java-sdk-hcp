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
package com.hitachivantara.hcp.query.model.request;

import java.util.List;

import com.hitachivantara.hcp.query.define.ObjectProperty;
import com.hitachivantara.hcp.query.define.Transaction;

public class OperationBasedQueryRequest extends QueryRequest<OperationBasedQueryRequest, OperationBasedQueryBody> {
	/**
	 * The body of an operation-based query request consists of entries in XML or JSON format.
	 */
	private final OperationBasedQueryBody body = new OperationBasedQueryBody();

	public OperationBasedQueryRequest() {
		this.withRequestBody(body);
	}

	public Boolean getIndexable() {
		return body.getIndexable();
	}

	public void setIndexable(Boolean indexable) {
		body.setIndexable(indexable);
	}

	public Boolean getReplicationCollision() {
		return body.getReplicationCollision();
	}

	public void setReplicationCollision(Boolean replicationCollision) {
		body.setReplicationCollision(replicationCollision);
	}

	public Long getChangeTimeStart() {
		return body.getChangeTimeStart();
	}

	public void setChangeTimeStart(Long changeTimeStart) {
		body.setChangeTimeStart(changeTimeStart);
	}

	public Long getChangeTimeEnd() {
		return body.getChangeTimeEnd();
	}

	public void setChangeTimeEnd(Long changeTimeEnd) {
		body.setChangeTimeEnd(changeTimeEnd);
	}

	public List<String> getDirectories() {
		return body.getDirectories();
	}

	public void addDirectory(String dirKey) {
		body.addDirectory(dirKey);
	}

	public List<String> getNamespaces() {
		return body.getNamespaces();
	}

	public void addNamespace(String namespace) {
		body.addNamespace(namespace);
	}

	public List<Transaction> getTransactions() {
		return body.getTransactions();
	}

	public void addTransaction(Transaction transaction) {
		body.addTransaction(transaction);
	}

	public int getResults() {
		return body.getResults();
	}

	public void setResults(int resultsPerPage) {
		body.setResults(resultsPerPage);
	}

	public boolean isVerbose() {
		return body.isVerbose();
	}

	public void setVerbose(boolean verbose) {
		body.setVerbose(verbose);
	}

	public List<ObjectProperty> getObjectProperties() {
		return body.getObjectProperties();
	}

	public void addProperty(ObjectProperty objectProperty) {
		body.addProperty(objectProperty);
	}

}
