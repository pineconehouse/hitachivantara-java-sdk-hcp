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

import java.io.Serializable;

/**
 */
public class PartETag implements Serializable {

	private Integer partNumber;

	private String eTag;

	private Long size;

	public PartETag(Integer partNumber, String eTag, Long size) {
		super();
		this.partNumber = partNumber;
		this.eTag = eTag;
		this.size = size;
	}

	public Integer getPartNumber() {
		return partNumber;
	}

	public String getETag() {
		return eTag;
	}

	public Long getSize() {
		return size;
	}

}