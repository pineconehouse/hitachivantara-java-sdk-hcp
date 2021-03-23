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

public class NfsProtocolSettings extends ProtocolSettings {

	private Boolean enabled;
	private Integer uid;
	private Integer gid;

	public NfsProtocolSettings() {
		super(Protocols.NFS);
	}

	public NfsProtocolSettings(Boolean enabled, Integer uid, Integer gid, IPSettings ipSettings) {
		super(Protocols.NFS, ipSettings);
		this.enabled = enabled;
		this.uid = uid;
		this.gid = gid;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

}
