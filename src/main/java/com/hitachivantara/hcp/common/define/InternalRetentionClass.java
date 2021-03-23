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

import com.hitachivantara.hcp.standard.model.Retention;

public class InternalRetentionClass {
	/**
	 * The object can be deleted at any time.
	 */
	public static final Retention DeletionAllowed = new Retention("0");
	/**
	 * The object cannot be deleted, except by a privileged delete, and the retention setting cannot be changed.
	 */
	public static final Retention DeletionProhibited = new Retention("-1");

	/**
	 * The object does not yet have a retention setting. An object that has this value cannot be deleted, except by a privileged delete. You can
	 * change this retention setting to any other setting.
	 */
	public static final Retention InitialUnspecified = new Retention("-2");
}