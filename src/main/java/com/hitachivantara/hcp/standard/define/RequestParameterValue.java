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

public class RequestParameterValue {
	public static enum Type {
		WHOLE_OBJECT("whole-object"), 
		CUSTOM_METADATA("custom-metadata"), 
		CUSTOM_METADATA_INFO("custom-metadata-info"), 
		DIRECTORY("directory"),
		ACL("acl") ;

		private String keyname = null;

		private Type(String key) {
			this.keyname = key;
		}

		public String getKeyname() {
			return keyname;
		}
	}

	public static enum Version {
		LIST("list");

		private String keyname = null;

		private Version(String key) {
			this.keyname = key;
		}

		public String getKeyname() {
			return keyname;
		}
	}

	public static enum PredefinedAcl {
		ALL_READ("all_read"), AUTH_READ("auth_read");

		private String keyname = null;

		private PredefinedAcl(String key) {
			this.keyname = key;
		}

		public String getKeyname() {
			return keyname;
		}

		public static PredefinedAcl parse(String keyname) {
			if (ALL_READ.keyname.equalsIgnoreCase(keyname)) {
				return ALL_READ;
			} else if (AUTH_READ.keyname.equalsIgnoreCase(keyname)) {
				return AUTH_READ;
			}
			return null;
		}
	}
}
