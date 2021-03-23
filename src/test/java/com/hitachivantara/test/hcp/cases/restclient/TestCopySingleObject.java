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

import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadata;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CopyObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListVersionRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestCopySingleObject extends TestHCPBase {

	@Test
	public void testCopyObject() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String sourceKey = basedir + TestDataFactory.getRandomName(10) + ".txt";
		// String sourceNamespace =namespaceName;
		String targetKey = sourceKey + ".copy.txt";
		// String targetNamespace =namespaceName;
		TestDataFactory.getInstance().prepareSmallObject(sourceKey);
		String metadataName1 = "metadata1";
		String sourceMetadataContent = TestDataFactory.getInstance().prepareCustomMetadataForObject(sourceKey, metadataName1);
		S3CompatibleMetadata sourceSimpleMetadata = TestDataFactory.getInstance().prepareSimpleMetadataForObject(sourceKey);

		// String sourceMetadataContentMD5hash = DigestUtils.format2Hex(DigestUtils.calcMD5(metadataContent)).toUpperCase();
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.copyObject(sourceKey, targetKey);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(sourceKey);
		HCPObjectSummary targetObj = hcpClient.getObjectSummary(targetKey);

		String sourceMD5 = sourceObj.getContentHash();
		String targetMD5 = targetObj.getContentHash();

		print.appendRecord("SourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);

		assertTrue(sourceMD5.equals(targetMD5));

		HCPMetadata targetCustomMetadata = hcpClient.getMetadata(targetKey, metadataName1);
		// print.addRow("SourceMetadataHash=" + targetCustomMetadata.getETag().toUpperCase());
		// print.addRow("TargetMetadataHash=" + sourceMetadataContentMD5hash);
		String targetMetadataContent = StreamUtils.inputStreamToString(targetCustomMetadata.getContent(), true);
		console.println(sourceMetadataContent);
		console.println(targetMetadataContent);
		assertTrue(targetMetadataContent.equals(sourceMetadataContent));

		S3CompatibleMetadata simpleMetadataCopied = hcpClient.getMetadata(targetKey);
		assertTrue(simpleMetadataCopied.equals(sourceSimpleMetadata));
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testCopyObjectWithoutMetadata() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String sourceKey = basedir + TestDataFactory.getRandomName(10) + ".txt";
		// String sourceNamespace =namespaceName;
		String targetKey = sourceKey + ".copy.txt";
		// String targetNamespace =namespaceName;
		TestDataFactory.getInstance().prepareSmallObject(sourceKey);
		String metadataName1 = "metadata1";
		String sourceMetadataContent = TestDataFactory.getInstance().prepareCustomMetadataForObject(sourceKey, metadataName1);
		S3CompatibleMetadata sourceSimpleMetadata = TestDataFactory.getInstance().prepareSimpleMetadataForObject(sourceKey);

		// String sourceMetadataContentMD5hash = DigestUtils.format2Hex(DigestUtils.calcMD5(metadataContent)).toUpperCase();
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		CopyObjectRequest copyRequest = new CopyObjectRequest(sourceKey, targetKey).withCopyingMetadata(false);
		hcpClient.copyObject(copyRequest);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(sourceKey);
		HCPObjectSummary targetObj = hcpClient.getObjectSummary(targetKey);

		String sourceMD5 = sourceObj.getContentHash();
		String targetMD5 = targetObj.getContentHash();

		print.appendRecord("SourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);

		assertTrue(sourceMD5.equals(targetMD5));
		assertTrue(sourceObj.getCustomMetadatas().length == 2);
		assertTrue(targetObj.getCustomMetadatas() == null);

		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testCopyObjectBetweenDifferentNamespace() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String sourceKey = basedir + TestDataFactory.getRandomName(10) + ".txt";
		String sourceNamespace = namespace2Name;
		String targetKey = sourceKey + ".copy.txt";
		String targetNamespace = namespace1Name;
		TestDataFactory.getInstance().prepareSmallObject(sourceKey, sourceNamespace);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		CopyObjectRequest copyRequest = new CopyObjectRequest()
				.withSourceKey(sourceKey)
				.withSourceNamespace(sourceNamespace)
				.withTargetKey(targetKey)
				.withTargetNamespace(targetNamespace)
				.withCopyingMetadata(true)
//				.withCopyingOldVersion(false);
				.withCopyingOldVersion(true);
		hcpClient.copyObject(copyRequest);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(!namespace2Name.equals(namespace1Name));

		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(new CheckObjectRequest(sourceKey).withNamespace(sourceNamespace));
		HCPObjectSummary targetObj = hcpClient.getObjectSummary(new CheckObjectRequest(targetKey).withNamespace(targetNamespace));

		String sourceMD5 = sourceObj.getContentHash();
		String targetMD5 = targetObj.getContentHash();

		print.appendRecord("SourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);

		assertTrue(sourceMD5.equals(targetMD5));
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testCopyAllVersionOfObject() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String sourceKey = basedir + TestDataFactory.getRandomName(10) + ".txt";
		String targetKey = sourceKey + ".copy.txt";
		TestDataFactory.getInstance().prepare10VersionsOfObject(sourceKey);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		CopyObjectRequest copyRequest = new CopyObjectRequest(sourceKey, targetKey).withCopyingMetadata(true).withCopyingOldVersion(true);
		hcpClient.copyObject(copyRequest);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		HCPObjectEntrys sourceVersionEntry = hcpClient.listVersions(sourceKey);
		HCPObjectEntrys targetVersionEntry = hcpClient.listVersions(targetKey);

		ObjectEntryIterator sit = sourceVersionEntry.iterator();
		ObjectEntryIterator tit = targetVersionEntry.iterator();

		List<HCPObjectEntry> sourceVersions = sit.next(100);
		List<HCPObjectEntry> targetVersions = tit.next(100);

		assertTrue(sourceVersions.size() >= 10);
		assertTrue(sourceVersions.size() == targetVersions.size());

		print.appendRecord("Source", " VersionId", "HashName", "Hash");
		for (int i = 0; i < sourceVersions.size(); i++) {
			HCPObjectEntry sourceObj = sourceVersions.get(i);
			HCPObjectEntry targetObj = targetVersions.get(i);

			print.appendRecord("Source=" + sourceObj.getKey(), sourceObj.getVersionId(), sourceObj.getHashAlgorithmName(), sourceObj.getContentHash());
			print.appendRecord("Target=" + targetObj.getKey(), targetObj.getVersionId(), targetObj.getHashAlgorithmName(), targetObj.getContentHash());

			assertTrue(sourceObj.getContentHash().equals(targetObj.getContentHash()));
		}
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testCopyVersionedObjectBetweenDifferentNamespace() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String sourceKey = basedir + TestDataFactory.getRandomName(10) + ".txt";
		String sourceNamespace = namespace2Name;
		String targetKey = sourceKey + ".copy.txt";
		String targetNamespace = namespace1Name;
		TestDataFactory.getInstance().prepare10VersionsOfObject(sourceKey, sourceNamespace);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		CopyObjectRequest copyRequest = new CopyObjectRequest(sourceKey, targetKey).withSourceNamespace(sourceNamespace).withTargetNamespace(targetNamespace)
				.withCopyingMetadata(true).withCopyingOldVersion(true);// .withDeletedObject(true);
		hcpClient.copyObject(copyRequest);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(!namespace2Name.equals(namespace1Name));

		HCPObjectEntrys sourceVersionEntry = hcpClient.listVersions(new ListVersionRequest(sourceKey).withNamespace(sourceNamespace));
		HCPObjectEntrys targetVersionEntry = hcpClient.listVersions(new ListVersionRequest(targetKey).withNamespace(targetNamespace));

		ObjectEntryIterator sit = sourceVersionEntry.iterator();
		ObjectEntryIterator tit = targetVersionEntry.iterator();

		List<HCPObjectEntry> sourceVersions = sit.next(100);
		List<HCPObjectEntry> targetVersions = tit.next(100);

		assertTrue(sourceVersions.size() >= 10);
		assertTrue(sourceVersions.size() == targetVersions.size());

		print.appendRecord("Source", " VersionId", "HashName", "Hash");
		for (int i = 0; i < sourceVersions.size(); i++) {
			HCPObjectEntry sourceObj = sourceVersions.get(i);
			HCPObjectEntry targetObj = targetVersions.get(i);

			print.appendRecord("Source=" + sourceObj.getKey(), sourceObj.getVersionId(), sourceObj.getHashAlgorithmName(), sourceObj.getContentHash());
			print.appendRecord("Target=" + targetObj.getKey(), targetObj.getVersionId(), targetObj.getHashAlgorithmName(), targetObj.getContentHash());

			assertTrue(sourceObj.getContentHash().equals(targetObj.getContentHash()));
		}
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testCopyObjectUploadedByMultipart() throws Exception {

		String targetKey = bigKey1 + ".copy";
		String sourceKey = bigKey1;
		// String targetKey = sourceKey + ".copy";

		hcpClient.deleteObject(new DeleteObjectRequest(targetKey).withPurge(true));

		hcpClient.copyObject(sourceKey, targetKey);

		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(sourceKey);
		HCPObjectSummary targetObj = hcpClient.getObjectSummary(targetKey);

		String sourceMD5 = sourceObj.getContentHash();
		String targetMD5 = targetObj.getContentHash();

		// GetObjectRequest request = new GetObjectRequest(bigKey1).withForceGenerateEtag(true);
		// HCPObject bigObj = hcpClient.getObject(request);
		// bigObj.getContent().close();

		assertTrue(sourceMD5.equals(targetMD5));

		// ----------------------------------------------------------------------------------------

		print.appendRecord("sourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);
	}

}