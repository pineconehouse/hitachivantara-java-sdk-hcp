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
package com.hitachivantara.hcp.standard.api;

import com.amituofo.common.ex.AlgorithmException;
import com.hitachivantara.hcp.standard.internal.ConservativeKeyHashAlgorithm;
import com.hitachivantara.hcp.standard.internal.EntireKeyHashAlgorithm;

public interface KeyAlgorithm {
	public final static KeyAlgorithm DEFAULT = new KeyAlgorithm() {
		public String generate(String key) {
			return key;
		}
	};

	/**
	 * Full path hash algorithm(include object name), the original object name will not be retained
	 */
	public final static EntireKeyHashAlgorithm ENTIRE_KEY_HASH_D32 = new EntireKeyHashAlgorithm(3, 2);

	/**
	 * Path hash algorithm, the original object name will be retained
	 */
	public final static ConservativeKeyHashAlgorithm CONSERVATIVE_KEY_HASH_D32 = new ConservativeKeyHashAlgorithm(3, 2);

	String generate(String key) throws AlgorithmException;
}
