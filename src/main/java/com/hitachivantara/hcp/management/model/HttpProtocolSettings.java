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

public class HttpProtocolSettings extends ProtocolSettings {

	private Boolean httpEnabled;
	private Boolean httpsEnabled;
	private Boolean restEnabled;
	private Boolean restRequiresAuthentication;
	private Boolean httpActiveDirectorySSOEnabled;
	private Boolean hs3Enabled;
	private Boolean hs3RequiresAuthentication;
	private Boolean webdavEnabled;
	private Boolean webdavBasicAuthEnabled;
	private Boolean webdavCustomMetadata;
	private String webdavBasicAuthUsername;
	private String webdavBasicAuthPassword;
	private Boolean hswiftEnabled;
	private Boolean hswiftRequiresAuthentication;

	public HttpProtocolSettings() {
		super(Protocols.HTTP);
	}

	public HttpProtocolSettings(Boolean httpEnabled,
			Boolean httpsEnabled,
			Boolean restEnabled,
			Boolean restRequiresAuthentication,
			Boolean httpActiveDirectorySSOEnabled,
			Boolean hs3Enabled,
			Boolean hs3RequiresAuthentication,
			Boolean webdavEnabled,
			Boolean webdavBasicAuthEnabled,
			Boolean webdavCustomMetadata,
			String webdavBasicAuthUsername,
			String webdavBasicAuthPassword,
			Boolean hswiftEnabled,
			Boolean hswiftRequiresAuthentication,
			IPSettings ipSettings) {
		super(Protocols.HTTP, ipSettings);
		this.httpEnabled = httpEnabled;
		this.httpsEnabled = httpsEnabled;
		this.restEnabled = restEnabled;
		this.restRequiresAuthentication = restRequiresAuthentication;
		this.httpActiveDirectorySSOEnabled = httpActiveDirectorySSOEnabled;
		this.hs3Enabled = hs3Enabled;
		this.hs3RequiresAuthentication = hs3RequiresAuthentication;
		this.webdavEnabled = webdavEnabled;
		this.webdavBasicAuthEnabled = webdavBasicAuthEnabled;
		this.webdavCustomMetadata = webdavCustomMetadata;
		this.webdavBasicAuthUsername = webdavBasicAuthUsername;
		this.webdavBasicAuthPassword = webdavBasicAuthPassword;
		this.hswiftEnabled = hswiftEnabled;
		this.hswiftRequiresAuthentication = hswiftRequiresAuthentication;
	}

	public Boolean getHttpEnabled() {
		return httpEnabled;
	}

	public void setHttpEnabled(Boolean httpEnabled) {
		this.httpEnabled = httpEnabled;
	}

	public Boolean getHttpsEnabled() {
		return httpsEnabled;
	}

	public void setHttpsEnabled(Boolean httpsEnabled) {
		this.httpsEnabled = httpsEnabled;
	}

	public Boolean getRestEnabled() {
		return restEnabled;
	}

	public void setRestEnabled(Boolean restEnabled) {
		this.restEnabled = restEnabled;
	}

	public Boolean getRestRequiresAuthentication() {
		return restRequiresAuthentication;
	}

	public void setRestRequiresAuthentication(Boolean restRequiresAuthentication) {
		this.restRequiresAuthentication = restRequiresAuthentication;
	}

	public Boolean getHttpActiveDirectorySSOEnabled() {
		return httpActiveDirectorySSOEnabled;
	}

	public void setHttpActiveDirectorySSOEnabled(Boolean httpActiveDirectorySSOEnabled) {
		this.httpActiveDirectorySSOEnabled = httpActiveDirectorySSOEnabled;
	}

	public Boolean getHs3Enabled() {
		return hs3Enabled;
	}

	public void setHs3Enabled(Boolean hs3Enabled) {
		this.hs3Enabled = hs3Enabled;
	}

	public Boolean getHs3RequiresAuthentication() {
		return hs3RequiresAuthentication;
	}

	public void setHs3RequiresAuthentication(Boolean hs3RequiresAuthentication) {
		this.hs3RequiresAuthentication = hs3RequiresAuthentication;
	}

	public Boolean getWebdavEnabled() {
		return webdavEnabled;
	}

	public void setWebdavEnabled(Boolean webdavEnabled) {
		this.webdavEnabled = webdavEnabled;
	}

	public Boolean getWebdavBasicAuthEnabled() {
		return webdavBasicAuthEnabled;
	}

	public void setWebdavBasicAuthEnabled(Boolean webdavBasicAuthEnabled) {
		this.webdavBasicAuthEnabled = webdavBasicAuthEnabled;
	}

	public Boolean getWebdavCustomMetadata() {
		return webdavCustomMetadata;
	}

	public void setWebdavCustomMetadata(Boolean webdavCustomMetadata) {
		this.webdavCustomMetadata = webdavCustomMetadata;
	}

	public String getWebdavBasicAuthUsername() {
		return webdavBasicAuthUsername;
	}

	public void setWebdavBasicAuthUsername(String webdavBasicAuthUsername) {
		this.webdavBasicAuthUsername = webdavBasicAuthUsername;
	}

	public String getWebdavBasicAuthPassword() {
		return webdavBasicAuthPassword;
	}

	public void setWebdavBasicAuthPassword(String webdavBasicAuthPassword) {
		this.webdavBasicAuthPassword = webdavBasicAuthPassword;
	}

	public Boolean getHswiftEnabled() {
		return hswiftEnabled;
	}

	public void setHswiftEnabled(Boolean hswiftEnabled) {
		this.hswiftEnabled = hswiftEnabled;
	}

	public Boolean getHswiftRequiresAuthentication() {
		return hswiftRequiresAuthentication;
	}

	public void setHswiftRequiresAuthentication(Boolean hswiftRequiresAuthentication) {
		this.hswiftRequiresAuthentication = hswiftRequiresAuthentication;
	}

}
