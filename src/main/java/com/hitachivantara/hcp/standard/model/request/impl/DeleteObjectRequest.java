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
import com.hitachivantara.hcp.standard.model.request.content.ReqWithPurgeDelete;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithVersion;

/**
 * @author sohan
 *
 */
public class DeleteObjectRequest extends HCPStandardRequest<DeleteObjectRequest> implements ReqWithVersion<DeleteObjectRequest>, ReqWithPurgeDelete<DeleteObjectRequest> {
	private boolean purge = false;
	private boolean privileged = false;
	private String reason = "";
	private String versionId = null;

	public DeleteObjectRequest() {
		super(Method.DELETE);
	}

	public DeleteObjectRequest(String key) {
		super(Method.DELETE, key);
	}

	public DeleteObjectRequest(String key, boolean privileged, String reason) {
		super(Method.DELETE, key);
		this.privileged = privileged;
		this.reason = reason;
	}

	/**
	 * Deleting a specific old version
	 * 
	 * @param versionId
	 * @return
	 */
	public DeleteObjectRequest withVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithPurgeDelete#withPurgeDelete(boolean)
	 */
	public DeleteObjectRequest withPurge(boolean with) {
		this.purge = with;
		return this;
	}

	/**
	 * Privileged delete is an HCP feature that enables you to delete objects even if they are under retention. This feature is available only
	 * for namespaces in enterprise mode. If a namespace is in compliance mode, you cannot delete objects that are under retention.
	 * 
	 * Privileged delete supports government regulations that require the destruction of certain types of data in response to changing
	 * circumstances. For example, companies may be required to destroy particular information about employees who leave. If that data is under
	 * retention, it cannot be deleted through normal delete operations.
	 * 
	 * If the namespace supports versioning, you can turn a privileged delete operation into a privileged purge operation. This deletes all
	 * versions of the target object.
	 * 
	 * When using privileged delete, you need to specify a reason for the deletion. The tenant log records all privileged delete operations,
	 * including the specified reasons, thereby creating an audit trail.
	 * 
	 * Using privileged delete, you can also delete objects that are not under retention. You would do this, for example, if you wanted to
	 * record the reason for an object deletion.
	 * 
	 * You cannot use privileged delete to delete objects that are on hold, regardless of their retention settings.
	 * 
	 * @param with
	 * @param reason
	 * @return
	 */
	public DeleteObjectRequest withPrivileged(boolean with, String reason) {
		this.privileged = with;
		this.reason = reason;
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

	public String getVersionId() {
		return versionId;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
	}

}
