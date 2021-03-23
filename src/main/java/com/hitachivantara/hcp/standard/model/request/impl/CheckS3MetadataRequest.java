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
package com.hitachivantara.hcp.standard.model.request.impl;

import com.hitachivantara.hcp.standard.model.request.DefaultS3CompatibleMetadataRequest;

/**
 * Request for check whether an object or specific version of an object has a specific s3 metadata.
 * @author sohan
 *
 */
public class CheckS3MetadataRequest extends DefaultS3CompatibleMetadataRequest<CheckS3MetadataRequest> {

	public CheckS3MetadataRequest() {
		super();
	}

	public CheckS3MetadataRequest(String key) {
		super(key);
	}

}
