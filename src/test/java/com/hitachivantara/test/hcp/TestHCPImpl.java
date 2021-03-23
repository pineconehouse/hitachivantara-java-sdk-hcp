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
package com.hitachivantara.test.hcp;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.kit.PrettyRecordPrinter;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.RandomUtils;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.common.define.HashAlgorithm;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.api.MultipartUpload;
import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.api.event.ListObjectHandler;
import com.hitachivantara.hcp.standard.api.event.ObjectCopyingListener;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.PartETag;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadata;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummary;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummarys;
import com.hitachivantara.hcp.standard.model.metadata.HCPSystemMetadata;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CopyDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartUploadRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory.ContentType;

/**
 * @author sohan
 *
 */
public class TestHCPImpl extends TestHCPBase {

	// @Test
	// public void testCreateNamespace() throws HSCException {
	//// hks3test
	// hcpClient.copyDirectory(null);
	// }

	@Test
	public void testAccessSpecificPath() throws HSCException {
		// data＞test2＞HCPSystemManagementHelp＞Content＞Administering HCP
		HCPObjectSummary summary = hcpClient.getObject("data/test2/HCPSystemManagementHelp/Content/Administering HCP");
		print.appendRecord("Put Object... " + summary.getContentHash());
	}

	// @Test
	// public void testGetBigData() throws IOException, HSCException {
	// HCPObject big = hcpClient.getObject("hcp-test/Windows7SP1x64.zip");
	// StreamUtils.inputStreamToFile(big.getContent(), "C:\\VDisk\\DriverD\\Downloads\\Temp\\big.zip",
	// true);
	// }

	@Test
	public void testPutBigData() throws IOException, HSCException, NoSuchAlgorithmException {
		PutObjectRequest req = new PutObjectRequest("hcp-test/Windows7SP1x64-1.zip");
		req.withContent(new File("C:\\VDisk\\Virtual Machines\\Windows7SP1x64.zip")).withVerifyContent(HashAlgorithm.SHA256);
		PutObjectResult resultPut = hcpClient.putObject(req);
		print.appendRecord("Put Object... " + key1);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------
		HCPObjectSummary result = hcpClient.getObjectSummary(key1);

		// ----------------------------------------------------------------------------------------
		print.appendRecord("Get Object... " + key1);
		print.appendRecord("Location=" + key1);
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("VersionId=" + result.getVersionId());

		// ----------------------------------------------------------------------------------------
		assertTrue(result.getContentHash().equals(resultPut.getContentHash()));
	}

	@Test
	public void testGetObjectFromAnotherNamespace() throws IOException, HSCException {
		String sourceKey = "zhongwen中文/邮件审计实施（solrcloud集群搭建）.docx";
		GetObjectRequest request = new GetObjectRequest(sourceKey);
		request.withVersionId("97737812308737");
		request.withNamespace("anywhere");
		HCPObject big = hcpClient.getObject(request);
		// StreamUtils.inputStreamToConsole(big.getContent(), true);

		assertTrue(false == hcpClient.getNamespace().equals(request.getNamespace()));
	}

	@Test
	public void testCreateObjWithConservativeHashKeyGeneratorV1() throws Exception {
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
	public void testCreate100Obj() throws Exception {
		for (int i = 0; i < 10; i++) {
			new Thread() {
				@Override
				public void run() {
					for (int i = 0; i < 10000; i++) {
						InputStream in = TestDataFactory.createTestInputStream(ContentType.TIME);
						String key = moreThan100objs + RandomUtils.randomString(10) + ".txt";
						try {
							hcpClient.putObject(new PutObjectRequest(key).withNamespace(namespace2Name).withContent(in));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		for (int i = 0; i < 10; i++) {
			new Thread() {
				@Override
				public void run() {
					for (int i = 0; i < 10000; i++) {
						InputStream in = TestDataFactory.createTestInputStream(ContentType.TIME);
						String key = moreThan100objs + RandomUtils.randomInt(10, 100) + "/" + RandomUtils.randomString(10) + ".txt";
						try {
							hcpClient.putObject(new PutObjectRequest(key).withNamespace(namespace2Name).withContent(in));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		for (int i = 0; i < 10; i++) {
			new Thread() {
				@Override
				public void run() {
					for (int i = 0; i < 10000; i++) {
						InputStream in = TestDataFactory.createTestInputStream(ContentType.TIME);
						String key = moreThan100objs + RandomUtils.randomInt(10, 15) + "/" + RandomUtils.randomString(2) + "/" + RandomUtils.randomString(10) + ".txt";
						try {
							hcpClient.putObject(new PutObjectRequest(key).withNamespace(namespace2Name).withContent(in));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		Thread.sleep(1000 * 60 * 60 * 24);
	}

	// int deletedDirCount = 0;
	// int deletedObjCount = 0;
	// long totalDeletedSize = 0;
	//
	// @Test
	// public void testDeleteFolder() throws Exception {
	//// testCreate100Obj();
	//
	// DeleteDirectoryRequest req = new DeleteDirectoryRequest("data/HCI");// .withNamespace("test");
	//// DeleteDirectoryRequest req = new DeleteDirectoryRequest(moreThan100objs);// .withNamespace("test");
	// req.withPurgeDelete(true);//.withPrivilegedDelete(true, "i said");
	//
	// req.withRecursiveDirectory(true).withDeleteListener(new ObjectDeleteListener() {
	// int i = 0;
	//
	// public void afterDelete(HCPObjectEntry objectEntry, boolean deleted) {
	// // System.out.println(++i + " " + objectEntry.getKey() + " " + objectEntry.getType() + " " + deleted);
	// if (deleted) {
	// if (objectEntry.isDirectory()) {
	// deletedDirCount++;
	// } else if (objectEntry.isObject()) {
	// deletedObjCount++;
	// totalDeletedSize += objectEntry.getSize();
	// }
	// }
	// }
	//
	// public void beforeDelete(HCPObjectEntry hcpObjectEntry) {
	// }
	//
	// });
	// boolean result = hcpClient.deleteDirectory(req);
	//
	// System.out.println("deletedDirCount=" + deletedDirCount + " deletedObjCount=" + deletedObjCount + " totalDeletedSize=" +
	// totalDeletedSize);
	//
	// assertTrue(hcpClient.doesDirectoryExist(moreThan100objs) == false);
	// }
	//
	// @Test
	// public void testDeleteSubFolder() throws Exception {
	// for (int i = 0; i < 100; i++) {
	// File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
	// String key = basedir + "10objs/" + KeyAlgorithm.CONSERVATIVE_PATH_HASH_D32.generate(tempFile.getName());
	// // ----------------------------------------------------------------------------------------
	// PutObjectResult result = hcpClient.putObject(key, tempFile);
	// // ----------------------------------------------------------------------------------------
	// }
	//
	// for (int i = 0; i < 150; i++) {
	// File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
	// String key = basedir + "10objs/sub1/" + tempFile.getName();
	// // ----------------------------------------------------------------------------------------
	// PutObjectResult result = hcpClient.putObject(key, tempFile);
	// // ----------------------------------------------------------------------------------------
	// }
	//
	// DeleteDirectoryRequest req = new DeleteDirectoryRequest(basedir + "10objs");
	// req.withRecursiveDirectory(true).withDeleteListener(new ObjectDeleteListener() {
	// int totalDirectoryCount = 0;
	// int totalObjectCount = 0;
	// int totalCount = 0;
	// int totalSize = 0;
	//
	// public void afterDelete(HCPObjectEntry objectEntry, boolean deleted) {
	// System.out.println(objectEntry.getKey() + " " + objectEntry.getType() + " " + deleted);
	// if (deleted) {
	// if (objectEntry.getType() == ObjectType.object) {
	// totalObjectCount++;
	// }
	// }
	// }
	//
	// public void beforeDelete(HCPObjectEntry hcpObjectEntry) {
	// }
	//
	// });
	// boolean result = hcpClient.deleteDirectory(req);
	// // print.addRow("TotalCount=" + result.getTotalCount());
	// // print.addRow("TotalDirectoryCount=" + result.getTotalDirectoryCount());
	// // print.addRow("TotalObjectCount=" + result.getTotalObjectCount());
	// // print.addRow("TotalSize=" + result.getTotalSize());
	// }

	@Test
	public void testCopyObject() throws Exception {
		File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
		String key = basedir + tempFile.getName();
		String content = TestDataFactory.getContent(tempFile);
		// ----------------------------------------------------------------------------------------
		PutObjectResult result = hcpClient.putObject(key, tempFile);

		String targetKey = key + ".copy";
		String sourceKey = key;
		// String targetKey = sourceKey + ".copy";
		hcpClient.copyObject(sourceKey, targetKey);

		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(sourceKey);
		HCPObjectSummary targetObj = hcpClient.getObjectSummary(targetKey);

		String sourceMD5 = sourceObj.getContentHash();
		String targetMD5 = targetObj.getContentHash();

		// String targetMD5 =
		// DigestUtils.format2Hex(DigestUtils.calcHash(MessageDigest.getInstance(sourceObj.getHashAlgorithmName()),hcpobj.getContent()));
		// String resultContent = StreamUtils.inputStreamToString(hcpobj.getContent(), true);
		assertTrue(sourceMD5.equals(targetMD5));

		// ----------------------------------------------------------------------------------------

		print.appendRecord("sourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);
	}

	@Test
	public void testCopyDirectory() throws Exception {
		// for (int i = 0; i < 100; i++) {
		// File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
		// String key = basedir + "10objs/" +
		// KeyAlgorithm.CONSERVATIVE_PATH_HASH_D32.generate(tempFile.getName());
		// // ----------------------------------------------------------------------------------------
		// PutObjectResult result = hcpClient.putObject(key, tempFile);
		// // ----------------------------------------------------------------------------------------
		// }
		//
		// for (int i = 0; i < 150; i++) {
		// File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
		// String key = basedir + "10objs/sub1/" + tempFile.getName();
		// // ----------------------------------------------------------------------------------------
		// PutObjectResult result = hcpClient.putObject(key, tempFile);
		// // ----------------------------------------------------------------------------------------
		// }

		// CopyDirectoryRequest req = new CopyDirectoryRequest(basedir + "10objs", basedir + "10objs-copy");
		// CopyDirectoryRequest req = new CopyDirectoryRequest(basedir + "folder1", basedir + "folder2");
		CopyDirectoryRequest req = new CopyDirectoryRequest();
		req.withSourceKey(this.basedir).withTargetKey(basedir + "cp2");
		// req.withSourceNamespace("n1").withSourceKey("email").withTargetKey("mail-n1");
		req.withRecursiveDirectory(true).withCopyListener(new ObjectCopyingListener() {

			public NextAction beforeCopying(HCPObjectSummary sourceObjectEntry) {
				System.out.println(sourceObjectEntry.getKey() + " " + sourceObjectEntry.getType() + "B");
				return null;
			}

			public NextAction afterCopying(HCPObjectSummary sourceObjectEntry) {
				System.out.println(sourceObjectEntry.getKey() + " " + sourceObjectEntry.getType() + "A");
				return null;
			}

			public void finished() {
				// TODO Auto-generated method stub

			}
		});

		hcpClient.copyDirectory(req);

		// HCPObjectSummary sourceObj = hcpClient.getObjectSummary(sourceKey);
		// HCPObjectSummary targetObj = hcpClient.getObjectSummary(targetKey);
		//
		// String sourceMD5 = sourceObj.getContentHash();
		// String targetMD5 = targetObj.getContentHash();
		//
		// // String targetMD5 =
		// //
		// DigestUtils.format2Hex(DigestUtils.calcHash(MessageDigest.getInstance(sourceObj.getHashAlgorithmName()),hcpobj.getContent()));
		// // String resultContent = StreamUtils.inputStreamToString(hcpobj.getContent(), true);
		// assertTrue(sourceMD5.equals(targetMD5));
		//
		// // ----------------------------------------------------------------------------------------
		//
		// print.addRow("sourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
		// print.addRow("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);
	}

//	@Test
//	public void testMoveDirectory() throws Exception {
//
//		// CopyDirectoryRequest req = new CopyDirectoryRequest(basedir + "10objs", basedir + "10objs-copy");
//		// CopyDirectoryRequest req = new CopyDirectoryRequest(basedir + "folder1", basedir + "folder2");
//		MoveDirectoryRequest req = new MoveDirectoryRequest();
//		req.withSourceKey("hci-1").withTargetKey("/hci-2/");
//		// req.withSourceNamespace("n1").withSourceKey("email").withTargetKey("mail-n1");
//		req.withRecursiveDirectory(true).withMoveListener(new ObjectMovingListener() {
//
//			public NextAction beforeMove(HCPObjectSummary sourceSummary) {
//				System.out.println(sourceSummary.getKey() + " " + sourceSummary.getType() + "B");
//				return null;
//			}
//
//			public NextAction afterMove(HCPObjectSummary sourceSummary) {
//				System.out.println(sourceSummary.getKey() + " " + sourceSummary.getType() + "A");
//				return null;
//			}
//		});
//
//		hcpClient.moveDirectory(req);
//	}

	@Test
	public void testCopyObjectFromDifferentNamespace() throws Exception {
		// File tempFile = TestDataFactory.createTestFile(ContentType.TIME);
		// String key = basedir + tempFile.getName();
		// String content = TestDataFactory.getContent(tempFile);
		// // ----------------------------------------------------------------------------------------
		// PutObjectResult result = hcpClient.putObject(key, tempFile);

		String sourceNamespace = "anywhere";
		String sourceKey = "zhongwen中文/邮件审计实施（solrcloud集群搭建）.docx";
		String targetKey = sourceKey + ".copy.docx";

		CheckObjectRequest hasrequest = new CheckObjectRequest(sourceKey);
		hasrequest.withVersionId("97737812308737");
		hasrequest.withNamespace(sourceNamespace);
		boolean exist = hcpClient.doesObjectExist(hasrequest);
		assertTrue(exist == true);

		// hcpClient.putSimpleMetadata(key, metadata);

		CopyDirectoryRequest request = new CopyDirectoryRequest(sourceKey, targetKey);
		request.withCopyingMetadata(true).withSourceNamespace(sourceNamespace);
		hcpClient.copyDirectory(request);

		HCPObjectSummary sourceObj = hcpClient.getObjectSummary(hasrequest);
		HCPObjectSummary targetObj = hcpClient.getObjectSummary(targetKey);

		String sourceMD5 = sourceObj.getContentHash();
		String targetMD5 = targetObj.getContentHash();

		assertTrue(sourceMD5.equals(targetMD5));

		// ----------------------------------------------------------------------------------------

		print.appendRecord("sourceHash=" + sourceObj.getHashAlgorithmName() + " " + sourceMD5);
		print.appendRecord("TargetHash=" + targetObj.getHashAlgorithmName() + " " + targetMD5);
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

	@Test
	public void testCreateObjWithCompressed() throws Exception {
		File tempFile = TestDataFactory.createTestFile(ContentType.TXT_100MB);
		String key = basedir + tempFile.getName();
		CalcTime time = new CalcTime();
		String localDigest = DigestUtils.format2Hex(DigestUtils.calcSHA256(tempFile));

		// ----------------------------------------------------------------------------------------
		PutObjectRequest request = new PutObjectRequest(key);
		request.withVerifyContent(HashAlgorithm.SHA256);

		for (int i = 0; i < 3; i++) {
			request.withContent(tempFile, true);
			time.start();
			PutObjectResult result = hcpClient.putObject(request);
			time.end();
			time.print("WithCompressed");
			assertTrue(result.getContentHash().equals(localDigest));
		}

		for (int i = 0; i < 3; i++) {
			request.withContent(tempFile, false);
			time.start();
			PutObjectResult result = hcpClient.putObject(request);
			time.end();
			time.print("WithoutCompressed");
			assertTrue(result.getContentHash().equals(localDigest));
		}
		// ----------------------------------------------------------------------------------------
	}

	// @Test
	// public void testDeleteLargeObj() throws Exception {
	// File tempFile = new File("C:\\VDisk\\DriverD\\Downloads\\Libs\\aws-java-sdk-1.11.305.zip");
	// String key = basedir + tempFile.getName();
	// // ----------------------------------------------------------------------------------------
	// hcpClient.deleteObject(key);
	// }
	private int dircount = 0;
	private int objcount = 0;
	private double size = 0;

	@Test
	public void testSummaryDirectory() throws Exception {
		testCreate100Obj();
		final long startTime = Calendar.getInstance().getTimeInMillis();
		ListObjectHandler listener = new ListObjectHandler() {
			public NextAction foundObject(HCPObjectSummary objectEntry) throws HSCException {
				if (objectEntry.isDirectory()) {
					dircount++;
				} else if (objectEntry.isObject()) {
					objcount++;
					size += objectEntry.getSize();
				}
				// System.out.println(objectEntry.getName() + "\t" + objectEntry.getKey() + "\t" + objectEntry.getUrlName());
				return null;
			}

			// public void finished() {
			// final long endTime = Calendar.getInstance().getTimeInMillis();
			// System.out.println(dircount + "\t" + objcount + "\t" + size / 1024);
			// System.out.println("Used Time=" + (endTime - startTime));
			// }
		};
		ListObjectRequest request = new ListObjectRequest();
		request.withKey(moreThan100objs).withRecursiveDirectory(true);
		hcpClient.listObjects(request, listener);
		final long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println(dircount + "\t" + objcount + "\t" + size / 1024);
		System.out.println("Used Time=" + (endTime - startTime));
	}

	@Test
	public void testListDir() throws Exception {
		HCPObjectEntrys result = hcpClient.listDirectory(super.basedir + "xf/");
		ObjectEntryIterator it = result.iterator();
		List<HCPObjectEntry> list;
		int totalcount = 0;
		double totalsize = 0;
		int c = 10;
		while ((list = it.next(c)) != null) {
			totalcount += list.size();
			// print.addRow("-" + list.size() + "-----------------------------------------------");
			for (int i = 0; i < list.size(); i++) {
				HCPObjectEntry hcpObjectEntry = list.get(i);

				// print.addRow((ii++) + "\t" + hcpObjectEntry.getType() + "\t" + hcpObjectEntry.getKey() + "\t" +
				// hcpObjectEntry.getName() + "\t" + hcpObjectEntry.getSize());

				if (hcpObjectEntry.isDirectory()) {
					print.appendRecord(new Object[] { hcpObjectEntry
							.getKey(), hcpObjectEntry.getName(), hcpObjectEntry.getType(), hcpObjectEntry.getIngestTime(), hcpObjectEntry.getChangeTime() });
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

					totalsize += hcpObjectEntry.getSize();
				}
			}
			print.appendCuttingLine('-');
			print.printout();
		}
		print.appendRecord("totalcount: " + totalcount);
		print.appendRecord("totalsize: " + totalsize);

		it.close();
		assertTrue(true);
	}

	@Test
	public void testListAllSubFolder() throws HSCException {
		listFolder("/sdk-test/moreThan100objs/", namespace2Name);
	}

	private void listFolder(String dir, String namespace) throws HSCException {
		HCPObjectEntrys result = hcpClient.listDirectory(new ListDirectoryRequest(dir).withNamespace(namespace));
		ObjectEntryIterator it = result.iterator();
		List<HCPObjectEntry> list;
		List<HCPObjectEntry> subfolder = new ArrayList<HCPObjectEntry>();
		int c = 10;
		while ((list = it.next(c)) != null) {
			for (int i = 0; i < list.size(); i++) {
				HCPObjectEntry hcpObjectEntry = list.get(i);

				// print.addRow(new Object[] { hcpObjectEntry.getKey(), hcpObjectEntry.getName(),
				// hcpObjectEntry.getType(), hcpObjectEntry.getChangeTime() });
				print.appendRecord(hcpObjectEntry.getKey());

				if (hcpObjectEntry.isDirectory()) {
					// if (!hcpObjectEntry.getName().equals(".lost+found"));
					// hcpClient.deleteDirectory(hcpObjectEntry.getKey());
					// listFolder(hcpObjectEntry.getKey());
					subfolder.add(hcpObjectEntry);
				}
			}
		}

		it.close();

		for (HCPObjectEntry hcpObjectEntry : subfolder) {
			listFolder(hcpObjectEntry.getKey(), namespace);
		}

		// print.printout();

	}

	@Test
	public void testListVersion() throws Exception {
		hcpClient.deleteObject(key1);
		List<PutObjectResult> putlist = new ArrayList<PutObjectResult>();
		for (int i = 0; i < 10; i++) {
			File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
			PutObjectResult resultPut = hcpClient.putObject(key1, tempFile2);
			putlist.add(resultPut);
		}

		PrettyRecordPrinter print = new PrettyRecordPrinter();
//		print.setPadTab(8);
		// print.setColumnSeparator("\t");
//		print.disableLeftBorder();
//		print.disableRightBorder();
		// print.enableTopBorder('+', '-', '+');
		// print.enableBottomBorder('\\', '-', '/');

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
			print.printout();
		}
		print.appendRecord("totalcount: " + totalcount);

		it.close();
		assertTrue(true);

	}

	@Test
	public void testCreateObjWithVerify() throws Exception {
		// ----------------------------------------------------------------------------------------
		File tempFile = TestDataFactory.createTestFile(ContentType.TXT_5MB);
		String localDigest = DigestUtils.format2Hex(DigestUtils.calcSHA256(tempFile));
		String localDigestMd5 = DigestUtils.format2Hex(DigestUtils.calcMD5(tempFile)).toLowerCase();
		PutObjectRequest request = new PutObjectRequest(key1);
		request.withContent(tempFile);
		// request.withContentVerify(HashAlgorithm.MD5);
		CalcTime time = new CalcTime();
		time.start();
		PutObjectResult resultPut = hcpClient.putObject(request);
		time.end();
		time.print();
		// ----------------------------------------------------------------------------------------
		print.appendRecord("Put Object... " + key1);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("LocalDigest=" + localDigest);
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------

		assertTrue(resultPut.getContentHash().equals(localDigest));
		assertTrue(resultPut.getETag().equals(localDigestMd5));
	}

	@Test
	public void testMultiVersionObj() throws Exception {
		// ----------------------------------------------------------------------------------------
		PutObjectResult resultPUT = null;
		File tempFile1 = TestDataFactory.createTestFile(ContentType.TIME);
		resultPUT = hcpClient.putObject(key1, tempFile1);
		String vid = resultPUT.getVersionId();
		String content1 = TestDataFactory.getContent(tempFile1);

		File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
		resultPUT = hcpClient.putObject(key1, tempFile2);

		// ----------------------------------------------------------------------------------------
		HCPObject result = hcpClient.getObject(key1, vid);
		String resultContent = StreamUtils.inputStreamToString(result.getContent(), true);

		// ----------------------------------------------------------------------------------------
		print.appendRecord("Location=" + key1);
		print.appendRecord("ContentHash=" + result.getContentHash());
		print.appendRecord("VersionId=" + vid);
		// print.addRow("VersionId=" + result.getVersionId());
		print.appendRecord("OriginalContent=" + content1);
		print.appendRecord("RetrievedContent=" + resultContent);

		// ----------------------------------------------------------------------------------------
		assertTrue(resultContent.equals(content1));
	}

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

	@Test
	public void testDefaultMetadata() throws Exception {
		File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
		String key = key1;// basedir + tempFile2.getName();

		Date dt = Calendar.getInstance().getTime();
		S3CompatibleMetadata metadata = new S3CompatibleMetadata();
		metadata.put("k1", "TEST_CONTENTS\t ");
		metadata.put("k2", "!@#$%^&*()_+{}:\">?<|1234567890-=qwertyuiop[]\';lkjhgfdsazxcvbnm,./'");
		metadata.put("k3", dt.toString());
		metadata.put("k4", "中文ディレクトリ디렉토리");
		metadata.put("k5中文", "中文ディレクトリ디렉토리中文ディレクトリ디렉토리");

		PutObjectRequest request = new PutObjectRequest(key).withContent(tempFile2).withMetadata(metadata);
		PutObjectResult result = hcpClient.putObject(request);
		print.appendRecord("Location=" + result.getLocation());

		S3CompatibleMetadata metadata1 = hcpClient.getMetadata(key);
		assertTrue("TEST_CONTENTS\t ".equals(metadata1.get("k1")));
		assertTrue("!@#$%^&*()_+{}:\">?<|1234567890-=qwertyuiop[]\';lkjhgfdsazxcvbnm,./'".equals(metadata1.get("k2")));
		assertTrue(dt.toString().equals(metadata1.get("k3")));
		assertTrue("中文ディレクトリ디렉토리".equals(metadata1.get("k4")));
		assertTrue("中文ディレクトリ디렉토리中文ディレクトリ디렉토리".equals(metadata1.get("k5中文")));

		boolean exist = hcpClient.doesS3MetadataExist(key1);
		assertTrue(exist == true);

		hcpClient.deleteS3Metadata(key1);

		exist = hcpClient.doesS3MetadataExist(key1);
		assertTrue(exist == false);
	}

	@Test
	public void testSystemMetadata() throws Exception {
		File tempFile1 = TestDataFactory.createTestFile(ContentType.TIME);
		PutObjectResult resultPUT = hcpClient.putObject(key2, tempFile1);

		print.appendRecord("Location=" + key2);

		HCPSystemMetadata metadata = new HCPSystemMetadata();
		metadata.setHold(true);

		hcpClient.setSystemMetadata(key2, metadata);

		int statusCode = -1;
		try {
			hcpClient.deleteObject(key2);
		} catch (InvalidResponseException e) {
			statusCode = e.getStatusCode();
		}

		assertTrue(statusCode == 403);

		boolean exist = hcpClient.doesObjectExist(key2);
		assertTrue(exist == true);

		metadata = new HCPSystemMetadata();
		metadata.setHold(false);
		hcpClient.setSystemMetadata(key2, metadata);
		hcpClient.deleteObject(key2);
		exist = hcpClient.doesObjectExist(key2);
		assertTrue(exist == false);

		tempFile1 = TestDataFactory.createTestFile(ContentType.TIME);
		resultPUT = hcpClient.putObject(key3, tempFile1);
		metadata.setIndex(false);
		metadata.setShred(true);
		metadata.setOwner(localUserName2);
		hcpClient.setSystemMetadata(key3, metadata);

		HCPObjectSummary summary = hcpClient.getObjectSummary(key3);
		assertTrue(summary.isIndexed() == false);
		assertTrue(summary.isShred() == true);
		assertTrue(localUserName2.equals(summary.getOwner()));

		// metadata.setIndex(false);
		// metadata.setShred(false);
		// metadata.setOwner(localUserName1);
		// hcpClient.putSystemMetadata(key3, metadata);
		//
		// summary = hcpClient.getObjectSummary(key3);
		// assertTrue(summary.isIndexEnabled() == false);
		// assertTrue(summary.isShredEnabled() == false);
	}

	@Test
	public void testListMetadata() throws Exception {
		testDefaultMetadata();
		testCustomMetadata();
		String key = key1;// basedir + tempFile2.getName();
		HCPMetadataSummarys metadatas = hcpClient.listMetadatas(key);
		Collection<HCPMetadataSummary> list = metadatas.getMetadatas();
		for (HCPMetadataSummary type : list) {
			print.appendRecord("MetadataName=" + type.getName());
			print.appendRecord("ContentType=" + type.getContentType());
			print.appendRecord("Length=" + type.getSize());
			print.appendRecord("HashAlgorithmName=" + type.getHashAlgorithmName());
			print.appendRecord("ContentHash=" + type.getContentHash());
			print.appendRecord("ChangeTime=" + type.getChangeTime());
		}
	}

	// @Test
	// public void testDeletedVersionObj() throws Exception {
	// // TODO Auto-generated method stub
	//
	// }

	@Test
	public void testCustomMetadata() throws Exception {
		File f = TestDataFactory.createTestFile(ContentType.EMPTY);
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<employees>\r\n"
				+ "    <employee id=\"001\">\r\n"
				+ "        <name>cici</name>\r\n"
				+ "        <department>finace</department>\r\n"
				+ "        <supervisor>lily</supervisor>\r\n"
				+ "    </employee>\r\n"
				+ "    <employee id=\"002\">\r\n"
				+ "        <name>alex</name>\r\n"
				+ "        <department>develope</department>\r\n"
				+ "        <supervisor>lily</supervisor>\r\n"
				+ "    </employee>\r\n"
				+ "</employees>";

		String metadataName1 = "test";
		{
			S3CompatibleMetadata metadata = new S3CompatibleMetadata();
			String key2value = new Date().toString();
			metadata.put("key1", "123");
			metadata.put("key2", key2value);

			PutObjectRequest request = new PutObjectRequest(key1);
			request.withContent(f).withMetadata(metadataName1, xml.getBytes()).withMetadata(metadata);
			PutObjectResult result = hcpClient.putObject(request);
			print.appendRecord("Location=" + result.getLocation());
			print.appendRecord("ContentHash=" + result.getContentHash());
			print.appendRecord("ETag=" + result.getETag());
			print.appendRecord("VersionId=" + result.getVersionId());
			print.appendRecord("IngestTimeMilliseconds=" + result.getIngestTime());

			HCPMetadata cm = hcpClient.getMetadata(key1, metadataName1);
			String xml1 = StreamUtils.inputStreamToString(cm.getContent(), true);
			assertTrue(xml.equals(xml1));

			S3CompatibleMetadata metadata1 = hcpClient.getMetadata(key1);
			assertTrue(metadata1.get("key1").equals("123"));
			assertTrue(metadata1.get("key2").equals(key2value));
		}

		String metadataName2 = "test1";
		{
			xml = xml.replace("finace", "qwe");
			PutMetadataRequest request1 = new PutMetadataRequest(key1, metadataName2, xml.getBytes());
			hcpClient.putMetadata(request1);

			HCPMetadata cm1 = hcpClient.getMetadata(key1, metadataName2);
			String xml2 = StreamUtils.inputStreamToString(cm1.getContent(), true);

			assertTrue(xml.equals(xml2));
		}

		{
			HCPMetadataSummarys metadatas = hcpClient.listMetadatas(key1);
			assertTrue(metadatas.get(S3CompatibleMetadata.DEFAULT_METADATA_NAME) != null);
			assertTrue(metadatas.get(metadataName1) != null);
			assertTrue(metadatas.get(metadataName2) != null);
		}

		{
			boolean exist = false;
			exist = hcpClient.doesMetadataExist(key1, metadataName1);
			assertTrue(exist == true);
			exist = hcpClient.doesMetadataExist(key1, metadataName2);
			assertTrue(exist == true);
			exist = hcpClient.doesS3MetadataExist(key1);
			assertTrue(exist == true);

			hcpClient.deleteMetadata(key1, metadataName1);
			hcpClient.deleteMetadata(key1, metadataName2);
			hcpClient.deleteS3Metadata(key1);

			exist = hcpClient.doesMetadataExist(key1, metadataName1);
			assertTrue(exist == false);
			exist = hcpClient.doesMetadataExist(key1, metadataName2);
			assertTrue(exist == false);
			exist = hcpClient.doesS3MetadataExist(key1);
			assertTrue(exist == false);
		}
	}

	@Test
	public void testDir() throws Exception {
		String dir = "folder_test/new_folder_" + Calendar.getInstance().getTimeInMillis();
		hcpClient.createDirectory(dir);
		boolean exist = hcpClient.doesDirectoryExist(dir);

		print.appendRecord("Created=" + dir);
		print.appendRecord("Exist=" + exist);
		assertTrue(exist == true);

		hcpClient.deleteDirectory(new DeleteDirectoryRequest(dir).withDeleteContainedObjects(true));
		exist = hcpClient.doesObjectExist(dir);
		print.appendRecord("Deleted=" + dir);
		print.appendRecord("Exist=" + exist);
		assertTrue(exist == false);
	}

	@Test
	public void testDirCN() throws Exception {
		String dir = "folder_test/new_folder_" + Calendar.getInstance().getTimeInMillis() + "中文ディレクトリ디렉토리";
		// dir = URLEncoder.encode(dir,"utf-8");
		hcpClient.createDirectory(dir);
		boolean exist = hcpClient.doesDirectoryExist(dir);

		print.appendRecord("Created=" + dir);
		print.appendRecord("Exist=" + exist);
		assertTrue(exist == true);

		hcpClient.deleteDirectory(new DeleteDirectoryRequest(dir).withDeleteContainedObjects(true));
		exist = hcpClient.doesObjectExist(dir);
		print.appendRecord("Deleted=" + dir);
		print.appendRecord("Exist=" + exist);
		assertTrue(exist == false);
	}

	@Test
	public void testX() throws NoSuchAlgorithmException {
		MessageDigest.getInstance("SHA-384");
		MessageDigest.getInstance("SHA-512");
		MessageDigest.getInstance("SHA-1");
		// MessageDigest.getInstance("RIPEMD160");

	}

	@Test
	public void testMultipartUpload() throws Exception {
		String key = key9;
		String uploadId = null;
		MultipartUploadRequest request = new MultipartUploadRequest(key9);
		MultipartUpload api = hcpClient.getMultipartUpload(request);
		uploadId = api.initiate();
		print.appendRecord("key=" + key);
		print.appendRecord("uploadId=" + uploadId);

		List<PartETag> partETags = new ArrayList<PartETag>();

		File file = TestDataFactory.createTestFile(ContentType.TXT_50MB);
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

	@Test
	public void testretentionj() throws Exception {
		File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
		String content2 = TestDataFactory.getContent(tempFile2);

		// ----------------------------------------------------------------------------------------
		PutObjectRequest req = new PutObjectRequest("06e/test.txt");
		req.withNamespace("space1").withContent(tempFile2);
		PutObjectResult resultPut = hcpClient.putObject(req);

		print.appendRecord("Put Object... " + key1);
		print.appendRecord("OriginalContent=" + content2);
		print.appendRecord("Location=" + resultPut.getLocation());
		print.appendRecord("HashAlgorithmName=" + resultPut.getHashAlgorithmName());
		print.appendRecord("ContentHash=" + resultPut.getContentHash());
		print.appendRecord("ETag=" + resultPut.getETag());
		print.appendRecord("VersionId=" + resultPut.getVersionId());
		print.appendRecord("IngestTimeMilliseconds=" + resultPut.getIngestTime());

		// ----------------------------------------------------------------------------------------
		HCPObject result = hcpClient.getObject(key1);
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

	@Test
	public void testRetentionCustomMetadata() throws Exception {
		File f = TestDataFactory.createTestFile(ContentType.EMPTY);
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<employees>\r\n"
				+ "    <employee id=\"001\">\r\n"
				+ "        <name>cici</name>\r\n"
				+ "        <department>finace</department>\r\n"
				+ "        <supervisor>lily</supervisor>\r\n"
				+ "    </employee>\r\n"
				+ "    <employee id=\"002\">\r\n"
				+ "        <name>alex</name>\r\n"
				+ "        <department>develope</department>\r\n"
				+ "        <supervisor>lily</supervisor>\r\n"
				+ "    </employee>\r\n"
				+ "</employees>";

		String metadataName1 = "test";
		{
			S3CompatibleMetadata metadata = new S3CompatibleMetadata();
			String key2value = new Date().toString();
			metadata.put("key-1", "123");
			metadata.put("key2", key2value);

			PutObjectRequest request = new PutObjectRequest("06e/test.txt");
			request.withContent(f).withMetadata(metadataName1, xml.getBytes()).withMetadata(metadata);
			PutObjectResult result = hcpClient.putObject(request);
			print.appendRecord("Location=" + result.getLocation());
			print.appendRecord("ContentHash=" + result.getContentHash());
			print.appendRecord("ETag=" + result.getETag());
			print.appendRecord("VersionId=" + result.getVersionId());
			print.appendRecord("IngestTimeMilliseconds=" + result.getIngestTime());

			HCPMetadata cm = hcpClient.getMetadata("06e/test.txt", metadataName1);
			String xml1 = StreamUtils.inputStreamToString(cm.getContent(), true);
			assertTrue(xml.equals(xml1));

			S3CompatibleMetadata metadata1 = hcpClient.getMetadata("06e/test.txt");
			assertTrue(metadata1.get("key-1").equals("123"));
			assertTrue(metadata1.get("key2").equals(key2value));
		}

		String metadataName2 = "test1";
		{
			xml = xml.replace("finace", "qwe");
			PutMetadataRequest request1 = new PutMetadataRequest("06e/test.txt", metadataName2, xml.getBytes());
			hcpClient.putMetadata(request1);

			HCPMetadata cm1 = hcpClient.getMetadata("06e/test.txt", metadataName2);
			String xml2 = StreamUtils.inputStreamToString(cm1.getContent(), true);

			assertTrue(xml.equals(xml2));
		}

		{
			HCPMetadataSummarys metadatas = hcpClient.listMetadatas("06e/test.txt");
			assertTrue(metadatas.get(S3CompatibleMetadata.DEFAULT_METADATA_NAME) != null);
			assertTrue(metadatas.get(metadataName1) != null);
			assertTrue(metadatas.get(metadataName2) != null);
		}

		{
			boolean exist = false;
			exist = hcpClient.doesMetadataExist("06e/test.txt", metadataName1);
			assertTrue(exist == true);
			exist = hcpClient.doesMetadataExist("06e/test.txt", metadataName2);
			assertTrue(exist == true);
			exist = hcpClient.doesS3MetadataExist("06e/test.txt");
			assertTrue(exist == true);

			hcpClient.deleteMetadata("06e/test.txt", metadataName1);
			hcpClient.deleteMetadata("06e/test.txt", metadataName2);
			hcpClient.deleteS3Metadata("06e/test.txt");

			exist = hcpClient.doesMetadataExist("06e/test.txt", metadataName1);
			assertTrue(exist == false);
			exist = hcpClient.doesMetadataExist("06e/test.txt", metadataName2);
			assertTrue(exist == false);
			exist = hcpClient.doesS3MetadataExist("06e/test.txt");
			assertTrue(exist == false);
		}
	}
}