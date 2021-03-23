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

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.query.define.Facet;
import com.hitachivantara.hcp.query.define.ObjectProperty;
import com.hitachivantara.hcp.query.define.Order;
import com.hitachivantara.hcp.query.model.OrderBy;

public class ObjectBasedQueryRequest extends QueryRequest<ObjectBasedQueryRequest, ObjectBasedQueryBody> {
	/**
	 * The body of an object-based query request consists of entries in XML or JSON format.
	 */
	private final ObjectBasedQueryBody body = new ObjectBasedQueryBody();

	public ObjectBasedQueryRequest() {
		this.withRequestBody(body);
	}

	public String getQuery() {
		return body.getQuery();
	}

	public void setQuery(String query) {
		body.setQuery(query);
	}

	public boolean isContentProperties() {
		return body.isContentProperties();
	}

	public void setContentProperties(boolean contentProperties) {
		body.setContentProperties(contentProperties);
	}

	public int getResults() {
		return body.getResults();
	}

	public void setResults(int resultsPerPage) {
		body.setResults(resultsPerPage);
	}

	public List<Facet> getFacets() {
		return body.getFacets();
	}

	public void addFacet(Facet facet) {
		body.addFacet(facet);
	}

	public int getOffset() {
		return body.getOffset();
	}

	public void setOffset(int resultOffset) {
		body.setOffset(resultOffset);
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

	public List<OrderBy> getSort() {
		return body.getSort();
	}

	public void addSort(ObjectProperty objectProperty, Order sortType) {
		body.addSort(objectProperty, sortType);
	}

	public void addSort(ObjectProperty objectProperty) throws InvalidParameterException {
		body.addSort(objectProperty);
	}

	public void nextOffset() {
		body.nextOffset();
	}

	public void prevOffset() {
		body.prevOffset();
	}

}
