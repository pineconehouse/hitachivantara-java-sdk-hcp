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
package com.hitachivantara.hcp.query.model;

import java.util.List;

import com.hitachivantara.hcp.query.model.QueryStatus.ResultCode;

public class QueryResult {
	private List<ObjectSummary> results;
	private QueryStatus status;

	public QueryResult() {
	}

	public List<ObjectSummary> getResults() {
		return results;
	}

	public void setResults(List<ObjectSummary> results) {
		this.results = results;
	}

	public QueryStatus getStatus() {
		return status;
	}

	public void setStatus(QueryStatus status) {
		this.status = status;
	}

	public boolean isIncomplete() {
		return status.getCode() != ResultCode.COMPLETE;
	}
}
