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
import com.hitachivantara.hcp.standard.body.HCPNamespaceBase;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;

public class PartObjectGetter implements Runnable {
	private final String localFile;
	private final HCPNamespaceBase hcpClient;

	public PartObjectGetter(String localFile, long offset, HCPNamespaceBase hcpClient) {
		this.localFile = localFile;
		this.hcpClient = hcpClient;
	}

	public void run() {
		// hcpClient.getObject(request)
	}

	public long retrievingPartObject(GetObjectRequest request) throws HSCException {
		long writeLen = 0;
		
//		HCPObject obj = hcpClient.getObject(request);
		
//		HCPInputStream in = obj.getContent();
		
//		FileWriter fw = new FileWriter();
		
		
		return writeLen;
	}

}
