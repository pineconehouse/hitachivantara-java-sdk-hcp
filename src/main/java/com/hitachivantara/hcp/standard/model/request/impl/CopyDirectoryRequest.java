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
import com.hitachivantara.hcp.standard.api.event.ObjectCopyingListener;
import com.hitachivantara.hcp.standard.model.request.CopyRequestBase;
import com.hitachivantara.hcp.standard.model.request.content.ReqWithRecursiveDirectory;

/**
 * Request for copy an directory and there objects or existing version of an object from one location to another.
 * Specific source directory and target directory <b>(the namespace of source and target can be different)</b>, specific whether copying all
 * the objects in sub directory or the metadata of each object, specific whether copying old versions
 * 
 * @author sohan
 *
 */
public class CopyDirectoryRequest extends CopyRequestBase<CopyDirectoryRequest> implements ReqWithRecursiveDirectory<CopyDirectoryRequest> {
	private ObjectCopyingListener listener;
	private boolean recursiveDirectory = true;

	public CopyDirectoryRequest() {
		super();
	}

	public CopyDirectoryRequest(String sourceKey, String targetKey) {
		super(sourceKey, targetKey);
	}


	/* (non-Javadoc)
	 * @see com.hitachivantara.hcp.standard.model.request.content.ReqWithRecursiveDirectory#withRecursiveDirectory(boolean)
	 */
	public CopyDirectoryRequest withRecursiveDirectory(boolean with) {
		this.recursiveDirectory = with;
		return this;
	}

	/**
	 * @param listener
	 * @return
	 */
	public CopyDirectoryRequest withCopyListener(ObjectCopyingListener listener) {
		this.listener = listener;
		return this;
	}

	public ObjectCopyingListener getObjectCopyListener() {
		return listener;
	}

	public boolean isRecursiveDirectory() {
		return recursiveDirectory;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		super.validRequestParameter();
	}

}
