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
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.junit.Test;

import com.amituofo.common.util.DigestUtils;
import com.hitachivantara.hcp.standard.api.MultipartUpload;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartUploadRequest;
import com.hitachivantara.hcp.standard.util.multipartupload.MulitipartUploadException;
import com.hitachivantara.hcp.standard.util.multipartupload.MulitipartUploadException.Stage;
import com.hitachivantara.hcp.standard.util.multipartupload.MulitipartUploaderExecutor;
import com.hitachivantara.hcp.standard.util.multipartupload.UploadEventHandler;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * S3 ??????????????????
 * 
 * @author sohan
 *
 */
public class TestUtilHCPMultipartUpload extends TestHCPBase {
	@Test
	public void testMultipartUpload() throws Exception {

		// ???????????????**????????????????????????100MB**????????????????????????10MB???
		final int PART_SIZE = 10 * 1024 * 1024; // Set part size to 10 MB.

		// ?????????????????????**???????????????????????????????????????500MB???????????????????????????????????????????????????**???
		final File tobeUploadFile = new File("C:\\VDisk\\DriverD\\Downloads\\Libs\\cosbench-master.zip");
		// ??????key
		final String objectPath = "hcp-test/" + tobeUploadFile.getName() + "90.zip";

		MultipartUploadRequest request = new MultipartUploadRequest(objectPath);
		MultipartUpload api = hcpClient.getMultipartUpload(request);
		// ==========================================================================================================================
		MulitipartUploaderExecutor exec = new MulitipartUploaderExecutor(api, objectPath, tobeUploadFile, PART_SIZE);
		// ???????????????????????????10???????????????,???????????????10??????
		exec.multiThreadUpload(2,
				/**
				 * ????????????????????????
				 * 
				 * @author sohan
				 *
				 */
				new UploadEventHandler() {
					private final PrintStream log = System.out;

					@Override
					public void init(String namespaceName, String objectPath, String uploadId) {
						log.println("Step 1: Initialize [" + objectPath + "] [" + uploadId + "]");
					}

					@Override
					public void beforePartUpload(String namespaceName, String objectPath, String uploadId, int partNumber, long uploadPartsize, long startTime) {
						log.println("Step 2: Upload parts... [" + objectPath + "] [" + uploadId + "] " + partNumber + " " + uploadPartsize);
					}

					@Override
					public void caughtException(Stage stage, MulitipartUploadException e) {
						log.println("Step " + stage + ": Error [" + objectPath + "] [" + e.getUploadId() + "] " + e.getPartNumber() + " " + e.getUploadPartsize());
						e.printStackTrace();

						// **???????????????????????????????????????????????????????????????**
						// Do something
					}

					@Override
					public void afterPartUpload(String namespaceName, String objectPath, String uploadId, int partNumber, long uploadPartsize, long startTime, long endTime) {
						log.println("Step 2: Upload parts OK ["
								+ objectPath
								+ "] ["
								+ uploadId
								+ "] "
								+ partNumber
								+ " "
								+ uploadPartsize
								+ "\t??????:"
								+ (((double) (endTime - startTime)) / 1000)
								+ " sec");
					}

					@Override
					public void complete(String namespaceName, String objectPath, String uploadId, Long uploadedSize, long startTime, long endTime) {
						log.println("Step 3: Complete... [" + objectPath + "] [" + uploadId + "]");

						// ???????????????????????????MD5??????????????????????????????
						// **??????????????????????????????????????????-???????????????????????????**
						try {
							HCPObject s3Object = hcpClient.getObject(objectPath);
							InputStream in = s3Object.getContent();
							byte[] orginalFileMd5;
							orginalFileMd5 = DigestUtils.calcMD5(tobeUploadFile);
							byte[] objectFromHCPMd5 = DigestUtils.calcMD5(in);
							in.close();

							boolean equals = Arrays.equals(orginalFileMd5, objectFromHCPMd5);
							assertTrue(equals == true);
							System.out.println("***Upload " + objectPath + " Successfully!***");
						} catch (Exception e1) {
							e1.printStackTrace();
						}

					}
				}, null);
		// =========================================================================================================================

		// =========================================================================================================================
		// // ???????????????????????????
		// // ??????????????????????????????id??????
		// String uploadId = "xxxxx";
		// MulitipartUploaderExecutor exec2 = new MulitipartUploaderExecutor(s3Client, namespaceName, objectPath, tobeUploadFile, PART_SIZE, uploadId);
		// // ??????????????????????????????????????????=?????????3
		// exec2.uploadPart(3);
		// // ????????????
		// exec2.complete();
		// =========================================================================================================================

	}

}
