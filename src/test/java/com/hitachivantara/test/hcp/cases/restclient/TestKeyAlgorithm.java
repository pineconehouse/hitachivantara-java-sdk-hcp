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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.amituofo.common.ex.AlgorithmException;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestKeyAlgorithm extends TestHCPBase {
	@Test
	public void testConservativeHashKeyGeneratorV1() throws Exception {
		File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
		String key = basedir + tempFile.getName();
		String content = TestDataFactory.getContent(tempFile);
		// ----------------------------------------------------------------------------------------
		hcpClient.setKeyAlgorithm(KeyAlgorithm.CONSERVATIVE_KEY_HASH_D32);
		PutObjectResult result = hcpClient.putObject(key, tempFile);
		assertTrue(result.getLocation().contains(tempFile.getName()));

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
		String key = basedir + tempFile.getName();
		String content = TestDataFactory.getContent(tempFile);
		// ----------------------------------------------------------------------------------------
		hcpClient.setKeyAlgorithm(KeyAlgorithm.CONSERVATIVE_KEY_HASH_D32);
		PutObjectResult result = hcpClient.putObject(key, tempFile);
		assertTrue(result.getLocation().contains(tempFile.getName()));

		print.appendRecord("OriginalKey=" + key);
		key = KeyAlgorithm.CONSERVATIVE_KEY_HASH_D32.generate(key);
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

	@Test
	public void testKeyAlgorithm() throws Exception {
		ThreadPoolExecutor exe = new ThreadPoolExecutor(5000, 5000, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());

		int size = 100 * 10000;
		// List<String> keys = new ArrayList<String>(size);
		// final List<String> keys1 = Collections.synchronizedList(new ArrayList<String>(size));
		// final List<String> keys2 = Collections.synchronizedList(new ArrayList<String>(size));
		final Map<Integer, String> keys1 = Collections.synchronizedMap(new HashMap<Integer, String>());
		final Map<Integer, String> keys2 = new HashMap<Integer, String>();

		// final Counter c =new Counter();
		final CountDownLatch latch = new CountDownLatch(size);
		for (int i = 0; i < size; i++) {
			final int k = i;
			exe.execute(new Runnable() {

				@Override
				public void run() {
					String key;
					try {
						key = KeyAlgorithm.CONSERVATIVE_KEY_HASH_D32.generate(k + "xxxx");
						keys1.put(k, key);

						latch.countDown();
						// synchronized (c) {
						// c.i++;
						// }
					} catch (AlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

		latch.await();
		System.out.println("Finished 1");

		for (int i = 0; i < size; i++) {
			final int k = i;
			String key = KeyAlgorithm.CONSERVATIVE_KEY_HASH_D32.generate(k + "xxxx");
			keys2.put(i, key);
		}
		System.out.println("Finished 2");

		for (int i = 0; i < size; i++) {
			try {
				if (!keys1.get(i).equals(keys2.get(i))) {
					System.out.println(i + " " + keys1.get(i) + " " + keys2.get(i));
				} 
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("error" + i);
			}
		}

		// ----------------------------------------------------------------------------------------
	}
}