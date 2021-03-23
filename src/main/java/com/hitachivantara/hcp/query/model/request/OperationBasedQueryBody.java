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

import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.query.define.ObjectProperty;
import com.hitachivantara.hcp.query.define.Transaction;
import com.hitachivantara.hcp.query.model.RequestBody;

/**
 * Object-based queries.
 * 
 * Operation-based queries search for objects based on any combination of create, delete, and disposition operations and, for HCP namespaces
 * that support versioning, purge and prune operations. Operation-based queries are useful for applications that need to track changes to
 * namespace content.
 * <p>
 * In response to an operation-based query, HCP returns a set of operation records, each of which identifies an object and an operation on
 * the object and contains additional metadata for the object.
 * 
 * @author sohan
 *
 */
public class OperationBasedQueryBody implements RequestBody {

	/**
	 * One of:
	 * 
	 * -1, to request all results
	 * 
	 * 0 to request a response that includes only the object count and, if requested, content properties and facets.
	 * 
	 * An integer between one and 10,000
	 */
	private Integer results = null;

	private class LastResult {
		private Integer results = null;
		private String urlName = null;
		private String changeTimeMilliseconds = null;
		private String version = null;
	}

	private final List<LastResult> lastResults = new ArrayList<LastResult>();
	private LastResult lastResult;

	/**
	 * A comma-separated list of object properties
	 */
	private final List<ObjectProperty> objectProperties = new ArrayList<ObjectProperty>();

	private class SystemMetadata {
		private Boolean indexable = null;
		private Boolean replicationCollision = null;
		private Long changeTimeStart = null;
		private Long changeTimeEnd = null;
		private final List<String> directories = new ArrayList<String>();
		private final List<String> namespaces = new ArrayList<String>();
		private final List<Transaction> transactions = new ArrayList<Transaction>();

		public boolean containValues() {
			return indexable != null
					|| replicationCollision != null
					|| changeTimeStart != null
					|| changeTimeEnd != null
					|| directories.size() > 0
					|| namespaces.size() > 0
					|| transactions.size() > 0;
		}

	}

	private final SystemMetadata systemMetadata = new SystemMetadata();

	public void setLastResult(Integer results, String urlName, Long changeTimeMilliseconds, String version) {
		LastResult lr = new LastResult();

		lr.results = results;
		lr.urlName = urlName;
		lr.changeTimeMilliseconds = (changeTimeMilliseconds != null ? (changeTimeMilliseconds.toString() + ".00") : null);
		lr.version = version;

		this.lastResults.add(lr);
	}

	/**
	 * One of:
	 * 
	 * true - Return all object properties.
	 * 
	 * false - Return only the object URL, version ID, operation, and change time.
	 */
	private Boolean verbose = null;

	public Boolean getIndexable() {
		return systemMetadata.indexable;
	}

	public void setIndexable(Boolean indexable) {
		this.systemMetadata.indexable = indexable;
	}

	public Boolean getReplicationCollision() {
		return systemMetadata.replicationCollision;
	}

	public void setReplicationCollision(Boolean replicationCollision) {
		this.systemMetadata.replicationCollision = replicationCollision;
	}

	public Long getChangeTimeStart() {
		return systemMetadata.changeTimeStart;
	}

	public void setChangeTimeStart(Long changeTimeStart) {
		this.systemMetadata.changeTimeStart = changeTimeStart;
	}

	public Long getChangeTimeEnd() {
		return systemMetadata.changeTimeEnd;
	}

	public void setChangeTimeEnd(Long changeTimeEnd) {
		this.systemMetadata.changeTimeEnd = changeTimeEnd;
	}

	public List<String> getDirectories() {
		return systemMetadata.directories;
	}

	public void addDirectory(String dirKey) {
		this.systemMetadata.directories.add(dirKey);
	}

	public List<String> getNamespaces() {
		return this.systemMetadata.namespaces;
	}

	public void addNamespace(String namespace) {
		this.systemMetadata.namespaces.add(namespace);
	}

	public List<Transaction> getTransactions() {
		return this.systemMetadata.transactions;
	}

	public void addTransaction(Transaction transaction) {
		this.systemMetadata.transactions.add(transaction);
	}

	public int getResults() {
		return results;
	}

	/**
	 * @param resultsPerPage
	 *            One of:
	 *            <p>
	 * 
	 *            -1, to request all results
	 *            <p>
	 * 
	 *            0 to request a response that includes only the object count and, if requested, content properties and facets.
	 *            <p>
	 * 
	 *            An integer between one and 10,000
	 *            <p>
	 */
	public void setResults(Integer resultsPerPage) {
		if (resultsPerPage != null) {
			if (resultsPerPage < -1) {
				resultsPerPage = -1;
			} else if (resultsPerPage > 10000) {
				resultsPerPage = 10000;
			}
		}

		this.results = resultsPerPage;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public List<ObjectProperty> getObjectProperties() {
		return objectProperties;
	}

	public void addProperty(ObjectProperty objectProperty) {
		if (objectProperty != null) {
			this.objectProperties.add(objectProperty);
		}
	}

	@Override
	public String toString() {
		return build();
	}

	public void nextOffset() {
		if (this.lastResults.size() > 0) {
			int lastIndex = this.lastResults.size() - 1;
			this.lastResult = this.lastResults.get(lastIndex);
			this.results = this.lastResult.results;
		} else {
			this.lastResult = null;
		}
	}

	public void prevOffset() {
		if (this.lastResult != null && this.lastResults.size() > 2) {
			this.lastResults.remove(this.lastResults.size() - 1);
			this.lastResults.remove(this.lastResults.size() - 1);
			this.lastResult = this.lastResults.get(this.lastResults.size() - 1);
			this.results = this.lastResult.results;
		} else {
			this.lastResult = null;
		}
	}

	@Override
	public void resetOffset() {
		this.lastResults.clear();
		this.lastResult = null;
	}

	public String build() {
		StringBuilder buf = new StringBuilder();

		buf.append("<queryRequest> \n");
		buf.append(" <operation> \n");

		if (results != null) {
			buf.append("  <count>" + results.toString() + "</count> \n");
		}

		if (lastResult != null) {
			buf.append("  <lastResult> \n");
			if (lastResult.urlName != null) {
				buf.append("   <urlName>" + lastResult.urlName + "</urlName> \n");
			}
			if (lastResult.changeTimeMilliseconds != null) {
				buf.append("   <changeTimeMilliseconds>" + lastResult.changeTimeMilliseconds + "</changeTimeMilliseconds> \n");
			}
			if (lastResult.version != null) {
				buf.append("   <version>" + lastResult.version + "</version> \n");
			}
			buf.append("  </lastResult> \n");
		}

		int length = objectProperties.size();
		if (length > 0) {
			buf.append("  <objectProperties>");
			for (int i = 0; i < length - 1; i++) {
				ObjectProperty op = objectProperties.get(i);
				buf.append(op.getKeyname());
				buf.append(",");
			}
			buf.append(objectProperties.get(length - 1).getKeyname());
			buf.append("  </objectProperties> \n");
		}

		if (systemMetadata.containValues()) {
			buf.append("  <systemMetadata> \n");

			if (systemMetadata.changeTimeStart != null || systemMetadata.changeTimeEnd != null) {
				buf.append("   <changeTime> \n");
				if (systemMetadata.changeTimeStart != null) {
					buf.append("    <start>" + systemMetadata.changeTimeStart + "</start> \n");
				}
				if (systemMetadata.changeTimeEnd != null) {
					buf.append("    <end>" + systemMetadata.changeTimeEnd + "</end> \n");
				}
				buf.append("   </changeTime> \n");
			}

			if (systemMetadata.directories.size() > 0) {
				buf.append("   <directories> \n");
				for (String dir : systemMetadata.directories) {
					buf.append("    <directory>" + dir + "</directory> \n");
				}
				buf.append("   </directories> \n");
			}

			if (systemMetadata.namespaces.size() > 0) {
				buf.append("   <namespaces> \n");
				for (String namespace : systemMetadata.namespaces) {
					buf.append("    <namespace>" + namespace + "</namespace> \n");
				}
				buf.append("   </namespaces> \n");
			}

			if (systemMetadata.transactions.size() > 0) {
				buf.append("   <transactions> \n");
				for (Transaction transaction : systemMetadata.transactions) {
					buf.append("    <transaction>" + transaction.name() + "</transaction> \n");
				}
				buf.append("   </transactions> \n");
			}

			if (systemMetadata.indexable != null) {
				buf.append("   <indexable>" + systemMetadata.indexable + "</indexable> \n");
			}

			if (systemMetadata.replicationCollision != null) {
				buf.append("   <replicationCollision>" + systemMetadata.replicationCollision + "</replicationCollision> \n");
			}

			buf.append("  </systemMetadata> \n");
		}

		if (verbose != null) {
			buf.append("  <verbose>" + verbose + "</verbose> \n");
		}

		buf.append(" </operation> \n");
		buf.append("</queryRequest>");

		return buf.toString();
	}

	public void validParameter() throws InvalidParameterException {
		// TODO Auto-generated method stub
		// ValidUtils.exceptionWhenEmpty(query, "Query expression must be specificed.");
	}

}
