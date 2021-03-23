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
package com.hitachivantara.hcp.query.define;

public class Facet {

	/**
	 * Returns the numbers of objects in the result set that are on hold and not on hold.
	 */
	public final static Facet hold = new Facet("hold");

	/**
	 * Returns the names of namespaces that contain objects in the result set and the number of objects in the result set in each of those
	 * namespaces.
	 */
	public final static Facet namespace = new Facet("namespace");

	/**
	 * For each of these retention values, returns the number of objects in the result set that have that value:
	 * 
	 * initialUnspecified - For objects with a retention setting of Initial Unspecified
	 * 
	 * neverDeletable - For objects with a retention setting of Deletion Prohibited
	 * 
	 * expired - For objects with a retention setting that is Deletion Allowed or a specific date in the past
	 * 
	 * not expired - For objects with a retention setting that is a specific date in the future
	 */
	public final static Facet retention = new Facet("retention");

	/**
	 * Returns the retention classes that are retention settings for objects in the result set and the number of objects in each retention
	 * class.
	 * 
	 * The count of objects in a retention class can include objects from more than one namespace. This is because multiple namespaces can have
	 * retention classes with the same name. To get an accurate count of the objects in a namespace that are in a specific retention class,
	 * restrict the query to a single namespace.
	 */
	public final static Facet retentionClass = new Facet("retentionClass");

	/**
	 * For Boolean and string content properties, returns the number of objects with the specified property value. For numeric and date
	 * properties, returns the number of objects in ranges of values.
	 * 
	 * You cannot use tokenized (full-text searchable) content properties with facets.
	 * 
	 * For information on specifying ranges for numeric and date content properties, see Content property facet ranges below.
	 * 
	 */
//	public final static Facet any = new Facet(null);
	
	private String keyname;

	public Facet(String keyname) {
		this.keyname = keyname;
	}

	public String getKeyname() {
		return keyname;
	}

}
