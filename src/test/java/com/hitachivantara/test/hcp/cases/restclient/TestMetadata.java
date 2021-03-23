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
import java.util.Collection;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadata;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummary;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummarys;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestMetadata extends TestHCPBase {
	String metadataName = "poweredby";
	String metadataContent = "<name> han song 韩松 </name>";
	S3CompatibleMetadata metadata = new S3CompatibleMetadata();
	private String key1Contents;

	private void prepareTestData(ContentType type) throws Exception {

		metadata.put("name-en", "Rison han");
		metadata.put("name-cn", "<韩松>");
		metadata.put("name_jp", "まつ【松】マツ");
		metadata.put("k1", "<TEST_CONTENTS></TEST_CONTENTS>\t ");
		metadata.put("k2", "!@#$%^&*()_+{}:\">?<|1234567890-=qwertyuiop[]\';lkjhgfdsazxcvbnm,./'");
		metadata.put("k3", "https://\nanywhere.tn9.hcp8.hdim.lab\nbrowser\n");
		metadata.put("k4", "中文ディレクトリ디렉토리");
		metadata.put("k5中文", "中文ディレクトリ디렉토리中文ディレクトリ디렉토리");

		File tempFile2 = TestDataFactory.createTestFile(type);
		key1Contents = TestDataFactory.getContent(tempFile2);

		// ----------------------------------------------------------------------------------------

		PutObjectRequest request = new PutObjectRequest(key1).withContent(tempFile2).withMetadata(metadata).withMetadata(metadataName, metadataContent, "utf-8");
		PutObjectResult resultPut = hcpClient.putObject(request);

		// ----------------------------------------------------------------------------------------
		HCPObject result = hcpClient.getObject(key1);
		String resultContent = StreamUtils.inputStreamToString(result.getContent(), true);

		// ----------------------------------------------------------------------------------------
		// print.addRow("Get Object... " + key1);
		// print.addRow("Location=" + key1);
		// print.addRow("ContentHash=" + result.getContentHash());
		// print.addRow("VersionId=" + result.getVersionId());
		// print.addRow("OriginalContent=" + key1Contents);
		// print.addRow("RetrievedContent=" + resultContent);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(key1Contents));

		console.println("test data reay!");

	}

	@Test
	public void testPutMetadata() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteObject(new DeleteObjectRequest(key1).withPurge(true));
		assertTrue(hcpClient.doesObjectExist(key1) == false);
		hcpClient.putObject(key1, TestDataFactory.createTestFile(ContentType.TIME));
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		PutMetadataRequest request = new PutMetadataRequest(key1).withMetadataName(metadataName).withContent(metadataContent);
		hcpClient.putMetadata(request);

		metadata.put("name-en", "Rison han");
		metadata.put("name-cn", "<韩松>");
		metadata.put("name_jp", "まつ【松】マツ");
		metadata.put("k1", "<TEST_CONTENTS></TEST_CONTENTS>\t ");
		metadata.put("k2", "!@#$%^&*()_+{}:\">?<|1234567890-=qwertyuiop[]\';lkjhgfdsazxcvbnm,./'");
		metadata.put("k3", "https://\nanywhere.tn9.hcp8.hdim.lab\nbrowser\n");
		metadata.put("k4", "中文ディレクトリ디렉토리");
//		metadata.put("k5中文", "中文ディレクトリ디렉토리中文ディレクトリ디렉토리");

		hcpClient.putMetadata(key1, metadata);
		// RESULT VERIFICATION --------------------------------------------------------------------
		{
			boolean exist = hcpClient.doesS3MetadataExist(key1);
			assertTrue(exist == true);

			exist = hcpClient.doesMetadataExist(key1, metadataName);
			assertTrue(exist == true);

			S3CompatibleMetadata sm = hcpClient.getMetadata(key1);
			assertTrue("Rison han".equals(sm.get("name-en")));
			assertTrue("<韩松>".equals(sm.get("name-cn")));
			assertTrue("まつ【松】マツ".equals(sm.get("name_jp")));
			assertTrue("<TEST_CONTENTS></TEST_CONTENTS>\t ".equals(sm.get("k1")));
			assertTrue("!@#$%^&*()_+{}:\">?<|1234567890-=qwertyuiop[]\';lkjhgfdsazxcvbnm,./'".equals(sm.get("k2")));
			assertTrue("https://\nanywhere.tn9.hcp8.hdim.lab\nbrowser\n".equals(sm.get("k3")));
			assertTrue("中文ディレクトリ디렉토리".equals(sm.get("k4")));
//			assertTrue("中文ディレクトリ디렉토리中文ディレクトリ디렉토리".equals(sm.get("k5中文")));

			HCPMetadata cm = hcpClient.getMetadata(key1, metadataName);
			assertTrue(metadataName.equals(cm.getName()));
			assertTrue(metadataContent.equals(StreamUtils.inputStreamToString(cm.getContent(), true)));
		}
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testCreateObjWithMetadataAndRetrievingMetadata() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		prepareTestData(ContentType.TIME);

		boolean exist = hcpClient.doesS3MetadataExist(key1);
		assertTrue(exist == true);

		exist = hcpClient.doesMetadataExist(key1, metadataName);
		assertTrue(exist == true);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		S3CompatibleMetadata sm = hcpClient.getMetadata(key1);
		HCPMetadata cm = hcpClient.getMetadata(key1, metadataName);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue("Rison han".equals(sm.get("name-en")));
		assertTrue("<韩松>".equals(sm.get("name-cn")));
		assertTrue("まつ【松】マツ".equals(sm.get("name_jp")));
		assertTrue("<TEST_CONTENTS></TEST_CONTENTS>\t ".equals(sm.get("k1")));
		assertTrue("!@#$%^&*()_+{}:\">?<|1234567890-=qwertyuiop[]\';lkjhgfdsazxcvbnm,./'".equals(sm.get("k2")));
		assertTrue("https://\nanywhere.tn9.hcp8.hdim.lab\nbrowser\n".equals(sm.get("k3")));
		assertTrue("中文ディレクトリ디렉토리".equals(sm.get("k4")));
//		assertTrue("中文ディレクトリ디렉토리中文ディレクトリ디렉토리".equals(sm.get("k5中文")));

		assertTrue(metadataName.equals(cm.getName()));
		assertTrue(metadataContent.equals(StreamUtils.inputStreamToString(cm.getContent(), true)));
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testCreateEmptyObjWithMetadataAndRetrievingMetadata() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		prepareTestData(ContentType.EMPTY);

		boolean exist = hcpClient.doesS3MetadataExist(key1);
		assertTrue(exist);

		exist = hcpClient.doesMetadataExist(key1, metadataName);
		assertTrue(exist);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		S3CompatibleMetadata sm = hcpClient.getMetadata(key1);
		HCPMetadata cm = hcpClient.getMetadata(key1, metadataName);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue("Rison han".equals(sm.get("name-en")));
		assertTrue("<韩松>".equals(sm.get("name-cn")));
		assertTrue("まつ【松】マツ".equals(sm.get("name_jp")));
		assertTrue("<TEST_CONTENTS></TEST_CONTENTS>\t ".equals(sm.get("k1")));
		assertTrue("!@#$%^&*()_+{}:\">?<|1234567890-=qwertyuiop[]\';lkjhgfdsazxcvbnm,./'".equals(sm.get("k2")));
		assertTrue("https://\nanywhere.tn9.hcp8.hdim.lab\nbrowser\n".equals(sm.get("k3")));
		assertTrue("中文ディレクトリ디렉토리".equals(sm.get("k4")));
//		assertTrue("中文ディレクトリ디렉토리中文ディレクトリ디렉토리".equals(sm.get("k5中文")));

		assertTrue(metadataName.equals(cm.getName()));
		assertTrue(metadataContent.equals(StreamUtils.inputStreamToString(cm.getContent(), true)));
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testDeleteMetadata() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		prepareTestData(ContentType.EMPTY);

		boolean exist = hcpClient.doesS3MetadataExist(key1);
		assertTrue(exist == true);

		exist = hcpClient.doesMetadataExist(key1, metadataName);
		assertTrue(exist == true);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.deleteS3Metadata(key1);
		hcpClient.deleteMetadata(key1, metadataName);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		exist = hcpClient.doesS3MetadataExist(key1);
		assertTrue(exist == false);

		exist = hcpClient.doesMetadataExist(key1, metadataName);
		assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testListMetadata() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		prepareTestData(ContentType.TIME);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		HCPMetadataSummarys metadatas = hcpClient.listMetadatas(key1);
		Collection<HCPMetadataSummary> ms = metadatas.getMetadatas();
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(ms.size() == 2);
		int i = 0;
		for (HCPMetadataSummary m : ms) {
			print.appendRecord(m.getName(), m.getSize(), m.getContentType(), m.getChangeTime(), m.getHashAlgorithmName(), m.getContentHash());

			if (i == 0) {
				assertTrue(".metapairs".equals(m.getName()));
			} else if (i == 1) {
				assertTrue(metadataName.equals(m.getName()));
			}

			i++;
		}
		// RESULT VERIFICATION --------------------------------------------------------------------

	}
}