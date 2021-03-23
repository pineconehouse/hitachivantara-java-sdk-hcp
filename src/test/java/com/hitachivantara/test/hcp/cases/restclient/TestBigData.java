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
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import com.amituofo.common.define.Constants;
import com.amituofo.common.ex.HSCException;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.common.define.HashAlgorithm;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestBigData extends TestHCPBase {

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
	public void testPutBigData() throws IOException, HSCException, NoSuchAlgorithmException {
		String SHA256_local = DigestUtils.format2Hex(DigestUtils.calcSHA256(localBigFile1)).toUpperCase();
		
		PutObjectRequest req = new PutObjectRequest(bigKey1);
		req.withContent(localBigFile1)
		.withVerifyContent(HashAlgorithm.SHA256)
//		.withProgressListener(new ProgressListener<Long>() {
//			long len = 0;
//
//			@Override
//			public void progressChanged(ProgressEventType event, Long data) {
//				len += data;
//				System.out.println(event + " / " + data + " / " + len + " / " + localBigFile1.length());
//			}
//		})
		;
		// System.out.println("filelen="+localBigFile3.length()+" read="+len);
		PutObjectResult resultPut = hcpClient.putObject(req);
		print.appendRecord("Put Object=" + bigKey1);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------
		HCPObjectSummary result = hcpClient.getObjectSummary(bigKey1);

		// ----------------------------------------------------------------------------------------
		print.appendRecord("Get Object... " + bigKey1);
		print.appendRecord("Location=" + bigKey1);
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("VersionId=" + result.getVersionId());

		// ----------------------------------------------------------------------------------------
		assertTrue(result.getContentHash().equals(resultPut.getContentHash()));
		assertTrue(SHA256_local.equals(resultPut.getContentHash()));
	}
	

	@Test
	public void testGetBigData() throws IOException, HSCException {
		HCPObjectSummary summary = hcpClient.getObjectSummary(bigKey1);
		
		assertTrue(summary.getSize()>Constants.SIZE_100MB);

		HCPObject big = hcpClient.getObject(bigKey1);
		StreamUtils.inputStreamToFile(big.getContent(), LOCAL_TMP_PATH + "big.zip", true);
		String SHA256_local = DigestUtils.format2Hex(DigestUtils.calcSHA256(new File(LOCAL_TMP_PATH + "big.zip"))).toUpperCase();
		System.out.println(SHA256_local);
		System.out.println(big.getContentHash().toUpperCase());
		assertTrue(SHA256_local.equals(big.getContentHash().toUpperCase()));
		
//		new File(LOCAL_TMP_PATH + "big.zip").delete();
	}
	
}