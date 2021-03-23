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
import java.util.List;

import org.junit.Test;

import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestVersion extends TestHCPBase {
	@Test
	public void testListVersion() throws Exception {
		TestDataFactory.getInstance().prepare10VersionsOfObject(key1);

		HCPObjectEntrys result = hcpClient.listVersions(key1);
		ObjectEntryIterator it = result.iterator();
		List<HCPObjectEntry> list;
		int totalcount = 0;
		int c = 3;
		while ((list = it.next(c)) != null) {
			totalcount += list.size();
			// print.addRow("-" + list.size() + "-----------------------------------------------");
			for (int i = 0; i < list.size(); i++) {
				HCPObjectEntry hcpObjectEntry = list.get(i);

				// print.addRow((ii++) + "\t" + hcpObjectEntry.getType() + "\t" + hcpObjectEntry.getKey() + "\t" +
				// hcpObjectEntry.getName() + "\t" + hcpObjectEntry.getSize());

				if (hcpObjectEntry.isDirectory()) {
					print.appendRecord(new Object[] { hcpObjectEntry.getKey(), hcpObjectEntry.getName(), hcpObjectEntry.getType(), hcpObjectEntry.getChangeTime() });
				} else {
					print.appendRecord(new Object[] { hcpObjectEntry.getKey(),
							hcpObjectEntry.getName(),
							hcpObjectEntry.getType(),
							hcpObjectEntry.getSize(),
							hcpObjectEntry.getHashAlgorithmName(),
							hcpObjectEntry.getContentHash(),
							hcpObjectEntry.getETag(),
							hcpObjectEntry.getRetention(),
							hcpObjectEntry.getRetentionString(),
							hcpObjectEntry.getRetentionClass(),
							hcpObjectEntry.getIngestTime(),
							hcpObjectEntry.isHold(),
							hcpObjectEntry.isShred(),
							hcpObjectEntry.getDpl(),
							hcpObjectEntry.isIndexed(),
							hcpObjectEntry.hasMetadata(),
							hcpObjectEntry.getCustomMetadatas(),
							hcpObjectEntry.getVersionId(),
							hcpObjectEntry.isReplicated(),
							hcpObjectEntry.getChangeTime(),
							hcpObjectEntry.getOwner(),
							hcpObjectEntry.getDomain(),
							hcpObjectEntry.hasAcl(),
							hcpObjectEntry.getState() });
				}
			}
			print.appendCuttingLine('-');
//			print.printout();
		}
		print.appendRecord("totalcount: " + totalcount);

		it.close();
		assertTrue(true);

	}
	
	@Test
	public void testGetSpecificVersionOfObj() throws Exception {
		DeleteObjectRequest request = new DeleteObjectRequest(key1).withPurge(true).withPrivileged(true, "I said");
		hcpClient.deleteObject(request);

		final int putindex = 3;
		final int getindex = 5 - 3;

		String content2 = "";
		for (int i = 1; i <= 5; i++) {
			File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
			PutObjectResult resultPut = hcpClient.putObject(key1, tempFile2);

			if (i == putindex) {
				content2 = TestDataFactory.getContent(tempFile2);
				print.appendCuttingLine('-');
				print.appendRecord("Put Object=" + key1);
				print.appendRecord("OriginalContent=" + content2);
				print.appendRecord("Location=" + resultPut.getLocation());
				print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
				print.appendRecord("ContentHash=" + resultPut.getContentHash());
				print.appendRecord("ETag=" + resultPut.getETag());
				print.appendRecord("VersionId=" + resultPut.getVersionId());
				print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());
				print.appendCuttingLine('-');
			} else {
				print.appendRecord("VersionId=" + resultPut.getVersionId());
			}

			Thread.sleep(1000);
		}
		// ----------------------------------------------------------------------------------------

		HCPObjectEntrys entrys = hcpClient.listVersions(key1);
		ObjectEntryIterator it = entrys.iterator();
		List<HCPObjectEntry> list = it.next(100);

		HCPObjectEntry entry = list.get(getindex);

		HCPObject result = hcpClient.getObject(entry.getKey(), entry.getVersionId());
		String resultContent = StreamUtils.inputStreamToString(result.getContent(), true);

		// ----------------------------------------------------------------------------------------
		print.appendCuttingLine('-');
		print.appendRecord("Get Object... " + key1);
		print.appendRecord("Location=" + key1);
		print.appendRecord("HashAlgorithmName=" + result.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("VersionId=" + result.getVersionId());
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("RetrievedContent=" + resultContent);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(content2));
	}

	@Test
	public void testGetSpecificVersionOfDeletedObj() throws Exception {
		{
			DeleteObjectRequest request = new DeleteObjectRequest(key1).withPurge(true).withPrivileged(true, "I said");
			hcpClient.deleteObject(request);
		}
		
		String getversion = null;

		String content2 = "";
		for (int i = 1; i <= 5; i++) {
			File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
			PutObjectResult resultPut = hcpClient.putObject(key1, tempFile2);

			if (i == 3) {
				getversion = resultPut.getVersionId();

				content2 = TestDataFactory.getContent(tempFile2);
				print.appendCuttingLine('-');
				print.appendRecord("Put Object=" + key1);
				print.appendRecord("OriginalContent=" + content2);
				print.appendRecord("Location=" + resultPut.getLocation());
				print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
				print.appendRecord("ContentHash=" + resultPut.getContentHash());
				print.appendRecord("ETag=" + resultPut.getETag());
				print.appendRecord("VersionId=" + resultPut.getVersionId());
				print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());
				print.appendCuttingLine('-');
			} else {
				print.appendRecord("VersionId=" + resultPut.getVersionId());
			}

			Thread.sleep(1000);
		}
		// ----------------------------------------------------------------------------------------

		hcpClient.deleteObject(key1);

		File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
		hcpClient.putObject(key1, tempFile2);

		hcpClient.deleteObject(key1);

		GetObjectRequest request = new GetObjectRequest(key1).withVersionId(getversion).withDeletedObject(true);
		HCPObject result = hcpClient.getObject(request );
		String resultContent = StreamUtils.inputStreamToString(result.getContent(), true);

		// ----------------------------------------------------------------------------------------
		print.appendCuttingLine('-');
		print.appendRecord("Get Object... " + key1);
		print.appendRecord("Location=" + key1);
		print.appendRecord("HashAlgorithmName=" + result.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("VersionId=" + result.getVersionId());
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("RetrievedContent=" + resultContent);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(content2));
	}

}