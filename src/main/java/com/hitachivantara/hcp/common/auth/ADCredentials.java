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
package com.hitachivantara.hcp.common.auth;

import com.hitachivantara.hcp.management.define.AuthenticationType;

/**
 * To authenticate to HCP with Active Directory, you need to construct an authentication token from a AD user account and then submit it
 * using a request header with all requests. The username and password does not need to be encoded.
 * 
 * @author sohan
 *
 */
public class ADCredentials implements Credentials {

	private String user;
	private String password;

	/**
	 * An AD authentication token consists of an AD username and password
	 * 
	 * @param user
	 * @param password
	 */
	public ADCredentials(String user, String password) {
		super();

		this.user = user;
		this.password = password;
	}

	public String getAccessKey() {
		return user;
	}

	public String getSecretKey() {
		return password;
	}

	@Override
	public AuthenticationType getAuthenticationType() {
		return AuthenticationType.EXTERNAL;
	}

	// public String getCredentialsKey() {
	// return user + ":" + password;
	// }

}
