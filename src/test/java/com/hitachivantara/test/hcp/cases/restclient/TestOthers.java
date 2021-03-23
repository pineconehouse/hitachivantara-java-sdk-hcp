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
package com.hitachivantara.test.hcp.cases.restclient;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.standard.api.event.ListObjectHandler;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestOthers extends TestHCPBase {
	long dircount = 0;
	long count = 0;
	double size = 0;

	@Test
	public void testClearNamespace() throws Exception {
		final String namespace = "mail";
		ListObjectHandler listener = new ListObjectHandler() {
			public NextAction foundObject(HCPObjectSummary objectEntry) throws HSCException {
				if (objectEntry.isDirectory() && ".lost+found".equals(objectEntry.getName())) {
					return null;
				}

				boolean ok = false;
				if (objectEntry.isDirectory()) {
					// ok = hcpClient.deleteDirectory(new
					// DeleteDirectoryRequest().withNamespace(namespace).withKey(objectEntry.getKey()).withPurgeDelete(true).withPrivilegedDelete(true, "I
					// said"));
					ok = hcpClient.deleteDirectory(new DeleteDirectoryRequest().withNamespace(namespace).withKey(objectEntry.getKey()));
				} else {
					size += objectEntry.getSize();
					ok = hcpClient.deleteObject(new DeleteObjectRequest(objectEntry.getKey()).withNamespace(namespace).withPurge(true).withPrivileged(true, "I said"));

				}
				print.appendRecord(ok, objectEntry.getKey(), objectEntry.getType(), objectEntry.getSize(), objectEntry.getIngestTime(), objectEntry.getChangeTime());
				print.printout();
				return null;
			}
		};
		ListObjectRequest request = new ListObjectRequest().withDirectory("/").withNamespace(namespace).withRecursiveDirectory(false).withDeletedObject(true);
		hcpClient.listObjects(request, listener);

	}

	@Test
	public void testX1() throws Exception {
		HCPObjectSummary summary = hcpClient.getObjectSummary("sdk-test1/xx/tomcathcp.png");
		System.out.println(summary.getContentType());
	}

}