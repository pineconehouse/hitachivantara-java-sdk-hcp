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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.event.PartialHandlingListener;
import com.hitachivantara.hcp.standard.internal.FileWriteHandler;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartDownloadRequest;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestMultpartDownload extends TestHCPBase {

	@Test
	public void testMultipartDownload() throws IOException, HSCException, InterruptedException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		File file1 = new File(LOCAL_TMP_PATH + "big1.zip");
		File file2 = new File(LOCAL_TMP_PATH + "big2.zip");
		if (file1.exists()) {
			file1.delete();
		}
		if (file2.exists()) {
			file2.delete();
		}
		final CountDownLatch latch = new CountDownLatch(2);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		final long b = Calendar.getInstance().getTimeInMillis();
//		 MultipartDownloadRequest request = new MultipartDownloadRequest("/sdk-test99/jdk-8u201-windows-x64.exe");
		// MultipartDownloadRequest request = new MultipartDownloadRequest(basedir + "cosbench-master.zip");
		// MultipartDownloadRequest request = new MultipartDownloadRequest(basedir + "aws-sdk-java-master.zip");
		MultipartDownloadRequest request = new MultipartDownloadRequest(bigKey1);
		// MultipartDownloadRequest request = new MultipartDownloadRequest(basedir + "test.txt");//.withMinimumObjectSize(100);// bigKey1);

		 request.withMinimumObjectSize(1024*1024*100);
		 
		request.withParts(30).withWaitForComplete(false);

		FileWriteHandler handler = new FileWriteHandler(new File(LOCAL_TMP_PATH + "big2.zip"));
		handler.setOverrideLocalFile(true);
		handler.setVerifyContent(true);

		handler.setListener(new PartialHandlingListener() {
			double size = 0;

			public void catchException(HSCException e) {
				e.printStackTrace();
			}

			public void completed() {
				long e = Calendar.getInstance().getTimeInMillis();
				double time = e - b;

				double mbs = (size / 1024 / 1024) / (time / 1000);
				console.println("completed speed=" + mbs + "MB/s " + mbs * 8 + "Mbps/s");
				
				latch.countDown();
			}

			public void partCompleted(int partNumber, long beginOffset, long length) {
				size += length;
				console.println("partCompleted= " + partNumber + " " + beginOffset + " " + ((double) length) / 1024 / 1024);
			}

			public void outProgress(int id, long seekOffset, long length) {
//				console.println("progress=" + id + " " + seekOffset + " " + length);
			}
		});

		hcpClient.getObject(request, handler);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
	
		// RESULT VERIFICATION --------------------------------------------------------------------
		testGetBigData();
		latch.countDown();

		latch.await();
		DigestUtils.isMd5Equals(file1, file2);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testGetBigData() throws IOException {
		final long b = Calendar.getInstance().getTimeInMillis();
		HCPObject big=null;
		console.println("single Start");
		try {
//			big = hcpClient.getObject("/sdk-test99/jdk-8u201-windows-x64.exe");
			big = hcpClient.getObject(bigKey1);
		} catch (InvalidResponseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(e1.getReason());
			System.out.println(e1.getMessage());
			System.out.println(e1.getStatusCode());
		} catch (HSCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// HCPObject big = hcpClient.getObject(basedir + "aws-sdk-java-master.zip");
		StreamUtils.inputStreamToFile(big.getContent(), LOCAL_TMP_PATH + "big1.zip", true);

		long e = Calendar.getInstance().getTimeInMillis();
		double time = e - b;

		double mbs = (((double) big.getSize()) / 1024 / 1024) / (time / 1000);
		console.println("single completed speed=" + mbs + "MB/s " + mbs * 8 + "Mbps/s");

	}

}