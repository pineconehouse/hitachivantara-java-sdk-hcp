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

import java.io.PrintStream;
import java.util.UUID;

import com.amituofo.common.api.ProgressListener;
import com.amituofo.common.define.ReadProgressEvent;

/**
 * 处理单个分片上传的线程
 * 
 * @author sohan
 *
 */
public class UploadRunnable implements Runnable {
	private final String ID = UUID.randomUUID().toString().replaceAll("-", "");
	private final PrintStream log = System.out;

	final private S3MultipartUploader uploader;
	final private PartData partData;
	final private ProgressListener<ReadProgressEvent, Integer> progressListener;
	
	public UploadRunnable(S3MultipartUploader uploader, PartData partData, ProgressListener<ReadProgressEvent, Integer> progressListener) {
		this.uploader = uploader;
		this.partData = partData;
		this.progressListener = progressListener;
	}

	public PartData getPartData() {
		return partData;
	}

	@Override
	public void run() {
		try {
			if (partData != null) {
				// 上传分片数据数据
				uploader.upload(partData.getIndex(), partData.getInputStream(), partData.getSize(), " by " + ID, progressListener);
				partData.close();
			}
		} catch (Exception e) {
			if (partData != null) {
				partData.close();
			}

			log.println("Error occured when get part data. Index:" + partData.getIndex());
			e.printStackTrace();
			return;
		}

	}
}