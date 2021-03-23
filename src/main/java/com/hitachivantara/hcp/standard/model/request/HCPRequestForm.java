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
package com.hitachivantara.hcp.standard.model.request;

import com.amituofo.common.ex.BuildException;
import com.hitachivantara.core.http.model.HttpForm;
import com.hitachivantara.hcp.standard.define.RequestParamKey;
import com.hitachivantara.hcp.standard.define.SystemMetadataKey;

public class HCPRequestForm extends HttpForm {

	public <T> void setParameter(RequestParamKey<T> param, T value) throws BuildException {
		this.put(param.getKeyname(), param.build((T)value));
	}

	public <T> String getParameter(RequestParamKey<T> param) {
		return this.get(param.getKeyname()).getValue();
	}
	
	public void setParameter(SystemMetadataKey param, Object value) throws BuildException {
		this.put(param.getKeyname(), param.getKey().build(value));
	}

	public String getParameter(SystemMetadataKey param) {
		return this.get(param.getKeyname()).getValue();
	}

	public void enableParameter(RequestParamKey<Boolean> param) throws BuildException {
		this.put(param.getKeyname(), param.build(Boolean.TRUE));
	}

	public void disableParameter(RequestParamKey<Boolean> param) throws BuildException {
		this.put(param.getKeyname(), param.build(Boolean.FALSE));
	}
}
