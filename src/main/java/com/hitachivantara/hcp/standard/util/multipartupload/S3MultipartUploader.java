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
package com.hitachivantara.hcp.standard.util.multipartupload;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.api.ProgressListener;
import com.amituofo.common.define.ReadProgressEvent;
import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.MultipartUpload;
import com.hitachivantara.hcp.standard.model.PartETag;
import com.hitachivantara.hcp.standard.model.request.impl.UploadPartRequest;
import com.hitachivantara.hcp.standard.util.multipartupload.MulitipartUploadException.Stage;

/**
 * 封装S3分片上传功能
 * 
 * @author sohan
 *
 */
public class S3MultipartUploader {

	private final MultipartUpload hs3Client;
	private final String namespaceName;
	private final String objectPath;
	private final long expectObjectSize;
	private Long uploadedSize = new Long(0);

	private String uploadId = null;
	private final List<PartETag> partETags = new ArrayList<PartETag>();

	private UploadEventHandler handler = null;

	private long startTime;
	private long endTime;

	public S3MultipartUploader(MultipartUpload hs3Client, String objectPath, long expectObjectSize) {
		this(hs3Client, objectPath, expectObjectSize, null);
	}

	public S3MultipartUploader(MultipartUpload hs3Client, String objectPath, long expectObjectSize, String uploadId) {
		this.hs3Client = hs3Client;
		this.namespaceName = hs3Client.getNamespace();
		this.objectPath = objectPath;
		this.expectObjectSize = expectObjectSize;
		this.uploadId = uploadId;
	}

	public void setHandler(UploadEventHandler handler) {
		this.handler = handler;
	}

	public String init() throws InvalidResponseException, HSCException {
		startTime = System.currentTimeMillis();

		// Step 1: Initialize.
		if (uploadId == null) {

			// InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(namespaceName, objectPath);
			// InitiateMultipartUploadResult initResponse = hs3Client.initiateMultipartUpload(initRequest);
//			try {
				uploadId = hs3Client.initiate();
//			} catch (Exception e) {
//				MulitipartUploadException me = new MulitipartUploadException(Stage.INIT, namespaceName, objectPath, null, -1, -1, "", e);
//				handler.caughtException(Stage.INIT, me);
//				return uploadId;
//			}

			if (handler != null) {
				handler.init(namespaceName, objectPath, uploadId);
			}
		}

		return uploadId;
	}

	public PartETag upload(int partNumber, InputStream in, long uploadPartsize, String executor, ProgressListener<ReadProgressEvent, Integer> progressListener) {
		// Create request to upload a part.
		UploadPartRequest uploadRequest = new UploadPartRequest(partNumber, in, uploadPartsize);
		// uploadRequest.withKey(objectPath);
		// uploadRequest.withUploadId(uploadId);
		// uploadRequest.withPartNumber(partNumber);
		// uploadRequest.withInputStream(in);
		// uploadRequest.withPartSize(uploadPartsize);
		uploadRequest.withProgressListener(progressListener);

		long startTime, endTime = 0;
		startTime = System.currentTimeMillis();
		if (handler != null) {
			handler.beforePartUpload(namespaceName, objectPath, uploadId, partNumber, uploadPartsize, startTime);
		}

		PartETag result = null;
		try {
			// 上传分片
			result = hs3Client.uploadPart(uploadRequest).getPartETag();
			endTime = System.currentTimeMillis();
			partETags.add(result);

			if (handler != null) {
				handler.afterPartUpload(namespaceName, objectPath, uploadId, partNumber, uploadPartsize, startTime, endTime);
			}

			synchronized (uploadedSize) {
				uploadedSize += uploadPartsize;
			}

		} catch (Exception e) {
			endTime = System.currentTimeMillis();

			if (handler != null) {
				MulitipartUploadException me = new MulitipartUploadException(Stage.PART_UPLOAD, namespaceName, objectPath, uploadId, partNumber, uploadPartsize, "Failed to upload part data.", e);
				handler.caughtException(Stage.PART_UPLOAD, me);
			}
		}

		return result;
	}

	public void complete() throws InvalidResponseException, HSCException {
		// Step 3: Complete.
		if (uploadedSize != expectObjectSize) {
			MulitipartUploadException me = new MulitipartUploadException(Stage.COMPLETE, namespaceName, objectPath, uploadId, -1, -1, "Uncompleted! " + uploadedSize + "/" + expectObjectSize, null);
			handler.caughtException(Stage.COMPLETE, me);
		}

		// 合并各个分片为一个整体文件
//		try {
			hs3Client.complete(partETags);
//		} catch (Exception e) {
//			MulitipartUploadException me = new MulitipartUploadException(Stage.COMPLETE, namespaceName, objectPath, uploadId, -1, -1, "Failed to complete.", null);
//			handler.caughtException(Stage.COMPLETE, me);
//		}

		endTime = System.currentTimeMillis();

		if (handler != null) {
			handler.complete(namespaceName, objectPath, uploadId, uploadedSize, startTime, endTime);
		}
	}

	public void abortMultipartUpload() throws InvalidResponseException, HSCException {
		// 清除分片上传的数据（当希望清除失败的分片上传任务时才调用此函数）
		hs3Client.abort();
		uploadId = null;
	}

	public String getUploadId() {
		return uploadId;
	}

	public List<PartETag> getPartETags() {
		return partETags;
	}

}