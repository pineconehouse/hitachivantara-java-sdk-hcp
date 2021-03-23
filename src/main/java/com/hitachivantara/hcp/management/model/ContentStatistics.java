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
package com.hitachivantara.hcp.management.model;

/**
 * The statistics data type in this section describes the statistics resource for tenants and namespaces.
 * 
 * @author sohan
 *
 */
public class ContentStatistics {
	private long objectCount;
	private long shredCount;
	private long storageCapacityUsed;
	private long shredSize;
	private long ingestedVolume;
	private long customMetadataCount;
	private long customMetadataSize;

	private Long compressedCount;
	private Long compressedSavedSize;
	private Long erasureCodedCount;
	private Long erasureCodedSize;

	public ContentStatistics(long objectCount,
			long shredCount,
			long storageCapacityUsed,
			long shredSize,
			long ingestedVolume,
			long customMetadataCount,
			long customMetadataSize,
			Long compressedCount,
			Long compressedSavedSize,
			Long erasureCodedCount,
			Long erasureCodedSize) {
		super();
		this.objectCount = objectCount;
		this.shredCount = shredCount;
		this.storageCapacityUsed = storageCapacityUsed;
		this.shredSize = shredSize;
		this.ingestedVolume = ingestedVolume;
		this.customMetadataCount = customMetadataCount;
		this.customMetadataSize = customMetadataSize;

		this.compressedCount = compressedCount;
		this.compressedSavedSize = compressedSavedSize;
		this.erasureCodedCount = erasureCodedCount;
		this.erasureCodedSize = erasureCodedSize;
	}

	/**
	 * @return Specifies the number of objects, including old versions of objects, in a given namespace or in all the namespaces owned by a
	 *         given tenant.
	 * 
	 *         Each multipart object counts as a single object. Objects that are in the process of being created by multipart uploads are not
	 *         included in the object count.
	 * 
	 *         The object count does not include object versions that are delete markers.
	 */
	public long getObjectCount() {
		return objectCount;
	}

	/**
	 * @return Specifies thetotal number of these items waiting to be shredded in a given namespace or in all the namespaces owned by a given
	 *         tenant: objects, old versions of objects, parts of multipart objects, parts of old multipart versions of objects, replaced parts
	 *         of multipart uploads, parts of aborted multipart uploads, unused parts of completed multipart uploads, and transient parts
	 *         created during the processing of certain multipart upload operations.
	 */
	public long getShredCount() {
		return shredCount;
	}

	/**
	 * @return Specifies the total number of bytes occupied by stored data in the given namespace or in all the namespaces owned by the given
	 *         tenant. This includes object data, metadata, and any redundant data required to satisfy the applicable service plans.
	 */
	public long getStorageCapacityUsed() {
		return storageCapacityUsed;
	}

	/**
	 * @return Specifies the total number of bytes of object, object version, and part data and metadata waiting to be shredded in a given
	 *         namespace or in all the namespaces owned by a given tenant.
	 */
	public long getShredSize() {
		return shredSize;
	}

	/**
	 * @return Specifies the total size of the stored data and custom metadata, in bytes, before it was added to a given namespace or to all the
	 *         namespaces owned by a given tenant.
	 */
	public long getIngestedVolume() {
		return ingestedVolume;
	}

	/**
	 * @return Specifies the number of custom metadata files stored in a given namespace or in all the namespaces owned by a given tenant.
	 */
	public long getCustomMetadataCount() {
		return customMetadataCount;
	}

	/**
	 * @return Specifies the total number of bytes of custom metadata stored in a given namespace or in all the namespaces owned by a given
	 *         tenant.
	 * 
	 * 
	 */
	public long getCustomMetadataSize() {
		return customMetadataSize;
	}

	/**
	 * @return Specifies the number of compressed objects in a given namespace or in all the namespaces owned by a given tenant. <b>This
	 *         property is included in the response body only when the request is made using a system-level user account.</b>
	 */
	public Long getCompressedCount() {
		return compressedCount;
	}

	/**
	 * @return Specifies the total number of bytes of storage freed by compression in a given namespace or in all the namespaces owned by a
	 *         given tenant. <b>This property is included in the response body only when the request is made using a system-level user
	 *         account.</b>
	 */
	public Long getCompressedSavedSize() {
		return compressedSavedSize;
	}

	/**
	 * @return Specifies the total number of erasure-coded objects and erasure-coded parts of multipart objects stored in a given namespace or
	 *         in all the namespaces owned by a given tenant.<b>This property is included in the response body only when the request is made
	 *         using a system-level user account.</b>
	 */
	public Long getErasureCodedCount() {
		return erasureCodedCount;
	}

	/**
	 * @return Specifies the total size, in bytes, of the chunks for erasure-coded objects and erasure-coded parts of multipart objects stored
	 *         in a given namespace or in all the namespaces owned by a given tenant.<b>This property is included in the response body only when
	 *         the request is made using a system-level user account.</b>
	 */
	public Long getErasureCodedSize() {
		return erasureCodedSize;
	}

}
