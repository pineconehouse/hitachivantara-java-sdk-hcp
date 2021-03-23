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
import com.hitachivantara.hcp.standard.api.event.ObjectFilter;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithDeletedObject;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithRecursiveDirectory;

/**
 * @author sohan
 *
 */
public class ListObjectRequest extends HCPStandardRequest<ListObjectRequest> implements ReqWithRecursiveDirectory<ListObjectRequest>, ReqWithDeletedObject<ListObjectRequest> {
	private boolean includeDeletedObject = false;
	private boolean recursiveDirectory = false;
	// private int maximumThreadSize = 3;
	private int folderTier = 0;

	private ObjectFilter objectFilter = null;

	public ListObjectRequest() {
		super(Method.GET);
	}

	public ListObjectRequest(String key) {
		super(Method.GET, key);
	}

	public ListObjectRequest withDeletedObject(boolean with) {
		this.includeDeletedObject = with;
		return this;
	}

	public ListObjectRequest withRecursiveDirectory(boolean with) {
		this.recursiveDirectory = with;
		return this;
	}

	public ListObjectRequest withObjectFilter(ObjectFilter objectFilter) {
		this.objectFilter = objectFilter;
		return this;
	}

	public ListObjectRequest withDirectory(String dir) {
		return this.withKey(dir);
	}

	// public ListObjectRequest withMaximumThreadSize(int size) {
	// this.maximumThreadSize = size;
	// return this;
	// }

	public boolean isWithDeletedObject() {
		return includeDeletedObject;
	}

	public boolean isRecursiveDirectory() {
		return recursiveDirectory;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}

	// public int getMaximumThreadSize() {
	// return maximumThreadSize;
	// }

	public int getFolderTier() {
		return folderTier;
	}

	public void setFolderTier(int folderTier) {
		this.folderTier = folderTier;
	}

	public ObjectFilter getObjectFilter() {
		return objectFilter;
	}

}
