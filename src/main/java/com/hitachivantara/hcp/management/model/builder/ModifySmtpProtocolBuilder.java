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

import com.hitachivantara.hcp.management.define.EmailFormat;
import com.hitachivantara.hcp.management.model.SmtpProtocolSettings;

public class ModifySmtpProtocolBuilder extends ModifyProtocolBuilder<ModifySmtpProtocolBuilder, SmtpProtocolSettings> {
	public ModifySmtpProtocolBuilder() {
		super(new SmtpProtocolSettings());
	}

	public ModifySmtpProtocolBuilder withEnabled(boolean enabled) {
		settings.setEnabled(enabled);
		return this;
	}

	public ModifySmtpProtocolBuilder withEmailFormat(EmailFormat emailFormat) {
		settings.setEmailFormat(emailFormat);
		return this;
	}

	public ModifySmtpProtocolBuilder withSeparateAttachments(boolean enabled) {
		settings.setSeparateAttachments(enabled);
		return this;
	}

	public ModifySmtpProtocolBuilder withEmailLocation(String location) {
		settings.setEmailLocation(location);
		return this;
	}

}
