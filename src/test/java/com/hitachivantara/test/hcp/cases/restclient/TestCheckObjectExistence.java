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
import com.hitachivantara.hcp.standard.model.request.impl.CheckMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CreateDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListVersionRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestCheckObjectExistence extends TestHCPBase {

	@Test
	public void testDoesObjectExist() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteObject(new DeleteObjectRequest(key1).withPurge(true));
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = hcpClient.doesObjectExist(key1);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepareSmallObject(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesObjectExist(key1);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testDoesObjectExistInSpecificNamespace() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteObject(new DeleteObjectRequest(key1).withNamespace(namespace2Name).withPurge(true));
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = hcpClient.doesObjectExist(new CheckObjectRequest(key1).withNamespace(namespace2Name));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepareSmallObject(key1, namespace2Name);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesObjectExist(new CheckObjectRequest(key1).withNamespace(namespace2Name));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testDoesDirectoryExist() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String dirKey = basedir + "test-folder";
		hcpClient.deleteDirectory(new DeleteDirectoryRequest(dirKey).withPurge(true));
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = hcpClient.doesDirectoryExist(dirKey);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.createDirectory(dirKey);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesDirectoryExist(dirKey);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testDoesDirectoryExistInSpecificNamespace() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String dirKey = basedir + "test-folder";
		hcpClient.deleteDirectory(new DeleteDirectoryRequest(dirKey).withNamespace(namespace2Name).withPurge(true).withDeleteContainedObjects(true));
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = hcpClient.doesDirectoryExist(new CheckObjectRequest(dirKey).withNamespace(namespace2Name));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.createDirectory(new CreateDirectoryRequest(dirKey).withNamespace(namespace2Name));
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesObjectExist(new CheckObjectRequest(dirKey).withNamespace(namespace2Name));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testDoesSpecificVersionExist() throws Exception {
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
	}

	@Test
	public void testDoesCustomMetadataExist() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepareSmallObject(key1);
		String metadataName1 = "meta1";
		TestDataFactory.getInstance().prepareCustomMetadataForObject(key1, metadataName1);
		String metadataName2 = "meta2";
		TestDataFactory.getInstance().prepareCustomMetadataForObject(key1, metadataName2);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = hcpClient.doesMetadataExist(key1, metadataName1);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesMetadataExist(new CheckMetadataRequest(key1).withMetadataName(metadataName1));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesMetadataExist(new CheckMetadataRequest(key1).withMetadataNames(new String[] { metadataName1, metadataName2 }));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteMetadata(key1, metadataName2);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesMetadataExist(new CheckMetadataRequest(key1).withMetadataNames(new String[] { metadataName1, metadataName2 }));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testDoesSimpleMetadataExist() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepareSmallObject(key1);
		TestDataFactory.getInstance().prepareSimpleMetadataForObject(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = hcpClient.doesS3MetadataExist(key1);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteS3Metadata(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		exist = hcpClient.doesS3MetadataExist(key1);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

}