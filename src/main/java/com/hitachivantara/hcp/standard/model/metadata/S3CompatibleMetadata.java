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
package com.hitachivantara.hcp.standard.model.metadata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.StringUtils;

public class S3CompatibleMetadata extends HCPMetadataSummary {
	public final static String DEFAULT_METADATA_NAME = ".metapairs";
	private Map<String, String> metadataMap = new HashMap<String, String>();
	private String encoding = null;

	public S3CompatibleMetadata() {
		super(DEFAULT_METADATA_NAME);
	}
	
	public S3CompatibleMetadata(String encoding) {
		super(DEFAULT_METADATA_NAME);
		this.encoding = encoding;
	}
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void put(String key, String value) {
		metadataMap.put(key, value);
	}
	
	public void putBase64Value(String key, String value) {
		metadataMap.put(key, DigestUtils.encodeBase64(value));
	}

	public String remove(String key) {
		return metadataMap.remove(key);
	}

	public void putAll(Map<String, String> m) {
		metadataMap.putAll(m);
	}

	public String get(String key) {
		return metadataMap.get(key);
	}
		
	public String getBase64Value(String key) {
		return DigestUtils.decodeBase64(metadataMap.get(key));
	}

	public Set<String> keySet() {
		return metadataMap.keySet();
	}

	public int size() {
		return metadataMap.size();
	}

//	 @Override
	public boolean equals(S3CompatibleMetadata metadata2) {
		if (metadata2 == null) {
			return false;
		}
		Set<String> m1ks = this.keySet();
		Set<String> m2ks = metadata2.keySet();

		if (m1ks.size() != m2ks.size()) {
			return false;
		}

		for (Iterator<String> it = m1ks.iterator(); it.hasNext();) {
			String m1key = it.next();
			if (!StringUtils.isEquals(this.get(m1key), metadata2.get(m1key))) {
				return false;
			}
		}

		return true;
	}

}
