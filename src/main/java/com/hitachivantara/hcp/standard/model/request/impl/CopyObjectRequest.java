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

/**
 * Request for copy an object or existing version of an object from one location to another.Specific source key and target key <b>(the
 * namespace of source and target can be different)</b>, specific whether copying all the objects in sub directory or the metadata of each
 * object, specific whether copying old versions
 * 
 * @author sohan
 *
 */
public class CopyObjectRequest extends CopyRequestBase<CopyObjectRequest> implements ReqWithSourceVersion<CopyObjectRequest> {
	private String sourceVersionId = null;

	public CopyObjectRequest() {
		super();
	}

	public CopyObjectRequest(String sourceKey, String targetKey) {
		super(sourceKey, targetKey);
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithSourceVersion#withSourceVersion(java.lang.String)
	 */
	public CopyObjectRequest withSourceVersion(String versionId) {
		this.sourceVersionId = versionId;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithSourceVersion#getSourceVersionId()
	 */
	public String getSourceVersionId() {
		return sourceVersionId;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		super.validRequestParameter();
		ValidUtils.invalidIfTrue(StringUtils.isNotEmpty(sourceVersionId) && super.isCopyingOldVersion(),
				"Do not specific source version ID if copying old version option is ON.");
	}
}
