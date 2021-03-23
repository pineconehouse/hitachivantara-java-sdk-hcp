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

import com.hitachivantara.hcp.management.define.RetentionMode;

public class NamespaceBasicSetting {
	private String name;
	private String nameIDNA;
	private boolean versioningEnabled;
	private boolean searchEnabled;
	private RetentionMode retentionMode;
	private boolean defaultShredValue;
	private boolean defaultIndexValue;
	private String defaultRetentionValue;
	private String hashScheme;
	private String dpl;
	private String description;

	public NamespaceBasicSetting(String name,
			String nameIDNA,
			boolean versioningEnabled,
			boolean searchEnabled,
			RetentionMode retentionMode,
			boolean defaultShredValue,
			boolean defaultIndexValue,
			String defaultRetentionValue,
			String hashScheme,
			String dpl,
			String description) {
		super();
		this.name = name;
		this.nameIDNA = nameIDNA;
		this.versioningEnabled = versioningEnabled;
		this.searchEnabled = searchEnabled;
		this.retentionMode = retentionMode;
		this.defaultShredValue = defaultShredValue;
		this.defaultIndexValue = defaultIndexValue;
		this.defaultRetentionValue = defaultRetentionValue;
		this.hashScheme = hashScheme;
		this.dpl = dpl;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getNameIDNA() {
		return nameIDNA;
	}

	public boolean isVersioningEnabled() {
		return versioningEnabled;
	}

	public boolean isSearchEnabled() {
		return searchEnabled;
	}

	public RetentionMode getRetentionMode() {
		return retentionMode;
	}

	public boolean isDefaultShredValue() {
		return defaultShredValue;
	}

	public boolean isDefaultIndexValue() {
		return defaultIndexValue;
	}

	public String getDefaultRetentionValue() {
		return defaultRetentionValue;
	}

	public String getHashScheme() {
		return hashScheme;
	}

	public String getDpl() {
		return dpl;
	}

	public String getDescription() {
		return description;
	}

}
