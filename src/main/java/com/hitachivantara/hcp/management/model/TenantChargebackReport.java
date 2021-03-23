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

public class TenantChargebackReport {
	private String systemName;
	private String tenantName;
	private String namespaceName;
	private String startTime;// 2018-03-10T13:00:00+0800",
	private String endTime;// 2018-09-06T13:36:23+0800",
	private boolean valid;
	private boolean deleted;
	private long objectCount;
	private long storageCapacityUsed;
	private long ingestedVolume;
	private long writes;
	private long deletes;
	private long bytesIn;
	private long reads;
	private long bytesOut;
	private long multipartObjects;
	private long multipartUploadParts;
	private long multipartUploadBytes;
	private long multipartUploads;
	private long multipartObjectParts;
	private long multipartObjectBytes;

	public TenantChargebackReport(String systemName,
			String tenantName,
			String namespaceName,
			String startTime,
			String endTime,
			boolean valid,
			boolean deleted,
			long objectCount,
			long storageCapacityUsed,
			long ingestedVolume,
			long writes,
			long deletes,
			long bytesIn,
			long reads,
			long bytesOut,
			long multipartObjects,
			long multipartUploadParts,
			long multipartUploadBytes,
			long multipartUploads,
			long multipartObjectParts,
			long multipartObjectBytes) {
		super();
		this.systemName = systemName;
		this.tenantName = tenantName;
		this.namespaceName = namespaceName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.valid = valid;
		this.deleted = deleted;
		this.objectCount = objectCount;
		this.storageCapacityUsed = storageCapacityUsed;
		this.ingestedVolume = ingestedVolume;
		this.writes = writes;
		this.deletes = deletes;
		this.bytesIn = bytesIn;
		this.reads = reads;
		this.bytesOut = bytesOut;
		this.multipartObjects = multipartObjects;
		this.multipartUploadParts = multipartUploadParts;
		this.multipartUploadBytes = multipartUploadBytes;
		this.multipartUploads = multipartUploads;
		this.multipartObjectParts = multipartObjectParts;
		this.multipartObjectBytes = multipartObjectBytes;
	}

	public String getSystemName() {
		return systemName;
	}

	public String getTenantName() {
		return tenantName;
	}

	public String getNamespaceName() {
		return namespaceName;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public long getObjectCount() {
		return objectCount;
	}

	public long getStorageCapacityUsed() {
		return storageCapacityUsed;
	}

	public long getIngestedVolume() {
		return ingestedVolume;
	}

	public long getWrites() {
		return writes;
	}

	public long getDeletes() {
		return deletes;
	}

	public long getBytesIn() {
		return bytesIn;
	}

	public long getReads() {
		return reads;
	}

	public long getBytesOut() {
		return bytesOut;
	}

	public long getMultipartObjects() {
		return multipartObjects;
	}

	public long getMultipartUploadParts() {
		return multipartUploadParts;
	}

	public long getMultipartUploadBytes() {
		return multipartUploadBytes;
	}

	public long getMultipartUploads() {
		return multipartUploads;
	}

	public long getMultipartObjectParts() {
		return multipartObjectParts;
	}

	public long getMultipartObjectBytes() {
		return multipartObjectBytes;
	}

}
