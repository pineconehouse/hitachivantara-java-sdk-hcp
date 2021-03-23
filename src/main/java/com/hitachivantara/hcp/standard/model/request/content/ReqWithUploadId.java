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
package com.hitachivantara.hcp.standard.model.request.content;

public interface ReqWithUploadId<T> {

	/**
	 * You use this ID in conjunction with the object name to identify the multipart upload in:
	 * <p>
	 * •Requests to upload parts for the multipart upload
	 * <p>
	 * •Requests to upload parts for the multipart upload by copying
	 * <p>
	 * •Requests to complete the multipart upload
	 * <p>
	 * •Requests to abort the multipart upload
	 * <p>
	 * •Requests to list the parts that have been uploaded for the multipart upload
	 * <p>
	 * •Responses to requests to list in-progress multipart uploads
	 * 
	 * @param uploadId
	 * @return
	 */
	T withUploadId(String uploadId);

	String getUploadId();
}
