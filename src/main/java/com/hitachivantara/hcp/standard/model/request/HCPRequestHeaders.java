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
import com.hitachivantara.core.http.define.HeaderKey;
import com.hitachivantara.core.http.model.HttpHeader;

public class HCPRequestHeaders extends HttpHeader {

	public <T> void setHeader(HeaderKey<T> header, T value) throws BuildException {
		this.put(header.getKeyname(), header.build((T) value));
	}

	public <T> String getHeader(HeaderKey<T> header, Object value) {
		return this.get(header.getKeyname()).getValue();
	}
}
