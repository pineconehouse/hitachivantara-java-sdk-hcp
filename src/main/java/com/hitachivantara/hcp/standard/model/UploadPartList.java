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

import java.util.ArrayList;
import java.util.List;

public class UploadPartList extends HCPStandardResult {
	private String namespace;
	private String key;
	private String uploadId;
	private Initiator initiator;
	private Owner owner;
	private String storageClass;
	private Integer partNumberMarker;
	private Integer nextPartNumberMarker;
	private Integer maxParts;
	private Boolean truncated;
	private List<PartSummary> partSummarys = new ArrayList<PartSummary>();

//	public UploadPartList() {
//		// TODO Auto-generated constructor stub
//	}

	public UploadPartList(String namespace,
			String key,
			String uploadId,
			Initiator initiator,
			Owner owner,
			String storageClass,
			Integer partNumberMarker,
			Integer nextPartNumberMarker,
			Integer maxParts,
			Boolean truncated) {
		super();
		this.namespace = namespace;
		this.key = key;
		this.uploadId = uploadId;
		this.initiator = initiator;
		this.owner = owner;
		this.storageClass = storageClass;
		this.partNumberMarker = partNumberMarker;
		this.nextPartNumberMarker = nextPartNumberMarker;
		this.maxParts = maxParts;
		this.truncated = truncated;
	}

	public boolean add(PartSummary e) {
		return partSummarys.add(e);
	}

	public String getNamespace() {
		return namespace;
	}

	public String getKey() {
		return key;
	}

	public String getUploadId() {
		return uploadId;
	}

	public Initiator getInitiator() {
		return initiator;
	}

	public Owner getOwner() {
		return owner;
	}

	public String getStorageClass() {
		return storageClass;
	}

	public Integer getPartNumberMarker() {
		return partNumberMarker;
	}

	public Integer getNextPartNumberMarker() {
		return nextPartNumberMarker;
	}

	public Integer getMaxParts() {
		return maxParts;
	}

	public Boolean getTruncated() {
		return truncated;
	}

	public List<PartSummary> getPartSummarys() {
		return partSummarys;
	}

}
