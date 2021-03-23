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
package com.hitachivantara.hcp.standard.model.metadata;

import java.io.Serializable;

public class Annotation implements Cloneable, Serializable {
	private String name;
	private Long size;

	public Annotation(String metadataName, Long size) {
		this.name = metadataName;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public Long getSize() {
		return size;
	}

	@Override
	public String toString() {
		return name + ";" + size;
	}

	// public void setSize(Long size) {
	// this.size = size;
	// }

	// public void setName(String name) {
	// this.metadataName = name;
	// }

}
