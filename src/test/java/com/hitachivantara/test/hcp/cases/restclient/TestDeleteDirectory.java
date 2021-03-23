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

import java.io.File;

import org.junit.Test;

import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.api.event.ObjectDeletingListener;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.define.ObjectType;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestDeleteDirectory extends TestHCPBase {
	int deletedDirCount = 0;
	int deletedObjCount = 0;
	long totalDeletedSize = 0;

	@Test
	public void testDeleteFolderInSpecificNamespace() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepare100MoreObjDir(moreThan100objs, namespace2Name);
		assertTrue(hcpClient.doesDirectoryExist(new CheckObjectRequest(moreThan100objs).withNamespace(namespace2Name)) == true);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		// DeleteDirectoryRequest req = new DeleteDirectoryRequest("data/HCI");// .withNamespace("test");
		DeleteDirectoryRequest req = new DeleteDirectoryRequest(moreThan100objs).withNamespace(namespace2Name).withPurge(true).withPrivileged(true, "I said!");

		req
//		.withRecursiveDirectory(true)
		.withDeleteContainedObjects(true)
		.withDeleteListener(new ObjectDeletingListener() {
			int i = 0;

			public NextAction afterDeleting(HCPObjectSummary objectEntry, boolean deleted) {
				System.out.println(++i + " " + objectEntry.getKey() + " " + objectEntry.getType() + " " + deleted);
				if (deleted) {
					if (objectEntry.isDirectory()) {
						deletedDirCount++;
					} else if (objectEntry.isObject()) {
						deletedObjCount++;
						totalDeletedSize += objectEntry.getSize();
					}
				}
				return null;
			}

			public NextAction beforeDeleting(HCPObjectSummary hcpObjectEntry) {
				return null;
			}

		});
		boolean result = hcpClient.deleteDirectory(req);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		System.out.println("deletedDirCount=" + deletedDirCount + " deletedObjCount=" + deletedObjCount + " totalDeletedSize=" + totalDeletedSize);

		assertTrue(hcpClient.doesDirectoryExist(new CheckObjectRequest(moreThan100objs).withNamespace(namespace2Name)) == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testDeleteSubFolder() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		for (int i = 0; i < 100; i++) {
			File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
			String key = basedir + "10objs/" + KeyAlgorithm.CONSERVATIVE_KEY_HASH_D32.generate(tempFile.getName());
			// ----------------------------------------------------------------------------------------
			PutObjectResult result = hcpClient.putObject(key, tempFile);
			// ----------------------------------------------------------------------------------------
		}

		for (int i = 0; i < 150; i++) {
			File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
			String key = basedir + "10objs/sub1/" + tempFile.getName();
			// ----------------------------------------------------------------------------------------
			PutObjectResult result = hcpClient.putObject(key, tempFile);
			// ----------------------------------------------------------------------------------------
		}
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		DeleteDirectoryRequest req = new DeleteDirectoryRequest(basedir + "10objs")
		.withDeleteContainedObjects(true)
//		.withRecursiveDirectory(true)
		.withDeleteListener(new ObjectDeletingListener() {
			int totalDirectoryCount = 0;
			int totalObjectCount = 0;
			int totalCount = 0;
			int totalSize = 0;

			public NextAction afterDeleting(HCPObjectSummary objectEntry, boolean deleted) {
				System.out.println(objectEntry.getKey() + " " + objectEntry.getType() + " " + deleted);
				if (deleted) {
					if (objectEntry.getType() == ObjectType.object) {
						totalObjectCount++;
					}
				}
				return null;
			}

			public NextAction beforeDeleting(HCPObjectSummary hcpObjectEntry) {
				return null;
			}
		});
		boolean result = hcpClient.deleteDirectory(req);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// print.addRow("TotalCount=" + result.getTotalCount());
		// print.addRow("TotalDirectoryCount=" + result.getTotalDirectoryCount());
		// print.addRow("TotalObjectCount=" + result.getTotalObjectCount());
		// print.addRow("TotalSize=" + result.getTotalSize());

		assertTrue(hcpClient.doesDirectoryExist(basedir + "10objs") == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}
	

//	@Test
//	public void testDeleteTargetFolderButWithoutSubFolder() throws Exception {
//		// PREPARE TEST DATA ----------------------------------------------------------------------
//		TestDataFactory.getInstance().prepare100MoreObjDir(basedir + "x1objs/", this.namespace1Name);
//		assertTrue(hcpClient.doesDirectoryExist(basedir + "x1objs") == true);
//		// PREPARE TEST DATA ----------------------------------------------------------------------
//
//		// EXEC TEST FUNCTION ---------------------------------------------------------------------
//		DeleteDirectoryRequest req = new DeleteDirectoryRequest(basedir + "x1objs")
//		.withDeleteContainedObjects(true)
////		.withRecursiveDirectory(false)
//		.withDeleteListener(new ObjectDeletingListener() {
//			int totalDirectoryCount = 0;
//			int totalObjectCount = 0;
//			int totalCount = 0;
//			int totalSize = 0;
//
//			public NextAction afterDeleting(HCPObjectSummary objectEntry, boolean deleted) {
//				System.out.println(objectEntry.getKey() + " " + objectEntry.getType() + " " + deleted);
//				if (deleted) {
//					if (objectEntry.getType() == ObjectType.object) {
//						totalObjectCount++;
//					}
//				}
//				return null;
//			}
//
//			public NextAction beforeDeleting(HCPObjectSummary hcpObjectEntry) {
//				return null;
//			}
//		});
//		boolean result = hcpClient.deleteDirectory(req);
//		// EXEC TEST FUNCTION ---------------------------------------------------------------------
//
//		// RESULT VERIFICATION --------------------------------------------------------------------
//		// print.addRow("TotalCount=" + result.getTotalCount());
//		// print.addRow("TotalDirectoryCount=" + result.getTotalDirectoryCount());
//		// print.addRow("TotalObjectCount=" + result.getTotalObjectCount());
//		// print.addRow("TotalSize=" + result.getTotalSize());
//
//		assertTrue(hcpClient.doesDirectoryExist(basedir + "10objs") == true);
//		// RESULT VERIFICATION --------------------------------------------------------------------
//	}
	
	@Test
	public void testDeleteEmptyFolder() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.createDirectory(basedir+"empty/rrt");
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		DeleteDirectoryRequest req = new DeleteDirectoryRequest(basedir + "empty")
		.withDeleteContainedObjects(true)
//		.withRecursiveDirectory(false)
		.withDeleteListener(new ObjectDeletingListener() {
			int totalDirectoryCount = 0;
			int totalObjectCount = 0;
			int totalCount = 0;
			int totalSize = 0;

			public NextAction afterDeleting(HCPObjectSummary objectEntry, boolean deleted) {
				System.out.println(objectEntry.getKey() + " " + objectEntry.getType() + " " + deleted);
				if (deleted) {
					if (objectEntry.getType() == ObjectType.object) {
						totalObjectCount++;
					}
				}
				return null;
			}

			public NextAction beforeDeleting(HCPObjectSummary hcpObjectEntry) {
				return null;
			}
		});
		boolean result = hcpClient.deleteDirectory(req);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// print.addRow("TotalCount=" + result.getTotalCount());
		// print.addRow("TotalDirectoryCount=" + result.getTotalDirectoryCount());
		// print.addRow("TotalObjectCount=" + result.getTotalObjectCount());
		// print.addRow("TotalSize=" + result.getTotalSize());

		assertTrue(hcpClient.doesDirectoryExist(basedir + "empty") == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}
	
//	@Test
//	public void testDelDir() throws Exception {
//		hcpClient.deleteDirectory(new DeleteDirectoryRequest("/Folder/BIOND_ETL_SCHEDULER").withDeleteContainedObjects(true).withDeleteListener(new ObjectDeletingListener() {
//			
//			@Override
//			public NextAction beforeDeleting(HCPObjectSummary objectSummary) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public NextAction afterDeleting(HCPObjectSummary objectSummary, boolean deleted) {
//				System.out.println(objectSummary.getKey()+ deleted);
//				return null;
//			}
//		}));
//	}
//	

}