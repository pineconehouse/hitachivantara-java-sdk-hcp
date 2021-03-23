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
package com.hitachivantara.hcp.standard.internal;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.amituofo.common.ex.AlgorithmException;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.hcp.common.define.Constants;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;

public class ConservativeKeyHashAlgorithm implements KeyAlgorithm {
	private int level1DirLen = 3;
	private int level2DirLen = 2;

//	private MessageDigest md5;

	public ConservativeKeyHashAlgorithm() {
//		try {
//			md5 = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
	}

	public ConservativeKeyHashAlgorithm(int level1DirLen, int level2DirLen) {
		this();
		this.level1DirLen = level1DirLen;
		this.level2DirLen = level2DirLen;
	}

	public int getLevel1DirLen() {
		return level1DirLen;
	}

	public void setLevel1DirLen(int level1DirLen) {
		this.level1DirLen = level1DirLen;
	}

	public int getLevel2DirLen() {
		return level2DirLen;
	}

	public void setLevel2DirLen(int level2DirLen) {
		this.level2DirLen = level2DirLen;
	}

	public String generate(String key) throws AlgorithmException {
		// if (key == null || key.length() == 0) {
		// return "";
		// }
		MessageDigest md5;
		
		try {
			// Get new instance every time for thread safety
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new AlgorithmException(e);
		}

		byte[] digest = md5.digest(key.getBytes(StandardCharsets.UTF_8));

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < digest.length; i++) {
			sb.append(String.format("%02x", digest[i] & 0xff));
		}
		
//		for (int i = 0; i < digest.length; i++) {
//			sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
//		}

		String hash = sb.toString();

		// Get orginal name
		String objName = URLUtils.getRequestTargetName(key);

		int start = sb.length();
		sb.append(hash.substring(0, level1DirLen));
		sb.append(Constants.URL_SEPARATOR);
		sb.append(hash.substring(level1DirLen, level1DirLen + level2DirLen));
		sb.append(Constants.URL_SEPARATOR);
		sb.append(objName);

		return sb.substring(start);
	}

}
