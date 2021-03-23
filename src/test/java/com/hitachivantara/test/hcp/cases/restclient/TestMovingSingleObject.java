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

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.amituofo.common.ex.ParseException;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.api.ObjectParser;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadata;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListVersionRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MoveObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestMovingSingleObject extends TestHCPBase {

	@Test
	public void testMoveObject() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String sourceKeyName = TestDataFactory.getRandomName(10) + ".txt";
		String sourceKey = basedir + sourceKeyName;
		// String sourceNamespace =namespaceName;
		String targetKey = basedir + "move2/" + sourceKeyName + ".copy.txt";
		// String targetNamespace =namespaceName;
		List<String> hashs = TestDataFactory.getInstance().prepare10VersionsOfObject(sourceKey);
		String metadataName1 = "metadata1";
		String sourceMetadataContent = TestDataFactory.getInstance().prepareCustomMetadataForObject(sourceKey, metadataName1);
		S3CompatibleMetadata sourceSimpleMetadata = TestDataFactory.getInstance().prepareSimpleMetadataForObject(sourceKey);

		// String sourceMetadataContentMD5hash = DigestUtils.format2Hex(DigestUtils.calcMD5(metadataContent)).toUpperCase();

		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(sourceKey);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		console.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
		hcpClient.moveObject(new MoveObjectRequest(sourceKey, targetKey));
		console.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		{
			assertTrue(hcpClient.doesObjectExist(sourceKey) == false);

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
			// console.println(sourceMetadataContent);
			// console.println(targetMetadataContent);
			assertTrue(targetMetadataContent.equals(sourceMetadataContent));

			S3CompatibleMetadata simpleMetadataCopied = hcpClient.getMetadata(targetKey);
			assertTrue(simpleMetadataCopied.equals(sourceSimpleMetadata));
		}
		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------
		{
			assertTrue(hcpClient.doesObjectExist(sourceKey) == false);
			assertTrue(hcpClient.doesObjectExist(targetKey) == true);

			HCPObjectEntrys targetVersion = hcpClient.listVersions(new ListVersionRequest(targetKey));
			ObjectEntryIterator it = targetVersion.iterator();
			List<HCPObjectEntry> targetVersions = it.next(100);
			for (int i = 0; i < targetVersions.size(); i++) {
				HCPObjectEntry hcpObjectEntry = targetVersions.get(i);
				String targetHash = hcpClient.getObject(new GetObjectRequest(hcpObjectEntry.getKey()).withVersionId(hcpObjectEntry.getVersionId()), new ObjectParser<String>() {

					@Override
					public String parse(HCPObject object) throws ParseException {
						try {
							return DigestUtils.format2Hex(DigestUtils.calcSHA256(object.getContent())).toUpperCase();
						} catch (IOException e) {
							e.printStackTrace();
						}
						return "";
					}
				});

				String sourceHash = hashs.get(i);
				//检查每个版本的数据hash是否一致
				assertTrue(targetHash.equals(sourceHash));
				
			}

		}
		// RESULT VERIFICATION --------------------------------------------------------------------

	}

	@Test
	public void testMoveObjectWithoutMetadataAndOldVersion() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String sourceKey = basedir + TestDataFactory.getRandomName(10) + ".txt";
		// String sourceNamespace =namespaceName;
		String targetKey = sourceKey + ".copy.txt";
		// String targetNamespace =namespaceName;
		List<String> hashs = TestDataFactory.getInstance().prepare10VersionsOfObject(sourceKey);
		String metadataName1 = "metadata1";
		String sourceMetadataContent = TestDataFactory.getInstance().prepareCustomMetadataForObject(sourceKey, metadataName1);
		S3CompatibleMetadata sourceSimpleMetadata = TestDataFactory.getInstance().prepareSimpleMetadataForObject(sourceKey);

		// String sourceMetadataContentMD5hash = DigestUtils.format2Hex(DigestUtils.calcMD5(metadataContent)).toUpperCase();
		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(sourceKey);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.moveObject(new MoveObjectRequest(sourceKey, targetKey).withCopyingMetadata(false).withCopyingOldVersion(false));
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(hcpClient.doesObjectExist(sourceKey) == false);
		assertTrue(hcpClient.doesObjectExist(targetKey) == true);

		HCPObjectSummary targetObj = hcpClient.getObjectSummary(targetKey);

		String sourceMD5 = sourceObj.getContentHash();
		String targetMD5 = targetObj.getContentHash();

		print.appendRecord("SourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);

		//测试目标数据与源一致
		assertTrue(sourceMD5.equals(targetMD5));
		//测试目录metadata没有，而源有2个
		assertTrue(sourceObj.getCustomMetadatas().length == 2);
		assertTrue(targetObj.getCustomMetadatas() == null);
		
		//测试目标只有一个版本
		HCPObjectEntrys targetVersion = hcpClient.listVersions(new ListVersionRequest(targetKey));
		ObjectEntryIterator it = targetVersion.iterator();
		List<HCPObjectEntry> targetVersions = it.next(100);
		assertTrue(targetVersions.size()==1);


		// RESULT VERIFICATION --------------------------------------------------------------------
	}

//	@Test
//	public void testCopyObjectBetweenDifferentNamespace() throws Exception {
//		// PREPARE TEST DATA ----------------------------------------------------------------------
//		String sourceKey = basedir + TestDataFactory.getRandomName(10) + ".txt";
//		String sourceNamespace = namespace2Name;
//		String targetKey = sourceKey + ".copy.txt";
//		String targetNamespace = namespace1Name;
//		TestDataFactory.getInstance().prepareSmallObject(sourceKey, sourceNamespace);
//		// PREPARE TEST DATA ----------------------------------------------------------------------
//
//		// EXEC TEST FUNCTION ---------------------------------------------------------------------
//		CopyObjectRequest copyRequest = new CopyObjectRequest().withSourceKey(sourceKey).withSourceNamespace(sourceNamespace).withTargetKey(targetKey)
//				.withTargetNamespace(targetNamespace).withCopyingMetadata(true).withCopyingOldVersion(true);
//		hcpClient.copyObject(copyRequest);
//		// EXEC TEST FUNCTION ---------------------------------------------------------------------
//
//		// RESULT VERIFICATION --------------------------------------------------------------------
//		assertTrue(!namespace2Name.equals(namespace1Name));
//
//		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(new CheckObjectRequest(sourceKey).withNamespace(sourceNamespace));
//		HCPObjectSummary targetObj = hcpClient.getObjectSummary(new CheckObjectRequest(targetKey).withNamespace(targetNamespace));
//
//		String sourceMD5 = sourceObj.getContentHash();
//		String targetMD5 = targetObj.getContentHash();
//
//		print.appendRecord("SourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
//		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);
//
//		assertTrue(sourceMD5.equals(targetMD5));
//		// RESULT VERIFICATION --------------------------------------------------------------------
//	}
//
//	@Test
//	public void testCopyObjectUploadedByMultipart() throws Exception {
//
//		String targetKey = bigKey1 + ".copy";
//		String sourceKey = bigKey1;
//		// String targetKey = sourceKey + ".copy";
//
//		hcpClient.deleteObject(new DeleteObjectRequest(targetKey).withPurgeDelete(true));
//
//		hcpClient.copyObject(sourceKey, targetKey);
//
//		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(sourceKey);
//		HCPObjectSummary targetObj = hcpClient.getObjectSummary(targetKey);
//
//		String sourceMD5 = sourceObj.getContentHash();
//		String targetMD5 = targetObj.getContentHash();
//
//		// GetObjectRequest request = new GetObjectRequest(bigKey1).withForceGenerateEtag(true);
//		// HCPObject bigObj = hcpClient.getObject(request);
//		// bigObj.getContent().close();
//
//		assertTrue(sourceMD5.equals(targetMD5));
//
//		// ----------------------------------------------------------------------------------------
//
//		print.appendRecord("sourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
//		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);
//	}

}