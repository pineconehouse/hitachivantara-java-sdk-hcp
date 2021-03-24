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
package com.hitachivantara.hcp.standard.internal;

import com.hitachivantara.core.http.model.HttpHeader;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.standard.api.Signature;
import com.hitachivantara.hcp.standard.model.request.HCPRequestParams;

public class S3V3Signature implements Signature {

	public String generate(String key, HttpHeader header, HCPRequestParams param, Credentials credentials) {
		// TODO Auto-generated method stub
		return null;
	}

}