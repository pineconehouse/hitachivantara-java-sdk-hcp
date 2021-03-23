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
import com.hitachivantara.hcp.standard.model.Retention;
import com.hitachivantara.hcp.standard.model.metadata.HCPSystemMetadata;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithSystemMetadata;

public class PutSystemMetadataRequest extends HCPStandardRequest<PutSystemMetadataRequest> implements ReqWithSystemMetadata<PutSystemMetadataRequest> {
	private HCPSystemMetadata metadata = new HCPSystemMetadata();

	public PutSystemMetadataRequest() {
		super(Method.POST);
	}

	public PutSystemMetadataRequest(String namespace, String key) {
		super(Method.POST, namespace, key);
	}

	public PutSystemMetadataRequest(String key) {
		super(Method.POST, key);
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfNull(metadata, "The parameter metadata must be specified.");
		ValidUtils.invalidIfZero(metadata.getMetadataMap().size(), "No configuration specified in system metadata.");
	}

	public boolean isHold() {
		return metadata.isHold();
	}

	public PutSystemMetadataRequest withHold(boolean hold) {
		metadata.setHold(hold);
		return this;
	}

	public boolean isIndex() {
		return metadata.isIndex();
	}

	public PutSystemMetadataRequest withIndex(boolean index) {
		metadata.setIndex(index);
		return this;
	}

	public String getRetention() {
		return metadata.getRetention();
	}

	public PutSystemMetadataRequest withRetention(Retention retention) {
		metadata.setRetention(retention);
		return this;
	}

	public boolean isShred() {
		return metadata.isShred();
	}

	public PutSystemMetadataRequest withShred(boolean shred) {
		metadata.setShred(shred);
		return this;
	}

	public PutSystemMetadataRequest withOwner(String localUserName) {
		metadata.setOwner(localUserName);
		return this;
	}

	public PutSystemMetadataRequest withOwner(String domain, String domainUserName) {
		metadata.setOwner(domain, domainUserName);
		return this;
	}

	public String getOwner() {
		return metadata.getOwner();
	}

	public String getOwnerDomain() {
		return metadata.getOwnerDomain();
	}

	public HCPSystemMetadata getMetadata() {
		return metadata;
	}

	public PutSystemMetadataRequest withMetadata(HCPSystemMetadata metadata) {
		this.metadata = metadata;
		return this;
	}

}
