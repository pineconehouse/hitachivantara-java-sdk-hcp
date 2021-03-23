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

public class SettingBuilders {

	public SettingBuilders() {
	}

	public final static CreateNamespaceSettingsBuilder createNamespaceBuilder() {
		return new CreateNamespaceSettingsBuilder();
	}

	public final static ModifyNamespaceSettingsBuilder modifyNamespaceBuilder() {
		return new ModifyNamespaceSettingsBuilder();
	}
	
	public final static ModifyHttpProtocolBuilder modifyHttpProtocolBuilder() {
		return new ModifyHttpProtocolBuilder();
	}
	
	public final static ModifyCifsProtocolBuilder modifyCifsProtocolBuilder() {
		return new ModifyCifsProtocolBuilder();
	}
	
	public final static ModifyNfsProtocolBuilder modifyNfsProtocolBuilder() {
		return new ModifyNfsProtocolBuilder();
	}
	
	public final static ModifySmtpProtocolBuilder modifySmtpProtocolBuilder() {
		return new ModifySmtpProtocolBuilder();
	}

	public final static ModifyDataAccessPermissionBuilder modifyDataAccessPermissionBuilder() {
		return new ModifyDataAccessPermissionBuilder();
	}
	
	public final static CreateUserAcccountBuilder createUserAcccountBuilder() {
		return new CreateUserAcccountBuilder();
	}
	
	public final static ModifyUserAcccountBuilder modifyUserAcccountBuilder() {
		return new ModifyUserAcccountBuilder();
	}
}
