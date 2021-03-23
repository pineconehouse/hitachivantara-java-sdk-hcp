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
package com.hitachivantara.hcp.management.model.builder;

import com.hitachivantara.hcp.management.model.HttpProtocolSettings;

public class ModifyHttpProtocolBuilder extends ModifyProtocolBuilder<ModifyHttpProtocolBuilder, HttpProtocolSettings> {
	public ModifyHttpProtocolBuilder() {
		super(new HttpProtocolSettings());
	}

	public ModifyHttpProtocolBuilder withHs3Enabled(boolean enabled) {
		settings.setHs3Enabled(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withHs3RequiresAuthentication(boolean enabled) {
		settings.setHs3RequiresAuthentication(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withHswiftEnabled(boolean enabled) {
		settings.setHswiftEnabled(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withHswiftRequiresAuthentication(boolean enabled) {
		settings.setHswiftRequiresAuthentication(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withHttpActiveDirectorySSOEnabled(boolean enabled) {
		settings.setHttpActiveDirectorySSOEnabled(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withHttpEnabled(boolean enabled) {
		settings.setHttpEnabled(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withHttpsEnabled(boolean enabled) {
		settings.setHttpsEnabled(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withRestEnabled(boolean enabled) {
		settings.setRestEnabled(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withRestRequiresAuthentication(boolean enabled) {
		settings.setRestRequiresAuthentication(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withoutWebdavBasicAuth() {
		settings.setWebdavBasicAuthEnabled(false);
		return this;
	}

	public ModifyHttpProtocolBuilder withWebdavBasicAuth(String webdavBasicAuthUsername, String webdavBasicAuthPassword) {
		settings.setWebdavBasicAuthEnabled(true);
		settings.setWebdavBasicAuthUsername(webdavBasicAuthUsername);
		settings.setWebdavBasicAuthPassword(webdavBasicAuthPassword);
		return this;
	}

	public ModifyHttpProtocolBuilder withWebdavCustomMetadata(boolean enabled) {
		settings.setWebdavCustomMetadata(enabled);
		return this;
	}

	public ModifyHttpProtocolBuilder withWebdavEnabled(boolean enabled) {
		settings.setWebdavEnabled(enabled);
		return this;
	}

}
