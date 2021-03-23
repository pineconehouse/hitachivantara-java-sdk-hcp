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
package com.hitachivantara.hcp.standard.util;

public class HCPRestUrlUtils {
	public static enum InfoType {
		FULL_REST_ROOT_URL, NAMESPACE_NAME, ENDPOINT_NAME, KEY, TENANT_NAME, DOMAIN_NAME
	}

	/**
	 * Get information from url
	 * 
	 * @param url
	 * @param type
	 * @return
	 */
	public static String getFromRequsetURL(String url, InfoType type) {
		if (url == null || !url.contains("/rest") || !url.startsWith("http")) {
			return null;
		}

		if (type == InfoType.FULL_REST_ROOT_URL) {
			int i = url.indexOf("/rest");
			if (i != -1) {
				return url.substring(0, i + 5);
			}
		} else if (type == InfoType.NAMESPACE_NAME) {
			int i1 = url.indexOf(':');
			int i2 = url.indexOf('.');
			if (i1 != -1 && i2 != -1) {
				return url.substring(i1 + 3, i2);
			}
		} else if (type == InfoType.TENANT_NAME) {
			// https://cloud.tn9.hcp8.hdim.lab
			int i1 = url.indexOf('.');
			if (i1 != -1) {
				int i2 = url.indexOf('.', i1 + 1);
				if (i2 != -1) {
					// tn9
					return url.substring(i1 + 1, i2);
				}
			}
		} else if (type == InfoType.DOMAIN_NAME) {
			// https://cloud.tn9.hcp8.hdim.lab
			int i1 = url.indexOf('.');
			if (i1 != -1) {
				i1 = url.indexOf('.', i1 + 1);
				if (i1 != -1) {
					int i2 = url.indexOf('/', i1 + 1);
					if (i2 != -1) {
						// hcp8.hdim.lab
						return url.substring(i1 + 1, i2);
					} else {
						return url.substring(i1 + 1);
					}
				}
			}
		} else if (type == InfoType.ENDPOINT_NAME) {
			int i1 = url.indexOf('.');
			int i2 = url.lastIndexOf("/rest");
			if (i1 != -1 && i2 != -1) {
				return url.substring(i1 + 1, i2);
			}
		} else if (type == InfoType.KEY) {
			int i1 = url.indexOf("/rest");
			int i2 = url.indexOf('?');
			// 如果是向根目录上传 : http://xx.ten.domain.com/rest
			if (i1 != -1 && url.length() == i1 + 5 && i2 == -1) {
				return "/";
			}
			// http://xx.ten.domain.com/rest/username/folder/obj.text
			if (i1 != -1 && i2 == -1) {
				// return: username/folder/obj.text
				return url.substring(i1 + 6);
			}
			// http://xx.ten.domain.com/rest/username/folder/obj.text?version=111
			if (i1 != -1 && i2 != -1) {
				// return: username/folder/obj.text
				return url.substring(i1 + 6, i2);
			}
		}

		return null;
	}

	/**
	 * Get information from url
	 * 
	 * @param url
	 * @param type
	 * @return
	 */
	public static String getFromEndpoint(String url, InfoType type) {
		if (url == null || !url.contains(".")) {
			return null;
		}

		if (type == InfoType.TENANT_NAME) {
			// https://cloud.tn9.hcp8.hdim.lab
			int i1 = url.indexOf('.');
			if (i1 != -1) {
				return url.substring(0, i1);
			}
		} else if (type == InfoType.DOMAIN_NAME) {
			// https://cloud.tn9.hcp8.hdim.lab
			int i1 = url.indexOf('.');
			if (i1 != -1) {
				return url.substring(i1 + 1);
			}
		}

		return null;
	}
}
