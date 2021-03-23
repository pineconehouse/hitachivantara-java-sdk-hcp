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

public class MulitipartUploadException extends Exception {
	private Stage stage;
	private String namespace;
	private String key;
	private String uploadId;
	private int partNumber;
	private long uploadPartsize;

	public static enum Stage {
		INIT, PART_UPLOAD, COMPLETE
	}

	public MulitipartUploadException(Stage stage, String namespace, String key, String uploadId, int partNumber, long uploadPartsize, String message, Throwable cause) {
		super(message, cause);
		this.stage = stage;
		this.namespace = namespace;
		this.key = key;
		this.uploadId = uploadId;
		this.partNumber = partNumber;
		this.uploadPartsize = uploadPartsize;
	}

	public Stage getStage() {
		return stage;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getKey() {
		return key;
	}

	public String getUploadId() {
		return uploadId;
	}

	public int getPartNumber() {
		return partNumber;
	}

	public long getUploadPartsize() {
		return uploadPartsize;
	}

}
