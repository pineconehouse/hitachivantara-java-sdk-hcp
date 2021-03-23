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
package com.hitachivantara.hcp.standard.define;

public enum SystemMetadataKey {
	
	HOLD(RequestParamKey.HOLD),
	INDEX(RequestParamKey.INDEX),
	RETENTION(RequestParamKey.RETENTION),
	SHRED(RequestParamKey.SHRED),
	OWNER(RequestParamKey.OWNER),
	DOMAIN(RequestParamKey.DOMAIN),
	ACL(RequestParamKey.ACL);
	
	private RequestParamKey key = null;

	private <T> SystemMetadataKey(RequestParamKey<T> key) {
		this.key = key;
	}

	public String getKeyname() {
		return key.getKeyname();
	}

	public RequestParamKey getKey() {
		return key;
	}

}
