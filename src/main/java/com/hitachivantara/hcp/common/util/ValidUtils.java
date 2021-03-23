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
package com.hitachivantara.hcp.common.util;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.hcp.common.define.HCPResponseKey;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;

public class ValidUtils extends com.amituofo.common.util.ValidUtils{
	public static void validateRequest(HCPStandardRequest<?> request) throws InvalidParameterException {
		ValidUtils.invalidIfNull(request, "The parameter request must be specified.");

		request.validParameter();
	}
	
	public static void validateRequest(HCPRequest request) throws InvalidParameterException {
		ValidUtils.invalidIfNull(request, "The parameter request must be specified.");

		request.validRequestParameter();
	}

	public static void validateResponse(HttpResponse response, HCPResponseKey expectcode) throws InvalidResponseException {
		HCPResponseKey responseCode = HCPResponseKey.valueOf(response.getStatusLine().getStatusCode());

		if (expectcode != responseCode) {
			throw new InvalidResponseException(response);
		}
	}

	public static void validateResponse(HttpResponse response, HCPResponseKey... expectcodes) throws InvalidResponseException {
		HCPResponseKey responseCode = HCPResponseKey.valueOf(response.getStatusLine().getStatusCode());

		int matchCount = 0;
		for (HCPResponseKey hcpResponseKey : expectcodes) {
			if (hcpResponseKey == responseCode) {
				matchCount++;
			}
		}

		if (matchCount == 0) {
			throw new InvalidResponseException(response);
		}
	}

	public static boolean isResponse(HttpResponse response, HCPResponseKey code) {
		HCPResponseKey responseCode = HCPResponseKey.valueOf(response.getStatusLine().getStatusCode());

		return (code == responseCode);
	}

	public static boolean isAnyResponse(HttpResponse response, HCPResponseKey... expectcodes) {
		HCPResponseKey responseCode = HCPResponseKey.valueOf(response.getStatusLine().getStatusCode());

		int matchCount = 0;
		for (HCPResponseKey hcpResponseKey : expectcodes) {
			if (hcpResponseKey == responseCode) {
				matchCount++;
				break;
			}
		}

		return (matchCount != 0);
	}

}
