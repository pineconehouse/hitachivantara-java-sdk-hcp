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

import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.common.define.HashAlgorithm;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

public class TestPutGetInCompress extends TestHCPBase {

	@Test
	public void testCreate100MObjWithCompressed() throws Exception {
		File tempFile = TestDataFactory.createTestFile(ContentType.TXT_100MB);
		String key = basedir + tempFile.getName();
		CalcTime time = new CalcTime();
		String localDigest = DigestUtils.format2Hex(DigestUtils.calcSHA256(tempFile));

		// ----------------------------------------------------------------------------------------
		PutObjectRequest request = new PutObjectRequest(key);
		request.withVerifyContent(HashAlgorithm.SHA256);

		for (int i = 0; i < 3; i++) {
			request.withContent(tempFile, true);
			time.start();
			PutObjectResult result = hcpClient.putObject(request);
			time.end();
			time.print("WithCompressed");
			assertTrue(result.getContentHash().equals(localDigest));
		}

		for (int i = 0; i < 3; i++) {
			request.withContent(tempFile, false);
			time.start();
			PutObjectResult result = hcpClient.putObject(request);
			time.end();
			time.print("WithoutCompressed");
			assertTrue(result.getContentHash().equals(localDigest));
		}
		// ----------------------------------------------------------------------------------------
	}
	
	@Test
	public void testCreate100MObjWithoutCompressed() throws Exception {
		File tempFile = TestDataFactory.createTestFile(ContentType.TXT_100MB);
		String key = basedir + tempFile.getName();
		CalcTime time = new CalcTime();
		String localDigest = DigestUtils.format2Hex(DigestUtils.calcSHA256(tempFile));

		// ----------------------------------------------------------------------------------------
		PutObjectRequest request = new PutObjectRequest(key);
		request.withVerifyContent(HashAlgorithm.SHA256);

		for (int i = 0; i < 3; i++) {
			request.withContent(tempFile, false);
			time.start();
			PutObjectResult result = hcpClient.putObject(request);
			time.end();
			time.print("WithoutCompressed");
			assertTrue(result.getContentHash().equals(localDigest));
		}
		// ----------------------------------------------------------------------------------------
	}
	

	@Test
	public void testCreateObjWithCompressed() throws Exception {
		File tempFile = TestDataFactory.createTestFile(ContentType.TXT_100MB);
		String key = basedir + tempFile.getName();
		CalcTime time = new CalcTime();
		String localDigest = DigestUtils.format2Hex(DigestUtils.calcSHA256(tempFile));

		// ----------------------------------------------------------------------------------------
		PutObjectRequest request = new PutObjectRequest(key);
		request.withVerifyContent(HashAlgorithm.SHA256);

		for (int i = 0; i < 3; i++) {
			request.withContent(tempFile, false);
			time.start();
			PutObjectResult result = hcpClient.putObject(request);
			time.end();
			time.print("WithoutCompressed");
			assertTrue(result.getContentHash().equals(localDigest));
		}

		for (int i = 0; i < 3; i++) {
			request.withContent(tempFile, true);
			time.start();
			PutObjectResult result = hcpClient.putObject(request);
			time.end();
			time.print("WithCompressed");
			assertTrue(result.getContentHash().equals(localDigest));
		}
		// ----------------------------------------------------------------------------------------
	}

	@Test
	public void testGetObjectWithZip() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		File tempFile = TestDataFactory.createTestFile(ContentType.TXT_100MB);
		String key = basedir + tempFile.getName();
		CalcTime time = new CalcTime();
		String localDigest = DigestUtils.format2Hex(DigestUtils.calcSHA256(tempFile));

		PutObjectRequest request = new PutObjectRequest(key);
		request.withVerifyContent(HashAlgorithm.SHA256);
		request.withContent(tempFile, false);
		PutObjectResult result = hcpClient.putObject(request);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		{
			for (int i = 0; i < 3; i++) {
				File x = File.createTempFile(key, "xxx" + i);

				time.start();
				HCPObject obj = hcpClient.getObject(new GetObjectRequest(key));
				StreamUtils.inputStreamToFile(obj.getContent(), x, true);
				String d1 = DigestUtils.format2Hex(DigestUtils.calcSHA256(x));
				assertTrue(d1.equals(localDigest));

				time.end();
				time.print("WithoutCompressed");
				x.delete();
			}

			for (int i = 0; i < 3; i++) {
				File x = File.createTempFile(key, "xxx" + i);

				time.start();
				HCPObject obj = hcpClient.getObject(new GetObjectRequest(key).withGetInCompressed(true));
				StreamUtils.inputStreamToFile(obj.getContent(), x, true);
				String d1 = DigestUtils.format2Hex(DigestUtils.calcSHA256(x));
				assertTrue(d1.equals(localDigest));

				time.end();
				time.print("WithCompressed");
				x.delete();
			}
		}
		tempFile.delete();
		// ----------------------------------------------------------------------------------------
	}
	
	
	@Test
	public void testGetObjectWithZip1() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		File tempFile = TestDataFactory.createTestFile(ContentType.TXT_100MB);
		String key = basedir + tempFile.getName();
		CalcTime time = new CalcTime();
		String localDigest = DigestUtils.format2Hex(DigestUtils.calcSHA256(tempFile));

		PutObjectRequest request = new PutObjectRequest(key);
		request.withVerifyContent(HashAlgorithm.SHA256);
		request.withContent(tempFile, false);
		PutObjectResult result = hcpClient.putObject(request);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		{
			for (int i = 0; i < 3; i++) {
				File x = File.createTempFile(key, "xxx" + i);

				time.start();
				HCPObject obj = hcpClient.getObject(new GetObjectRequest(key));
				StreamUtils.inputStream2None(obj.getContent(), true);

				time.end();
				time.print("WithoutCompressed");
				x.delete();
			}

			for (int i = 0; i < 3; i++) {
				File x = File.createTempFile(key, "xxx" + i);

				time.start();
				HCPObject obj = hcpClient.getObject(new GetObjectRequest(key).withGetInCompressed(true));
				StreamUtils.inputStream2None(obj.getContent(), true);

				time.end();
				time.print("WithCompressed");
				x.delete();
			}
		}
		tempFile.delete();
		// ----------------------------------------------------------------------------------------
	}

}
