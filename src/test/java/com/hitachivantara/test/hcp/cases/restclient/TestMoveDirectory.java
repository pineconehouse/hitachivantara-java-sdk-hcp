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
import com.hitachivantara.hcp.standard.api.event.ObjectDeletingListener;
import com.hitachivantara.hcp.standard.api.event.ObjectMovingListener;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListVersionRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MoveDirectoryRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestMoveDirectory extends TestHCPBase {

	int c = 0;

	@Test
	public void testMoveDirFromDifferentNamespaceWithoutOldVersionAndMetadata() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		console.println("preparing test data...");
		final String sourceDirectoryKey = moreThan100objs;
		final String sourceNamespace = namespace2Name;
		final String targetDirectoryKey = sourceDirectoryKey.substring(0, sourceDirectoryKey.length() - 2) + ".copy/";
		final String targetNamespace = namespace1Name;

		// Assert is different namespace
		assertTrue(!sourceNamespace.equalsIgnoreCase(targetNamespace));

		// Delete source dir
		if (hcpClient.doesDirectoryExist(new CheckObjectRequest(sourceDirectoryKey).withNamespace(sourceNamespace))) {
			hcpClient.deleteDirectory(
					new DeleteDirectoryRequest().withKey(sourceDirectoryKey).withNamespace(sourceNamespace).withPurge(true).withDeleteContainedObjects(true));
		}

		// Delete target dir
		if (hcpClient.doesDirectoryExist(new CheckObjectRequest(targetDirectoryKey).withNamespace(targetNamespace))) {
			hcpClient.deleteDirectory(
					new DeleteDirectoryRequest().withKey(targetDirectoryKey).withNamespace(targetNamespace).withPurge(true).withDeleteContainedObjects(true));
		}

		final int count = TestDataFactory.getInstance().prepare100MoreObjWithMetadataAndVersionsDir(sourceDirectoryKey, sourceNamespace);

		assertTrue(hcpClient.doesObjectExist(new CheckObjectRequest(sourceDirectoryKey).withNamespace(sourceNamespace)) == true);

		final List<HCPObjectSummary> sourceObjectEntrys = new ArrayList<HCPObjectSummary>();
		ListObjectRequest listreq = new ListObjectRequest().withDirectory(sourceDirectoryKey).withNamespace(sourceNamespace).withRecursiveDirectory(true);
		ListObjectHandler listener = new ListObjectHandler() {
			public NextAction foundObject(HCPObjectSummary objectEntry) throws HSCException {
				if (objectEntry.isObject()) {
					sourceObjectEntrys.add(objectEntry);
				}
				return null;
			}
		};
		hcpClient.listObjects(listreq, listener);

		console.println("test data ready!");
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		MoveDirectoryRequest request = new MoveDirectoryRequest(sourceDirectoryKey, targetDirectoryKey).withCopyingMetadata(false).withCopyingOldVersion(false)
				.withSourceNamespace(sourceNamespace).withTargetNamespace(targetNamespace).withMoveListener(new ObjectMovingListener() {

					public NextAction beforeMoving(HCPObjectSummary sourceObjectEntry) {
						// TODO Auto-generated method stub
						console.println("beforeMove " + sourceObjectEntry.getKey());
						return null;
					}

					public NextAction afterMoving(HCPObjectSummary sourceObjectEntry) {
						// TODO Auto-generated method stub
						console.println("afterMove " + sourceObjectEntry.getKey());
						return null;
					}
				});
		hcpClient.moveDirectory(request);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		{
			assertTrue(hcpClient.doesObjectExist(new CheckObjectRequest(targetDirectoryKey).withNamespace(targetNamespace)) == true);

			for (HCPObjectSummary sourceSummary : sourceObjectEntrys) {
				String tmpTargetKey = sourceSummary.getKey().replace(sourceDirectoryKey, targetDirectoryKey);
				console.println("check srcobj=" + sourceSummary.getKey() + "\t targetobj=" + tmpTargetKey);
				boolean exist = hcpClient.doesObjectExist(new CheckObjectRequest(tmpTargetKey).withNamespace(targetNamespace));
				if (!exist) {
					console.println("target obj not found=" + sourceSummary.getKey());
				} else {
					HCPObjectSummary targetSummary = hcpClient.getObjectSummary(new CheckObjectRequest().withNamespace(targetNamespace).withKey(tmpTargetKey));
					String targetHash = targetSummary.getContentHash();
					String sourceHash = sourceSummary.getContentHash();

					// Assert etag is euqals
					assertTrue(sourceHash.equals(targetHash));

					// Assert source object metadata count >0
					assertTrue(sourceSummary.getCustomMetadatas().length > 0);
					// Assert target object metadata did not be copied
					assertTrue(targetSummary.getCustomMetadatas() == null);

					HCPObjectEntrys targetVersion = hcpClient.listVersions(new ListVersionRequest(tmpTargetKey).withNamespace(targetNamespace));
					ObjectEntryIterator it = targetVersion.iterator();
					List<HCPObjectEntry> targetVersions = it.next(10);
					// Assert target object only has one version(himself)
					assertTrue(targetVersions.size() == 1);
				}
				assertTrue(exist == true);

				c++;
			}

			assertTrue(c == count);
		}
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testMoveDirectoryWithOldVersionsAndMetadata() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		console.println("preparing test data...");
		final String sourceDirectoryKey = moreThan100objs;
		final String sourceNamespace = namespace2Name;
		final String targetDirectoryKey = sourceDirectoryKey.substring(0, sourceDirectoryKey.length() - 2) + ".copy/";
		final String targetNamespace = namespace1Name;

		// Assert is different namespace
		assertTrue(!sourceNamespace.equalsIgnoreCase(targetNamespace));

		// Delete source dir
		if (hcpClient.doesDirectoryExist(new CheckObjectRequest(sourceDirectoryKey).withNamespace(sourceNamespace))) {
			hcpClient.deleteDirectory(new DeleteDirectoryRequest().withKey(sourceDirectoryKey).withNamespace(sourceNamespace).withPurge(true).withDeleteContainedObjects(true)
					.withDeleteListener(new ObjectDeletingListener() {

						@Override
						public NextAction beforeDeleting(HCPObjectSummary objectEntry) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public NextAction afterDeleting(HCPObjectSummary objectEntry, boolean deleted) {
							System.out.print("s");
							return null;
						}
					}));
		}

		// Delete target dir
		if (hcpClient.doesDirectoryExist(new CheckObjectRequest(targetDirectoryKey).withNamespace(targetNamespace))) {
			hcpClient.deleteDirectory(new DeleteDirectoryRequest().withKey(targetDirectoryKey).withNamespace(targetNamespace).withPurge(true).withDeleteContainedObjects(true)
					.withDeleteListener(new ObjectDeletingListener() {

						@Override
						public NextAction beforeDeleting(HCPObjectSummary objectEntry) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public NextAction afterDeleting(HCPObjectSummary objectEntry, boolean deleted) {
							System.out.print("t");
							return null;
						}
					}));
		}
		console.println();
		console.println("prepare100MoreObjWithMetadataAndVersionsDir:" + sourceDirectoryKey);

		final int count = TestDataFactory.getInstance().prepare100MoreObjWithMetadataAndVersionsDir(sourceDirectoryKey, sourceNamespace);

		boolean exist = hcpClient.doesObjectExist(new CheckObjectRequest(sourceDirectoryKey).withNamespace(sourceNamespace));
		assertTrue(exist == true);

		final List<HCPObjectSummary> sourceObjectEntrys = new ArrayList<HCPObjectSummary>();
		ListObjectRequest listreq = new ListObjectRequest().withDirectory(sourceDirectoryKey).withNamespace(sourceNamespace).withRecursiveDirectory(true);
		ListObjectHandler listener = new ListObjectHandler() {
			public NextAction foundObject(HCPObjectSummary objectEntry) throws HSCException {
				if (objectEntry.isObject()) {
					sourceObjectEntrys.add(objectEntry);
				}
				return null;
			}
		};
		hcpClient.listObjects(listreq, listener);

		console.println("test data ready!");
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		console.println("moving!");
		MoveDirectoryRequest request = new MoveDirectoryRequest(sourceDirectoryKey, targetDirectoryKey).withCopyingMetadata(true).withCopyingOldVersion(true)
				.withSourceNamespace(sourceNamespace).withTargetNamespace(targetNamespace);
		hcpClient.moveDirectory(request);
		console.println("moving done!");
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		{
			exist = hcpClient.doesObjectExist(new CheckObjectRequest(targetDirectoryKey).withNamespace(targetNamespace));
			assertTrue(exist == true);

			for (HCPObjectSummary sourceSummaryEntry : sourceObjectEntrys) {
				String tmpTargetKey = sourceSummaryEntry.getKey().replace(sourceDirectoryKey, targetDirectoryKey);
				console.println("check srcobj=" + sourceSummaryEntry.getKey() + "\t targetobj=" + tmpTargetKey);
				boolean targetexist = hcpClient.doesObjectExist(new CheckObjectRequest(tmpTargetKey).withNamespace(targetNamespace));
				boolean srcexist = hcpClient.doesObjectExist(new CheckObjectRequest(sourceSummaryEntry.getKey()).withNamespace(sourceNamespace));
				
				if (srcexist) {
					console.println("src obj did not deleted=" + sourceSummaryEntry.getKey());
				} 
				if (!targetexist) {
					console.println("target obj not found=" + tmpTargetKey);
				} else {
//					HCPObjectEntrys targetVersion = hcpClient.listVersions(new ListVersionRequest(tmpTargetKey).withNamespace(targetNamespace));
//					ObjectEntryIterator it = targetVersion.iterator();
//					List<HCPObjectEntry> targetVersions = it.next(10);
//					// Assert target object only has one version(himself)
//					assertTrue(targetVersions.size() > 3);
//
////					HCPObjectEntrys sourceVersion = hcpClient.listVersions(new ListVersionRequest(sourceSummaryEntry.getKey()).withNamespace(sourceNamespace));
////					ObjectEntryIterator sit = sourceVersion.iterator();
////					List<HCPObjectEntry> sourceVersions = sit.next(10);
//					// Assert target object only has one version(himself)
//					assertTrue(sourceVersions.size() > 3);
//
//					assertTrue(sourceVersions.size() == targetVersions.size());
//
//					for (int i = 0; i < sourceVersions.size(); i++) {
////						HCPObjectEntry sourceObjectEntry = sourceVersions.get(i);
//						HCPObjectEntry targetObjectEntry = targetVersions.get(i);
//
////						HCPObjectSummary sourceSummary = hcpClient.getObjectSummary(new CheckObjectRequest().withNamespace(sourceNamespace).withKey(sourceObjectEntry.getKey()));
//						HCPObjectSummary targetSummary = hcpClient.getObjectSummary(new CheckObjectRequest().withNamespace(targetNamespace).withKey(targetObjectEntry.getKey()));
//
////						String sourceHash = sourceSummary.getContentHash();
//						String targetHash = targetSummary.getContentHash();
//
//						assertTrue(sourceHash.equals(targetHash));
//
////						Annotation[] sourceMetadatas = sourceSummary.getMetadata();
//						Annotation[] targetMetadatas = targetSummary.getMetadata();
//
//						assertTrue(sourceMetadatas.length == targetMetadatas.length);
//						assertTrue(sourceMetadatas.length >= 2);
//
//						for (int j = 0; j < targetMetadatas.length; j++) {
//							Annotation targetAnnotation = targetMetadatas[j];
//							Annotation sourceAnnotation = sourceMetadatas[j];
//
//							assertTrue(targetAnnotation.getName().equals(sourceAnnotation.getName()));
//							assertTrue(targetAnnotation.getSize().equals(sourceAnnotation.getSize()));
//						}
//
//					}
				}
				assertTrue(exist == true);

				c++;
			}

			assertTrue(c == count);
		}
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

}