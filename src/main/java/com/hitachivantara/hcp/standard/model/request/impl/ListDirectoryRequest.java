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
package com.hitachivantara.hcp.standard.model.request.impl;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithDeletedObject;

/**
 * To list the contents of a directory in a namespace. This method does not list old versions of objects.
 * 
 * @author sohan
 *
 */
public class ListDirectoryRequest extends HCPStandardRequest<ListDirectoryRequest> implements ReqWithDeletedObject<ListDirectoryRequest> {
	/**
	 * Listing deleted objects and directories
	 */
	boolean includeDeletedObject = false;
	/**
	 * Managing sub directory change times
	 */
	boolean withDeletedDirTime = false;

	public ListDirectoryRequest() {
		super(Method.GET);
	}

	public ListDirectoryRequest(String key) {
		super(Method.GET, key);
	}

	/**
	 * By default, a directory list does not include deleted objects or directories. If the namespace supports versioning, you can include
	 * deleted objects and directories in the list.
	 * 
	 * @param with
	 * @return
	 */
	public ListDirectoryRequest withDeletedObject(boolean with) {
		this.includeDeletedObject = with;
		return this;
	}

	/**
	 * By default, the change time returned for each subdirectory in a directory list is the time the subdirectory was created or, if the
	 * subdirectory is a deleted directory, the time it was deleted. You can choose instead to return change times that are the most recent of:
	 * <p>
	 * <p>
	 * The time the subdirectory was created
	 * <p>
	 * The time the subdirectory was deleted
	 * <p>
	 * The time an object was most recently added to the subdirectory
	 * <p>
	 * The time an object was most recently deleted from the subdirectory
	 * 
	 * @param with
	 * @return
	 */
	public ListDirectoryRequest withDeletedDirTime(boolean with) {
		this.withDeletedDirTime = with;
		return this;
	}

	public boolean isWithDeletedDirTime() {
		return withDeletedDirTime;
	}

	public boolean isWithDeletedObject() {
		return includeDeletedObject;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}

}
