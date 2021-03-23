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
import java.security.MessageDigest;

import org.junit.Test;

import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.RandomUtils;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.common.define.HashAlgorithm;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestPutGetExistDeleteObject extends TestHCPBase {
	@Test
	public void testGetSummary() throws Exception {
		String content = TestDataFactory.getInstance().prepareSmallObject(key1);
		// ----------------------------------------------------------------------------------------

		HCPObjectSummary summary = hcpClient.getObjectSummary(key1);
		String hash = DigestUtils.format2Hex(DigestUtils.calcHash(MessageDigest.getInstance(summary.getHashAlgorithmName()), content)).toUpperCase();
		String md5hash = DigestUtils.format2Hex(DigestUtils.calcHash(MessageDigest.getInstance("MD5"), content)).toUpperCase();

		assertTrue(summary.getKey().equals(key1));
		assertTrue(summary.getContentHash().toUpperCase().equals(hash));
		assertTrue(summary.getContentLength() == content.length());
		assertTrue(summary.getSize() == content.length());
		assertTrue(summary.isObject());
		assertTrue(summary.getETag().toUpperCase().equals(md5hash));
		// assertTrue(summary.getName().equals(anObject));
	}

	@Test
	public void testCreateAndRetrievingObj() throws Exception {
		File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
		String content2 = TestDataFactory.getContent(tempFile2);

		PutObjectResult resultPut = hcpClient.putObject(key1, tempFile2);

		print.appendRecord("Put Object=" + key1);
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		HCPObject result = hcpClient.getObject(key1);
		String resultContent = StreamUtils.inputStreamToString(result.getContent(), true);

		print.appendRecord("Get Object... " + key1);
		print.appendRecord("Location=" + key1);
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("VersionId=" + result.getVersionId());
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("RetrievedContent=" + resultContent);

		assertTrue(resultContent.equals(content2));
	}

	@Test
	public void testCreateAndRetrievingRangeOfObj() throws Exception {
		String content = TestDataFactory.getInstance().prepareSmallObject(key1);

		// ----------------------------------------------------------------------------------------

		long beginOffset = 0;
		long endOffset = 1;
		String resultContent = "";
		final long size = hcpClient.getObjectSummary(key1).getSize();
		// ----------------------------------------------------------------------------------------
		for (endOffset = RandomUtils.randomInt(1, 237);;) {
			endOffset += beginOffset;

			if (endOffset > size) {
				endOffset = size;
			}

			HCPObject result = hcpClient.getObject(new GetObjectRequest(key1).withRange(beginOffset, endOffset));
			String c = StreamUtils.inputStreamToString(result.getContent(), true);
			print.appendRecord("Content" + beginOffset + "," + endOffset + "=" + c);
			resultContent += c;

			if (endOffset == size) {
				break;
			}

			beginOffset = endOffset + 1;
		}

		// ----------------------------------------------------------------------------------------
		print.appendRecord("OriginalContent=" + content);
		print.appendRecord("RetrievedContent=" + resultContent);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(content));
	}

	@Test
	public void testCreateAndRetrieving0SizeObj() throws Exception {
		File tempFile2 = TestDataFactory.createTestFile(ContentType.EMPTY);
		String content2 = TestDataFactory.getContent(tempFile2);

		// ----------------------------------------------------------------------------------------

		PutObjectResult resultPut = hcpClient.putObject(key1, tempFile2);

		print.appendRecord("Put Object=" + key1);
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------
		HCPObject result = hcpClient.getObject(key1);
		String resultContent = StreamUtils.inputStreamToString(result.getContent(), true);

		// ----------------------------------------------------------------------------------------
		print.appendRecord("Get Object... " + key1);
		print.appendRecord("Location=" + key1);
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("VersionId=" + result.getVersionId());
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("RetrievedContent=" + resultContent);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(content2));
	}

	@Test
	public void testCreateAndRetrievingObjWithSpecificLang() throws Exception {
		File tempFile2 = TestDataFactory.createTestFile(ContentType.XML);
		String content2 = TestDataFactory.getContent(tempFile2);

		String key = basedir + "简体中文繁体中文English注目の商品をチェックالصينية المبسطةна китайски， упрощенный китайский язык，Απλοποιημένα κινέζικα.txt";
		// ----------------------------------------------------------------------------------------

		PutObjectResult resultPut = hcpClient.putObject(key, tempFile2);

		print.appendRecord("Put Object=" + key);
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------
		HCPObject result = hcpClient.getObject(key);
		String resultContent = StreamUtils.inputStreamToString(result.getContent(), true);

		// ----------------------------------------------------------------------------------------
		print.appendRecord("Get Object... " + key);
		print.appendRecord("Location=" + key);
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("VersionId=" + result.getVersionId());
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("RetrievedContent=" + resultContent);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(content2));

		hcpClient.deleteObject(key);
		assertTrue(hcpClient.doesObjectExist(key) == false);
	}

	@Test
	public void testCreateObjWithSpecificedVerify() throws Exception {
		// ----------------------------------------------------------------------------------------
		File tempFile = TestDataFactory.createTestFile(ContentType.TXT_5MB);
		String localDigest = DigestUtils.format2Hex(DigestUtils.calcSHA256(tempFile));
		String localDigestMd5 = DigestUtils.format2Hex(DigestUtils.calcMD5(tempFile)).toLowerCase();
		PutObjectRequest request = new PutObjectRequest(key1);
		request.withContent(tempFile).withVerifyContent(HashAlgorithm.NOOP);
		// request.withContentVerify(HashAlgorithm.MD5);
		time.start();
		PutObjectResult resultPut = hcpClient.putObject(request);
		time.end();
		time.print();
		// ----------------------------------------------------------------------------------------
		print.appendRecord("Put Object=" + key1);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("LocalDigest=" + localDigest);
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------

		assertTrue(resultPut.getContentHash().equals(localDigest));
		assertTrue(resultPut.getETag().equals(localDigestMd5));
	}

	// @Test
	// public void testGetObjectFromAnotherNamespace() throws IOException, HSCException {
	// String sourceKey = "zhongwen中文/邮件审计实施（solrcloud集群搭建）.docx";
	// GetObjectRequest request = new GetObjectRequest(sourceKey);
	// request.withVersion("97737812308737");
	// request.withNamespace(Configs.getString("TestHCPBase.NAMESPACE_NAME2"));
	// HCPObject big = hcpClient.getObject(request);
	// // StreamUtils.inputStreamToConsole(big.getContent(), true);
	//
	// assertTrue(false == hcpClient.getNamespace().equals(request.getNamespace()));
	// }

	@Test
	public void testCreate1000MoreObj() throws Exception {
		for (int i = 0; i < 100; i++) {
			File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
			String key = moreThan100objs + tempFile.getName();
			String content = TestDataFactory.getContent(tempFile);
			// ---------------------------------------------------------------------------------------
			PutObjectResult result = hcpClient.putObject(key, tempFile);
			// ----------------------------------------------------------------------------------------
		}

		for (int i = 0; i < 100; i++) {
			File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
			String key = moreThan100objs + RandomUtils.randomInt(10, 15) + "/" + tempFile.getName();
			String content = TestDataFactory.getContent(tempFile);
			// ----------------------------------------------------------------------------------------
			PutObjectResult result = hcpClient.putObject(key, tempFile);
			// ----------------------------------------------------------------------------------------
		}

		for (int i = 0; i < 1000; i++) {
			File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
			String key = moreThan100objs + RandomUtils.randomInt(10, 15) + "/" + RandomUtils.randomString(2) + "/" + tempFile.getName();
			String content = TestDataFactory.getContent(tempFile);
			// ----------------------------------------------------------------------------------------
			PutObjectResult result = hcpClient.putObject(key, tempFile);
			// ----------------------------------------------------------------------------------------
		}
	}

}