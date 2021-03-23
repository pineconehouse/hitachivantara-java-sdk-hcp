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

import java.text.SimpleDateFormat;

public class Constants {
	public final static char URL_SEPARATOR = com.amituofo.common.define.Constants.URL_SEPARATOR;
	public final static String DUMMY_KEY = "." + URL_SEPARATOR + ".";
	public final static Long LONG_NOTHING = new Long(-1);

	public final static String DEFAULT_METADATA_NAME = ".metapairs";

	public final static String DEFAULT_URL_ENCODE = com.amituofo.common.define.Constants.DEFAULT_URL_ENCODE;

	public final static String DT_FMT_ISO_8601 = com.amituofo.common.define.Constants.DT_FMT_ISO_8601;

	public final static SimpleDateFormat DATE_FORMATTER_ISO_8601 = com.amituofo.common.define.Constants.DATE_FORMATTER_ISO_8601;


}
