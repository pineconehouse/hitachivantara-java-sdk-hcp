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

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.util.StreamUtils;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.api.event.ListObjectHandler;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestSpecialChar extends TestHCPBase {

	@Test
	public void testCreateObjWithSpecialCharsName() throws Exception {
		String specialChars = " `~!@#$%^&*()_+-= {}[]:;\"'<>,.?/|\\";
		char[] chars = specialChars.toCharArray();
		for (char c : chars) {
			File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
			String key = moreThan100objs + "SpeicialChar" + c + "Char";
			String content = TestDataFactory.getContent(tempFile);
			// ---------------------------------------------------------------------------------------
			PutObjectResult result = hcpClient.putObject(key, tempFile);
			// ----------------------------------------------------------------------------------------
			HCPObject obj = hcpClient.getObject(key);
			String content1 = StreamUtils.inputStreamToString(obj.getContent(), true);

			assertTrue(content.equals(content1));
		}
		
		ListObjectHandler listener=new ListObjectHandler() {

			public NextAction foundObject(HCPObjectSummary objectEntry) throws HSCException {
				System.out.println(objectEntry.getName()+"\t"+objectEntry.getKey()+"\t"+objectEntry.getType());
				return null;
			}
			
		};
		ListObjectRequest request = new ListObjectRequest(moreThan100objs);
		hcpClient.listObjects(request, listener);
		
//		hcpClient.deleteDirectory(moreThan100objs);
	}
	
//	@Test
//	public void testCreateObjMetadataWithSpecialCharsName() throws Exception {
//		String specialChars = " `~!@#$%^&*()_+-= {}[]:;\"'<>,.?/|\\";
//		char[] chars = specialChars.toCharArray();
//		for (char c : chars) {
//			File tempFile = this.createTestFile(ContentType.TIME);
//			String key = moreThan100objs + "SpeicialChar" + c + "Char";
//			String content = this.getContent(tempFile);
//			// ---------------------------------------------------------------------------------------
//			PutObjectResult result = hcpClient.putObject(key, tempFile);
//			// ----------------------------------------------------------------------------------------
//			HCPObject obj = hcpClient.getObject(key);
//			String content1 = StreamUtils.inputStreamToString(obj.getContent(), true);
//
//			assertTrue(content.equals(content1));
//		}
//	}
	
	@Test
	public void testCreateAndRetrievingObj() throws Exception {
		String content2 = TestDataFactory.getInstance().prepareSmallObject(AsiaKey4);
		HCPObject resultGet = hcpClient.getObject(AsiaKey4);
		String resultContent = StreamUtils.inputStreamToString(resultGet.getContent(), true);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(content2));
	}
	
	@Test
	public void testConservativeHashKeyGeneratorV1() throws Exception {
		File tempFile =TestDataFactory.createTestFile(ContentType.TIME);
		String key = AsiaKey4;
		String content = TestDataFactory.getContent(tempFile);
		// ----------------------------------------------------------------------------------------
		hcpClient.setKeyAlgorithm(KeyAlgorithm.CONSERVATIVE_KEY_HASH_D32);
		PutObjectResult result = hcpClient.putObject(key, tempFile);
		assertTrue(!URLUtils.urlDecode(result.getLocation(),"utf-8").equals(URLUtils.getRequestTargetName(AsiaKey4)));

		HCPObject hcpobj = hcpClient.getObject(key);
		String resultContent = StreamUtils.inputStreamToString(hcpobj.getContent(), true);
		assertTrue(resultContent.equals(content));

		boolean exist = hcpClient.doesObjectExist(key);
		assertTrue(exist == true);

		hcpClient.deleteObject(key);

		exist = hcpClient.doesObjectExist(key);
		assertTrue(exist == false);

		// ----------------------------------------------------------------------------------------

		print.appendRecord("OriginalContent=" + content);
		print.appendRecord("Location=" + result.getLocation());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("ETag=" + result.getETag());
		print.appendRecord("VersionId=" + result.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + result.getIngestTime());

		// ----------------------------------------------------------------------------------------
	}

	@Test
	public void testConservativeHashKeyGeneratorV1_2() throws Exception {
		File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
		String key = AsiaKey4;
		String content = TestDataFactory.getContent(tempFile);
		// ----------------------------------------------------------------------------------------
		hcpClient.setKeyAlgorithm(KeyAlgorithm.ENTIRE_KEY_HASH_D32);
		PutObjectResult result = hcpClient.putObject(key, tempFile);
//		assertTrue(result.getLocation().contains(tempFile.getName()));
//		assertTrue(!URLUtils.urlDecode(result.getLocation(),"utf-8").equals(URLUtils.getRequestTargetName(AsiaKey4)));
	
		print.appendRecord("OriginalKey=" + key);
		key = KeyAlgorithm.ENTIRE_KEY_HASH_D32.generate(key);
		print.appendRecord("RealKey=" + key);
		hcpClient.setKeyAlgorithm(KeyAlgorithm.DEFAULT);

		HCPObject hcpobj = hcpClient.getObject(key);
		String resultContent = StreamUtils.inputStreamToString(hcpobj.getContent(), true);
		assertTrue(resultContent.equals(content));

		boolean exist = hcpClient.doesObjectExist(key);
		assertTrue(exist == true);

		hcpClient.deleteObject(key);

		exist = hcpClient.doesObjectExist(key);
		assertTrue(exist == false);

		// ----------------------------------------------------------------------------------------

		print.appendRecord("OriginalContent=" + content);
		print.appendRecord("Location=" + result.getLocation());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("ETag=" + result.getETag());
		print.appendRecord("VersionId=" + result.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + result.getIngestTime());

		// ----------------------------------------------------------------------------------------
	}

}