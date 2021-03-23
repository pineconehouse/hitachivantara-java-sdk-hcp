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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;

public class DefaultMetadataHandler extends DefaultHandler {
	private S3CompatibleMetadata metadata = new S3CompatibleMetadata();
	// <metapairs version="600">
	// <meta-k1>
	// <![CDATA[TEST_CONTENTS]]>
	// </meta-k1>
	// <meta-k2>
	// <![CDATA[rison han]]>
	// </meta-k2>
	// </metapairs>
	private String version = null;
	private String currentQName = null;
	private String currentQValue = null;

	public S3CompatibleMetadata getMetadata() {
		return metadata;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ("600".equals(version) && qName.startsWith("meta-")) {
			currentQName = qName;
		} else if (qName.equalsIgnoreCase("metapairs")) {
			version = attributes.getValue("version");
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (currentQName != null) {
			currentQName = currentQName.substring(5);
			metadata.put(currentQName, currentQValue);
			currentQName = null;
			currentQValue = "";
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentQName != null) {
			String value = new String(ch, start, length);
			if (value.trim().length() > 0)
				currentQValue = value;
		}
	}

}
