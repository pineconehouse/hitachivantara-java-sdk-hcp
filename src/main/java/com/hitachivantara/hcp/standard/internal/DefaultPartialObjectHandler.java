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
package com.hitachivantara.hcp.standard.internal;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.standard.api.event.PartialHandlingListener;
import com.hitachivantara.hcp.standard.api.event.PartialObjectHandler;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;

public abstract class DefaultPartialObjectHandler implements PartialObjectHandler {
	protected PartialHandlingListener listener = null;
	protected HCPObjectSummary summary;
	protected int parts;
	protected boolean waitForComplete = true;

	public DefaultPartialObjectHandler() {
		// ValidUtils.exceptionWhenEmpty(localFile, "The local file path must be specificed.");
	}

	public void setRequestSummary(HCPObjectSummary summary, int parts) throws HSCException {
		this.summary = summary;
		this.parts = parts;
	}

	public PartialHandlingListener getListener() {
		return listener;
	}

	public void setListener(PartialHandlingListener listener) {
		this.listener = listener;
	}

	public HCPObjectSummary getSummary() {
		return summary;
	}

	public int getParts() {
		return parts;
	}

	public boolean isWaitForComplete() {
		return waitForComplete;
	}

	public void setWaitForComplete(boolean waitForComplete) {
		this.waitForComplete = waitForComplete;
	}

}
