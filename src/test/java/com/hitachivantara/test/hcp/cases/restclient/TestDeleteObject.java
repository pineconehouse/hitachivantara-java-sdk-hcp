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

import java.util.List;

import org.junit.Test;

import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListVersionRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestDeleteObject extends TestHCPBase {
	@Test
	public void testPurgeDeleteObjects() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepareSmallObject(key1);
		boolean exist = hcpClient.doesObjectExist(key1);
		assertTrue(exist);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean deleted = hcpClient.deleteObject(new DeleteObjectRequest().withKey(key1));
		assertTrue(deleted);
		deleted = hcpClient.deleteObject(new DeleteObjectRequest().withKey(key1));
		assertTrue(deleted==false);
		deleted = hcpClient.deleteObject(new DeleteObjectRequest().withKey(key1).withPurge(true));
		assertTrue(deleted);
		deleted = hcpClient.deleteObject(new DeleteObjectRequest().withKey(key1));
		assertTrue(deleted==false);
		deleted = hcpClient.deleteObject(new DeleteObjectRequest().withKey(key1).withPurge(true));
		assertTrue(deleted==false);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	
	@Test
	public void testDeleteAndCheckObjectExistion() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepareSmallObject(key1);
		boolean exist = hcpClient.doesObjectExist(key1);
		assertTrue(exist);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.deleteObject(key1);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		exist = hcpClient.doesObjectExist(key1);
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}


	@Test
	public void testDeleteSpecificVersion() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteObject(new DeleteObjectRequest(key1).withNamespace(namespace2Name).withPurge(true));
		TestDataFactory.getInstance().prepare10VersionsOfObject(key1, namespace2Name);
		HCPObjectEntrys verentry = hcpClient.listVersions(new ListVersionRequest(key1).withNamespace(namespace2Name));
		ObjectEntryIterator it = verentry.iterator();
		List<HCPObjectEntry> versions = it.next(100);
		String versionId = versions.get(versions.size() / 2).getVersionId();
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = hcpClient.doesObjectExist(new CheckObjectRequest(key1).withNamespace(namespace2Name).withVersionId(versionId));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// PREPARE TEST DATA ----------------------------------------------------------------------
		boolean deleted = hcpClient.deleteObject(new DeleteObjectRequest(key1).withNamespace(namespace2Name).withVersionId(versionId));
		assertTrue(deleted == true);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesObjectExist(new CheckObjectRequest(key1).withNamespace(namespace2Name).withVersionId(versionId));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}
}