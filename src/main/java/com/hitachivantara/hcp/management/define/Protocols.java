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
package com.hitachivantara.hcp.management.define;

import com.hitachivantara.hcp.management.model.CifsProtocolSettings;
import com.hitachivantara.hcp.management.model.HttpProtocolSettings;
import com.hitachivantara.hcp.management.model.NfsProtocolSettings;
import com.hitachivantara.hcp.management.model.SmtpProtocolSettings;
import com.hitachivantara.hcp.management.model.converter.MapiResponseHandler;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;

public class Protocols<T> {
	private final String name;
	private final ResponseHandler<T> retrievingNamespaceProtocolsResponseHandler;

	private Protocols(String name, ResponseHandler<T> retrievingNamespaceProtocolsResponseHandler) {
		super();
		this.name = name;
		this.retrievingNamespaceProtocolsResponseHandler = retrievingNamespaceProtocolsResponseHandler;
	}

	public String name() {
		return name;
	}

	public ResponseHandler<T> retrievingNamespaceProtocolsResponseHandler() {
		return retrievingNamespaceProtocolsResponseHandler;
	}

	public final static Protocols<HttpProtocolSettings> HTTP = new Protocols<HttpProtocolSettings>("http", MapiResponseHandler.GET_NAMESPACE_HTTP_PROTOCOLS_RESPONSE_HANDLER);
	public final static Protocols<CifsProtocolSettings> CIFS = new Protocols<CifsProtocolSettings>("cifs",MapiResponseHandler.GET_NAMESPACE_CIFS_PROTOCOLS_RESPONSE_HANDLER);
	public final static Protocols<NfsProtocolSettings> NFS = new Protocols<NfsProtocolSettings>("nfs",MapiResponseHandler.GET_NAMESPACE_NFS_PROTOCOLS_RESPONSE_HANDLER);
	public final static Protocols<SmtpProtocolSettings> SMTP = new Protocols<SmtpProtocolSettings>("smtp",MapiResponseHandler.GET_NAMESPACE_SMTP_PROTOCOLS_RESPONSE_HANDLER);
}
