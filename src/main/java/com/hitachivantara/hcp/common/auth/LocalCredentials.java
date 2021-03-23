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

import com.amituofo.common.util.DigestUtils;
import com.hitachivantara.hcp.management.define.AuthenticationType;

/**
 * To authenticate with HCP through HTTP, you need to construct an authentication token from a system-level user account or a tenant-level
 * user account and then submit it using a request header with all requests. Successful authentication requires encoding your account
 * information.
 * 
 * @author sohan
 *
 */
public class LocalCredentials implements Credentials {

	private String user;
	private String password;

	/**
	 * An authentication token consists of a username in Base64-encoded format and a password that’s hashed using the MD5 hash algorithm
	 * 
	 * @param user
	 * @param password
	 */
	public LocalCredentials(String user, String password) {
		this(user, password, false);
	}

	/**
	 * An authentication token consists of a username in Base64-encoded format and a password that’s hashed using the MD5 hash algorithm
	 * 
	 * @param user
	 * @param password
	 * @param encode
	 *            If true sdk will encode user and password into base64-encoded-username:md5-hashed-password automatically
	 */
	public LocalCredentials(String user, String password, boolean encode) {
		super();

		if (encode) {
			this.user = DigestUtils.encodeBase64(user);
			this.password = DigestUtils.format2Hex(DigestUtils.calcMD5(password)).toLowerCase();
		} else {
			this.user = user;
			this.password = password;
		}
	}

	public String getAccessKey() {
		return user;
	}

	public String getSecretKey() {
		return password;
	}

	@Override
	public AuthenticationType getAuthenticationType() {
		return AuthenticationType.LOCAL;
	}

	// public String getCredentialsKey() {
	// return user + ":" + password;
	// }

}
