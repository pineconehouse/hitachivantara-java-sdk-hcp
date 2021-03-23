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

import com.hitachivantara.hcp.management.define.CaseForcing;
import com.hitachivantara.hcp.management.define.Protocols;

public class CifsProtocolSettings extends ProtocolSettings {

	private Boolean enabled;
	private Boolean caseSensitive;
	private CaseForcing caseForcing;
	private Boolean requiresAuthentication;

	public CifsProtocolSettings() {
		super(Protocols.CIFS);
	}

	public CifsProtocolSettings(Boolean enabled, Boolean caseSensitive, CaseForcing caseForcing, Boolean requiresAuthentication, IPSettings ipSettings) {
		super(Protocols.CIFS, ipSettings);
		this.enabled = enabled;
		this.caseSensitive = caseSensitive;
		this.caseForcing = caseForcing;
		this.requiresAuthentication = requiresAuthentication;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(Boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public CaseForcing getCaseForcing() {
		return caseForcing;
	}

	public void setCaseForcing(CaseForcing caseForcing) {
		this.caseForcing = caseForcing;
	}

	public Boolean getRequiresAuthentication() {
		return requiresAuthentication;
	}

	public void setRequiresAuthentication(Boolean requiresAuthentication) {
		this.requiresAuthentication = requiresAuthentication;
	}

}
