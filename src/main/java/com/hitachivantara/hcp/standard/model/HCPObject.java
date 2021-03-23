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

import java.io.InputStream;

import com.hitachivantara.hcp.standard.io.HCPObjectInputStream;

public class HCPObject extends HCPObjectSummary {
	private static final long serialVersionUID = 6825838693062109494L;
	private HCPObjectInputStream content;

	public InputStream getContent() {
		return content;
	}

	public void setContent(HCPObjectInputStream content) {
		this.content = content;
	}

}
