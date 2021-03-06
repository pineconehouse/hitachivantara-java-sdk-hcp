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
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestParticular extends TestHCPBase {

	@Test
	public void testEmptyObj() throws Exception {
		// ----------------------------------------------------------------------------------------
		File tempFile2 = TestDataFactory.createTestFile(ContentType.EMPTY);
		String content2 = TestDataFactory.getContent(tempFile2);
		PutObjectResult resultPut = hcpClient.putObject(key1, tempFile2);
		// ----------------------------------------------------------------------------------------
		console.println("Put Object... " + key1);
		console.println("OriginalContent=" + content2);
		console.println("Location=" + resultPut.getLocation());
		console.println("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		console.println("ContentHash=" + resultPut.getContentHash());
		console.println("ETag=" + resultPut.getETag());
		console.println("VersionId=" + resultPut.getVersionId());
		console.println("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------
		HCPObject result = hcpClient.getObject(key1);
		String resultContent = StreamUtils.inputStreamToString(result.getContent(), true);

		// ----------------------------------------------------------------------------------------
		console.println("Get Object... " + key1);
		console.println("Location=" + key1);
		console.println("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		console.println("ContentHash=" + result.getContentHash());
		console.println("VersionId=" + result.getVersionId());
		console.println("OriginalContent=" + content2);
		console.println("RetrievedContent=" + resultContent);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(content2));
		assertTrue(resultContent.equals(""));
	}


}