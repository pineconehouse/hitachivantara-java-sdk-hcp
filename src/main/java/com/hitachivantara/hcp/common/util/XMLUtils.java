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

import java.util.ArrayList;
import java.util.List;

public class XMLUtils {
	public static String trackSingleNodeValue(String xmlContent, String nodeName) {
		return trackSingleNodeValue(xmlContent, 0, nodeName);
	}

	public static String trackSingleNodeValue(String xmlContent, int startIndex, String nodeName) {
		if (xmlContent == null) {
			return null;
		}

		String NODE_NAME_B = "<" + nodeName + ">";
		String NODE_NAME_E = "</" + nodeName + ">";

		int is = xmlContent.indexOf(NODE_NAME_B, startIndex);
		int ie;
		if (is != -1) {
			is = is + nodeName.length() + 2;
			ie = xmlContent.indexOf(NODE_NAME_E, is);

			if (ie != -1) {
				return xmlContent.substring(is, ie);
			}
		}

		return null;
	}

	public static List<String> trackNodeValue(String xmlContent, int startIndex, String nodeName) {
		if (xmlContent == null) {
			return null;
		}

		List<String> nodes = new ArrayList<String>();
		String NODE_NAME_B = "<" + nodeName + ">";
		String NODE_NAME_E = "</" + nodeName + ">";

		String nodeValue = null;
		int is = xmlContent.indexOf(NODE_NAME_B, startIndex);
		int ie = -1;
		if (is != -1) {
			is = is + nodeName.length() + 2;
			ie = xmlContent.indexOf(NODE_NAME_E, is);

			if (ie != -1) {
				nodeValue = xmlContent.substring(is, ie);
			}
		}
		while (nodeValue != null) {
			nodes.add(nodeValue);

			nodeValue = null;
			startIndex = ie + nodeName.length() + 3;

			is = xmlContent.indexOf(NODE_NAME_B, startIndex);
			if (is != -1) {
				is = is + nodeName.length() + 2;
				ie = xmlContent.indexOf(NODE_NAME_E, is);

				if (ie != -1) {
					nodeValue = xmlContent.substring(is, ie);
				}
			}
		}

		return nodes;
	}
}
