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
import java.util.Map;

import com.hitachivantara.hcp.query.define.Facet;

/**
 * Object-based queries return information about objects that currently exist in the repository. For objects with multiple versions, these
 * queries return information only for the current version.
 * 
 * Object-based queries return information only about objects that have been indexed.
 * 
 * @author sohan
 *
 */
public class ObjectQueryResult extends QueryResult {
	private String queryExpression;
	private Map<String, List<FacetFrequency>> facetFrequencys;

	public ObjectQueryResult() {
	}

	public String getQueryExpression() {
		return queryExpression;
	}

	public void setQueryExpression(String queryExpression) {
		this.queryExpression = queryExpression;
	}

	public void setFacetFrequencys(Map<String, List<FacetFrequency>> facetFrequencys) {
		this.facetFrequencys = facetFrequencys;
	}

	public Map<String, List<FacetFrequency>> getFacetFrequencys() {
		return facetFrequencys;
	}

	public List<FacetFrequency> getFacetFrequency(String facetName) {
		if (facetFrequencys != null) {
			return facetFrequencys.get(facetName);
		}

		return null;
	}

	public List<FacetFrequency> getFacetFrequency(Facet facet) {
		if (facetFrequencys != null) {
			return facetFrequencys.get(facet.getKeyname());
		}

		return null;
	}

}
