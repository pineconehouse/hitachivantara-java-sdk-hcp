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
package com.hitachivantara.hcp.standard.body;

import com.amituofo.common.ex.AlgorithmException;
import com.hitachivantara.hcp.common.HCPHttpClient;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;

public class HCPHttpRestClient extends HCPHttpClient<HCPStandardRequest> {
	private final HCPNamespaceBase hcpClient;

	public HCPHttpRestClient(HCPNamespaceBase hcpClient) {
		this(hcpClient, "/rest/");
	}

	public HCPHttpRestClient(HCPNamespaceBase hcpClient, String service) {
		super(hcpClient.getNamespace(), hcpClient.getEndpoint(), -1, service, hcpClient.getCredentials(), hcpClient.getClientConfiguration());
		this.hcpClient = hcpClient;
	}

	private String generateKey(String key) throws AlgorithmException {
		KeyAlgorithm keyAlgorithm = hcpClient.getKeyAlgorithm();
		if (keyAlgorithm != null) {
			return keyAlgorithm.generate(key);
		}

		return key;
	}

	protected String generateServicePoint(HCPStandardRequest request) throws Exception {
//		String servicePoint = generateBasicServicePoint(request.getNamespace());
		// if (usingIpAddress) {
		// this.addCommonHeader(HeaderKey.HOST.getKeyname(), host);
		//
		// host = "10.129.214.75";
		// }

//		return generateRestRequestUrl(servicePoint, request.getKey());
		return generateKey(request.getKey());
	}

//	private String generateRestRequestUrl(String host, String key) throws Exception {
//		if (key.length() > 0) {
//			char firstChar = key.charAt(0);
//			char lastChar = key.charAt(key.length() - 1);
//
//			if (firstChar == 47 || firstChar == 92) {
//				key = key.substring(1);
//			}
//
//			// As a directory 是否是目录的判断方式要改
//			if (lastChar == 47 || lastChar == 92) {
//				if (key.length() > 1) {
//					return host + key;
//				} else {
//					return host;
//				}
//			}
//
//			return host + generateKey(key);
//		}
//
//		return host;
//	}

}