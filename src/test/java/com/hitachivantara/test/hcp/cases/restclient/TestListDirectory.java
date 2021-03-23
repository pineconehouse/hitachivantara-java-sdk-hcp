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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.api.event.ListObjectHandler;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.request.impl.ListDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestListDirectory extends TestHCPBase {
	long index = 0;
	long dircount = 0;
	long objcount = 0;
	double size = 0;

//	@Test
//	public void testDeleteSetGetCheckACL() throws InvalidResponseException, HSCException, IOException {
//		// PREPARE TEST DATA ----------------------------------------------------------------------
//		// PREPARE TEST DATA ----------------------------------------------------------------------
//
//		// EXEC TEST FUNCTION ---------------------------------------------------------------------
//		// EXEC TEST FUNCTION ---------------------------------------------------------------------
//
//		// RESULT VERIFICATION --------------------------------------------------------------------
//		// RESULT VERIFICATION --------------------------------------------------------------------
//
//		// CLEAN ----------------------------------------------------------------------------------
//		// CLEAN ----------------------------------------------------------------------------------
//	}

	@Test
	public void testListDir() throws Exception {
		HCPObjectEntrys result = hcpClient.listDirectory(super.basedir);
		ObjectEntryIterator it = result.iterator();
		List<HCPObjectEntry> list;
		int totalcount = 0;
		double totalsize = 0;
		int c = 10;
		while ((list = it.next(c)) != null) {
			totalcount += list.size();
			// print.addRow("-" + list.size() + "-----------------------------------------------");
			for (int i = 0; i < list.size(); i++) {
				HCPObjectEntry hcpObjectEntry = list.get(i);

				// print.addRow((ii++) + "\t" + hcpObjectEntry.getType() + "\t" + hcpObjectEntry.getKey() + "\t" +
				// hcpObjectEntry.getName() + "\t" + hcpObjectEntry.getSize());

				if (hcpObjectEntry.isDirectory()) {
					print.appendRecord(new Object[] { hcpObjectEntry
							.getKey(), hcpObjectEntry.getName(), hcpObjectEntry.getType(), hcpObjectEntry.getIngestTime(), hcpObjectEntry.getChangeTime() });
				} else {
					print.appendRecord(new Object[] { hcpObjectEntry.getKey(),
							hcpObjectEntry.getName(),
							hcpObjectEntry.getType(),
							hcpObjectEntry.getSize(),
							hcpObjectEntry.getHashAlgorithmName(),
							hcpObjectEntry.getContentHash(),
							hcpObjectEntry.getETag(),
							hcpObjectEntry.getRetention(),
							hcpObjectEntry.getRetentionString(),
							hcpObjectEntry.getRetentionClass(),
							hcpObjectEntry.getIngestTime(),
							hcpObjectEntry.isHold(),
							hcpObjectEntry.isShred(),
							hcpObjectEntry.getDpl(),
							hcpObjectEntry.isIndexed(),
							hcpObjectEntry.hasMetadata(),
							hcpObjectEntry.getCustomMetadatas(),
							hcpObjectEntry.getVersionId(),
							hcpObjectEntry.isReplicated(),
							hcpObjectEntry.getChangeTime(),
							hcpObjectEntry.getOwner(),
							hcpObjectEntry.getDomain(),
							hcpObjectEntry.hasAcl(),
							hcpObjectEntry.getState() });

					totalsize += hcpObjectEntry.getSize();
				}
			}
			print.appendCuttingLine('-');
			print.printout();
		}
		print.appendRecord("totalcount: " + totalcount);
		print.appendRecord("totalsize: " + totalsize);

		it.close();
		assertTrue(true);
	}

//	@Test
//	public void testListAllSubFolder() throws HSCException {
//		listFolder("sdk-test/moreThan100objs/10", namespace2Name);
//		System.out.println("Final " + objcount + "\t" + dircount);
//	}

	private void listFolder(String dir, String namespace) throws HSCException {
		HCPObjectEntrys result = hcpClient.listDirectory(new ListDirectoryRequest(dir).withNamespace(namespace));
		ObjectEntryIterator it = result.iterator();
		List<HCPObjectEntry> list;
		List<HCPObjectEntry> subfolder = new ArrayList<HCPObjectEntry>();
		int c = 1000;
		long beginTime = System.currentTimeMillis();
		while ((list = it.next(c)) != null) {
			for (int i = 0; i < list.size(); i++) {
				HCPObjectEntry hcpObjectEntry = list.get(i);

				// print.addRow(new Object[] { hcpObjectEntry.getKey(), hcpObjectEntry.getName(),
				// hcpObjectEntry.getType(), hcpObjectEntry.getChangeTime() });
				// print.addRow(hcpObjectEntry.getKey());
				System.out.println(++index + "\t" + hcpObjectEntry.getKey());
				if (hcpObjectEntry.isDirectory()) {
					dircount++;
				} else {
					objcount++;
				}

				if (objcount % 10000 == 0) {
					long endTime = System.currentTimeMillis();
					System.out.println(objcount + "\t" + dircount + "\t" + (endTime - beginTime));
					beginTime = System.currentTimeMillis();
				}

				if (hcpObjectEntry.isDirectory()) {
					// if (!hcpObjectEntry.getName().equals(".lost+found"));
					// hcpClient.deleteDirectory(hcpObjectEntry.getKey());
					// listFolder(hcpObjectEntry.getKey());
					subfolder.add(hcpObjectEntry);
				}
			}
		}

		it.close();

		// for (HCPObjectEntry hcpObjectEntry : subfolder) {
		// listFolder(hcpObjectEntry.getKey(), namespace);
		// }

		// print.printout();

	}

	@Test
	public void testListAllInNamespace() throws Exception {
		ListObjectHandler listener = new ListObjectHandler() {
			public NextAction foundObject(HCPObjectSummary objectEntry) throws HSCException {
				 console.println(objectEntry.getKey()+"\t"+objectEntry.getType()+"\t"+objectEntry.getSize()+"\t"+objectEntry.getIngestTime()+"\t"+objectEntry.getChangeTime());
//				print.appendRecord(objectEntry.getKey(), objectEntry.getType(), objectEntry.getSize(), objectEntry.getIngestTime(), objectEntry.getChangeTime());
				if (objectEntry.isDirectory()) {
					dircount++;
				} else {
					objcount++;
				}
				size += (objectEntry.getSize() != null ? objectEntry.getSize() : 0);

				// if ("/data/data/273/pentaho_ba_solution_brief_20170721.pdf".equals(objectEntry.getKey())) {
				// return NextAction.STOP;
				// }
				return null;
			}
		};
		ListObjectRequest request = new ListObjectRequest("/").withRecursiveDirectory(true);
		hcpClient.listObjects(request, listener);

//		print.appendCuttingLine('-');
//		print.appendRecord(objcount);
//		print.appendRecord(dircount);
//		print.appendRecord(size, ((double) size) / (1024 * 1024 * 1024));
//		print.printout();
	}

}