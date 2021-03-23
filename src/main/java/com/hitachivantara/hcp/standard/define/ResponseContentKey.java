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
package com.hitachivantara.hcp.standard.define;

import com.amituofo.common.api.StringValueParser;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.define.CustomKey;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.standard.model.metadata.Annotation;

public final class ResponseContentKey {
	public final static class Entry {
		/****/
		public final static String KEY_NAME = "entry";

		/****/
		public final static CustomKey<String> URL_NAME = new CustomKey<String>("urlName", StringValueParser.STRING_TYPE_PARSER);
		/****/
		// public final static CustomKey<String> UTF8_NAME = new CustomKey<String>("utf8Name",
		// StringValueParser.STRING_TYPE_PARSER);
		public final static CustomKey<String> UTF8_NAME = new CustomKey<String>("utf8Name", new StringValueParser<String>() {
			public String parse(String value) {
				return URLUtils.xmlKeywordDecode(value);
			}
		});
		/****/
		public final static CustomKey<ObjectType> TYPE = new CustomKey<ObjectType>("type", new StringValueParser<ObjectType>() {
			public ObjectType parse(String value) {
				// if (value != null) {
				return ObjectType.valueOf(value);
				// }

				// return null;
			}
		});
		/****/
		public final static CustomKey<Long> SIZE = new CustomKey<Long>("size", StringValueParser.LONG_TYPE_PARSER);
		/****/
		public final static CustomKey<String> HASH_SCHEME = new CustomKey<String>("hashScheme", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> HASH = new CustomKey<String>("hash", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> ETAG = new CustomKey<String>("etag", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> RETENTION = new CustomKey<String>("retention", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> RETENTION_STRING = new CustomKey<String>("retentionString", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> RETENTION_CLASS = new CustomKey<String>("retentionClass", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<Long> INGESTTIME = new CustomKey<Long>("ingestTime", StringValueParser.TIMESTAMP_TYPE_PARSER);
		/****/
		public final static CustomKey<Long> INGESTTIME_MILLISECONDS = new CustomKey<Long>("ingestTimeMilliseconds", StringValueParser.TIMESTAMP_TYPE_PARSER);
		/****/
		public final static CustomKey<Boolean> HOLD = new CustomKey<Boolean>("hold", StringValueParser.BOOLEAN_TYPE_PARSER);
		/****/
		public final static CustomKey<Boolean> SHRED = new CustomKey<Boolean>("shred", StringValueParser.BOOLEAN_TYPE_PARSER);
		/****/
		public final static CustomKey<Integer> DPL = new CustomKey<Integer>("dpl", StringValueParser.INTEGER_TYPE_PARSER);
		/****/
		public final static CustomKey<Boolean> INDEX = new CustomKey<Boolean>("index", StringValueParser.BOOLEAN_TYPE_PARSER);
		/****/
		public final static CustomKey<Boolean> CUSTOM_METADATA = new CustomKey<Boolean>("customMetadata", StringValueParser.BOOLEAN_TYPE_PARSER);
		/****/
		public final static CustomKey<Annotation[]> CUSTOM_METADATA_ANNOTATIONS = new CustomKey<Annotation[]>(
				"customMetadataAnnotations",
				HCPHeaderKey.X_HCP_CUSTOM_METADATA_ANNOTATIONS.getValueParser());
		/****/
		public final static CustomKey<String> VERSION = new CustomKey<String>("version", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<Boolean> REPLICATED = new CustomKey<Boolean>("replicated", StringValueParser.BOOLEAN_TYPE_PARSER);
		/****/
		public final static CustomKey<Long> CHANGE_TIME_MILLISECONDS = new CustomKey<Long>("changeTimeMilliseconds", StringValueParser.TIMESTAMP_TYPE_PARSER);
		/****/
		public final static CustomKey<String> CHANGE_TIME_STRING = new CustomKey<String>("changeTimeString", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> OWNER = new CustomKey<String>("owner", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> DOMAIN = new CustomKey<String>("domain", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<Boolean> HASACL = new CustomKey<Boolean>("hasAcl", StringValueParser.BOOLEAN_TYPE_PARSER);
		/****/
		public final static CustomKey<String> STATE = new CustomKey<String>("state", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> UTF8_SYMLINK_TARGET = new CustomKey<String>("utf8SymlinkTarget", StringValueParser.STRING_TYPE_PARSER);
		/****/
		public final static CustomKey<String> SYMLINK_TARGET = new CustomKey<String>("symlinkTarget", StringValueParser.STRING_TYPE_PARSER);
	}
}
