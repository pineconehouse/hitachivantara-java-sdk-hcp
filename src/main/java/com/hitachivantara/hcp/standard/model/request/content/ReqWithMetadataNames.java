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

public interface ReqWithMetadataNames<T> {
	/**
	 * Where annotation-name is the name of the annotation you’re storing. 
	 * @param metadataName annotation-name. 
	 * @return
	 */
	T withMetadataName(String metadataName);

	/**
	 * Check whether an object or specific version of an object has those specific annotations.
	 * @param metadataNames
	 * @return
	 */
	T withMetadataNames(String... metadataNames);

	String[] getMetadataNames();
}
