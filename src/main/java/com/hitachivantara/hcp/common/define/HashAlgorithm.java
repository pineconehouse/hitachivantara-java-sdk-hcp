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
package com.hitachivantara.hcp.common.define;

public enum HashAlgorithm {
	SHA256("SHA-256"), SHA384("SHA-384"), SHA512("SHA-512"), MD5("MD5"), SHA1("SHA-1"),NOOP("");
	// RIPEMD160("RIPEMD160");

	private String keyname = null;

	private HashAlgorithm(String key) {
		this.keyname = key;
	}

	public String getKeyname() {
		return keyname;
	}
}
