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
package com.hitachivantara.hcp.standard.model.request.content;

import com.hitachivantara.hcp.standard.model.Retention;

public interface ReqWithSystemMetadata<T> {

	/**
	 * Paces an object on hold or specifies that it's not on hold
	 * 
	 * @param hold
	 * @return
	 */
	public T withHold(boolean hold);

	/**
	 * Specifies whether the object should be indexed for search
	 * 
	 * @param index
	 * @return
	 */
	public T withIndex(boolean index);

	/**
	 * Specifies the retention setting for the object
	 * 
	 * @param retention
	 * @return
	 */
	public T withRetention(Retention retention);

	/**
	 * Specifies whether to shred the object after it is deleted
	 * 
	 * @param shred
	 * @return
	 */
	public T withShred(boolean shred);

	/**
	 * Specifies the user that owns the object.
	 * 
	 * @param localUserName
	 * @return
	 */
	public T withOwner(String localUserName);

	/**
	 * Specifies the user that owns the object. If you specify an AD user account, you also need to specify the domain parameter.
	 * 
	 * @param domain
	 * @param domainUserName
	 * @return
	 */
	public T withOwner(String domain, String domainUserName);

	public boolean isHold();

	public boolean isIndex();

	public String getRetention();

	public boolean isShred();

	public String getOwner();

	public String getOwnerDomain();

}
