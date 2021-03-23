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
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.query.define.Facet;
import com.hitachivantara.hcp.query.define.ObjectProperty;
import com.hitachivantara.hcp.query.define.Order;
import com.hitachivantara.hcp.query.model.OrderBy;
import com.hitachivantara.hcp.query.model.RequestBody;

/**
 * Object-based queries.
 * 
 * Object-based queries search for objects currently in the repository based on any combination of system metadata, object paths, custom
 * metadata that’s well-formed XML, ACLs, and content properties. With object-based queries, you use a robust query language to construct
 * query criteria.
 * <p>
 * In response to an object-based query, HCP returns a set of results, each of which identifies an object and contains metadata for the
 * object. With object-based queries, you can specify sort criteria to manage the order in which results are returned. You can specify facet
 * criteria to return summary information about object properties that appear in the result set.
 * 
 * @author sohan
 *
 */
public class ObjectBasedQueryBody implements RequestBody {

	/**
	 * A query expression
	 */
	private String query;

	/**
	 * One of:
	 * 
	 * true - Return information for all content properties.
	 * 
	 * false - Do not return any information on content properties.
	 */
	private Boolean contentProperties = null;

	/**
	 * One of:
	 * 
	 * -1, to request all results
	 * 
	 * 0 to request a response that includes only the object count and, if requested, content properties and facets.
	 * 
	 * An integer between one and 10,000
	 */
	private Integer results = 100;

	/**
	 * A comma-separated list of zero or more of:
	 * 
	 * hold
	 * 
	 * namespace
	 * 
	 * retention
	 * 
	 * retentionClass
	 * 
	 * content-property-name
	 */
	private final List<Facet> facets = new ArrayList<Facet>();

	/**
	 * A comma-separated list of object properties
	 */
	private final List<ObjectProperty> objectProperties = new ArrayList<ObjectProperty>();

	/**
	 * An integer between zero and 100,000
	 */
	private Integer resultOffset = 0;

	/**
	 * A comma-separated list of object properties and content properties with optional sort-order indicators
	 */
	private final List<OrderBy> sort = new ArrayList<OrderBy>();

	/**
	 * One of:
	 * 
	 * true - Return all object properties.
	 * 
	 * false - Return only the object URL, version ID, operation, and change time.
	 */
	private Boolean verbose = null;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isContentProperties() {
		return contentProperties;
	}

	public void setContentProperties(boolean contentProperties) {
		this.contentProperties = contentProperties;
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
		if (resultsPerPage == null || resultsPerPage > 10000) {
			resultsPerPage = 10000;
		} else if (resultsPerPage < -1) {
			resultsPerPage = -1;
		}

		this.results = resultsPerPage;
	}

	public List<Facet> getFacets() {
		return facets;
	}

	public void addFacet(Facet facet) {
		this.facets.add(facet);
	}

	public int getOffset() {
		return resultOffset;
	}

	/**
	 * Skips the specified number of object entries in the complete result set. Specify this entry when you’re performing a paged query.
	 * 
	 * The default is zero.
	 * 
	 * @param resultOffset
	 *            An integer between zero and 100,000
	 */
	public void setOffset(int resultOffset) {
		// An integer between zero and 100,000
		if (resultOffset > 100000) {
			resultOffset = 100000;
		} else if (resultOffset < 0) {
			resultOffset = 0;
		}

		this.resultOffset = resultOffset;
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

	public void addProperty(ObjectProperty objectProperty) {// throws InvalidParameterException {
		if (objectProperty != null) {
			// ValidUtils.exceptionIfTrue(objectProperty.getKeyname().equalsIgnoreCase(ObjectProperty.customMetadataContent.getKeyname()),
			// "This property can be used only in queries. It cannot be used in objectProperties.");

			this.objectProperties.add(objectProperty);
		}
	}

	public List<OrderBy> getSort() {
		return sort;
	}

	public void addSort(ObjectProperty objectProperty, Order sortType) {// throws InvalidParameterException {
		if (objectProperty != null) {
			// ValidUtils.exceptionIfTrue(objectProperty.getKeyname().equalsIgnoreCase(ObjectProperty.customMetadataContent.getKeyname()),
			// "This property can be used only in queries. It cannot be used in sort.");

			this.sort.add(new OrderBy(objectProperty, sortType));
		}
	}

	public void addSort(ObjectProperty objectProperty) throws InvalidParameterException {
		addSort(objectProperty, null);
	}

	@Override
	public String toString() {
		return build();
	}

	public void nextOffset() {
		if (this.results != null && this.results > 0) {
			this.setOffset(resultOffset + results);
		}
	}

	public void prevOffset() {
		if (this.results != null && this.results > 0) {
			this.setOffset(resultOffset - results);
		}
	}

	@Override
	public void resetOffset() {
		this.resultOffset = 0;
	}

	public String build() {
		StringBuilder buf = new StringBuilder();

		buf.append("<queryRequest> \n");
		buf.append("  <object> \n");
		buf.append("    <query>" + query + "</query> \n");
		if (contentProperties != null) {
			buf.append("    <contentProperties>" + contentProperties.toString() + "</contentProperties> \n");
		}
		if (results != null) {
			buf.append("    <count>" + results.toString() + "</count> \n");
		}
		// if (facets.size() > 0) {
		// buf.append(" <facets>");
		// for (Iterator<Facet> it = facets.keySet().iterator(); it.hasNext();) {
		// Facet facet = (Facet) it.next();
		// buf.append(facet.getKeyname());
		// Object value = facets.get(facet);
		// if (value != null) {
		//// buf.append(value.toString());
		// }
		// }
		// buf.append("</facets>");
		// }
		int length = facets.size();
		if (length > 0) {
			buf.append("    <facets>");
			for (int i = 0; i < length - 1; i++) {
				Facet f = facets.get(i);
				buf.append(f.getKeyname());
				buf.append(",");
			}
			buf.append(facets.get(length - 1).getKeyname());
			buf.append("</facets> \n");
		}

		length = objectProperties.size();
		if (length > 0) {
			buf.append("    <objectProperties>");
			for (int i = 0; i < length - 1; i++) {
				ObjectProperty op = objectProperties.get(i);
				buf.append(op.getKeyname());
				buf.append(",");
			}
			buf.append(objectProperties.get(length - 1).getKeyname());
			buf.append("</objectProperties> \n");
		}
		if (resultOffset != null) {
			buf.append("    <offset>" + resultOffset.toString() + "</offset> \n");
		}
		length = sort.size();
		if (length > 0) {
			// ObjectProperty, SortType
			buf.append("    <sort>");
			for (int i = 0; i < sort.size(); i++) {
				OrderBy orderBy = sort.get(i);
				ObjectProperty objectProperty = orderBy.getObjectProperty();
				Order sortType = orderBy.getOrder();
				buf.append(objectProperty.getKeyname());
				if (sortType != null) {
					buf.append("+" + sortType.name());
				}

				if (i != sort.size() - 1) {
					buf.append(",");
				}
			}
			buf.append("</sort> \n");
		}
		if (verbose != null) {
			buf.append("    <verbose>" + verbose + "</verbose> \n");
		}
		buf.append("  </object> \n");
		buf.append("</queryRequest>");

		return buf.toString();
	}

	public void validParameter() throws InvalidParameterException {
		// TODO Auto-generated method stub
		ValidUtils.invalidIfEmpty(query, "Query expression must be specificed.");
	}

}
