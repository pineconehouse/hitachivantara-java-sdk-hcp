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

import com.hitachivantara.hcp.standard.define.ObjectState;

/**
 * @author sohan
 *
 */
public class HCPObjectEntry extends HCPObjectSummary {
	private String urlName;
	private ObjectState state; // "created"
	private String symlinkTarget;

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public ObjectState getState() {
		return state;
	}

	public void setState(ObjectState state) {
		this.state = state;
	}

	public String getSymlinkTarget() {
		return symlinkTarget;
	}

	public void setSymlinkTarget(String symlinkTarget) {
		this.symlinkTarget = symlinkTarget;
	}

}
