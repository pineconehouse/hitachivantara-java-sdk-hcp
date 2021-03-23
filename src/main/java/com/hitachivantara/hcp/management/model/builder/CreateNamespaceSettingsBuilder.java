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
package com.hitachivantara.hcp.management.model.builder;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.management.define.HashScheme;
import com.hitachivantara.hcp.management.model.NamespaceSettings;

public class CreateNamespaceSettingsBuilder extends BasicNamespaceSettingsBuilder<CreateNamespaceSettingsBuilder> {
	public CreateNamespaceSettingsBuilder() {
	}

	public CreateNamespaceSettingsBuilder withHashScheme(HashScheme hashScheme) {
		ns.setHashScheme(hashScheme);
		return this;
	}

	public CreateNamespaceSettingsBuilder withEnterpriseMode(boolean enabled) {
		ns.setEnterpriseMode(enabled);
		return this;
	}

	public CreateNamespaceSettingsBuilder withVersioningEnabled(boolean enabled) {
		versioningSettings.setEnabled(enabled);
		ns.setVersioningSettings(versioningSettings);
		return this;
	}

	public CreateNamespaceSettingsBuilder withVersioningPrune(boolean prune) {
		versioningSettings.setPrune(prune);
		ns.setVersioningSettings(versioningSettings);
		return this;
	}

	public CreateNamespaceSettingsBuilder withVersioningKeepDeletionRecords(boolean keepDeletionRecords) {
		versioningSettings.setKeepDeletionRecords(keepDeletionRecords);
		ns.setVersioningSettings(versioningSettings);
		return this;
	}

	public CreateNamespaceSettingsBuilder withVersioningPruneDays(int pruneDays) {
		versioningSettings.setPruneDays(pruneDays);
		ns.setVersioningSettings(versioningSettings);
		return this;
	}

	@Override
	public NamespaceSettings bulid() throws HSCException, InvalidParameterException {
		ValidUtils.invalidIfEmpty(ns.getName(), "Need to specifies the name of the namespace.");
		return ns;
	}


}
