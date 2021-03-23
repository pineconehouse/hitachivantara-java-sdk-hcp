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
package com.hitachivantara.hcp.common.define;

public class HCPRequestHeadValue {
	public final static class CONTENT_ENCODING {
		public final static String GZIP = "gzip";
	}

	public final static class TRANSFER_ENCODING {
		public final static String CHUNKED = "chunked";
	}

	public final static class METADATA_DIRECTIVE {

		/**
		 * Copy all the source object custom metadata. If the source object has multiple annotations, copy all the annotations.
		 */
		public final static String ALL = "ALL";
		/**
		 * Do not copy any custom metadata with the source object.
		 */
		public final static String NONE = "NONE";
	}

	public final static class CONTENT_TYPE {
		/** The default XML mimetype: application/xml */
		public static final String APPLICATION_XML = "application/xml";
		
		/** The default XML mimetype: application/json */
		public static final String APPLICATION_JSON = "application/json";
		
		public static final String APPLICATION_FORM= "application/x-www-form-urlencoded";

		/** The default HTML mimetype: text/xml */
		public static final String TEXT_XML = "text/xml";

		/** The default HTML mimetype: text/html */
		public static final String TEXT_HTML = "text/html";

		/** The default binary mimetype: application/octet-stream */
		public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	}

	public final static class CONNECTION {
		public final static String CLOSE = "close";
		public final static String KEEP_ALIVE = "keep-alive";
	}

}
