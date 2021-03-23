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
package com.hitachivantara.hcp.standard.model;

import java.io.Serializable;

import com.hitachivantara.hcp.common.define.HCPResponseKey;

public abstract class HCPStandardResult implements Cloneable, Serializable {
	private HCPResponseKey responseCode;
	private String date;
	private String server;
	private String requestId;
	private String servicedBySystem;
	private Long hcpTime;
	private String hcpVersion;
	private String contentType;
	private Long contentLength;

	public HCPResponseKey getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(HCPResponseKey responseCode) {
		this.responseCode = responseCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getServicedBySystem() {
		return servicedBySystem;
	}

	public void setServicedBySystem(String servicedBySystem) {
		this.servicedBySystem = servicedBySystem;
	}

	public Long getHcpTime() {
		return hcpTime;
	}

	public void setHcpTime(Long hcpTime) {
		this.hcpTime = hcpTime;
	}

	public String getHcpVersion() {
		return hcpVersion;
	}

	public void setHcpVersion(String hcpVersion) {
		this.hcpVersion = hcpVersion;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

}
