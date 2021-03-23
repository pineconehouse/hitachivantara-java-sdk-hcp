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
package com.hitachivantara.hcp.standard.model;

public class NamespaceStatistics {
	/**
	 * The namespace name.
	 */
	private String namespaceName;

	/**
	 * The number of bytes of storage allocated to the namespace. This is the total space available for all data stored in the namespace,
	 * including object data, metadata, and the redundant data required to satisfy the namespace DPL.
	 */
	private long totalCapacityBytes;

	/**
	 * The number of bytes currently occupied by all data stored in the namespace, including object data, metadata, and any redundant data
	 * required to satisfy the namespace DPL.
	 */
	private long usedCapacityBytes;

	/**
	 * The percent of the totalCapacityBytes value at which HCP notifies the tenant that free storage space for the namespace is running low.
	 */
	private double softQuotaPercent;

	/**
	 * The number of objects, including old versions of objects, stored in the namespace.
	 * 
	 * The object count includes versions of objects that were deleted, but not delete markers.For example,if you create an object, add a
	 * version, delete the object while versioning is enabled but without specifying a version ID, and then add a new version of the object, the
	 * object count increases by three.
	 */
	private long objectCount;

	/**
	 * The number of objects and versions that have been deleted and are waiting to be shredded.
	 */
	private long shredObjectCount;

	/**
	 * The number of bytes occupied by the objects and versions that are waiting to be shredded.
	 */
	private long shredObjectBytes;

	/**
	 * The number of objects and versions that have one or more annotations.
	 */
	private long customMetadataObjectCount;

	/**
	 * The number of bytes occupied by custom metadata in all annotations for all objects.
	 */
	private long customMetadataObjectBytes;

	public NamespaceStatistics(String namespaceName,
			long totalCapacityBytes,
			long usedCapacityBytes,
			double softQuotaPercent,
			long objectCount,
			long shredObjectCount,
			long shredObjectBytes,
			long customMetadataObjectCount,
			long customMetadataObjectBytes) {
		super();
		this.namespaceName = namespaceName;
		this.totalCapacityBytes = totalCapacityBytes;
		this.usedCapacityBytes = usedCapacityBytes;
		this.softQuotaPercent = softQuotaPercent;
		this.objectCount = objectCount;
		this.shredObjectCount = shredObjectCount;
		this.shredObjectBytes = shredObjectBytes;
		this.customMetadataObjectCount = customMetadataObjectCount;
		this.customMetadataObjectBytes = customMetadataObjectBytes;
	}

	public String getNamespaceName() {
		return namespaceName;
	}

	public long getTotalCapacityBytes() {
		return totalCapacityBytes;
	}

	public long getUsedCapacityBytes() {
		return usedCapacityBytes;
	}

	public double getSoftQuotaPercent() {
		return softQuotaPercent;
	}

	public long getObjectCount() {
		return objectCount;
	}

	public long getShredObjectCount() {
		return shredObjectCount;
	}

	public long getShredObjectBytes() {
		return shredObjectBytes;
	}

	public long getCustomMetadataObjectCount() {
		return customMetadataObjectCount;
	}

	public long getCustomMetadataObjectBytes() {
		return customMetadataObjectBytes;
	}



}
