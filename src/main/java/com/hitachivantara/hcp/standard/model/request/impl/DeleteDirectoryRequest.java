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
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.api.event.ObjectDeletingListener;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithPurgeDelete;

/**
 * @author sohan
 *
 */
public class DeleteDirectoryRequest extends HCPStandardRequest<DeleteDirectoryRequest>
		implements ReqWithPurgeDelete<DeleteDirectoryRequest> {
	private ObjectDeletingListener listener;
//	private final boolean recursiveDirectory = true;
	private boolean purge = false;
	private boolean privileged = false;
	private String reason = "";
	private boolean deleteContainedObjects = false;

	public DeleteDirectoryRequest() {
		super(Method.DELETE);
	}

	public DeleteDirectoryRequest(String key) {
		super(Method.DELETE, key);
	}

//	public DeleteDirectoryRequest withRecursiveDirectory(boolean with) {
//		this.recursiveDirectory = with;
//		return this;
//	}

	public DeleteDirectoryRequest withDeleteListener(ObjectDeletingListener listener) {
		this.listener = listener;
		return this;
	}

	public DeleteDirectoryRequest withDirectory(String directory) {
		return this.withKey(directory);
	}

	/**
	 * If versioning has never been enabled for the namespace, a purge request deletes the object. If versioning was enabled in the past but is
	 * no longer enabled, the purge request deletes all existing versions of the object.
	 * 
	 * @return
	 */
	public DeleteDirectoryRequest withPurge(boolean with) {
		this.purge = with;
		return this;
	}

	public DeleteDirectoryRequest withPrivileged(boolean with, String reason) {
		this.privileged = with;
		this.reason = reason;
		return this;
	}

	public boolean isDeleteContainedObjects() {
		return deleteContainedObjects;
	}

	public DeleteDirectoryRequest withDeleteContainedObjects(boolean deleteContainedObjects) {
		this.deleteContainedObjects = deleteContainedObjects;
		return this;
	}

	public boolean isPrivileged() {
		return privileged;
	}

	public String getReason() {
		return reason;
	}

	public boolean isPurge() {
		return purge;
	}

//	public boolean isRecursiveDirectory() {
//		return recursiveDirectory;
//	}

	public ObjectDeletingListener getObjectDeleteListener() {
		return listener;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfTrue("/".equals(this.getKey()), "Prohibit deletion of root directory.");
		// ValidUtils.exceptionWhenTrue(".lost+found".equals(this.getKey()), "Prohibit deletion of system directory.");

	}

}
