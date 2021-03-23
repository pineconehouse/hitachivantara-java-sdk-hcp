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
package com.hitachivantara.hcp.common.ex;

import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.common.define.HCPResponseKey;

/**
 * Represents an error response returned by Hitachi product service. Receiving an exception of this type indicates that the caller's request
 * was correctly transmitted to the service, but for some reason, the service was not able to process it, and returned an error response
 * code and reason instead.
 * 
 * @author sohan
 *
 */
public class InvalidResponseException extends com.hitachivantara.core.http.ex.InvalidResponseException {
	private static final long serialVersionUID = 1L;

	public InvalidResponseException() {
	}

	public InvalidResponseException(String message) {
		super(message);
	}

	public InvalidResponseException(Throwable cause) {
		super(cause);
	}

	public InvalidResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidResponseException(HttpResponse response) {
		super(getErrorDetails(response));

		statusCode = response.getStatusLine().getStatusCode();
		reason = HCPResponseKey.valueOf(response.getStatusLine().getStatusCode()).getDesc();
	}

	private static String getErrorDetails(HttpResponse response) {
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		buf.append(response.getStatusLine().getStatusCode());
		buf.append(") ");
		buf.append(response.getStatusLine().getReasonPhrase());

		String hcpErr = HCPHeaderKey.X_HCP_ERRORMESSAGE.parse(response);
		if (StringUtils.isNotEmpty(hcpErr)) {
			buf.append(" / ");
			buf.append(hcpErr);
		}

		return buf.toString();
	}

}
