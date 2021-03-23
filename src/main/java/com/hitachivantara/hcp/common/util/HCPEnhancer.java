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
package com.hitachivantara.hcp.common.util;

import java.io.File;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.HCPNamespace;
import com.hitachivantara.hcp.standard.api.event.ListObjectHandler;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;

public class HCPEnhancer {
	HCPNamespace client;

	public HCPEnhancer(HCPNamespace namespace) {
		super();
		this.client = namespace;
	}

	public void truncateNamespace(String namespace) throws InvalidResponseException, HSCException {
		ListObjectHandler handler = new ListObjectHandler() {

			@Override
			public NextAction foundObject(HCPObjectSummary objectSummary) throws HSCException {
				if (!".lost+found".equals(objectSummary.getName())) {
					if (objectSummary.isDirectory()) {
						client.deleteDirectory(new DeleteDirectoryRequest(objectSummary.getKey()).withDeleteContainedObjects(true).withPurge(true));
					} else {
						client.deleteObject(new DeleteObjectRequest(objectSummary.getKey()).withPurge(true));
					}
				}

				return null;
			}
		};
		ListObjectRequest request = new ListObjectRequest("/").withNamespace(namespace).withRecursiveDirectory(false);

		client.listObjects(request, handler);
	}

	public void uploadDirectory(File localDirectory, String targetDirKey, int thread ) {

	}
}
