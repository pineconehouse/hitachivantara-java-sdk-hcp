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

public class RetentionClasses {
	private String name;
	private String value;
	private boolean autoDelete;
	private String description;

	public RetentionClasses(String name, String value, boolean autoDelete, String description) {
		super();
		this.name = name;
		this.value = value;
		this.autoDelete = autoDelete;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public boolean isAutoDelete() {
		return autoDelete;
	}

	public String getDescription() {
		return description;
	}

}
