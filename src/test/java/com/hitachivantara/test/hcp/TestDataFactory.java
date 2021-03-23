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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.kit.io.SimpleFileReader;
import com.amituofo.common.kit.io.SimpleFileWriter;
import com.amituofo.common.util.RandomUtils;
import com.hitachivantara.hcp.standard.api.MultipartUpload;
import com.hitachivantara.hcp.standard.model.PartETag;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartUploadRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutS3MetadataRequest;

public class TestDataFactory extends TestHCPBase {
	private static TestDataFactory instance = new TestDataFactory();
	private static final List<File> tempFiles = new ArrayList<File>();

	private TestDataFactory() {
		try {
			testInitBeforeEach();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void testFinalAfterEach() {
		super.testFinalAfterEach();

		for (File file : tempFiles) {
			if (file.exists()) {
				file.delete();
			}
		}
		tempFiles.clear();
	}

	public static TestDataFactory getInstance() {
		return instance;
	}

	public String prepareCustomMetadataForObject(String key, String namespace, String metadataName) throws HSCException, IOException {
		File metadatafile = this.createTestFile(ContentType.XML);
		PutMetadataRequest request = new PutMetadataRequest(key).withNamespace(namespace).withMetadataName(metadataName).withContent(metadatafile, false);
		hcpClient.putMetadata(request);

		return this.getContent(metadatafile);
	}

	public String prepareCustomMetadataForObject(String key, String metadataName) throws HSCException, IOException {
		File metadatafile = this.createTestFile(ContentType.XML);
		PutMetadataRequest request = new PutMetadataRequest(key).withMetadataName(metadataName).withContent(metadatafile, false);
		hcpClient.putMetadata(request);

		return this.getContent(metadatafile);
	}

	public S3CompatibleMetadata prepareSimpleMetadataForObject(String key) throws HSCException, IOException {
		S3CompatibleMetadata metadata = createTestSimpleMetadata();
		hcpClient.putMetadata(key, metadata);

		return metadata;
	}

	public S3CompatibleMetadata prepareSimpleMetadataForObject(String key, String namespace) throws HSCException, IOException {
		S3CompatibleMetadata metadata = createTestSimpleMetadata();
		PutS3MetadataRequest request = new PutS3MetadataRequest(key, metadata).withNamespace(namespace);
		hcpClient.putMetadata(request);

		return metadata;
	}

	public String prepareSmallObject(String key) throws HSCException, IOException {
		return prepareSmallObject(key, super.namespace1Name, ContentType.TIME);
	}

	public String prepareSmallObjectWithS3Metadata(String key, S3CompatibleMetadata metadata) throws HSCException, IOException {
		return prepareSmallObjectWithS3Metadata(key, super.namespace1Name, metadata);
	}

	public String prepareSmallObject(String key, String namespace) throws HSCException, IOException {
		return prepareSmallObject(key, namespace, ContentType.TIME);
	}

	public String prepareSmallObjectWithS3Metadata(String key, String namespace, S3CompatibleMetadata metadata) throws HSCException, IOException {
		File tempFile2 = this.createTestFile(ContentType.TIME);
		String content = this.getContent(tempFile2);

		PutObjectRequest putrequest = new PutObjectRequest(key);
		putrequest.withNamespace(namespace).withContent(tempFile2).withMetadata(metadata);
		PutObjectResult result = hcpClient.putObject(putrequest);
		tempFile2.delete();

		return content;
	}

	public String prepareEmptyObject(String key) throws HSCException, IOException {
		return prepareSmallObject(key, super.namespace1Name, ContentType.EMPTY);
	}

	public String prepareEmptyObject(String key, String namespace) throws HSCException, IOException {
		return prepareSmallObject(key, namespace, ContentType.EMPTY);
	}

	public String prepareSmallObject(String key, ContentType ct) throws HSCException, IOException {
		return prepareSmallObject(key, super.namespace1Name, ct);
	}

	public String prepareSmallObject(String key, String namespace, ContentType ct) throws HSCException, IOException {
		File tempFile2 = this.createTestFile(ct);
		String content = this.getContent(tempFile2);

		PutObjectRequest putrequest = new PutObjectRequest(key);
		putrequest.withNamespace(namespace).withContent(tempFile2);
		PutObjectResult result = hcpClient.putObject(putrequest);

		return content;
	}

	public List<String> prepare10VersionsOfObject(String key) throws HSCException, IOException {
		return prepare10VersionsOfObject(key, namespace1Name);
	}

	public List<String> prepare10VersionsOfObject(String key, String namespace) throws HSCException, IOException {
		List<String> hash = new ArrayList<String>();
		hcpClient.deleteObject(new DeleteObjectRequest(key).withNamespace(namespace));
		List<PutObjectResult> putlist = new ArrayList<PutObjectResult>();
		for (int i = 0; i < 10; i++) {
//			File tempFile2 = TestDataFactory.createTestFile(ContentType.TXT_RANDOM);
			InputStream in = TestDataFactory.createTestInputStream(ContentType.TXT_RANDOM);
			PutObjectResult resultPut = hcpClient.putObject(new PutObjectRequest(key).withNamespace(namespace).withContent(in));
			in.close();
//			tempFile2.delete();

			hash.add(resultPut.getContentHash());

			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return hash;
	}

	public List<String> prepare10VersionsOfObjectWithCustomMetaAndS3Meta(String key, String namespace, String metadata1) throws HSCException, IOException {
		List<String> hash = new ArrayList<String>();
		hcpClient.deleteObject(new DeleteObjectRequest(key).withNamespace(namespace));
		List<PutObjectResult> putlist = new ArrayList<PutObjectResult>();
		for (int i = 0; i < 10; i++) {
			File tempFile2 = TestDataFactory.createTestFile(ContentType.TIME);
			String metadataContent = "<content>" + RandomUtils.randomString(RandomUtils.randomInt(10, 1000)) + "</content>";
			PutObjectResult resultPut = hcpClient.putObject(new PutObjectRequest(key).withNamespace(namespace).withContent(tempFile2).withMetadata(createTestSimpleMetadata())
					.withMetadata(metadata1, metadataContent, "utf-8"));
			tempFile2.delete();

			hash.add(resultPut.getContentHash());
		}

		return hash;
	}

	public int prepareObjsDir(int count, String dirKey, String namespace) throws Exception {
		int maxc = count/3;
		for (int i = 0; i < maxc; i++) {
			File tempFile = this.createTestFile(ContentType.TIME);
			String key = dirKey + tempFile.getName();
			String content = this.getContent(tempFile);
			// ---------------------------------------------------------------------------------------
			PutObjectRequest putrequest = new PutObjectRequest(key);
			putrequest.withNamespace(namespace).withContent(tempFile);
			PutObjectResult result = hcpClient.putObject(putrequest);
			// ----------------------------------------------------------------------------------------
		}

		maxc-=maxc;
		for (int i = 0; i < maxc; i++) {
			File tempFile = this.createTestFile(ContentType.TIME);
			String key = dirKey + RandomUtils.randomInt(10, 15) + "/" + tempFile.getName();
			String content = this.getContent(tempFile);
			// ----------------------------------------------------------------------------------------
			PutObjectRequest putrequest = new PutObjectRequest(key);
			putrequest.withNamespace(namespace).withContent(tempFile);
			PutObjectResult result = hcpClient.putObject(putrequest);
			// ----------------------------------------------------------------------------------------
		}

		maxc-=maxc;
		for (int i = 0; i < maxc; i++) {
			File tempFile = this.createTestFile(ContentType.TIME);
			String key = dirKey + RandomUtils.randomInt(10, 15) + "/" + RandomUtils.randomString(2) + "/" + tempFile.getName();
			String content = this.getContent(tempFile);
			// ----------------------------------------------------------------------------------------
			PutObjectRequest putrequest = new PutObjectRequest(key);
			putrequest.withNamespace(namespace).withContent(tempFile);
			PutObjectResult result = hcpClient.putObject(putrequest);
			// ----------------------------------------------------------------------------------------
		}

		return count;
	}
	
	public int prepare100MoreObjDir(String dirKey, String namespace) throws Exception {
		return prepareObjsDir(100, dirKey, namespace);
	}

	public int prepare100MoreObjWithMetadataAndVersionsDir(String dirKey, String namespace) throws Exception {
		int count = 20;
		for (int i = 0; i < count; i++) {
			File tempFile = this.createTestFile(ContentType.TIME);
			String key = dirKey + tempFile.getName();
			// ---------------------------------------------------------------------------------------
			String metadata1 = RandomUtils.randomString(10);
			this.prepare10VersionsOfObjectWithCustomMetaAndS3Meta(key, namespace, metadata1);
			// ----------------------------------------------------------------------------------------
		}

		for (int i = 0; i < count; i++) {
			File tempFile = this.createTestFile(ContentType.TIME);
			String key = dirKey + RandomUtils.randomInt(10, 15) + "/" + tempFile.getName();
			// ----------------------------------------------------------------------------------------
			String metadata1 = RandomUtils.randomString(10);
			this.prepare10VersionsOfObjectWithCustomMetaAndS3Meta(key, namespace, metadata1);
			// ----------------------------------------------------------------------------------------
		}

		for (int i = 0; i < count; i++) {
			File tempFile = this.createTestFile(ContentType.TIME);
			String key = dirKey + RandomUtils.randomInt(10, 15) + "/" + RandomUtils.randomString(2) + "/" + tempFile.getName();
			// ----------------------------------------------------------------------------------------
			String metadata1 = RandomUtils.randomString(10);
			this.prepare10VersionsOfObjectWithCustomMetaAndS3Meta(key, namespace, metadata1);
			// ----------------------------------------------------------------------------------------
		}

		return count * 3;
	}

	public String prepareUncompleteMultipartUploadObj(String key, List<PartETag> existPartETags) throws HSCException, IOException {
		String uploadId = null;
		MultipartUploadRequest request = new MultipartUploadRequest(key);
		final MultipartUpload api = hcpClient.getMultipartUpload(request);
		uploadId = api.initiate();

		final List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());

		final File file = TestDataFactory.createTestFile(ContentType.TXT_100MB);// new File(localBigFile1);//

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

		int index = 1;
		final Queue<long[]> parts = new LinkedList<long[]>();
		while (remainLength > 0) {
			long uploadLength = Math.min(remainLength, partLength);

			parts.add(new long[] { index++, startOffset, uploadLength });

			startOffset += (uploadLength + 0);
			remainLength -= uploadLength;
		}
		final int partsSize = parts.size();

		for (int i = 0; i < partsSize; i++) {
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

							PartETag etag = api.uploadPart((int) part[0], in, part[2]).getPartETag();
							console.println("etag=" + etag.getPartNumber() + " " + etag.getETag());
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
				}

			}// .run();
			).start();
		}

		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		} while (partETags.size() != partsSize);

		if (existPartETags != null) {
			existPartETags.addAll(partETags);
		}

		return uploadId;
	}

	public static File createTestFile(ContentType ct) {
		try {
			String content = null;
			if (ct == ContentType.TIME) {
				Date dt = Calendar.getInstance().getTime();
				content = dt.toString() + " - " + dt.getTime() + "-" + RandomUtils.randomInt(10000, 99999); //$NON-NLS-1$
			} else if (ct == ContentType.TXT_100MB) {
				content = getRandomString(100 * 1024 * 1024);
			} else if (ct == ContentType.TXT_50MB) {
				content = getRandomString(50 * 1024 * 1024);
			} else if (ct == ContentType.TXT_5MB) {
				content = getRandomString(5 * 1024 * 1024);
			} else if (ct == ContentType.TXT_10MB) {
				content = getRandomString(10 * 1024 * 1024);
			} else if (ct == ContentType.TXT_RANDOM) {
				content = getRandomString(RandomUtils.randomInt(1024 * 10, 2024 * 50));
			} else if (ct == ContentType.EMPTY) {
				content = ""; //$NON-NLS-1$
			} else if (ct == ContentType.XML) {
				content = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>   "
						+ "<!--  Copyright w3school.com.cn -->           "
						+ "<note>                                        "
						+ "	<to>"
						+ getRandomString(100)
						+ "</to>          "
						+ "	<from>"
						+ getRandomString(50)
						+ "</from>      "
						+ "	<heading>"
						+ Calendar.getInstance().getTime().toString()
						+ "</heading>                "
						+ "	<body>Don't forget the meeting!</body>     "
						+ "</note>                                       ";
			}

			File tf = File.createTempFile("TestHCP", ".txt"); //$NON-NLS-1$ //$NON-NLS-2$
			tempFiles.add(tf);
			SimpleFileWriter w = new SimpleFileWriter(tf);
			w.createWriter();
			w.write(content);
			w.closeWriter();

			return tf;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
	}

	public static S3CompatibleMetadata createTestSimpleMetadata() {
		S3CompatibleMetadata metadata = new S3CompatibleMetadata();

		metadata.put("key1", new Date().toString());
		metadata.put("key2", getRandomString(50));
		metadata.put("key2", "简体中文，繁体中文，English. 注目の商品をチェック， الصينية المبسطة ，на китайски， упрощенный китайский язык，Απλοποιημένα κινέζικα");
		metadata.put("author", "SDK Powered by Rison Han");

		return metadata;
	}

	public static InputStream createTestInputStream(ContentType ct) {
		Date dt = Calendar.getInstance().getTime();
		String content = null;
		if (ct == ContentType.TIME) {
			dt = Calendar.getInstance().getTime();
			content = dt.toString() + " - " + dt.getTime(); //$NON-NLS-1$
		} else if (ct == ContentType.TXT_100MB) {
			content = getRandomString(100 * 1024 * 1024);
		} else if (ct == ContentType.TXT_50MB) {
			content = getRandomString(50 * 1024 * 1024);
		} else if (ct == ContentType.TXT_5MB) {
			content = getRandomString(5 * 1024 * 1024);
		} else if (ct == ContentType.TXT_10MB) {
			content = getRandomString(10 * 1024 * 1024);
		} else if (ct == ContentType.TXT_RANDOM) {
			content = getRandomString(RandomUtils.randomInt(1024 * 10, 2024 * 50));
		} else if (ct == ContentType.EMPTY) {
			content = ""; //$NON-NLS-1$
		}
		ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
		return in;
	}

	public static String getContent(File file) {
		try {
			SimpleFileReader r = new SimpleFileReader(file);
			String content = r.readAll().toString();
			r.closeReader();
			return content;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static String getRandomName(int length) {
		// 定义一个字符串（A-Z，a-z，0-9）即62位；
		String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890"; //$NON-NLS-1$
		// 由Random生成随机数
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		// 长度为几就循环几次
		for (int i = 0; i < length; ++i) {
			// 产生0-61的数字
			int number = random.nextInt(str.length());
			// 将产生的数字通过length次承载到sb中
			sb.append(str.charAt(number));
		}
		// 将承载的字符转换成字符串
		return sb.toString();
	}

	public static String getRandomString(int length) {
		// 定义一个字符串（A-Z，a-z，0-9）即62位；
		String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890\r\n \t"; //$NON-NLS-1$
		// 由Random生成随机数
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		// 长度为几就循环几次
		for (int i = 0; i < length; ++i) {
			// 产生0-61的数字
			int number = random.nextInt(str.length());
			// 将产生的数字通过length次承载到sb中
			sb.append(str.charAt(number));
		}
		// 将承载的字符转换成字符串
		return sb.toString();
	}

	public static enum ContentType {
		TIME, FIXED, TXT_50MB, TXT_5MB, TXT_10MB, TXT_100MB, EMPTY, XML, TXT_RANDOM
	}
}
