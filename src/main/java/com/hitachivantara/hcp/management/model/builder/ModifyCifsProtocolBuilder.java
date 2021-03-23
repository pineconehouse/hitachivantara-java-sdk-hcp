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

import com.hitachivantara.hcp.management.define.CaseForcing;
import com.hitachivantara.hcp.management.model.CifsProtocolSettings;

public class ModifyCifsProtocolBuilder extends ModifyProtocolBuilder<ModifyCifsProtocolBuilder, CifsProtocolSettings> {
	public ModifyCifsProtocolBuilder() {
		super(new CifsProtocolSettings());
	}

	public ModifyCifsProtocolBuilder withEnabled(boolean enabled) {
		settings.setEnabled(enabled);
		return this;
	}

	public ModifyCifsProtocolBuilder withCaseForcing(CaseForcing caseForcing) {
		settings.setCaseForcing(caseForcing);
		return this;
	}

	public ModifyCifsProtocolBuilder withCaseSensitive(boolean enabled) {
		settings.setCaseSensitive(enabled);
		return this;
	}

	public ModifyCifsProtocolBuilder withRequiresAuthentication(boolean enabled) {
		settings.setRequiresAuthentication(enabled);
		return this;
	}

}
