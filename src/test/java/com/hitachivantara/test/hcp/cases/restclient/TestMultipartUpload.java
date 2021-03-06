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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.util.DigestUtils;
import com.hitachivantara.hcp.common.define.HashAlgorithm;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.MultipartUpload;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.PartETag;
import com.hitachivantara.hcp.standard.model.PartSummary;
import com.hitachivantara.hcp.standard.model.UploadPartList;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartUploadRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestMultipartUpload extends TestHCPBase {

	@Test
	public void testMultipartUpload() throws Exception {
		String key = "test2/mult1.txt";// key9;
		// String key = key9 + "uc";
		String uploadId = null;
		MultipartUploadRequest request = new MultipartUploadRequest(key);
		MultipartUpload api = hcpClient.getMultipartUpload(request);
		uploadId = api.initiate();
		print.appendRecord("key=" + key);
		print.appendRecord("uploadId=" + uploadId);

		List<PartETag> partETags = new ArrayList<PartETag>();

		File file = TestDataFactory.createTestFile(ContentType.TXT_10MB);
		String orgMd5 = DigestUtils.format2Hex(DigestUtils.calcMD5(file));

		InputStream in = new FileInputStream(file);
		long length = in.available();
		PartETag etag = api.uploadPart(1, in, length).getPartETag();
		print.appendRecord("etag=" + etag.getPartNumber() + " " + etag.getETag());
		partETags.add(etag);

		api.complete(partETags);

		HCPObject obj = hcpClient.getObject(key);
		String destMd5 = DigestUtils.format2Hex(DigestUtils.calcMD5(obj.getContent()));

		print.appendRecord("orgMd5=" + orgMd5);
		print.appendRecord("desMd5=" + destMd5);
		assertTrue(orgMd5.equals(destMd5));
	}

	@Test
	public void testMultipartUpload100M() throws Exception {
		final File file = TestDataFactory.createTestFile(ContentType.TXT_100MB);// new File(localBigFile1);//
		// final File file = new File("C:\\VDisk\\DriverD\\Downloads\\Libs\\cosbench-master - ?????? (2) - ??????.zip");
		final String key = key9 + file.getName();
		String uploadId = null;
		MultipartUploadRequest request = new MultipartUploadRequest(key);
		final MultipartUpload api = hcpClient.getMultipartUpload(request);
		uploadId = api.initiate();
		print.appendRecord("key=" + key);
		print.appendRecord("uploadId=" + uploadId);
		print.printout();

		final List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());

		final String orgMd5 = DigestUtils.format2Hex(DigestUtils.calcMD5(file));

		final long length = file.length();
		final long partLength = 1024 * 1024 * 8; // Min 5M
		long remainLength = length;
		long startOffset = 0;

		// final ThreadLocal<Long> remainLengthLocal = new ThreadLocal<Long>() {
		// @Override
		// protected Long initialValue() {
		// return length;
		// }
		// };

		final MessageDigest md = MessageDigest.getInstance(HashAlgorithm.MD5.getKeyname());

		int index = 1;
		final Queue<long[]> parts = new LinkedList<long[]>();
		while (remainLength > 0) {
			long uploadLength = Math.min(remainLength, partLength);

			parts.add(new long[] { index++, startOffset, uploadLength });

			startOffset += (uploadLength + 0);
			remainLength -= uploadLength;
		}
		final int partsSize = parts.size();

		final CountDownLatch latch = new CountDownLatch(partsSize);

		for (int i = 0; i < partsSize; i++) {
			final int id = i;
			new Thread(new Runnable() {
				public void run() {
					InputStream in = null;
					try {
						long[] part = null;
						synchronized (parts) {
							part = parts.poll();
						}

						if (part != null) {
							in = new FileInputStream(file);
							in.skip(part[1]);

							console.println("uploadPart... " + id);
							PartETag etag = api.uploadPart((int) part[0], in, part[2]).getPartETag();
							console.println("etag=" + etag.getPartNumber() + " " + etag.getETag());
							// md.update(("\""+etag.getETag()+"\"").getBytes());
							// md.update((etag.getETag().toUpperCase()).getBytes());
							md.update((etag.getETag().toLowerCase(Locale.ENGLISH)).getBytes());
							partETags.add(etag);
						} else {
							console.println("null");
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
							}
						}
					}

					latch.countDown();
				}

			}// .run();
			).start();
		}

		try {
			latch.await();

			api.complete(partETags);

			HCPObject obj = hcpClient.getObject(key);
			String destMd5 = DigestUtils.format2Hex(DigestUtils.calcMD5(obj.getContent()));

			print.appendRecord("orgMd5=" + orgMd5, file.length());
			print.appendRecord("desMd5=" + destMd5, obj.getSize());

			print.appendRecord("calcETag=" + DigestUtils.format2Hex(md.digest()));
			print.appendRecord("desETag=" + obj.getETag());
			assertTrue(orgMd5.equals(destMd5));
		} catch (HSCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testListParts() throws HSCException, IOException {
		String key = key9 + "uc";
		List<PartETag> etags = new ArrayList<PartETag>();
		String uploadId = TestDataFactory.getInstance().prepareUncompleteMultipartUploadObj(key, etags);
		MultipartUploadRequest request = new MultipartUploadRequest(key).withUploadId(uploadId);

		final MultipartUpload api = hcpClient.getMultipartUpload(request);
		UploadPartList parts = api.listParts();
		int found = 0;
		for (PartSummary s : parts.getPartSummarys()) {
			int pn = s.getPartNumber();
			for (PartETag partETag : etags) {
				if (partETag.getPartNumber() == pn) {
					assertTrue(partETag.getETag().equals(s.getETag()));
					assertTrue(partETag.getSize().equals(s.getSize()));
					found++;
				}
			}
		}
		assertTrue(found > 0);
		assertTrue(found == etags.size());
	}

	@Test
	public void testAbort() throws HSCException, IOException {
		String key = key9 + "uc";
		List<PartETag> etags = new ArrayList<PartETag>();
		String uploadId = TestDataFactory.getInstance().prepareUncompleteMultipartUploadObj(key, etags);
		MultipartUploadRequest request = new MultipartUploadRequest(key).withUploadId(uploadId);

		final MultipartUpload api = hcpClient.getMultipartUpload(request);

		UploadPartList parts = api.listParts();
		assertTrue(parts.getPartSummarys().size() > 0);

		api.abort();
		try {
			api.listParts();
			// assertTrue(parts);
		} catch (InvalidParameterException e1) {
			assertTrue("The parameter uploadId must be specified.".equals(e1.getMessage()));
			return;
		} catch (InvalidResponseException e) {
			assertTrue(e.getStatusCode() == 404);
			return;
		}

		assertTrue(false);
	}
}