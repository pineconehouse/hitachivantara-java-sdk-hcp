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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.api.ProgressListener;
import com.amituofo.common.define.ReadProgressEvent;
import com.amituofo.common.ex.HSCException;
import com.amituofo.common.kit.thread.ExecuteHandler;
import com.amituofo.common.kit.thread.ThreadExecutor;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.MultipartUpload;

public class MulitipartUploaderExecutor {
	private final S3MultipartUploader uploader;
	private final FilePartDataProvider provider;
	private List<PartData> partDatas = new ArrayList<PartData>();

	public MulitipartUploaderExecutor(MultipartUpload s3Client, String objectPath, File uploadFile, long partSize) throws HSCException, IOException {
		this(s3Client, objectPath, uploadFile, partSize, null);
	}

	public MulitipartUploaderExecutor(MultipartUpload s3Client, String objectPath, File uploadFile, long partSize, String uploadId) throws HSCException, IOException {
		this.provider = new FilePartDataProvider(uploadFile, partSize);
		this.uploader = new S3MultipartUploader(s3Client, objectPath, provider.getFileSize(), uploadId);
	}

	/**
	 * 多线程上传
	 * 
	 * @param threadCount
	 * @throws HSCException
	 * @throws InvalidResponseException
	 */
	public void multiThreadUpload(final int threadCount,
			UploadEventHandler handler,
			ProgressListener<ReadProgressEvent, Integer> progressListener) throws InvalidResponseException, HSCException {
		uploader.setHandler(handler);

		uploader.init();

		// 多线程上传-线程池
		ThreadExecutor TP = new ThreadExecutor(threadCount);

		TP.setExecuteHandler(new ExecuteHandler() {

			@Override
			public void finalExecute() {
				try {
					uploader.complete();
				} catch (Exception e) {
				}
			}
		});

		final int count = provider.getTotalPartCount();// Math.min(provider.getTotalPartCount(), threadCount);

		for (int i = 0; i < count; i++) {
			PartData partData = provider.nextPartData();
			partDatas.add(partData);

			TP.execute(new UploadRunnable(uploader, partData, progressListener));
		}

		TP.seal();
		TP.waitForComplete();

		// uploader.complete();
	}

	/**
	 * 单独上传某一块
	 * 
	 * @param partNumber
	 * @throws HSCException
	 */
	public void uploadPart(final int partNumber, ProgressListener<ReadProgressEvent, Integer> progressListener) throws HSCException {
		PartData partData = null;
		try {
			partData = provider.partData(partNumber);

			if (partData != null) {
				uploader.upload(partData.getIndex(), partData.getInputStream(), partData.getSize(), "", progressListener);
			}
		} finally {
			if (partData != null) {
				partData.close();
			}
		}
	}

	public void abortMultipartUpload() throws InvalidResponseException, HSCException {
		if (partDatas.size() > 0) {
			for (PartData partData : partDatas) {
				partData.close();
			}

			uploader.abortMultipartUpload();
			
			partDatas.clear();
		}
	}

	// public void complete() throws InvalidResponseException, HSCException {
	// uploader.complete();
	// }

}
