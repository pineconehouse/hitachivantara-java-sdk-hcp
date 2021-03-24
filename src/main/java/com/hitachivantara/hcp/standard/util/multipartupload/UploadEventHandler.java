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

import com.hitachivantara.hcp.standard.util.multipartupload.MulitipartUploadException.Stage;

public interface UploadEventHandler {
	// void partUploaded(int partNumber, long size, long startTime, long endTime);

	// void complete(String key, long size, long startTime, long endTime);

	void init(String namespace, String key, String uploadId);

	void beforePartUpload(String namespace, String key, String uploadId, int partNumber, long uploadPartsize, long startTime);

	// void caughtPartUploadException(String namespace, String key, String uploadId, int partNumber, long uploadPartsize, Exception e);

	void afterPartUpload(String namespace, String key, String uploadId, int partNumber, long uploadPartsize, long startTime, long endTime);

	void complete(String namespace, String key, String uploadId, Long uploadedSize, long startTime, long endTime);

	void caughtException(Stage stage, MulitipartUploadException e);
}