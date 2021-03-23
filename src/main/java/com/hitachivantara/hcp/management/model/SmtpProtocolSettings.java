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

import com.hitachivantara.hcp.management.define.EmailFormat;
import com.hitachivantara.hcp.management.define.Protocols;

public class SmtpProtocolSettings extends ProtocolSettings {

	private Boolean enabled;
	private String emailLocation;
	private EmailFormat emailFormat;
	private Boolean separateAttachments;

	public SmtpProtocolSettings() {
		super(Protocols.SMTP);
	}

	public SmtpProtocolSettings(Boolean enabled, String emailLocation, EmailFormat emailFormat, Boolean separateAttachments, IPSettings ipSettings) {
		super(Protocols.SMTP, ipSettings);
		this.enabled = enabled;
		this.emailLocation = emailLocation;
		this.emailFormat = emailFormat;
		this.separateAttachments = separateAttachments;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getEmailLocation() {
		return emailLocation;
	}

	public void setEmailLocation(String emailLocation) {
		this.emailLocation = emailLocation;
	}

	public EmailFormat getEmailFormat() {
		return emailFormat;
	}

	public void setEmailFormat(EmailFormat emailFormat) {
		this.emailFormat = emailFormat;
	}

	public Boolean getSeparateAttachments() {
		return separateAttachments;
	}

	public void setSeparateAttachments(Boolean separateAttachments) {
		this.separateAttachments = separateAttachments;
	}

}
