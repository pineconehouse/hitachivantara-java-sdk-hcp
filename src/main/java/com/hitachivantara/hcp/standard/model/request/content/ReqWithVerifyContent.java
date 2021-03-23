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

public interface ReqWithVerifyContent<T> {

	/**
	 * When an object is created, HCP uses cryptographic hash algorithms to calculate various hash values for it. These values, which are
	 * generated based on the object data, SDK will also generate hash value to compare with the one HCP generated. using this hash value to
	 * verify whether the data was uploaded to HCP integrallty.
	 * 
	 * @param verify
	 * @return
	 */
	T withVerifyContent(boolean verify);

	boolean isVerifyContent();
}
