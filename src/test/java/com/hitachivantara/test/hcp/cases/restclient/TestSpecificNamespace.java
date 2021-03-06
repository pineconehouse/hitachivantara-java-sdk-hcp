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

import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.standard.model.HCPObject;
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
public class TestSpecificNamespace extends TestHCPBase {
	@Test
	public void testCreateAndRetrievingObjFromOtherNamespace() throws Exception {
		File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
		String content2 = TestDataFactory.getContent(tempFile2);

		// ----------------------------------------------------------------------------------------

		PutObjectRequest putRequest = new PutObjectRequest(key1).withContent(content2).withNamespace(namespace2Name);
		PutObjectResult resultPut = hcpClient.putObject(putRequest);

		print.appendRecord("Put Object=" + key1);
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------
		GetObjectRequest getRequest = new GetObjectRequest(key1).withNamespace(namespace2Name);
		HCPObject result = hcpClient.getObject(getRequest);
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

	// static Integer total = 0;
	// @Test
	// public void testCreate100WFile() throws Exception {
	// final Random rand = new Random();
	//
	// // ----------------------------------------------------------------------------------------
	// Thread[] threads = new Thread[10];
	// for (int i = 0; i < threads.length; i++) {
	// Runnable run = new Runnable() {
	// public void run() {
	// long lastTime = Calendar.getInstance().getTimeInMillis();
	// final int COUNT = 1000;
	// for (int i = 1; i <= COUNT; i++) {
	// try {
	// InputStream in = createTestInputStream(ContentType.TIME);
	// String key = "00003/" + Calendar.getInstance().getTimeInMillis() + rand.nextInt(999999999);
	// // print.addRow(key);
	// // ----------------------------------------------------------------------------------------
	// PutObjectResult result = hcpClient.putObject(key, in);
	// synchronized (total) {
	// total += 1;
	// }
	// if (i % 100 == 0) {
	// long nowTime = Calendar.getInstance().getTimeInMillis();
	// print.addRow("100/" + total + "\tCrateOK " + (nowTime - lastTime) + "ms");
	// lastTime = nowTime;
	//
	// if (COUNT == i) {
	// print.addRow("Finished");
	// }
	// }
	// in.close();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	// };
	// threads[i] = new Thread(run);
	// threads[i].start();
	// }
	//
	// Thread.sleep(100000000000000L);
	// // ----------------------------------------------------------------------------------------
	// assertTrue(true);
	// }

	// @Test
	// public void testCreateLargeObj() throws Exception {
	// File tempFile = new File("C:\\VDisk\\DriverD\\Downloads\\Libs\\aws-java-sdk-1.11.305.zip");
	// String key = basedir + tempFile.getName();
	//
	// // ----------------------------------------------------------------------------------------
	//
	// PutObjectResult result = hcpClient.putObject(key, tempFile);
	//
	// // ----------------------------------------------------------------------------------------
	//
	// print.addRow("Location=" + result.getLocation());
	// print.addRow("ContentHash=" + result.getContentHash());
	// print.addRow("ETag=" + result.getETag());
	// print.addRow("VersionId=" + result.getVersionId());
	// print.addRow("IngestTimeMilliseconds=" + result.getIngestTimeMilliseconds());
	//
	// // ----------------------------------------------------------------------------------------
	//
	// assertTrue(true);
	// }


	// @Test
	// public void testDeleteObj() throws Exception {
	// boolean exist = false;
	// exist = hcpClient.hasObject(target1);
	//
	// // ----------------------------------------------------------------------------------------
	// print.addRow("Location=" + target1);
	// print.addRow("Exist=" + exist);
	// assertTrue(exist == false);
	// }

	// @Test
	// public void testDeletedVersionObj() throws Exception {
	// // TODO Auto-generated method stub
	//
	// }

	// @Test
	// public void testMultipartUpload1() throws Exception {
	// // ClientConfiguration configuration = new ClientConfiguration();
	// // SimpleHttpClient client = new SimpleHttpClient();
	//
	// // POST /namespace-name/object-name?uploads HTTP/1.1
	// BasicHCPCredentials c = new BasicHCPCredentials(super.user, super.password);
	//
	// String dt = DateUtils.RFC822_DATE_FORMAT.print(Calendar.getInstance().getTimeInMillis());
	// header.put("Host", "cloud.tn9.hcp8.hdim.lab");
	// header.put("Content-Type", "application/octet-stream");
	// header.put("Date", dt);
	//
	// param.put("uploads", null);
	// // String canonicalString = "POST\n\napplication/octet-stream\n" + dt +
	// // "\n/cloud/mulitpart1.obj?uploads";
	// String canonicalString = S3SignerUtils.makeS3CanonicalString(Method.POST,
	// "/cloud/mulitpart1.obj", header, param);
	// byte[] signature = S3SignerUtils.calcSignature(canonicalString, c.getSecretKey());
	//
	// header.put("Authorization", "AWS " + c.getAccessKey() + ":" +
	// S3SignerUtils.BASE64_ENCODER.encode(signature));
	//
	// HttpResponse response = null;
	// response = HttpsUtils.post("http://cloud.tn9.hcp8.hdim.lab/mulitpart1.obj?uploads", header,
	// param, null);
	// HttpsUtils.printHttpResponse(response, true);
	//
	// // header.put("Content-Length", "");
	// //// HttpEntity entity = new HttpEntity();
	// // response =
	// //
	// HttpsUtils.put("http://cloud.tn9.hcp8.hdim.lab/mulitpart1.obj?uploadId=97743106494145&partNumber=1",
	// // header, param, null);
	// // HttpsUtils.printHttpResponse(response, true);
	//
	// }
}