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

import com.hitachivantara.hcp.management.define.Protocols;

public abstract class ProtocolSettings {
	private final Protocols protocol;

	private IPSettings ipSettings;// = new IPSettings();

	public ProtocolSettings(Protocols protocol) {
		this.protocol = protocol;
	}

	public ProtocolSettings(Protocols protocol, IPSettings ipSettings) {
		super();
		this.protocol = protocol;
		this.ipSettings = ipSettings;
	}

	public IPSettings getIpSettings() {
		return ipSettings;
	}

	public void setIpSettings(IPSettings ipSettings) {
		this.ipSettings = ipSettings;
	}

	public Protocols getNamespaceProtocol() {
		return protocol;
	}

}
