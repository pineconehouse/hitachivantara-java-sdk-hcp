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
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.model.request.CopyRequestBase;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithSourceVersion;

public class MoveObjectRequest extends CopyRequestBase<MoveObjectRequest> implements ReqWithSourceVersion<MoveObjectRequest> {
	private String sourceVersionId = null;

	public MoveObjectRequest() {
		super();
		this.withCopyingOldVersion(true);
	}

	public MoveObjectRequest(String sourceKey, String targetKey) {
		super(sourceKey, targetKey);
		this.withCopyingOldVersion(true);
	}

	/**
	 * Get a specific old version
	 * 
	 * @param versionId
	 * @return
	 */
	public MoveObjectRequest withSourceVersion(String versionId) {
		this.sourceVersionId = versionId;
		// Disable copying old version when version id specificed
		this.withCopyingOldVersion(false);
		return this;
	}

	public String getSourceVersionId() {
		return sourceVersionId;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		super.validRequestParameter();
		
		ValidUtils.invalidIfTrue(StringUtils.isNotEmpty(sourceVersionId) && super.isCopyingOldVersion(),
				"Do not specific source version ID if moving old version option is ON.");
	}
}
