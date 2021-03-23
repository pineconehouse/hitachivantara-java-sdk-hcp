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
package com.hitachivantara.hcp.standard.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummary;

public class MetadataListHandler extends DefaultHandler {
	// <annotations>
	// <annotation>
	// <name>.metapairs</name>
	// <hash>SHA-256 5544876AE8DF0CD43C6294094BBF7D3BEBDC70CCDF8F7452808934584135413A</hash>
	// <changeTimeMilliseconds>1525760181000.00</changeTimeMilliseconds>
	// <changeTimeString>2018-05-08T14:16:21+0800</changeTimeString>
	// <size>261</size>
	// <contentType>text/xml</contentType>
	// </annotation>
	// </annotations>

	private List<HCPMetadataSummary> metadatas = new ArrayList<HCPMetadataSummary>();
	private String currentQValue;
	private HCPMetadataSummary metadata;

	public List<HCPMetadataSummary> getMetadatas() {
		return metadatas;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("name")) {
			metadata = new HCPMetadataSummary(currentQValue);
			metadatas.add(metadata);

		} else if (qName.equalsIgnoreCase("hash")) {
			String[] hash = currentQValue.split(" ");
			metadata.setHashAlgorithmName(hash[0]);
			metadata.setContentHash(hash[1]);

		} else if (qName.equalsIgnoreCase("changeTimeMilliseconds")) {
			metadata.setChangeTime(Double.valueOf(currentQValue).longValue());

		} else if (qName.equalsIgnoreCase("size")) {
			metadata.setSize(Long.parseLong(currentQValue));

		} else if (qName.equalsIgnoreCase("contentType")) {
			metadata.setContentType(currentQValue);

		} else if (qName.equalsIgnoreCase("annotation")) {
			metadata = null;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String value = new String(ch, start, length);
		if (value.trim().length() > 0) {
			currentQValue = value;
		} else {
			currentQValue = "";
		}
	}

}
