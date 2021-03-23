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
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.util.DigestUtils;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestSpecific extends TestHCPBase {

	// @Test
	// public void testAccessSpecificPath() throws HSCException, IOException {
	// // data＞test2＞HCPSystemManagementHelp＞Content＞Administering HCP
	// // HCPObjectSummary summary = hcpClient.getObject("data/test2/HCPSystemManagementHelp/Content/Administering HCP");
	// // print.addRow("Put Object=" + summary.getContentHash());
	//
	// File file = new File("C:\\VDisk\\DriverD\\Downloads\\Temp\\02-卫星中心推送影像(勿删除!!!) - Copy.exe");
	// String key = basedir + file.getName();
	// PutObjectResult result = hcpClient.putObject(key, file);
	// print.appendRecord(result.getContentLength());
	// hcpClient.copyObject(key, key + ".copy");
	// hcpClient.getObjectSummary(key);
	// HCPObject obj = hcpClient.getObject(key);
	//
	// assertTrue(DigestUtils.isMd5Equals(obj.getContent(), file));
	// }

	@Test
	public void testCaseSensitive() throws HSCException, IOException {
		File file = TestDataFactory.createTestFile(ContentType.TIME);
		String key1 = basedir + file.getName().toUpperCase();
		hcpClient.putObject(key1, file);
		System.out.println(key1);

		String key2 = basedir + file.getName().toLowerCase();
		hcpClient.putObject(key2, TestDataFactory.createTestInputStream(ContentType.TXT_RANDOM));
		System.out.println(key2);

		HCPObjectSummary summary1 = hcpClient.getObjectSummary(key1);
		HCPObjectSummary summary2 = hcpClient.getObjectSummary(key2);

		assertTrue(false == summary1.getContentHash().equals(summary2.getContentHash()));
	}

	// @Test
	// public void testIfMatch() throws HSCException, IOException {
	// File file = new File("C:\\VDisk\\DriverD\\Downloads\\Soft\\jdk-8u201-windows-x64.exe");
	// String key = basedir + file.getName();
	// PutObjectRequest request = new PutObjectRequest(key, file);
	// request.customHeader().put("If-None-Match", "5f15eecc137bc07fc16d729ab0f98f38");
	// hcpClient.putObject(request);
	// System.out.println(key);
	// }

	@Test
	public void testPressGet() throws HSCException, IOException, InterruptedException {
		final String c = TestDataFactory.getInstance().prepareSmallObject(key1, ContentType.TXT_5MB);
		final CountDownLatch latch = new CountDownLatch(1000);
		for (int i = 0; i < 1000; i++) {
			new Thread() {

				@Override
				public void run() {
					long t1 = System.currentTimeMillis();
					// HCPObjectSummary summary;
					try {
						// CheckObjectRequest req = new CheckObjectRequest(key1);
						GetObjectRequest req = new GetObjectRequest(key1);
						// summary = hcpClient.getObjectSummary(req);
						// System.out.println(summary.getKey() + " " + summary.getContentHash() + " " + System.currentTimeMillis());
						InputStream in = hcpClient.getObject(req).getContent();
						// System.out.println(StreamUtils.inputStreamToString(in, true) + " " + System.currentTimeMillis());

						boolean equals = DigestUtils.isMd5Equals(in, c);
						assertTrue(equals == true);
						latch.countDown();
						long t2 = System.currentTimeMillis();
						System.out.println("Finished " + (t2 - t1) + "ms " + Thread.currentThread().getName());
					} catch (InvalidResponseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}.start();
		}

		latch.await();
	}

}