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
import com.hitachivantara.hcp.standard.api.event.ObjectMovingListener;
import com.hitachivantara.hcp.standard.model.request.CopyRequestBase;

public class MoveDirectoryRequest extends CopyRequestBase<MoveDirectoryRequest> {//implements ReqWithRecursiveDirectory<MoveDirectoryRequest> {
	private ObjectMovingListener listener;
//	private boolean recursiveDirectory = true;

	public MoveDirectoryRequest() {
		super();
		this.withCopyingOldVersion(true);
	}

	public MoveDirectoryRequest(String sourceKey, String targetKey) {
		super(sourceKey, targetKey);
		this.withCopyingOldVersion(true);
	}

//	public MoveDirectoryRequest withRecursiveDirectory(boolean with) {
//		this.recursiveDirectory = with;
//		return this;
//	}
	
	public MoveDirectoryRequest withMoveListener(ObjectMovingListener listener) {
		this.listener = listener;
		return this;
	}

	public ObjectMovingListener getObjectMoveListener() {
		return listener;
	}

//	public boolean isRecursiveDirectory() {
//		return recursiveDirectory;
//	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		super.validRequestParameter();
	}
	
}
